package org.noahsark.rpc.common.remote;

import java.io.Serializable;

/**
 * @author: zhangxt
 * @desc:
 * @version:
 * @date: 2021/7/21
 */
public interface Subject extends Serializable {

    String getId();

    void copyFrom(Subject oldSubject);

    void setupRepeatLogin();

    boolean isRepeatLogin();

    void resetRepeatLogin();

}
