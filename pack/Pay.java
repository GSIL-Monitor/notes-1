package com.test.effective.pack;

/**
 * Author   NieYinjun
 * Date     2018/8/27 20:08
 * TAG    T 入参参数 ， E 出参参数类型
 */
public interface Pay<T,E> {
    E doPay(T e);
}
