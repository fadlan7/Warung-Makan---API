package com.enigma.wmb_api.entity;

import com.enigma.wmb_api.constant.ConstantTable;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = ConstantTable.CUSTOMER)
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "customer_name")
    private String name;

    @Column(name = "mobile_phone_no")
    private String mobilePhoneNo;

    @Column(name = "is_member")
    private Boolean isMember;

    @OneToOne
    @JoinColumn(name = "user_account_id", unique = true)
    @JsonManagedReference
    private UserAccount userAccount;
}
