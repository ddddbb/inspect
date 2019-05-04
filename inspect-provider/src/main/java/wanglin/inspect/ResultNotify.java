package wanglin.inspect;

import org.springframework.context.ApplicationEvent;

public class ResultNotify  extends ApplicationEvent {
    String biztype;
    Long   sequence;
    Object result;



    public ResultNotify(Object obj) {
        super(obj);
    }
    public ResultNotify(String biztype, Long sequence, Object result) {
        super(sequence);
        this.biztype = biztype;
        this.sequence = sequence;
        this.result = result;
    }

    public String getBiztype() {
        return biztype;
    }

    public void setBiztype(String biztype) {
        this.biztype = biztype;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
