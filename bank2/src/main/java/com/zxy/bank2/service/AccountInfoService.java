package com.zxy.bank2.service;

import com.zxy.bank2.vo.AccountTransferVO;

public interface AccountInfoService {

    Boolean transferToBank1_m1(String id, String money, String toId);

    Boolean transferToBank1_m3(String id, String money, String toId);

    void updateAccount(AccountTransferVO accountTransferVO);
}
