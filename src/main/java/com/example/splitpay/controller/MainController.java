package com.example.splitpay.controller;

import com.example.splitpay.entity.Expense;
import com.example.splitpay.entity.SplitPayGroup;
import com.example.splitpay.entity.SplitPayUser;
import com.example.splitpay.exceptions.AuthenticationFailedException;
import com.example.splitpay.exceptions.GroupNotFoundException;
import com.example.splitpay.exceptions.InvalidDataException;
import com.example.splitpay.exceptions.UserNotFoundException;
import com.example.splitpay.services.ExpenseService;
import com.example.splitpay.services.SplitPayGroupService;
import com.example.splitpay.services.SplitPayUserService;
import com.sun.mail.iap.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RestController
public class MainController {
    @Autowired
    SplitPayUserService userService ;

    @Autowired
    SplitPayGroupService groupService;

    @Autowired
    ExpenseService expenseService;

    // ping the server for testing whether it is available or not .
    @GetMapping("/test")
    public String test(){
        throw new NullPointerException("this is null request");
    //    return "<h1> This is SpringBoot hosted server.</h1>";
    }

    // Get list of users
    @GetMapping ("/allusers")
    public List<SplitPayUser>  getAllUsers(){
        return userService.getAllUsers();
    }


    @GetMapping( value="/allgroups")
    public List<SplitPayGroup> allGroups( ){
        return groupService.getAllGroups();
    }

    // List of all bills of the specified group
    // The userid is here in case of authentication
    @GetMapping ( value="/{userid}/{groupid}/groupexpenses")
    public List<Expense> getGroupExpenses(@PathVariable(name="userid") int userid ,
                                          @PathVariable(name="groupid") int groupid){
        // Authenticating
        authenticate(userid);
        SplitPayGroup group = groupService.getGroup(groupid);
        return group.getExpenses();
    }

    // A template for authenticating user
    public void authenticate(int token){
        try{
            userService.getUser(token);
        }catch( Exception e){
            throw new AuthenticationFailedException("Not authorized");
        }

    }


    // This method returns all the expenses the current user has done.
    @GetMapping ( value = "/{userid}/userexpenses")
    public List<Expense> userExpense( @PathVariable(name="userid") Integer userid){
        authenticate(userid);
        return userService.getUserExpenses(userid);
    }


    // This method returns all the groups this user belongs to.
    @GetMapping ( value="/{userid}/groups")
    public List<SplitPayGroup> userGroups( @PathVariable(name="userid") Integer userid ){
        authenticate(userid);
        return userService.userGroups(userid);
    }



    @PostMapping( value="/adduser")
    public ResponseEntity<SplitPayUser>  addUser(@RequestBody SplitPayUser user ){
        user = userService.save(user);
        return new ResponseEntity<SplitPayUser>( user , HttpStatus.ACCEPTED);
    }


    @PostMapping(value="/{userid}/addgroup")
    public ResponseEntity<SplitPayGroup> addGroup(@PathVariable(name="userid") Integer userid , @RequestBody Map<String,Object> map){
        // You can find out the type of each object in map using getClass() method of object.
        String groupname = (String)map.get("groupname");
        String description = (String)map.get("description");
        ArrayList<Integer> membersId = (ArrayList<Integer>) map.get("members");
        if ( groupname == null || description == null || membersId == null)
            return new ResponseEntity<SplitPayGroup>(HttpStatus.BAD_REQUEST);
        SplitPayGroup group = groupService.createGroup(description,groupname,membersId);
        return new ResponseEntity<SplitPayGroup>(group , HttpStatus.ACCEPTED);
    }

    // Adding a new bill
    @PostMapping(value="/{userid}/{groupid}/addbill")
    public ResponseEntity<Expense> addBill( @PathVariable(name="groupid") Integer groupid,
                                            @RequestBody Map<String, Object> params){

        SplitPayGroup group = groupService.getGroup(groupid);
        String billname = (String)params.get("billname");
        Integer amount = (Integer) params.get("amount");
        String paidOn = (String) params.get("paidon");
        Integer paidBy = (Integer) params.get("paidby");
        ArrayList<Integer> splittedBetweenMembersId = (ArrayList<Integer>) params.get("splittedbetween");

        if( group == null || billname == null || amount == null || paidOn == null || paidBy == null ){
            return new ResponseEntity<Expense>(HttpStatus.BAD_REQUEST);
        }

        Expense expense = expenseService.createExpense(billname,amount,paidOn,paidBy,splittedBetweenMembersId,groupid);
        if ( expense == null ){
            return new ResponseEntity<Expense> (HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Expense> (expense, HttpStatus.ACCEPTED);
    }


    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<String> handleInvalidDataException( InvalidDataException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(GroupNotFoundException.class)
    public ResponseEntity<String> handleGroupNotFoundException( GroupNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException( UserNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }




}
