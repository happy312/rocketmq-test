package com.zxy.bank1.message;

import com.alibaba.fastjson.JSONObject;
import com.zxy.bank1.service.AccountInfoService;
import com.zxy.bank1.vo.AccountTransferVO;
import com.zxy.bank1.vo.NotifyVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(consumerGroup = "consumer_group_bank1_notify", topic = "topic_notify")
@Slf4j
public class NotifyConsumerMsgListener implements RocketMQListener<String> {

    @Autowired
    private AccountInfoService accountInfoService;
    @Override
    public void onMessage(String s) {
        log.info("接收消息内容：" + s);
        NotifyVO notifyVO = JSONObject.parseObject(s, NotifyVO.class);
        // 接收到消息，处理业务
        // 可能重复消费，要注意幂等控制 todo
        // 可以使用事务id来做幂等控制
    }
}
