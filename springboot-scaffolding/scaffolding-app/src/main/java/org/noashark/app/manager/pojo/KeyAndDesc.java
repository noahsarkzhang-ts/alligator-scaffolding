package org.noashark.app.manager.pojo;

import java.io.Serializable;
import java.util.Objects;

/**
 * ID 及描述信息
 *
 * @author zhangxt
 * @date 2023/11/29 15:36
 **/
public class KeyAndDesc implements Serializable {

    /**
     * key
     */
    private String key;

    /**
     * 描述/标签
     */
    private String label;

    /**
     * 单位描述
     */
    private String unitDesc;

    public KeyAndDesc() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUnitDesc() {
        return unitDesc;
    }

    public void setUnitDesc(String unitDesc) {
        this.unitDesc = unitDesc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KeyAndDesc)) {
            return false;
        }
        KeyAndDesc keyAndDesc = (KeyAndDesc) o;
        return getKey().equals(keyAndDesc.getKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey());
    }
}
