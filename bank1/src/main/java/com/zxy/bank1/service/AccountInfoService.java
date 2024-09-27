package com.zxy.bank1.service;

import com.zxy.bank1.vo.AccountTransferVO;

public interface AccountInfoService {

    void transfer(AccountTransferVO accountTransferVO);

    void updateAccountInfo(String toId, Double money);

}
