package wanglin.inspect;

public interface DataService <D>{
    String name();
    void fetch(Long sequence, String varName, BizType bizType, Object request);


}
