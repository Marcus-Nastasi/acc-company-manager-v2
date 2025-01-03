package com.accenture.test;

import com.accenture.test.application.gateways.company.CompanyGateway;
import com.accenture.test.domain.cep.Cep;
import com.accenture.test.domain.company.Company;
import com.accenture.test.domain.company.CompanyPag;
import com.accenture.test.domain.supplier.Supplier;
import com.accenture.test.application.exception.AppException;
import com.accenture.test.application.usecase.cep.CepUseCase;
import com.accenture.test.application.usecase.company.CompanyUseCase;
import com.accenture.test.application.usecase.supplier.SupplierUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompanyTest {

    @Mock
    private CompanyGateway companyGateway;
    @Mock
    private CepUseCase cepUseCase;
    @Mock
    private SupplierUseCase supplierUseCase;

    @InjectMocks
    private CompanyUseCase companyUseCase;

    Company company1 = new Company(
        UUID.randomUUID(),
        "",
        "",
        "",
        List.of()
    );

    Company company2 = new Company(
            UUID.randomUUID(),
            "",
            "",
            "",
            List.of()
    );

    Supplier supplier1 = new Supplier(
            UUID.randomUUID(),
            "",
            "",
            LocalDate.now(),
            "",
            "",
            "",
            false,
            List.of()
    );

    Supplier supplier2 = new Supplier(
            UUID.randomUUID(),
            "",
            "",
            LocalDate.now(),
            "",
            "",
            "",
            false,
            List.of()
    );

    CompanyPag companyPag = new CompanyPag(List.of(), 0, 10, 0);

    Cep cep = new Cep("000001", "", "", "", "", "", "SP", "", "", "", "", "", "");
    Cep cepPr = new Cep("000008", "", "", "", "", "", "PR", "", "", "", "", "", "");

    @Test
    void getAll() {
        company1.setSuppliers(new ArrayList<>(List.of(supplier1, supplier2)));
        company2.setSuppliers(new ArrayList<>(List.of(supplier1, supplier2)));
        supplier1.setCompanies(new ArrayList<>(List.of(company1, company2)));
        supplier2.setCompanies(new ArrayList<>(List.of(company1, company2)));

        when(companyGateway.getAll(anyInt(), anyInt(), anyString(), anyString(), anyString())).thenReturn(companyPag);

        assertDoesNotThrow(() -> companyUseCase.getAll(1, 1, "nome", "cnpjCpf", "cep"));

        verify(companyGateway, times(1)).getAll(1, 1, "nome", "cnpjCpf", "cep");
    }

    @Test
    void getSingleCompany() {
        company1.setSuppliers(List.of(supplier1));

        when(companyGateway.get(any(UUID.class))).thenReturn(company1);

        assertDoesNotThrow(() -> companyUseCase.get(UUID.randomUUID()));
        assertEquals(companyUseCase.get(UUID.randomUUID()), company1);
        assertEquals(companyUseCase.get(UUID.randomUUID()).getName(), company1.getName());

        verify(companyGateway, times(3)).get(any(UUID.class));
    }

    @Test
    void register() {
        when(companyGateway.save(any(Company.class))).thenReturn(null);

        assertDoesNotThrow(() -> companyUseCase.register(company1));

        verify(companyGateway, times(1)).save(any(Company.class));
    }

    @Test
    void update() {
        company1.setSuppliers(List.of(supplier1));
        when(companyGateway.get(any(UUID.class))).thenReturn(company1);
        when(companyGateway.save(any(Company.class))).thenReturn(null);
        assertDoesNotThrow(() -> {
            companyUseCase.update(UUID.randomUUID(), company1);
        });
        verify(companyGateway, times(1)).get(any(UUID.class));
        verify(companyGateway, times(1)).save(any(Company.class));
    }

    @Test
    void delete() {
        when(companyGateway.delete(any(UUID.class))).thenReturn(company1);
        assertDoesNotThrow(() -> companyUseCase.delete(company1.getId()));
        verify(companyGateway, times(1)).delete(any(UUID.class));
    }

    @Test
    void associateSupplier() {
        company1.setSuppliers(new ArrayList<>(List.of(supplier1, supplier2)));
        supplier1.setCompanies(new ArrayList<>(List.of(company1, company2)));
        when(companyGateway.get(any(UUID.class))).thenReturn(company1);
        when(supplierUseCase.get(any(UUID.class))).thenReturn(supplier1);
        when(companyGateway.save(any(Company.class))).thenReturn(company1);
        when(supplierUseCase.save(any(Supplier.class))).thenReturn(supplier1);

        Company result = companyUseCase.associateSupplier(UUID.randomUUID(), UUID.randomUUID());
        assertNotNull(result);

        // não deve permitir a associação de fornecedores pessoa física e menores de idade em empresas de ceps do Paraná
        supplier1.setE_pf(true);
        supplier1.setRg("321321112");
        supplier1.setBirth(LocalDate.now());
        company1.setCep(cepPr.getCep());
        supplier1.setCompanies(new ArrayList<>(List.of(company1)));

        when(cepUseCase.getCep(cepPr.getCep())).thenReturn(cepPr);
        when(supplierUseCase.validatesUnderageSuppliers(supplier1.getBirth())).thenReturn(true);

        assertThrows(AppException.class, () -> companyUseCase.associateSupplier(company1.getId(), supplier1.getId()));

        verify(companyGateway, times(2)).get(any(UUID.class));
        verify(companyGateway, times(1)).save(any(Company.class));
        verify(supplierUseCase, times(1)).validatesUnderageSuppliers(any(LocalDate.class));
        verify(supplierUseCase, times(1)).save(any(Supplier.class));
    }

    @Test
    void disassociateSupplier() {
        company1.setSuppliers(new ArrayList<>(List.of(supplier1, supplier2)));
        supplier1.setCompanies(new ArrayList<>(List.of(company1, company2)));
        when(companyGateway.get(any(UUID.class))).thenReturn(company1);
        when(supplierUseCase.get(any(UUID.class))).thenReturn(supplier1);
        when(companyGateway.save(any(Company.class))).thenReturn(company1);
        when(supplierUseCase.save(any(Supplier.class))).thenReturn(supplier1);

        Company result = companyUseCase.disassociateSupplier(UUID.randomUUID(), UUID.randomUUID());

        verify(companyGateway, times(1)).get(any(UUID.class));
        verify(supplierUseCase, times(1)).get(any(UUID.class));

        assertFalse(company1.getSuppliers().contains(supplier1));
        assertFalse(supplier1.getCompanies().contains(company1));

        verify(supplierUseCase, times(1)).save(any(Supplier.class));
        verify(companyGateway, times(1)).save(any(Company.class));

        assertNotNull(result);
    }

    @Test
    void isPr() {
        when(cepUseCase.getCep(cep.getCep())).thenReturn(cep);
        when(cepUseCase.getCep(cepPr.getCep())).thenReturn(cepPr);

        assertFalse(companyUseCase.isPr(cep.getCep()));
        assertTrue(companyUseCase.isPr(cepPr.getCep()));

        verify(cepUseCase, times(1)).getCep(cep.getCep());
        verify(cepUseCase, times(1)).getCep(cepPr.getCep());
    }

    @Test
    void linkCompanySupplier() {
        company1.setSuppliers(new ArrayList<>(List.of(supplier1, supplier2)));
        supplier1.setCompanies(new ArrayList<>(List.of(company1)));

        when(companyGateway.save(any(Company.class))).thenReturn(company1);
        when(supplierUseCase.save(any(Supplier.class))).thenReturn(supplier1);

        assertDoesNotThrow(() -> companyUseCase.linkCompanySupplier(company1, supplier1));

        verify(companyGateway, times(1)).save(any(Company.class));
        verify(supplierUseCase, times(1)).save(any(Supplier.class));
    }

    @Test
    void unlinkCompanySupplier() {
        company1.setSuppliers(new ArrayList<>(List.of(supplier1, supplier2)));
        supplier1.setCompanies(new ArrayList<>(List.of(company1)));

        when(companyGateway.save(any(Company.class))).thenReturn(company1);
        when(supplierUseCase.save(any(Supplier.class))).thenReturn(supplier1);

        assertDoesNotThrow(() -> companyUseCase.unlinkCompanySupplier(company1, supplier1));

        verify(companyGateway, times(1)).save(any(Company.class));
        verify(supplierUseCase, times(1)).save(any(Supplier.class));
    }
}
