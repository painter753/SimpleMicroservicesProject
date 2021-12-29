package com.lazyboyprod.kafka.mapper;

public interface Mapper<T, V> {

    V map(T source);
    T unmap(V source);


}
