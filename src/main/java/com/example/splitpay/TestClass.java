package com.example.splitpay;

import com.example.splitpay.entity.SplitPayUser;
import com.example.splitpay.services.SplitPayUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestClass {
    @Autowired
    SplitPayUserService userService ;


    public  void  saveSomeData(){
//        SplitPayUser user = new SplitPayUser();
//        user.setUsername("ranavishal2015");
//        user.setFullname("Vishal Rana");
//        user.setEmailId("ranavishal2015@gmail.com");
//        user.setBalance(2);
//        user.setPhoneNumber("23423424");
//        user.setPassword("Password");
        if ( userService == null){
            System.out.println("user service is null");
        }
        else{
            System.out.println("Do nothing");
            // I am getting null
          //  userService.save(user);
        }

    }

}
