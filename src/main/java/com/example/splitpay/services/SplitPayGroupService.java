package com.example.splitpay.services;

import com.example.splitpay.entity.SplitPayGroup;
import com.example.splitpay.entity.SplitPayUser;
import com.example.splitpay.exceptions.GroupNotFoundException;
import com.example.splitpay.repository.SplitPayGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SplitPayGroupService {
    @Autowired
    SplitPayGroupRepository repository;
    @Autowired
    SplitPayUserService userService;
    public List<SplitPayGroup> getAllGroups(){
        return repository.findAll();
    }

    public SplitPayGroup getGroup(Integer id ){
        SplitPayGroup group;
        try{
          group= repository.findById(id).get();
        }
        catch(Exception e) {
            throw new GroupNotFoundException("Group " + id + " not found exception");
        }
        return group;
    }

    public SplitPayGroup createGroup(String description , String groupname , ArrayList<Integer> membersId){
        SplitPayGroup group =  new SplitPayGroup();
        group.setGroupName(groupname);
        group.setDescription(description);
        List<SplitPayUser> users = new ArrayList<>();
        for ( Integer id : membersId){
            users.add(userService.getUser(id));
        }
        group.setMembers(users);
        return repository.save(group);
    }
}
