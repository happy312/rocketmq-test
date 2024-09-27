package com.zxy.bank2.test.txMessage;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

/**
 * 事务消息生产者
 */
public class PorducerTest {
    public static void main(String[] args) throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        // 1、创建 TransactionMQProducer
        TransactionMQProducer producer = new TransactionMQProducer("producer_group_tx_1");
        // 2、设置namesrvAddr
        producer.setNamesrvAddr("localhost:9876");

        // 指定消息监听对象，用于执行本地事务和消息回查
        TransactionListener transactionListener = new TransactionListenerImpl();
        producer.setTransactionListener(transactionListener);

        ExecutorService executorService = new ThreadPoolExecutor(
                2,
                5,
                100,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(2000),
                new ThreadFactory(){
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("thread_name_tx");
                        return thread;
                    }
                }
        );
        producer.setExecutorService(executorService);
        // 3、开启DefaultMQProducer
        producer.start();

        // 4、创建消息Message
        for (int i = 0; i < 10; i++) {
            Message message = new Message("tx_topic2"
                    , "tags_2" // 主要用于消息过滤
                    , "keys_" + i // 消息的唯一值
                    , ("消息内容hello，这是一个事务消息" + i).getBytes(StandardCharsets.UTF_8));
            TransactionSendResult result = producer.sendMessageInTransaction(
                    // 消息内容
                    message,
                    "hell0-trsaction" + i);
            System.out.println(result);
        }
        // 6、关闭DefaultMQProducer
        producer.shutdown();
    }
}
