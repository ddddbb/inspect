package wanglin.stream;

/**
 * 有两种类型滑动时间窗口
 * 1，过去几秒钟xxx
 * 2，三月份以来xxx
 * 3，3月份xxxx
 */
public interface StreamScript {
    void on(StreamData type, Object request);

    void expire(StreamData type, Object request);
}
