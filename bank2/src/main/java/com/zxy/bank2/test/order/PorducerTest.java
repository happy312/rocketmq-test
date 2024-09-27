package com.zxy.bank2.test.order;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 有序消息生产者
 */
public class PorducerTest {
    public static void main(String[] args) throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        // 1、创建DefaultMQProducer
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer("pg_2");
        // 2、设置namesrvAddr
        defaultMQProducer.setNamesrvAddr("localhost:9876");
        // 3、开启DefaultMQProducer
        defaultMQProducer.start();

        // 5、发送消息。5条消息将发送到同一个队列里
        for (int i = 0; i < 20; i++) {
            // 4、创建消息Message
            Message message = new Message("topic_2"
                    , "tags_2" // 主要用于消息过滤
                    , "keys_" + i // 消息的唯一值
                    , ("消息内容hello" + i).getBytes(StandardCharsets.UTF_8));
            SendResult result = defaultMQProducer.send(
                    // 消息内容
                    message,
                    // 选择指定的消息队列对象
                    new MessageQueueSelector() {
                        @Override
                        public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
                            // 获取队列的下标
                            Integer index = (Integer) o;
                            // 获取对应下标的队列
                            return list.get(index);
                        }
                    },
                    // 指定对应的队列下标
                    1);
            System.out.println(result);
        }
        // 6、关闭DefaultMQProducer
        defaultMQProducer.shutdown();
    }
}
