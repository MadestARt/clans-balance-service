package org.asvladimirov.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClanEntity {
    private long id;
    private String name;
    private int gold;
    private short membersAmount;
}
