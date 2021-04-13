package com.example.demo;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class GlobalObjects {
    static SessionFactory sessionFactory;
    static boolean isSessionFactoryInitialized = false;
    // A global a function to get sessionFactory object to create Session in the application. As this is an expensive object.

    public static SessionFactory initializeSessionFactory(){
        if (! isSessionFactoryInitialized) {
            sessionFactory = new Configuration().configure().buildSessionFactory();
            isSessionFactoryInitialized= true;
        }
        return sessionFactory;
    }


}
