package org.noahsark.gw.ws.manager;

import org.noahsark.rpc.common.thread.ServiceThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author: zhangxt
 * @desc: 线性处理器
 * @version:
 * @date: 2021/7/21
 */
@Component
public class GroupLinearManager {

    private static Logger log = LoggerFactory.getLogger(GroupLinearManager.class);

    private static final int DEFAULT_MAX_QUEUE_NUM = 5000;

    private static final int DEFAULT_MAX_THREAD_NUM = 10;

    private int maxQueueNum = DEFAULT_MAX_QUEUE_NUM;

    private int maxThreadNum = DEFAULT_MAX_THREAD_NUM;

    // 工作线程池
    private WorkThread[] threads;

    public GroupLinearManager() {
    }

    /**
     * 初始化工作队列
     */
    @PostConstruct
    public void init() {
        threads = new WorkThread[this.maxThreadNum];

        WorkThread thread;
        for (int i = 0; i < maxThreadNum; i++) {
            thread = new WorkThread(i);

            threads[i] = thread;

            thread.start();
        }

        log.info("GroupLinearManager start... ");
    }

    /**
     * 根据一致性哈希算法将相同索引的任务添加到同一个线程中。
     *
     * @param key  索引
     * @param task 任务
     */
    public synchronized void add(String key, Runnable task) {
        int hashValue = getHash(key);
        int index = hashValue % maxThreadNum;

        WorkThread thread = threads[index];
        thread.addTask(task);
    }


    /**
     * 使用FNV1_32_HASH算法计算Hash值
     *
     * @param key 字符串
     * @return 哈希值
     */
    private int getHash(String key) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < key.length(); i++) {
            hash = (hash ^ key.charAt(i)) * p;
        }

        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        if (hash < 0)
            hash = Math.abs(hash);
        return hash;
    }


    /**
     * 关闭线程池
     */
    public void shutdown() {
        for (WorkThread thread : threads) {
            thread.shutdown();
        }
    }

    private static class WorkThread extends ServiceThread {

        // 线程编号
        private int seq;

        // 工作队列
        private BlockingQueue<Runnable> queue;

        public WorkThread(int seq) {
            this.seq = seq;
            this.queue = new LinkedBlockingQueue<>(DEFAULT_MAX_QUEUE_NUM);
        }

        @Override
        public void run() {

            while (!this.isStopped()) {
                try {

                    Runnable task = this.queue.take();
                    if (task != null) {
                        task.run();
                    }

                } catch (Exception ex) {
                    log.warn(this.getServiceName() + " controller has exception. ", ex);
                }

            }
        }

        public void addTask(Runnable task) {
            if (this.queue.size() <= DEFAULT_MAX_QUEUE_NUM) {
                this.queue.add(task);
            } else {
                log.warn("Because request size[{}] was not enough, so drop this request {}", this.queue.size());
            }
        }

        @Override
        public String getServiceName() {
            return "GroupLinearThread - " + seq;
        }

    }


}
