package org.noahsark.rpc.common.dispatcher;

import org.noahsark.rpc.common.thread.ServiceThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public class WorkQueue {

    private static Logger log = LoggerFactory.getLogger(WorkQueue.class);

    private static final int DEFAULT_MAX_QUEUE_NUM = 10000;

    private static final int DEFAULT_MAX_THREAD_NUM = 100;

    private int maxQueueNum = DEFAULT_MAX_QUEUE_NUM;

    private int maxThreadNum = DEFAULT_MAX_THREAD_NUM;

    // 工作线程池
    private WorkThread[] threads;

    // 阻塞队列
    private BlockingQueue<Runnable> queue;

    public WorkQueue() {
    }

    /**
     * 初始化工作队列
     */
    public void init(String name) {
        queue = new LinkedBlockingQueue<>(this.maxQueueNum);
        threads = new WorkThread[this.maxThreadNum];

        WorkThread thread;
        for (int i = 0; i < maxThreadNum; i++) {
            thread = new WorkThread(i, queue);

            threads[i] = thread;

            thread.start();
        }

        log.info("{} WorkQueue start... ", name);
    }

    public boolean isBusy() {
        return queue.size() >= maxQueueNum;
    }

    /**
     * 添加任务
     *
     * @param task 任务
     */
    public void add(Runnable task) {
        if (this.queue.size() <= maxQueueNum) {
            // log.info("Add request: {}", task);
            this.queue.add(task);
        } else {
            log.warn("busevent request size[{}] enough, so drop this request {}", this.queue.size(), queue.toString());
        }
    }

    public int getMaxQueueNum() {
        return maxQueueNum;
    }

    public void setMaxQueueNum(int maxQueueNum) {
        this.maxQueueNum = maxQueueNum;
    }

    public int getMaxThreadNum() {
        return maxThreadNum;
    }

    public void setMaxThreadNum(int maxThreadNum) {
        this.maxThreadNum = maxThreadNum;
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

        public WorkThread(int seq, BlockingQueue<Runnable> queue) {
            this.seq = seq;
            this.queue = queue;
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

        @Override
        public String getServiceName() {
            return "WorkThread - " + seq;
        }

    }


}
