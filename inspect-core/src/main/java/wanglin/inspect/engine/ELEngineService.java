package wanglin.inspect.engine;

import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;
import wanglin.inspect.*;
import wanglin.inspect.engine.el.Token;
import wanglin.inspect.engine.el.Tokenizer;
import wanglin.inspect.exception.ExpressionExecuteException;

import java.util.*;

public class ELEngineService implements EngineService {
    @Override
    public Set<String> analyze(Rule rule) {
        Set<String> ruleVars = new HashSet<>();
        List<Token> tokens   = new Tokenizer((String) rule.getContext()).process();
        tokens.forEach(token -> {
            if (token.isRootIdentifier()) {
                ruleVars.add(token.data);
            }
        });
        return ruleVars;
    }

    @Override
    public Object execute(Rule rule, InspectContext context) throws ExpressionExecuteException {
        try {
            return Ognl.getValue((String) rule.getContext(), buildRuleContext(context));
        } catch (OgnlException e) {
            e.printStackTrace();
            throw new ExpressionExecuteException(e);
        }

    }

    @Override
    public EngineEnum name() {
        return EngineEnum.EXPRESSION;
    }


    public Object buildRuleContext(InspectContext context) {
        Map<String,Object> ctx = new HashMap<>();
        ctx.put(Coasts.CTX.ID, context.sequence);
        ctx.put(Coasts.CTX.REQUEST, context.request);
        ctx.put(Coasts.CTX.BIZ_TYPE, context.bizType);
        context.vars.forEach(var->{
            if(var.isCompleted()){
                ctx.put(var.getName(), var.getResult());
            }
        });
        OgnlContext rt =  new OgnlContext(ctx);
        rt.put("map",ctx);
        rt.setRoot(ctx);
        return rt;
    }
}
