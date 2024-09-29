package com.zxy.bank2.service;

import com.zxy.bank2.model.AccountInfo;
import com.zxy.bank2.vo.AccountTransferVO;
import com.zxy.bank2.vo.NotifyVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface AccountInfoService {

    Boolean transferToBank1_m1(String id, String money, String toId);

    Boolean transferToBank1_m3(String id, String money, String toId);

    void updateAccount(AccountTransferVO accountTransferVO);

    Boolean saveAccountInfo(NotifyVO notifyVO);

    AccountInfo getById(String id);
}
