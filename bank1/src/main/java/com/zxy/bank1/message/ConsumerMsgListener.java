package com.zxy.bank1.message;

import com.alibaba.fastjson.JSONObject;
import com.zxy.bank1.service.AccountInfoService;
import com.zxy.bank1.vo.AccountTransferVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(consumerGroup = "consumer_group_bank1", topic = "topic_transfer_bank2")
@Slf4j
public class ConsumerMsgListener implements RocketMQListener<String> {

    @Autowired
    private AccountInfoService accountInfoService;
    @Override
    public void onMessage(String s) {
        JSONObject object = JSONObject.parseObject(s);
        String msgJson = object.getString("accountChange");
        AccountTransferVO accountTransferVO = JSONObject.parseObject(msgJson, AccountTransferVO.class);

        accountInfoService.transfer(accountTransferVO);
    }
}
