package wanglin.inspect;

public interface RuleResultStrategy {

    String name();

    boolean test(Rule rule,InspectContext context);
}
