package wanglin.inspect.exception;

public class NoSuchVarException extends Exception {
    private final String varName;

    public NoSuchVarException(String bizType,String varName) {
        super("no such var :"+varName + " in bizType :"+bizType);
        this.varName = varName;
    }

    public String getVarName() {
        return varName;
    }
}
