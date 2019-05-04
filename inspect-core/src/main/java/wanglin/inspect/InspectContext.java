package wanglin.inspect;


import wanglin.inspect.exception.NoSuchVarException;

import java.io.Serializable;
import java.util.*;

@lombok.Data
public class InspectContext implements Serializable {
    public Long                sequence;
    public ResultStatus        status;
    public BizType             bizType;
    public Object              request;
    public Map<String, Object> result = new HashMap<>();
    boolean resultIsDirty = false;
    boolean needNotify = false;
    public List<Rule> rules;
    public List<Data> vars;

    public InspectContext() {

    }

    public InspectContext(long sequence, BizType bizType, Object request, Set<Rule> rules, Set<Data> vars) {
        this.sequence = sequence;
        this.status = ResultStatus.INIT;
        this.bizType = bizType;
        this.request = request;
        this.rules = new ArrayList<>();
        this.vars = new ArrayList<>();
        if (null != rules) {
            rules.forEach(rule -> {
                this.rules.add(rule.init());
            });
        }
        if (null != vars) {
            vars.forEach(var -> {
                this.vars.add(var.init());
            });
            this.vars.add(new Data(Long.MAX_VALUE, Coasts.CTX.BIZ_TYPE, "typeDataService"));
            this.vars.add(new Data((Long.MAX_VALUE - 1), Coasts.CTX.REQUEST, "requestDataService"));
        }
    }


    public boolean result() {
        return rules.stream().allMatch(t -> t.status != ResultStatus.INIT) || status != ResultStatus.INIT;
    }


    public void setVarByName(String varName, Object value) {
        vars.forEach(v -> {
            if (v.name.equals(varName)) {
                v._setResult(value);
            }
        });
    }

    public boolean ruleIsReady(Rule rule) {
        for (String varName : rule.varNames) {
            try {
                Data var = getVar(varName);
                if (null == var) continue;
                if (!var.status.equals(ResultStatus.COMPLETED)) {
                    return false;
                }
            } catch (NoSuchVarException e) {
                return false;
            }
        }
        return true;
    }


    public Data getVar(String varName) throws NoSuchVarException {
        for (Data var : vars) {
            if (var.name.equals(varName)) {
                return var;
            }
        }
        throw new NoSuchVarException(bizType.code, varName);
    }

    public void buildResult() {
        //todo
        rules.forEach(rule -> {
            if (rule.isCompleted()) {
                result.put(rule.getResultField(), rule.getResult());
            }
        });
        resultIsDirty = true;
    }


}
