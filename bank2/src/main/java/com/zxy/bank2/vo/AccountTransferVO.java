package com.zxy.bank2.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountTransferVO implements Serializable {
    /**
     * 转账账号id
     */
    private String id;
    /**
     * 转账金额
     */
    private Double money;
    /**
     * 事务id
     */
    private String taxNo;
    /**
     * 收款账号id
     */
    private String toId;
}
