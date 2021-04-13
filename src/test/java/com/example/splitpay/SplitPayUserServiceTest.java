package com.example.splitpay;

import com.example.splitpay.controller.MainController;
import com.example.splitpay.entity.Expense;
import com.example.splitpay.entity.SplitPayGroup;
import com.example.splitpay.entity.SplitPayUser;
import com.example.splitpay.exceptions.UserNotFoundException;
import com.example.splitpay.repository.SplitPayGroupRepository;
import com.example.splitpay.repository.SplitPayUserRepository;
import com.example.splitpay.services.ExpenseService;
import com.example.splitpay.services.SplitPayGroupService;
import com.example.splitpay.services.SplitPayUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
public class SplitPayUserServiceTest {
    @Mock
    private SplitPayUserRepository userRepository;

    @Mock
    private SplitPayGroupService groupService;

    @InjectMocks
    private SplitPayUserService userService;


    @BeforeEach
    @DisplayName("Setting data")
    public void set(){
        SplitPayUser u1 = new SplitPayUser();
        SplitPayUser u2 = new SplitPayUser();
        SplitPayUser u3 = new SplitPayUser();
        SplitPayUser u4 = new SplitPayUser();
        u1.setUserId(1);
        u2.setUserId(2);
        u3.setUserId(3);
        u4.setUserId(4);

        SplitPayGroup g1 = new SplitPayGroup();
        g1.setGroupId(1);
        SplitPayGroup g2 = new SplitPayGroup();
        g2.setGroupId(2);

        ArrayList<SplitPayUser> list1 = new ArrayList<>();
        ArrayList<SplitPayUser> list2 = new ArrayList<>();
        list1.add(u1);
        list1.add(u2);
        list1.add(u3);
        list2.add(u2);
        list2.add(u3);

        // set groups
        g1.setMembers(list1);
        g2.setMembers(list2);
        ArrayList<SplitPayGroup> groups = new ArrayList<>();
        groups.add(g1);
        groups.add(g2);
        Mockito.when( userRepository.findById(1)).thenReturn(Optional.of(u1));
        Mockito.when( userRepository.findById(2)).thenReturn(Optional.of(u2));
        Mockito.when( userRepository.findById(3)).thenReturn(Optional.of(u3));
        Mockito.when( userRepository.findById(4)).thenReturn(Optional.of(u4));
        Mockito.when( groupService.getAllGroups()).thenReturn(groups);


        Expense e1 = new Expense();
        e1.setExpenseId(1);
        Expense e2 = new Expense();
        e2.setExpenseId(2);
        Expense e3 = new Expense();
        e3.setExpenseId(3);

        List<SplitPayUser> l1 = new ArrayList<>();
        List<SplitPayUser> l2= new ArrayList<>();
        List<SplitPayUser> l3 = new ArrayList<>();
        l1.add(u1);
        l1.add(u3);
        e1.setSplittedBetween(l1);

        l2.add(u2);
        l2.add(u3);
        e2.setSplittedBetween(l2);

        l3.add(u1);
        l3.add(u2);
        l3.add(u3);
        e3.setSplittedBetween(l3);


        ArrayList<Expense> le1 =new  ArrayList<>();
        le1.add(e1);
        le1.add(e3);

        ArrayList<Expense> le2= new ArrayList<>();
        le2.add(e2);
        g1.setExpenses(le1);
        g2.setExpenses(le2);

    }
    @Test
    @DisplayName("Test 1")
    public void test1(){
        assertNotNull(userService);
        assertNotNull(userRepository);
    }

    @Test
    @DisplayName("Testing all users")
    public void test_getAllUsers_method (){
        List<SplitPayUser> users = new ArrayList<>();
        int count = 2;
        for ( int i =0; i <count; i++){
            users.add(new SplitPayUser());
        }

        Mockito.when( userRepository.findAll()).thenReturn(users);
        List<SplitPayUser> users2 = userService.getAllUsers();
        assertEquals(users2.size(), users.size(), "Testing whether their sizes are equal of not");
    }
    @Test
    @DisplayName("UserNotFound Negative test case")
    public void test_getUser_when_usernotexists(){
        assertThrows(UserNotFoundException.class,()->userService.getUser(20));
    }
    @Test
    @DisplayName("When user exists test case")
    public void test_getUser_positive(){
        SplitPayUser user = new SplitPayUser();
        user.setUserId(1);
        user.setBalance(2000);
        user.setUsername("username");
        Mockito.when( userRepository.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findById(2)).thenThrow( new RuntimeException("Not found"));
        SplitPayUser response_user = userService.getUser(1);
        assertEquals( response_user.getUserId() , user.getUserId() , "Testing their id to be same");
        assertThrows( UserNotFoundException.class , ()-> userService.getUser(2) );
    }


    @Test
    @DisplayName("Testing User Group")
    public void test_userGroups( ){
        List<SplitPayGroup> u2_groups = userService.userGroups(2);
        assertEquals( u2_groups.size() , 2, "Testing no of groups for u2 should be 2");
        List<SplitPayGroup> u1_groups = userService.userGroups(1);
        assertEquals(u1_groups.size() , 1 , "Testing no of groups for u1 should be 1");
        List<SplitPayGroup> u4_groups = userService.userGroups(4);
        assertEquals(u4_groups.size() , 0, "Testing there should be no group correspond to user u4");
    }
    @Test
    @DisplayName("Negative Test case for userGroups")
    public void test_userGroups_negative(){
        assertThrows( UserNotFoundException.class, ()->userService.userGroups(20));
    }
    @Test
    @DisplayName("Testing userExpenses")
    public void test_useExpenses_positive(){
        List<Expense> expenses = userService.getUserExpenses(2);
        assertEquals( expenses.size(),2 , "Total expenses by user 2 are 2");
        assertEquals( userService.getUserExpenses(3).size(),3,"Total Expenses by user 3 are 1");
        assertEquals( userService.getUserExpenses(4).size(), 0 , "Total Expenses by user 4 is 0");
    }

}
