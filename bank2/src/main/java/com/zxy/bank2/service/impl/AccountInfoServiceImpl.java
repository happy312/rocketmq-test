package com.zxy.bank2.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.zxy.bank2.client.Bank1Client;
import com.zxy.bank2.mapper.AccountInfoMapper;
import com.zxy.bank2.service.AccountInfoService;
import com.zxy.bank2.vo.AccountTransferVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
public class AccountInfoServiceImpl implements AccountInfoService {
    @Autowired
    private AccountInfoMapper accountInfoMapper;

    @Autowired
    private Bank1Client bank1Client;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public Boolean transferToBank1_m1(String id, String money, String toId) {
        accountInfoMapper.updateAccountInfo(id, String.valueOf(Double.parseDouble(money) * -1));

        bank1Client.transferToBank1(toId, money);
        return true;
    }

    @Override
    public Boolean transferToBank1_m3(String id, String money, String toId) {
        AccountTransferVO accountTransferVO = new AccountTransferVO(id, Double.parseDouble(money), UUID.randomUUID().toString(), toId);
        this.sendUpdateAccountBalance(accountTransferVO);
        return true;
    }

    @Transactional
    @Override
    public void updateAccount(AccountTransferVO accountTransferVO) {
        // 幂等判断
        if (accountInfoMapper.getByTaxNo(accountTransferVO.getTaxNo()) > 0) {
            return;
        }
        // 2、bank2扣减金额，本地事务提交
        accountInfoMapper.updateAccountInfo(accountTransferVO.getId(), String.valueOf(accountTransferVO.getMoney() * -1));
        accountInfoMapper.saveLog(accountTransferVO.getTaxNo());
    }

    private void sendUpdateAccountBalance(AccountTransferVO accountTransferVO) {
        JSONObject object = new JSONObject();
        object.put("accountChange", accountTransferVO);
        String jsonString = object.toJSONString();
        Message<String> message = MessageBuilder.withPayload(jsonString).build();

        /**
         * String txProducerGroup,
         * String destination,
         * Message<?> message,
         * Object arg
         */
        log.info("消息发送内容：" + JSONObject.toJSONString(accountTransferVO));
        TransactionSendResult transactionSendResult = rocketMQTemplate.sendMessageInTransaction("producer_group_tx_bank2", "topic_transfer_bank2", message, null);
        log.info("消息发送结果：" + JSONObject.toJSONString(transactionSendResult));
    }
}
