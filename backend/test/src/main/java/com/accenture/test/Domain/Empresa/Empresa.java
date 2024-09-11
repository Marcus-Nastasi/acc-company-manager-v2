package com.accenture.test.Domain.Empresa;

import com.accenture.test.Domain.Fornecedor.Fornecedor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "empresa")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Empresa {

    @Id
    private String cnpj;
    @Column(name = "nome_fantasia")
    private String nome_fantasia;
    @Column(name = "cep")
    private String cep;
    @ManyToMany(mappedBy = "empresas", fetch = FetchType.LAZY)
    private List<Fornecedor> fornecedores;
}
