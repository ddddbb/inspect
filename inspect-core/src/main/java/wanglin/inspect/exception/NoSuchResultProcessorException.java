package wanglin.inspect.exception;

public class NoSuchResultProcessorException extends Throwable {
    public NoSuchResultProcessorException(String resultProcessorName) {
        super("no such result processor "+resultProcessorName);
    }
}
