package com.lion.utils;

import java.util.LinkedList;

public class CusStack<T> {
    private final LinkedList<T> storage = new LinkedList<>();

    /**
     * 添加一个元素
     */
    public void push(T v) {
        storage.addFirst(v);
    }
    /**
     * 展示第一个元素
     */
    public T peek() {
        return storage.getFirst();
    }
    /**
     * 弹出第一个元素
     */
    public T pop() {
        return storage.removeFirst();
    }
    /**
     * 判断是否为空
     */
    public boolean empty() {
        return storage.isEmpty();
    }
    /**
     * 获取长度
     */
    public int size() {
        return storage.size();
    }

    @Override
    public String toString() {
        return storage.toString();
    }
}
