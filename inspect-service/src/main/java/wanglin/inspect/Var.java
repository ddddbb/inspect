package wanglin.inspect;

import lombok.Data;

@Data
public class Var {
    String                  id;
    String                  name;
    String                  handlerName;
    ResultProcessorStrategy resultProcessorStrategy;

    public Var() {

    }

    public Var(String id, String name, String handlerName, ResultProcessorStrategy resultProcessorStrategy) {
        this.id = id;
        this.name = name;
        this.handlerName = handlerName;
        this.resultProcessorStrategy = resultProcessorStrategy;
    }

    public enum ResultProcessorStrategy {
        NONE,
        TRUE_REJECT,
        FALSE_REJECT
    }
}
