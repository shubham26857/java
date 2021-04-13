package com.example.splitpay;

import com.example.splitpay.controller.MainController;
import com.example.splitpay.entity.SplitPayGroup;
import com.example.splitpay.entity.SplitPayUser;
import com.example.splitpay.repository.SplitPayGroupRepository;
import com.example.splitpay.repository.SplitPayUserRepository;
import com.example.splitpay.services.ExpenseService;
import com.example.splitpay.services.SplitPayGroupService;
import com.example.splitpay.services.SplitPayUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SplityPayGroupServiceTest {
    @Mock
    private SplitPayGroupRepository groupRepository;
    @InjectMocks
    private SplitPayGroupService groupService;

    @Test
    @DisplayName( "Testing getAll Groups")
    public void test_getAllGroups(){
        List<SplitPayGroup> groups1 = new ArrayList<>();
        int count= 2;
        for ( int i =0; i < count ; i++){
            SplitPayGroup group = new SplitPayGroup();
            group.setGroupId(i);
            groups1.add( group);
        }

        Mockito.when( groupRepository.findAll()).thenReturn(groups1);
        List<SplitPayGroup> groups2 = groupService.getAllGroups();
        assertEquals( groups1.size(), groups2.size() , "Testing their size to be same");
        assertEquals( groups1.get(0).getGroupId() , groups2.get(0).getGroupId());
    }


}
