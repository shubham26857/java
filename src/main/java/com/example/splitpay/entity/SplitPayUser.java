package com.example.splitpay.entity;

import lombok.Data;
import javax.persistence.*;
@Data
@Entity
@Table(name="SplitPayUser")
public class SplitPayUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userId;
    @Column( name="username" ,  unique = true)
    String username;
    String fullname;
    @Column( name="PhoneNumber", length = 10)
    String phoneNumber;
    @Column(name="emailId")
    String emailId;
    String password;
    int balance;
}
