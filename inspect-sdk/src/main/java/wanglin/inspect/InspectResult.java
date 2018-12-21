package wanglin.inspect;

public class InspectResult {
    public static final InspectResult NONE = new InspectResult(InspectStatus.NONE,null);
    InspectStatus status;
    Object result;

    public InspectResult() {

    }
    public InspectResult(InspectStatus status,Object result) {
        this.status = status;
        this.result = result;
    }


}
