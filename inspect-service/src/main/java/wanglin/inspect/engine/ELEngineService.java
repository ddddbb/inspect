package wanglin.inspect.engine;

import org.mvel2.MVEL;
import wanglin.inspect.Coasts;
import wanglin.inspect.EngineService;
import wanglin.inspect.InspectContext;
import wanglin.inspect.Rule;
import wanglin.inspect.engine.el.Token;
import wanglin.inspect.engine.el.Tokenizer;

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
        rule.setVarNames(ruleVars);
        return ruleVars;
    }

    @Override
    public Object execute(Rule rule, Object ruleContext) {
        return MVEL.eval((String) rule.getContext(),ruleContext,Boolean.class);
    }

    @Override
    public Object buildRuleContext(InspectContext context) {
        Map<String,Object> ctx = new HashMap<>();
        ctx.put(Coasts.Ctx.ID,context.getSequence());
        ctx.put(Coasts.Ctx.REQUEST,context.getRequest());
        ctx.put(Coasts.Ctx.BIZ_TYPE,context.getBizType());
        //TODO
        return ctx;
    }
}
