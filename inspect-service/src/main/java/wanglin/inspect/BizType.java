package wanglin.inspect;

public class BizType {
    public String name;
    public Integer timeout;
    public String resultProcessorName;
    public String callbackProcessor;

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                '}';
    }
}
