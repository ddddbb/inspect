package wanglin.inspect;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Set;

@Data
public class Rule {
    String      id;
    EngineEnum  engine;
    String      name;
    Object      context;
    /**
     * none             DefaultResultProcessorStrategy
     * true_reject      TrueRejectResultProcessorStrategy
     * true_pass        TruePassResultProcessorStrategy
     * XXXAction        SMSResultProcessorStrategy
     *                  HuoTiResultProcessorStrategy
     *                  ……
     *
     */
    String      resultProcessorStrategy;
    Set<String> varNames;

    public Rule() {

    }

    public Rule(EngineEnum engine, String name, Object context) {
        this.engine = engine;
        this.name = name;
        this.context = context;
    }

    public boolean containVar(String varName) {
        if (null == varNames || varNames.isEmpty()) return false;
        if (StringUtils.isEmpty(varName)) return false;
        return varNames.contains(varName);
    }


}
