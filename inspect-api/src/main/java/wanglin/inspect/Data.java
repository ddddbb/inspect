package wanglin.inspect;

@lombok.Data
public class Data {
    String bizCode;
    Long   id;
    String name;
    String serviceName;

      ResultStatus status = ResultStatus.INIT;
      Object       result;

    public Data() {

    }

    public Data(Long id, String name, String handlerName) {
        this.id = id;
        this.name = name;
        this.serviceName = handlerName;
    }

    public Data init() {
        Data var = new Data();
        var.bizCode = this.bizCode;
        var.id = this.id;
        var.name = this.name;
        var.serviceName = this.serviceName;
        var.status = ResultStatus.INIT;
        return var;
    }

    public boolean isCompleted() {
        return status == ResultStatus.COMPLETED;
    }


    public void _setResult(Object varResult) {
        this.result = varResult;
        if (null != result) {
            status = result instanceof Throwable ? ResultStatus.EXCEPTION : ResultStatus.COMPLETED;
        }
    }

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public ResultStatus getStatus() {
        return status;
    }

    public void setStatus(ResultStatus status) {
        this.status = status;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
