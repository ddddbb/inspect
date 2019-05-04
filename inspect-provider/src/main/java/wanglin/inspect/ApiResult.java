package wanglin.inspect;

import lombok.Data;

@Data
public class ApiResult {
    Boolean success;
    String code;
    String error;
    Object data;

    public ApiResult(Throwable e) {
        this.success = false;
        this.code = "1000";
        this.error = e.getMessage();
    }

    public ApiResult(Object result) {
        this.success = true;
        this.data = result;
    }
}
