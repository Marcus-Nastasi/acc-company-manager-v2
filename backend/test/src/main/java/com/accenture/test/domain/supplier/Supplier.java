package com.accenture.test.domain.supplier;

import com.accenture.test.domain.company.Company;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Supplier implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String cnpj_cpf;
    private String rg;
    private LocalDate nascimento;
    private String nome;
    private String email;
    private String cep;
    private boolean e_pf;
    private List<Company> companies;

    public Supplier(UUID id, String cnpj_cpf, String rg, LocalDate nascimento, String nome, String email, String cep, boolean e_pf, List<Company> companies) {
        this.id = id;
        this.cnpj_cpf = cnpj_cpf;
        this.rg = rg;
        this.nascimento = nascimento;
        this.nome = nome;
        this.email = email;
        this.cep = cep;
        this.e_pf = e_pf;
        this.companies = companies != null ? new ArrayList<>(companies) : new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCnpj_cpf() {
        return cnpj_cpf;
    }

    public void setCnpj_cpf(String cnpj_cpf) {
        this.cnpj_cpf = cnpj_cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public LocalDate getNascimento() {
        return nascimento;
    }

    public void setNascimento(LocalDate nascimento) {
        this.nascimento = nascimento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public boolean isE_pf() {
        return e_pf;
    }

    public void setE_pf(boolean e_pf) {
        this.e_pf = e_pf;
    }

    public List<Company> getEmpresas() {
        if (this.companies == null) this.companies = new ArrayList<>();
        return this.companies;
    }

    public void setEmpresas(List<Company> companies) {
        this.companies = companies != null ? new ArrayList<>(companies) : new ArrayList<>();
    }
}