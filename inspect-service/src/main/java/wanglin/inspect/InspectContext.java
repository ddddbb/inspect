package wanglin.inspect;


import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
public class InspectContext {
    public Long          sequence;
    public InspectStatus inspectStatus;
    public BizType       bizType;
    public Object        request;
    public Object        result;

    Map<Rule, Task> rules;
    Map<Var, Task>  vars;

    public InspectContext(long sequence, BizType bizType, Object request, Set<Rule> rules, Set<Var> vars) {
        this.sequence = sequence;
        this.inspectStatus = InspectStatus.ING;
        this.bizType = bizType;
        this.request = request;
        this.rules = new HashMap<>();
        this.vars = new HashMap<>();
        if (null != rules) {
            rules.forEach(rule -> {
                this.rules.put(rule, new Task());
            });
        }
        if (null != vars) {
            vars.forEach(var -> {
                this.vars.put(var, new Task());
            });
            this.vars.put(new Var("" + Integer.MAX_VALUE, Coasts.Ctx.BIZ_TYPE, Coasts.VarHandler.RequestHandler), new Task());
            this.vars.put(new Var("" + (Integer.MAX_VALUE - 1), Coasts.Ctx.REQUEST, Coasts.VarHandler.RequestHandler), new Task());
        }
    }

    public Var getVar(String varName) {
        return null;
    }

    public Rule getRule(Rule rule) {
        return null;
    }

    public void setVarByName(String varName, Object value) {
        Var var = getVar(varName);
        if (null != var && vars.containsKey(var)) {
            vars.get(var).setResult(value);
        } else {
            //todo
        }
    }


    public void setRule(Rule rule, Object value) {
        if (rules.containsKey(rule)) {
            rules.get(rule).setResult(value);
        } else {
            //todo
        }
    }

    public boolean hasCallback() {
        return false;
    }

    public boolean allRuleOver() {
        return rules.values().stream().allMatch(t -> t.status != Task.TaskStatus.INIT);
    }
}
