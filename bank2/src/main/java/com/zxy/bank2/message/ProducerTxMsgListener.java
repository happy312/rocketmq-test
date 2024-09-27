package com.zxy.bank2.message;

import com.alibaba.fastjson.JSONObject;
import com.zxy.bank2.mapper.AccountInfoMapper;
import com.zxy.bank2.service.AccountInfoService;
import com.zxy.bank2.vo.AccountTransferVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RocketMQTransactionListener(txProducerGroup = "producer_group_tx_bank2")
public class ProducerTxMsgListener implements RocketMQLocalTransactionListener {

    @Autowired
    private AccountInfoService accountInfoService;

    @Autowired
    private AccountInfoMapper accountInfoMapper;
    /**
     * 事务消息发送成功后的回调方法。当消息发送给mq成功，此方法被回调
     * @param message
     * @param o
     * @return
     */
    @Override
    @Transactional
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        try {
            String messageStr = new String((byte[]) message.getPayload());
            JSONObject object = JSONObject.parseObject(messageStr);
            String msgJson = object.getString("accountChange");
            AccountTransferVO accountTransferVO = JSONObject.parseObject(msgJson, AccountTransferVO.class);
            log.info("执行本地事务：" + JSONObject.toJSONString(accountTransferVO));
            accountInfoService.updateAccount(accountTransferVO);
            // 向mq发送commit，mq将消息标记为可消费
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            e.printStackTrace();
            log.debug(e.getMessage());
            return RocketMQLocalTransactionState.ROLLBACK;
        }

    }

    /**
     * 事务状态回查，回查转账方是否已扣减了金额
     * 防止转账方已经扣减了金额，但是向mq发送commit失败，导致收款方无法消费消息
     * @param message
     * @return
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {String messageStr = new String((byte[]) message.getPayload());
        JSONObject object = JSONObject.parseObject(messageStr);
        String msgJson = object.getString("accountChange");
        AccountTransferVO accountTransferVO = JSONObject.parseObject(msgJson, AccountTransferVO.class);
        log.info("事务状态回查：" + JSONObject.toJSONString(accountTransferVO));
        if (accountInfoMapper.getByTaxNo(accountTransferVO.getTaxNo()) > 0) {
            // 向mq发送commit，mq将消息标记为可消费
            return RocketMQLocalTransactionState.COMMIT;
        }
        // 状态不确定
        return RocketMQLocalTransactionState.UNKNOWN;
    }
}
