package com.zxy.bank2.controller;

import com.zxy.bank2.service.AccountInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * bank2服务向bank1服务转账，bank2扣减金额，bank1增加金额
 */
@RestController
@RequestMapping("/accountInfo")
public class AccountInfoController {

    @Autowired
    private AccountInfoService accountInfoService;

    /**
     * 方法一：bank2服务调用bank1服务
     * 该方法不能实现分布式事务管理，如果调用了bank1服务，并且bank1服务执行成功了，但是网络断开报错了，bank2服务就会执行回滚
     * @param id
     * @param money
     * @param toId
     * @return
     */
    @RequestMapping("/transferToBank1_m1")
    public Boolean transferToBank1_m1(@RequestParam(name = "id") String id
            , @RequestParam(name = "money") String money
            , @RequestParam(name = "toId") String toId) {
        return accountInfoService.transferToBank1_m1(id, money, toId);
    }

    /**
     * 方法二：可靠消息最终一致性之本地消息表 todo
     * 例如张三给李四转账，张三扣减金额和保存日志是一个事务，再启一个线程定时扫描日志表向消息中间件发送消息，消息中间件反馈消息发送成功后，删除日志表对应记录。
     * 以确保张三扣减金额和发送消息成功
     * 如何确保消费者一定消费消息成功？？？
     * 使用mq的ack机制，即消息确认机制。
     * 消费者接收到消息，并且业务处理成功后向mq发送ack,说明消费者正常消费消息完成，则mq就不再向消费者推该条消息。否则会一直推送直至成功。
     * @param id
     * @param money
     * @param toId
     * @return
     */
    @RequestMapping("/transferToBank1_m2")
    public Boolean transferToBank1_m2(@RequestParam(name = "id") String id
            , @RequestParam(name = "money") String money
            , @RequestParam(name = "toId") String toId) {
        return null;
    }

    /**
     * 方法三：可靠消息最终一致性之rockketmq事务消息
     * @param id
     * @param money
     * @param toId
     * @return
     */
    @RequestMapping("/transferToBank1_m3")
    public Boolean transferToBank1_m3(@RequestParam(name = "id") String id
            , @RequestParam(name = "money") String money
            , @RequestParam(name = "toId") String toId) {
        return accountInfoService.transferToBank1_m3(id, money, toId);
    }
}
