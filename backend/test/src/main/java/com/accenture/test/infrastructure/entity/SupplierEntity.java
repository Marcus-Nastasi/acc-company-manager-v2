package com.accenture.test.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "supplier")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true)
    private String cnpj_cpf;
    @Column
    private String rg;
    @Column
    private LocalDate birth;
    @Column
    private String name;
    @Column
    private String email;
    @Column
    private String cep;
    @Column
    private boolean e_pf;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "supplier_company",
        joinColumns = @JoinColumn(name = "supplier_id"),
        inverseJoinColumns = @JoinColumn(name = "company_id")
    )
    private List<CompanyEntity> companies = new ArrayList<>();
}
