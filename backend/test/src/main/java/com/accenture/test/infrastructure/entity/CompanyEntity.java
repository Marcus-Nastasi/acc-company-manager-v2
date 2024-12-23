package com.accenture.test.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "company")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true)
    private String cnpj;
    @Column(name = "name")
    private String name;
    @Column(name = "cep")
    private String cep;
    @ManyToMany(mappedBy = "companies", fetch = FetchType.LAZY)
    private List<SupplierEntity> suppliers = new ArrayList<>();
}
