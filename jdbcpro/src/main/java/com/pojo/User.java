package com.pojo;


/**
 * @description: 用户实体类
 */
public class User {
    private String username;
    private Double balance;
    
    public User() {
    }
    
    public User(String username, Double balance) {
        this.username = username;
        this.balance = balance;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public Double getBalance() {
        return balance;
    }
    
    public void setBalance(Double balance) {
        this.balance = balance;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", balance=" + balance +
                '}';
    }
}
