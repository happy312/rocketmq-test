package com.zxy.bank2.test;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class ConsumerTest {
    public static void main(String[] args) throws MQClientException {
        // 1、创建DefaultMQProducer
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("cg_1");
        // 2、设置namesrvAddr
        consumer.setNamesrvAddr("localhost:9876");
        // 最大拉取消息个数
        consumer.setConsumeMessageBatchMaxSize(2);
        // 3、订阅消息的主题
        consumer.subscribe("topic_1", "*");
        // 4、创建消息监听Listener
        consumer.setMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                // 5、获取消息
                for (MessageExt messageExt : list) {
                    try {
                        // 获取主题
                        String topic = messageExt.getTopic();
                        // 获取标签
                        String tags = messageExt.getTags();
                        // 获取消息内容
                        String result = new String(messageExt.getBody(), RemotingHelper.DEFAULT_CHARSET);
                        System.out.println("topic = " + topic);
                        System.out.println("tags = " + tags);
                        System.out.println("result = " + result);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        // 重试
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    }

                }
                // 6、返回状态
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
    }
}
