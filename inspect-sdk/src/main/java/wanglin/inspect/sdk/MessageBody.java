package wanglin.inspect.sdk;

public class MessageBody {
    String bizType;
    Long   sequence;
    Object data;
    public MessageBody(){}
    public MessageBody(String bizType,Long sequence,Object data){
        this.bizType = bizType;
        this.sequence = sequence;
        this.data = data;
    }

}
