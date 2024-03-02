package com.enigma.wmb_api.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "m_trans_type")
public class TransactionType {
    @Id
    private String id;


//    @Enumerated(EnumType.STRING)
    @Column(name = "description")
    private String description;
}
