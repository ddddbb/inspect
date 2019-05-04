package wanglin.inspect.factory;

import wanglin.inspect.InspectContext;
import wanglin.inspect.ResultStatus;
import wanglin.inspect.Rule;
import wanglin.inspect.RuleResultStrategy;

public class RuleResultStrategyFactory {
    IsTrue  isTrue  = new IsTrue();
    IsOver  isOver  = new IsOver();
    AllOver allOver = new AllOver();

    public RuleResultStrategy get(String resultStrategy) {
        if (resultStrategy == null) {
            return allOver;
        }
        if (resultStrategy.equals("IsTrue")) {
            return isTrue;
        }
        if (resultStrategy.equals("IsOver")) {
            return isOver;
        }
        return allOver;
    }

    public static class AllOver implements RuleResultStrategy {

        @Override
        public String name() {
            return "AllOver";
        }

        @Override
        public boolean test(Rule rule, InspectContext context) {
            return context.rules.stream().allMatch(t -> t.getStatus() == ResultStatus.COMPLETED || t.getStatus() == ResultStatus.EXCEPTION);
        }
    }

    public static class IsOver implements RuleResultStrategy {

        @Override
        public String name() {
            return "IsOver";
        }

        @Override
        public boolean test(Rule rule, InspectContext context) {
            return rule.getStatus() == ResultStatus.COMPLETED || rule.getStatus() == ResultStatus.EXCEPTION;
        }
    }

    public static class IsTrue implements RuleResultStrategy {

        @Override
        public String name() {
            return "IsTrue";
        }

        @Override
        public boolean test(Rule rule, InspectContext context) {
            return rule.getStatus() == ResultStatus.COMPLETED && rule.getResult() instanceof Boolean && rule.getResult().equals(Boolean.TRUE);
        }
    }
}
