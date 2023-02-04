package com.jdbc.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 用户实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String name;
    private Double balance;
}