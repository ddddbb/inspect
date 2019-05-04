package com.wanglin.domain;

import java.util.List;

public interface OrgMapper {
     List<Org> selectOrg(Org org) ;
     int insertOrg(Org org);
     int updateOrg(Org org);
     Org get(String orgCode);

     int disableOrgs(String[] ids);
}
