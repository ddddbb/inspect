package wanglin.inspect.sdk;

import java.io.Serializable;

public class MessageBody implements Serializable {
    String bizType;
    Long   sequence;
    Object data;

    public MessageBody() {
    }
    public MessageBody(byte[] body) {

    }

    public MessageBody(String bizType, Long sequence, Object data) {
        this.bizType = bizType;
        this.sequence = sequence;
        this.data = data;
    }


}
