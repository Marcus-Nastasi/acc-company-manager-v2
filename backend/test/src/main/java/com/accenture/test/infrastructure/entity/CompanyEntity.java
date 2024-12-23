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
@Table(name = "empresa")
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
    @Column(name = "nome_fantasia")
    private String nome_fantasia;
    @Column(name = "cep")
    private String cep;
    @ManyToMany(mappedBy = "empresas", fetch = FetchType.LAZY)
    private List<SupplierEntity> fornecedores = new ArrayList<>();
}
