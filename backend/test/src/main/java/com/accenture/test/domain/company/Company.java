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
    private String name;
    private String cep;
    private List<Supplier> suppliers;

    public Company(UUID id, String cnpj, String name, String cep, List<Supplier> suppliers) {
        this.id = id;
        this.cnpj = cnpj;
        this.name = name;
        this.cep = cep;
        this.suppliers = suppliers != null ? new ArrayList<>(suppliers) : new ArrayList<>();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public List<Supplier> getSuppliers() {
        if (this.suppliers == null) this.suppliers = new ArrayList<>();
        return this.suppliers;
    }

    public void setSuppliers(List<Supplier> suppliers) {
        this.suppliers = suppliers != null ? new ArrayList<>(suppliers) : new ArrayList<>();
    }
}
