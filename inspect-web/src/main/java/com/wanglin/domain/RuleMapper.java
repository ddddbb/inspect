package com.wanglin.domain;

import java.util.List;

public interface RuleMapper {
    List<Rule> selectRule(Rule b) ;
    int insertRule(Rule b);
    int updateRule(Rule org);
    Rule get(Long code);

    int disableRules(Long[] ids);
}
