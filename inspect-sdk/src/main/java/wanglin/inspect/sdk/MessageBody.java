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

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
