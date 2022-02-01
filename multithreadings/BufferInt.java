package com.company.multithreadings;

public interface BufferInt<T> {
    public void insertIntoBuffer(T value);

    public T consumeFromBuffer();
}
