package com.zxy.bank2.test;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.nio.charset.StandardCharsets;

public class PorducerTest {
    public static void main(String[] args) throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        // 1、创建DefaultMQProducer
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer("pg_1");
        // 2、设置namesrvAddr
        defaultMQProducer.setNamesrvAddr("localhost:9876");
        // 3、开启DefaultMQProducer
        defaultMQProducer.start();
        // 4、创建消息Message
        Message message = new Message("topic_1"
                , "tags_1" // 主要用于消息过滤
                , "keys_1" // 消息的唯一值
                , "消息内容hello".getBytes(StandardCharsets.UTF_8));
        // 5、发送消息
        SendResult result = defaultMQProducer.send(message);
        System.out.println(result);
        // 6、关闭DefaultMQProducer
        defaultMQProducer.shutdown();
    }
}
