package com.zxy.bank2.test.txMessage;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.concurrent.ConcurrentHashMap;

public class TransactionListenerImpl implements TransactionListener {

    // 0：本地事务执行中 1：本地事务执行成功 2：本地事务执行失败
    private ConcurrentHashMap<String, Integer> statusMap = new ConcurrentHashMap<>();
    /**
     * 执行本地事务
     * @param message
     * @param o
     * @return
     */
    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object o) {
        String txId = message.getTransactionId();

        try {
            System.out.println(txId + "本地事务执行中");
            statusMap.put(txId, 0);
            Thread.sleep(3000);
            System.out.println(txId + "本地事务执行成功");
            statusMap.put(txId, 1);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println(txId + "本地事务执行失败");
            statusMap.put(txId, 2);
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }

        return LocalTransactionState.COMMIT_MESSAGE;
    }

    /**
     * 消息回查
     * @param messageExt
     * @return
     */
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
        String txId = messageExt.getTransactionId();
        Integer status = statusMap.get(txId);
        System.out.println(txId + "回查状态是" + status);
        switch (status) {
            case 0 :
                return LocalTransactionState.UNKNOW;
            case 1 :
                return LocalTransactionState.COMMIT_MESSAGE;
            case 2 :
                return LocalTransactionState.ROLLBACK_MESSAGE;
        }
        return LocalTransactionState.UNKNOW;
    }
}
