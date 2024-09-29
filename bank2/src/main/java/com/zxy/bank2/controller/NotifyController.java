package com.zxy.bank2.controller;

import com.zxy.bank2.service.AccountInfoService;
import com.zxy.bank2.vo.NotifyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分布式事务管理之最大努力通知
 * 场景：增加用户送积分。bank1服务增加用户后，通知bank2服务，同时也提供查询接口供bank2服务查询
 * 为了练习，不新建微服务了，就用bank1和bank2模拟最大努力通知。
 */
@RestController
@RequestMapping("/notify")
public class NotifyController {

    @Autowired
    private AccountInfoService accountInfoService;

    @RequestMapping("/saveAccountInfo")
    public Boolean saveAccountInfo(@RequestParam(name = "id") String id
            , @RequestParam(name = "name") String name
            , @RequestParam(name = "balance") String balance) {
        NotifyVO notifyVO = new NotifyVO(id, name, Double.parseDouble(balance));

        return accountInfoService.saveAccountInfo(notifyVO);
    }
}
