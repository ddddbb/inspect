package com.wanglin.domain;

import com.wanglin.common.annotation.Excel;
import com.wanglin.common.base.BaseEntity;
import lombok.Data;


@Data
public class Org extends BaseEntity {
    @Excel(name = "结构码")
    String orgCode;
    @Excel(name = "机构名")
    String name;
    @Excel(name = "apikey")
    String apikey;
    String delFlag;
}
