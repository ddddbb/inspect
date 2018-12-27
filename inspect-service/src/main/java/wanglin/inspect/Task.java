package wanglin.inspect;

public class Task {
    public Object     result;
    public TaskStatus status = TaskStatus.INIT;

    public void setResult(Object value) {
        if (value instanceof Throwable) {
            status = TaskStatus.EXCEPTION;
            this.result = ((Throwable) value).getMessage();
        } else {
            status = TaskStatus.COMPLETED;
            this.result = value;
        }
    }


    public boolean isException() {
        return status.equals(TaskStatus.EXCEPTION);
    }

    public boolean isCompleted() {
        return status.equals(TaskStatus.COMPLETED);
    }

    public enum TaskStatus {
        INIT, COMPLETED, EXCEPTION;
    }


}
