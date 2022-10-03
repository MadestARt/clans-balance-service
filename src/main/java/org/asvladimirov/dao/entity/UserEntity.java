package org.asvladimirov.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserEntity {
    private long id;
    private String nickName;
    private int balance;
}
