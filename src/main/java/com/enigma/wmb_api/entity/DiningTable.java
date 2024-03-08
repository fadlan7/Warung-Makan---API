package com.enigma.wmb_api.entity;

import com.enigma.wmb_api.constant.ConstantTable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = ConstantTable.DINING_TABLE)
public class DiningTable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank(message = "Name is required")
    @Column(name = "table_name", nullable = false)
    private String name;

}
