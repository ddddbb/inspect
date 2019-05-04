package com.wanglin.domain;

import com.wanglin.common.annotation.Excel;
import com.wanglin.common.base.BaseEntity;

@lombok.Data
public class Data extends BaseEntity {
    @Excel(name = "业务码")
    String bizCode;
    @Excel(name = "数据ID")
    Long   id;
    @Excel(name = "数据名称")
    String name;
    @Excel(name = "数据处理类")
    String handlerName;
    String remark;
}
