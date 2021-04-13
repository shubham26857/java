package com.example.splitpay.entity;

import lombok.Data;
import lombok.Generated;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@Table(name="Expense")
public class Expense {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    int expenseId;
    String billName;
    int amount;

    @Temporal(TemporalType.DATE)
    Calendar paidOn;

    // An expense can only be paid by one, but a user can pay more than one expenses
    @ManyToOne
    SplitPayUser paidBy;
    // An expense can be splitted in more than one users, and a user can be present in more than one expense
    // I am making this relation as unidirectional. only this object serves as the purpose of the truth
    @ManyToMany
    List<SplitPayUser> splittedBetween ;
}
