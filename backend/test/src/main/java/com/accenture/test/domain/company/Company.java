package com.accenture.test.domain.company;

import com.accenture.test.domain.supplier.Supplier;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Company implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String cnpj;
    private String nome_fantasia;
    private String cep;
    private List<Supplier> fornecedores;

    public Company(UUID id, String cnpj, String nome_fantasia, String cep, List<Supplier> fornecedores) {
        this.id = id;
        this.cnpj = cnpj;
        this.nome_fantasia = nome_fantasia;
        this.cep = cep;
        this.fornecedores = fornecedores != null ? new ArrayList<>(fornecedores) : new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNome_fantasia() {
        return nome_fantasia;
    }

    public void setNome_fantasia(String nome_fantasia) {
        this.nome_fantasia = nome_fantasia;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public List<Supplier> getFornecedores() {
        if (this.fornecedores == null) this.fornecedores = new ArrayList<>();
        return this.fornecedores;
    }

    public void setFornecedores(List<Supplier> fornecedores) {
        this.fornecedores = fornecedores != null ? new ArrayList<>(fornecedores) : new ArrayList<>();
    }
}
