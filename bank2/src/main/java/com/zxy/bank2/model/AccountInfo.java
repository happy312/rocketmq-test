package com.zxy.bank2.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccountInfo implements Serializable {
    private String id;

    private String name;

    private Double balance;
}
