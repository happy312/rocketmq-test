package com.zxy.bank2.test.broadcasting;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 广播模式-生产者
 * 广播模式：所有的消费者都能消费到所有的消息
 */
public class PorducerTest {
    public static void main(String[] args) throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        // 1、创建DefaultMQProducer
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer("producer_group_broadcasting");
        // 2、设置namesrvAddr
        defaultMQProducer.setNamesrvAddr("localhost:9876");
        // 3、开启DefaultMQProducer
        defaultMQProducer.start();
        List<Message> messageList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            // 4、创建消息Message
            Message message = new Message("topic_broadcasting"
                    , "tags" // 主要用于消息过滤
                    , "keys" + i // 消息的唯一值
                    , ("消息内容hello" + i).getBytes(StandardCharsets.UTF_8));

            messageList.add(message);
        }
        // 5、发送消息
        SendResult result = defaultMQProducer.send(messageList);
        System.out.println(result);
        // 6、关闭DefaultMQProducer
        defaultMQProducer.shutdown();
    }
}
