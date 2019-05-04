package com.wanglin.domain;

import com.wanglin.common.annotation.Excel;
import com.wanglin.common.base.BaseEntity;
import lombok.Data;

@Data
public class Rule extends BaseEntity {
    @Excel(name = "业务码")
    String bizCode;
    @Excel(name = "规则ID")
    Long   id;
    @Excel(name = "规则名称")
    String name;
    @Excel(name = "规则内容")
    String context;
    @Excel(name = "引擎类型")
    String engine;
    @Excel(name = "结果处理策略")
    String resultStrategy;
    @Excel(name = "结果返回字段")
    String resultField;

    String delFlag;

}
