package com.zxy.bank2.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "bank1")
public interface Bank1Client {
    //@FeignClient(value = "bank1")已经是去bank1服务找方法，为什么@RequestMapping的路径里还要写bank1？？？
    @RequestMapping(value = "/bank1/accountInfo/transfer")
    String transferToBank1(@RequestParam(name = "id") String id, @RequestParam(name = "money") String money);
}
