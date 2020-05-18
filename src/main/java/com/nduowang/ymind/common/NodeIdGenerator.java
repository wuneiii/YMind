package com.nduowang.ymind.common;

import java.util.concurrent.atomic.AtomicInteger;

public class NodeIdGenerator {
    private static AtomicInteger nodeId = new AtomicInteger(0);

    public static Integer getId() {
        return nodeId.incrementAndGet();
    }
}
