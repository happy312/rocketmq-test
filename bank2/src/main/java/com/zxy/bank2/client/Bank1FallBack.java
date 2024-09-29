package com.zxy.bank2.client;

import org.springframework.stereotype.Component;

@Component
public class Bank1FallBack implements Bank1Client{
    /**
     * 降级方法
     * @param id
     * @param money
     * @return
     */
    @Override
    public String transferToBank1(String id, String money) {
        return "转账失败";
    }
}
