package com.zds.treedemo.service;

import com.zds.treedemo.domain.TbLogVisit;

public interface ActionService {
    void insertLogVisit(TbLogVisit tbLogVisit);

    int insertLog(TbLogVisit tbLogVisit);
}
