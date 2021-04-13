package com.example.splitpay.schedulars;

import com.example.demo.GlobalObjects;
import com.example.splitpay.entity.SplitPayUser;
import com.example.splitpay.services.SplitPayUserService;
import com.example.splitpay.utility.EmailUtility;
import com.example.splitpay.utility.SendEmail;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.persistence.Query;
import java.util.Calendar;
import java.util.List;

// Just to let it scan.
@Component
// This class will disable the employees whose notice period has been over
public class EmailSchedular {
    @Autowired
    SplitPayUserService userService;

    //At 12pm every day
    @Scheduled(cron = "0 0 12 * * ?")
    public void update(){
        // make your task here to work

        List<SplitPayUser> users = userService.getAllUsers();
        String message  = "";
        for ( SplitPayUser user : users){
            this.sendEmail(user);
            if ( user.getBalance() > 0){
                message = "People owe you"+ user.getBalance();
            }
            else
                message= "You owe "+ user.getBalance();
            SendEmail.sendEmail("sender","password","Your Splitpay owe status",message,user.getEmailId());
        }

    }

    public void sendEmail(SplitPayUser user){
        System.out.println("Email sent");
        // Write your email service here.
   }
}
