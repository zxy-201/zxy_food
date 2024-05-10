package com.zxy.food.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author 艾
 * @Time 2024/5/10 10:12 AM
 */
@Slf4j
public class CreateSnowflakeIdGenerator {
    /**
     * 开始时间戳（2024-01-01 00:00:00:000）
     */
    private static final long TWE_PO_CH = 1704038400000l;
    /**
     * 机器id所占的位数
     */
    private static final long WORKER_ID_BITS = 5l;
    /**
     * 数据标识id所占的位数
     */
    private static final long DATACENTER_ID_BITS = 5l;
    /**
     * 序列在id中占的位数
     */
    private static final long SEQUENCE_BITS = 12l;
    /**
     * 机器id向左移12位
     */
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    /**
     * 数据标识id向左移17位（12+5）
     */
    private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    /**
     * 时间戳向左移22位（5+5+12）
     */
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;
    /**
     * 生成序列的掩码，这里为4095
     */
    private static final long SEQUENCE_MASK = -1l ^ (-1l << SEQUENCE_BITS);
    /**
     * 工作机器ID（0-31）
     */
    private static long WORKER_ID = 1l;
    /**
     * 数据中心ID（0-31）
     */
    private static long DATACENTER_ID = 1l;
    /**
     * 毫秒内序列（0-455）
     */
    private static long SEQUENCE = 0l;
    /**
     * 上次生成ID的时间戳
     */
    private static long LAST_TIMESTAMP = -1l;

    public static synchronized long nextId() {
        long timestamp = timeGen();
        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候抛出异常
        if (timestamp < LAST_TIMESTAMP) {
            throw new RuntimeException(
                    String.format("时间回退！！",LAST_TIMESTAMP - timestamp)
            );
        }
        // 如果是同一时间生成的，则进行毫秒内序列
        if (LAST_TIMESTAMP == timestamp) {
            SEQUENCE = (SEQUENCE + 1) & SEQUENCE_MASK;
            // 毫秒内序列溢出
            if (SEQUENCE == 0) {
                // 阻塞到下一个毫秒，则获得新的时间戳
                timestamp = tilNextMills(LAST_TIMESTAMP);
            }
        } else {
            // 时间戳改变，毫秒内序列重置
            SEQUENCE = 0l;
        }
        // 上次生成ID的时间戳
        LAST_TIMESTAMP = timestamp;
        // 移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - TWE_PO_CH) << TIMESTAMP_LEFT_SHIFT)
                | (DATACENTER_ID << DATACENTER_ID_SHIFT)
                | (WORKER_ID << WORKER_ID_SHIFT)
                | SEQUENCE;
    }

    private static long tilNextMills(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     * @return
     */
    private static long timeGen() {
        return System.currentTimeMillis();
    }
}

