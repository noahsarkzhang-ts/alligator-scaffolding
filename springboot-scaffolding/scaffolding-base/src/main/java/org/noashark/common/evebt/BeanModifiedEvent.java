package org.noashark.common.evebt;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 实体对象修改事件
 * @author zhangxt
 * @date 2024/10/06 17:18
 **/
@Data
@NoArgsConstructor
public class BeanModifiedEvent<T> {

    /**
     * 添加的事件信息
     */
    private List<T> addedList = new ArrayList<>();

    /**
     * 移除的事件信息
     */
    private List<T> deledList = new ArrayList<>();

    /**
     * 更新的事件信息
     */
    private List<T> updatedList = new ArrayList<>();

    public void appendAdded(T event) {
        addedList.add(event);
    }

    public void appendDeled(T event) {
        deledList.add(event);
    }

    public void appendUpdated(T event) {
        updatedList.add(event);
    }
}
