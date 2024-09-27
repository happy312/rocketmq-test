package com.zxy.bank2.test.order;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 有序消息消费者
 */
public class ConsumerTest {
    public static void main(String[] args) throws MQClientException {
        // 1、创建DefaultMQProducer
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("cg_2");
        // 2、设置namesrvAddr
        consumer.setNamesrvAddr("localhost:9876");
        // 最大拉取消息个数
        consumer.setConsumeMessageBatchMaxSize(2);
        // 3、订阅消息的主题
        consumer.subscribe("topic_2", "*");
        // 4、创建消息监听Listener
        consumer.setMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
                // 5、获取消息
                for (MessageExt messageExt : list) {
                    try {
                        // 获取主题
                        String topic = messageExt.getTopic();
                        // 获取标签
                        String tags = messageExt.getTags();
                        // 获取消息内容
                        String result = new String(messageExt.getBody(), RemotingHelper.DEFAULT_CHARSET);
                        System.out.println("topic = " + topic + "tags = " + tags + "result = " + result);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        // 重试
                        return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                    }

                }
                // 6、返回状态
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });
        consumer.start();
    }
}
