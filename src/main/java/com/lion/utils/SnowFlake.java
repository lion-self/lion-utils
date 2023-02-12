package com.lion.utils;

/**
 * 雪花算法
 * 类型：64位数值型，因此用 long 表示
 * 格式：
 *      1位：符号位，正数，因此为0。
 *      41位：时间戳：当前时间-固定的开始时间
 *      10位：部署节点。前5位为数据中心标识，后5位为机器标识
 *      12位：序列号。
 */
public class SnowFlake {

    // 起始的时间戳：用于计算时间戳的差值
    private final static long START_TIMESTAMP = 1480166465631L;

    // 每一部分占用的位数
    private final static long SEQUENCE_BIT = 12;   //序列号占用的位数
    private final static long MACHINE_BIT = 5;     //机器标识占用的位数
    private final static long DATA_CENTER_BIT = 5; //数据中心占用的位数

    // 每一部分的最大值
    private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);
    private final static long MAX_MACHINE_NUM = ~(-1L << MACHINE_BIT);
    private final static long MAX_DATA_CENTER_NUM = ~(-1L << DATA_CENTER_BIT);

    // 每一部分向左的位移：用于拼接这几个数
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long DATA_CENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final static long TIMESTAMP_LEFT = DATA_CENTER_LEFT + DATA_CENTER_BIT;

    private long dataCenterId;  //数据中心
    private long machineId;     //机器标识
    private long sequence = 0L; //序列号：初始值为 0
    private long lastTimeStamp = -1L;  //上一次时间戳

    /**
     * 根据指定的数据中心ID和机器标志ID生成指定的序列号
     * @param dataCenterId 数据中心ID
     * @param machineId    机器标志ID
     */
    public SnowFlake(long dataCenterId, long machineId) {
        // 当传入的数据中心ID 或机器标志 ID 大于最大值或小于 0 时抛出异常
        if (dataCenterId > MAX_DATA_CENTER_NUM || dataCenterId < 0) {
            throw new IllegalArgumentException("DataCenterId can't be greater than MAX_DATA_CENTER_NUM or less than 0！");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("MachineId can't be greater than MAX_MACHINE_NUM or less than 0！");
        }
        this.dataCenterId = dataCenterId;
        this.machineId = machineId;
    }

    /**
     * 产生下一个ID
     * @return
     */
    public synchronized long nextId() {
        long currTimeStamp = getNewTimeStamp();
        // 获取时间戳，若小于上一次获取到的时间戳则抛出异常
        if (currTimeStamp < lastTimeStamp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }
        if (currTimeStamp == lastTimeStamp) {
            // 与上一次的时间戳相同，即相同毫秒内，序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            //同一毫秒的序列数已经达到最大，将时间戳替换为一个新的时间戳
            if (sequence == 0L) {
                currTimeStamp = getNextMill();
            }
        } else {
            //不同毫秒内，序列号置为0
            sequence = 0L;
        }
        // 将上一次时间戳的值设置为当前时间戳
        lastTimeStamp = currTimeStamp;
        // 返回新的ID：将得到的各个值左移对应位数，进行或运算，可以得到一整个的值
        return (currTimeStamp - START_TIMESTAMP) << TIMESTAMP_LEFT //时间戳部分
                | dataCenterId << DATA_CENTER_LEFT       //数据中心部分
                | machineId << MACHINE_LEFT             //机器标识部分
                | sequence;                             //序列号部分
    }

    // 获得新的时间戳
    private long getNextMill() {
        long mill = getNewTimeStamp();
        while (mill <= lastTimeStamp) {
            mill = getNewTimeStamp();
        }
        return mill;
    }

    private long getNewTimeStamp() {
        return System.currentTimeMillis();
    }

    // 测试
    public static void main(String[] args) {
        SnowFlake snowFlake = new SnowFlake(2, 3);
        for (int i = 0; i < (1 << 4); i++) {
            //10进制
            System.out.println(snowFlake.nextId());
        }
    }
}
