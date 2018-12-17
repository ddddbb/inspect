package wanglin.inspect;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Async
public class AnyTrueResultProcessor implements RuleResultProcessor {

    @Override
    public void process(InspectContext context) {
        if( context.rules.values().stream().allMatch(t->t.status != Task.TaskStatus.INIT)){
            context.result = false;
        }
        if(context.getRules().values().stream().filter(v->(null != v.result && v.result instanceof Boolean && (Boolean) v.result)).count()>0){
            context.result = true;
        }
    }
}
