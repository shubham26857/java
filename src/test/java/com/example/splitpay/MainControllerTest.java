package com.example.splitpay;

import com.example.splitpay.controller.MainController;
import com.example.splitpay.entity.Expense;
import com.example.splitpay.entity.SplitPayGroup;
import com.example.splitpay.entity.SplitPayUser;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureMockMvc
public class MainControllerTest {
    @Autowired
    ObjectMapper mapper ;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private SplitPayUserRepository userRepository;

    @MockBean
    private SplitPayUserService userService;

    @MockBean
    private SplitPayGroupService groupService;
    @MockBean
    private ExpenseService expenseService;

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

        List<SplitPayUser> users  = new ArrayList<>();
        users.add(u1);
        users.add(u2);
        users.add(u3);
        users.add(u4);
        Mockito.when(userService.getAllUsers()).thenReturn(users);
        Mockito.when( userService.getUser(1)).thenReturn( u1);
        Mockito.when( userService.getUser(2)).thenReturn( u2);
        Mockito.when( userService.getUser(3)).thenReturn( u3);
        Mockito.when( userService.getUser(4)).thenReturn( u4);
        Mockito.when( userRepository.findById(1)).thenReturn(Optional.of(u1));
        Mockito.when( userRepository.findById(2)).thenReturn(Optional.of(u2));
        Mockito.when( userRepository.findById(3)).thenReturn(Optional.of(u3));
        Mockito.when( userRepository.findById(4)).thenReturn(Optional.of(u4));


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

        // set groups members
        g1.setMembers(list1);
        g2.setMembers(list2);

        ArrayList<SplitPayGroup> groups = new ArrayList<>();
        groups.add(g1);
        groups.add(g2);


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
    @DisplayName( "Testing autowiring")
    public void test(){
        assertNotNull(mapper);
        assertNotNull(mockMvc);
        assertNotNull(userService);
        assertNotNull(expenseService);
        assertNotNull(groupService);
        assertTrue(true);
    }

    @Test
    @DisplayName("Testing /alluser functinality")
    public void test_allusers(){
        List<SplitPayUser> users = new ArrayList<>();
        for ( int i =0; i< 5 ; i++){
            SplitPayUser user = new SplitPayUser();
            user.setUserId(i);
            users.add(user);
        }

        try {
            Mockito.when(userService.getAllUsers()).thenReturn( users);
            MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/allusers"))
                    .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
            List<SplitPayUser> res_users = mapper.readValue(mvcResult.getResponse().getContentAsString(),List.class);
            assertEquals(res_users.size() , users.size() , "Asserting settted users and responde user's list size should be same");
        }catch (Exception e){
            System.out.println("Exception caught ");
        }
    }

    @Test
    @DisplayName("Testing /allgroups functionality")
    public void test_allgroups(){
        List<SplitPayGroup> groups = new ArrayList<>();
        for( int i =0; i < 5 ;i ++){
            SplitPayGroup group = new SplitPayGroup();
            group.setGroupId(i+1);
            groups.add(group);
        }
        try{
            Mockito.when(groupService.getAllGroups()).thenReturn(groups);
            MvcResult mvcResult = this.mockMvc.perform( MockMvcRequestBuilders.get("/allgroups"))
                    .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
            List<SplitPayGroup> res_groups = mapper.readValue(mvcResult.getResponse().getContentAsString(), List.class);
            assertEquals( res_groups.size(), groups.size(), "Asserting setted groups and returned groups must be of same size ");
        }catch(Exception e){
            System.out.println("Exception caught");
        }
    }

    @Test
    @DisplayName("Testing userGroups controller method")
    public void test_userGroups(){
        int id = 1;
        try{
            MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/"+id+"/groups/"))
                    .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
            List<SplitPayGroup> res_groups = mapper.readValue(mvcResult.getResponse().getContentAsString(),List.class);
            System.out.println("res_groups size : "+ res_groups.size());
/*            List<SplitPayGroup> groups2  = userService.userGroups(id);
            System.out.println("groups "+id+ ": " + groups2.size());*/
            System.out.println( "user id of returned used for given 1 parameter : "+ userService.getUser(id).getUserId());
            System.out.println(" groups size allgroups " + groupService.getAllGroups().size());

        }
        catch(Exception e){
            System.out.println("exception occcurred" + e.getMessage());
        }
    }
}
