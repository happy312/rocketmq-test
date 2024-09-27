package com.zxy.bank1.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.zxy.bank1.mapper.AccountInfoMapper;
import com.zxy.bank1.service.AccountInfoService;
import com.zxy.bank1.vo.AccountTransferVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Slf4j
@FeignClient(value = "bank2")
@Service
public class AccountInfoServiceImpl implements AccountInfoService {
    @Autowired
    private AccountInfoMapper accountInfoMapper;

    @Override
    @Transactional
    public void transfer(AccountTransferVO accountTransferVO) {
        log.info("消费消息" + JSONObject.toJSONString(accountTransferVO));
        if (accountInfoMapper.getByTaxNo(accountTransferVO.getTaxNo()) > 0) {
            return;
        }
        this.updateAccountInfo(accountTransferVO.getToId(), accountTransferVO.getMoney());
        accountInfoMapper.saveLog(accountTransferVO.getTaxNo());
        // 如果某一条消息，方法执行抛出异常，会一直消费该消息，直至执行成功
        /*if (accountTransferVO.getToId().equals("11")) {
            log.info("人为制造异常");
            throw new RuntimeException("人为制造异常");
        }*/
    }

    @Transactional
    @Override
    public void updateAccountInfo(String toId, Double money) {
        accountInfoMapper.updateAccountInfo(toId, String.valueOf(money));
    }

}
