package wanglin.inspect;

import lombok.Data;

import java.util.Set;


@Data
public class Rule {
    String     bizCode;
    Long       id;
    String     name;
    String     context;
    EngineEnum engine;
    String     resultStrategy;
    String     resultField;

      Set<String>  varNames;
      ResultStatus status;
      Object       result;

    public Rule() {

    }

    public Rule(EngineEnum engine, String name, String context) {
        this.engine = engine;
        this.name = name;
        this.context = context;
    }

    public Rule init() {
        Rule rule = new Rule();
        rule.bizCode = this.bizCode;
        rule.id = this.id;
        rule.name = this.name;
        rule.resultField = this.resultField;
        rule.context = this.context;
        rule.engine = this.engine;
        rule.resultStrategy = this.resultStrategy;
        rule.varNames = this.varNames;
        rule.status = ResultStatus.INIT;
        return rule;
    }

    public synchronized void _setResult(Object ruleResult) {
        this.result = ruleResult;
        if (null != result) {
            status = result instanceof Throwable ? ResultStatus.EXCEPTION : ResultStatus.COMPLETED;
        }
    }

    public boolean isCompleted() {
        return status == ResultStatus.COMPLETED;
    }
}
