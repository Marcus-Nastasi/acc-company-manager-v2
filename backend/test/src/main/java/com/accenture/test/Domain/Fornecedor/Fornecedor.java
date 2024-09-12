package com.accenture.test.Domain.Fornecedor;

import com.accenture.test.Domain.Empresa.Empresa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "fornecedor")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true)
    private String cnpj_cpf;
    @Column
    private String rg;
    @Column
    private LocalDate nascimento;
    @Column
    private String nome;
    @Column
    private String email;
    @Column
    private String cep;
    @Column
    private boolean e_pf;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "fornecedor_empresa",
        joinColumns = @JoinColumn(name = "fornecedor_id"),
        inverseJoinColumns = @JoinColumn(name = "empresa_id")
    )
    private List<Empresa> empresas;
}
