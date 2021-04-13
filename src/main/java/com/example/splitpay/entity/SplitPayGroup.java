package com.example.splitpay.entity;
import lombok.Data;
import javax.persistence.*;
import java.util.List;
@Data
@Entity
@Table(name="SplitPayGroup")
public class SplitPayGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int groupId;

    @Column(name="GroupName", length = 30)
    String groupName;


    @Column(name="Description",length = 500)
    String description;
    // A group can have more than one members, A member can belong to more than one group

    @ManyToMany
    List<SplitPayUser> members;
    // A group can have more than one expense and a expense can only belong to a group

    @OneToMany
    List<Expense> expenses;
}
