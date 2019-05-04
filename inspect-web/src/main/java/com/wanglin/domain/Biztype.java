package com.wanglin.domain;

import com.wanglin.common.annotation.Excel;
import com.wanglin.common.base.BaseEntity;
import lombok.Data;

@Data
public class Biztype extends BaseEntity {
    @Excel(name = "业务码")
    String  code;
    @Excel(name = "业务名称")
    String  name;
    @Excel(name = "回调地址")
    String  notifyUrl;
    @Excel(name = "字符集")
    String  charset;

    String delFlag;
    String remark;
}
