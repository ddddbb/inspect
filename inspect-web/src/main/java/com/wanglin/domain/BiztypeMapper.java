package com.wanglin.domain;

import java.util.List;

public interface BiztypeMapper {
    List<Biztype> selectBiztype(Biztype b) ;
    int insertBiztype(Biztype b);
    int updateBiztype(Biztype org);
    Biztype get(String code);

    int disableBiztypes(String[] ids);
}
