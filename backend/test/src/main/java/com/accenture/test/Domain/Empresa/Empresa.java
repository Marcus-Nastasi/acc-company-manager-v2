package com.accenture.test.Domain.Empresa;

import com.accenture.test.Domain.Fornecedor.Fornecedor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "empresa")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true)
    private String cnpj;
    @Column(name = "nome_fantasia")
    private String nome_fantasia;
    @Column(name = "cep")
    private String cep;
    @ManyToMany(mappedBy = "empresas", fetch = FetchType.EAGER)
    private List<Fornecedor> fornecedores;
}
