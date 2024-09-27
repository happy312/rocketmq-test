package com.zxy.bank1.controller;

import com.zxy.bank1.service.AccountInfoService;
import com.zxy.bank1.vo.AccountTransferVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accountInfo")
public class AccountInfoController {

    @Autowired
    private AccountInfoService accountInfoService;

    /**
     * 接受转账
     * @param id
     * @param money
     * @return
     */
    @GetMapping("/transfer")
    public String transfer(@RequestParam(name = "id") String id
            , @RequestParam(name = "money") String money) {
        accountInfoService.updateAccountInfo(id, Double.parseDouble(money));
        return "转账成功";
    }
}
