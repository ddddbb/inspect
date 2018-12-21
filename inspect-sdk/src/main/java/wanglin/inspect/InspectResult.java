package wanglin.inspect;

public class InspectResult {
    InspectStatus status;
    Object result;
    public enum InspectStatus{
        NONE,ING,FINISHED
    }
}
