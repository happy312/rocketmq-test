package com.zxy.bank1.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotifyVO implements Serializable {
    /**
     * 充值账号id
     */
    private String id;
    /**
     * 充值账号name
     */
    private String name;
    /**
     * 充值金额金额
     */
    private Double money;
}
