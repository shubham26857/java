package com.example.splitpay.services;

import com.example.splitpay.entity.Expense;
import com.example.splitpay.entity.SplitPayGroup;
import com.example.splitpay.entity.SplitPayUser;
import com.example.splitpay.exceptions.GroupNotFoundException;
import com.example.splitpay.exceptions.InvalidDataException;
import com.example.splitpay.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {
    @Autowired
    ExpenseRepository repository;

    @Autowired
    SplitPayUserService userService;

    @Autowired
    SplitPayGroupService groupService;

    public List<Expense> getAllExpenses(){
        return repository.findAll();
    }
    public Expense getExpense( Integer id ){
        Expense expense ;
        try{
            expense = repository.findById(id).get();
        }
        catch(Exception e){
            throw new GroupNotFoundException("Expense " + id + " not found.");
        }
        return expense;
    }

    public Expense createExpense(String billname, Integer amount , String paidOn , Integer paidBy , ArrayList<Integer> splittedBetween, int groupid){
        SplitPayUser paidByUser = userService.getUser(paidBy);
        if ( paidByUser == null){
            return null;
        }
        List<SplitPayUser> splittedBetweenusers = new ArrayList<SplitPayUser>();
        for ( Integer id : splittedBetween){
            SplitPayUser user = userService.getUser(id);
            // do not add duplicate
            splittedBetweenusers.add(user);
        }

        if( !splittedBetweenusers.contains(paidByUser))
            splittedBetweenusers.add(paidByUser);

        SplitPayGroup group = groupService.getGroup(groupid);

        for ( SplitPayUser user : splittedBetweenusers){
            if (  !groupHasMember( group, user.getUserId() ) )
                throw new InvalidDataException("some users doesn't belong to this group");
        }

        int splittedAmount = amount/ splittedBetweenusers.size();
        paidByUser.setBalance( paidByUser.getBalance() + (amount - splittedAmount));
        userService.save(paidByUser);

        // Similarly for every user now
        for ( SplitPayUser user : splittedBetweenusers){
            user.setBalance( user.getBalance() - splittedAmount);
            userService.save(user);
        }
        Expense expense = new Expense();
        expense.setBillName(billname);
        expense.setAmount(amount);
        expense.setPaidOn(Calendar.getInstance());
        expense.setPaidBy(paidByUser);
        expense.setSplittedBetween(splittedBetweenusers);
        return repository.save(expense);
    }

    public boolean groupHasMember( SplitPayGroup group , int userid){
        List<SplitPayUser> users  = group.getMembers();
        for ( SplitPayUser user : users){
            if ( user.getUserId() == userid){
                return true;
            }
        }
        return false;
    }
}


// services classes are responsible for business logic
