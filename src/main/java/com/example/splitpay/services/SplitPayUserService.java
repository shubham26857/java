package com.example.splitpay.services;

import com.example.splitpay.entity.Expense;
import com.example.splitpay.entity.SplitPayGroup;
import com.example.splitpay.entity.SplitPayUser;
import com.example.splitpay.exceptions.UserNotFoundException;
import com.example.splitpay.repository.SplitPayUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SplitPayUserService {

    @Autowired
    SplitPayUserRepository repository;
    @Autowired
    SplitPayGroupService groupService;

    public SplitPayUser save(SplitPayUser e){
        return repository.save(e);
        //System.out.println("employee with id : "+  e.getUserId()+ " saved");
    }

    public List<SplitPayUser> getAllUsers(){
        List<SplitPayUser> users = repository.findAll();
        return users;
    }

    public SplitPayUser getUser( Integer id){
        SplitPayUser user;
        try{
            user = repository.findById(id).get();
        }
        catch(Exception e){
            throw new UserNotFoundException("User with id: " + id + " not found.");
        }
        return user;
    }


    public List<Expense> getUserExpenses(Integer userid){
        SplitPayUser user = getUser(userid);
        List<SplitPayGroup> groups = groupService.getAllGroups();
        List<SplitPayGroup> ans = new ArrayList<>();
        for ( SplitPayGroup group : groups){
            if ( group.getMembers().contains(user)){
                ans.add(group);
            }
        }
        List<Expense> expenses = new ArrayList<>();
        for ( int i =0; i < ans.size() ; i++){
            SplitPayGroup group  = ans.get(i);
            List<Expense> le = group.getExpenses();
            if ( le == null)
                return expenses;
            for ( Expense expense : le){
                if( expense.getSplittedBetween().contains(user)){
                    expenses.add(expense);
                }
            }
        }
        return expenses;
    }

    public List<SplitPayGroup> userGroups( int userid){
        System.out.println("Inside userGroups method");
        System.out.println("userid :"+ userid);
        SplitPayUser user = getUser(userid);
        System.out.println(" user.id : " + user.getUserId());
        List<SplitPayGroup> groups = groupService.getAllGroups();
        System.out.println("groups size : "+ groups.size());
        List<SplitPayGroup> ans = new ArrayList<>();
        for ( SplitPayGroup group : groups){
            if ( group.getMembers().contains(user)){
                ans.add(group);
            }
        }
        System.out.println("getting outside of the userGroups method");
        return ans;
    }

}
