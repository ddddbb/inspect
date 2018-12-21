package wanglin.inspect;

public interface VarHandler {
    void handle(Long sequence, String varName,BizType bizType, Object request);
}
