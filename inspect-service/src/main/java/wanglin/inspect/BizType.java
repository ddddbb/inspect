package wanglin.inspect;

public class BizType {
    public String name;
    public Integer timeout;
    public String resultProcessorStrategy ;

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                '}';
    }
}
