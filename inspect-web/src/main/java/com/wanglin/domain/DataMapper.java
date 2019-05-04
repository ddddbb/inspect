package com.wanglin.domain;

import java.util.List;

public interface DataMapper {
    List<Data> selectData(Data b) ;
    int insertData(Data b);
    int updateData(Data org);
    Data get(Long code);

    int deleteDatas(Long[] ids);
}
