package com.accenture.test;

import com.accenture.test.application.gateways.supplier.SupplierGateway;
import com.accenture.test.domain.company.Company;
import com.accenture.test.domain.supplier.Supplier;
import com.accenture.test.domain.supplier.SupplierPag;
import com.accenture.test.application.exception.AppException;
import com.accenture.test.application.usecase.supplier.SupplierUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SupplierTest {

    @Mock
    private SupplierGateway supplierGateway;

    @InjectMocks
    private SupplierUseCase supplierUseCase;

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

    List<Supplier> suppliers = List.of(supplier1, supplier2);
    SupplierPag supplierPag = new SupplierPag(suppliers, 0, 10, 1);

    @Test
    void getAll() {
        supplier1.setCompanies(List.of(company1, company2));
        supplier2.setCompanies(List.of(company1, company2));
        when(supplierGateway.getAll(0, 10, "nome", "cnpjCpf")).thenReturn(supplierPag);

        assertDoesNotThrow(() -> supplierUseCase.getAll(0, 10, "nome", "cnpjCpf"));
        assertEquals(0, supplierUseCase.getAll(0, 10, "nome", "cnpjCpf").getPage());

        verify(supplierGateway, times(2)).getAll(0, 10, "nome", "cnpjCpf");
    }

    @Test
    void getSingleSupplier() {
        supplier1.setCompanies(List.of(company1, company2));
        when(supplierGateway.get(any(UUID.class))).thenReturn(supplier1);

        assertDoesNotThrow(() -> supplierUseCase.get(supplier1.getId()));
        assertEquals(supplierUseCase.get(supplier1.getId()), supplier1);

        verify(supplierGateway, times(2)).get(any(UUID.class));
    }

    @Test
    void register() {
        when(supplierGateway.save(any(Supplier.class))).thenReturn(null);

        assertDoesNotThrow(() -> supplierUseCase.register(supplier1));

        // teste para registro de pessoa física que deve lançar exceção
        supplier1.setE_pf(true);
        supplier1.setRg(null);
        supplier1.setBirth(null);
        assertThrows(AppException.class, () -> supplierUseCase.register(supplier1));

        verify(supplierGateway, times(1)).save(any(Supplier.class));
    }

    @Test
    void update() {
        supplier1.setCompanies(List.of(company1, company2));
        when(supplierGateway.get(any(UUID.class))).thenReturn(supplier1);
        when(supplierGateway.save(any(Supplier.class))).thenReturn(null);

        assertDoesNotThrow(() -> supplierUseCase.update(UUID.randomUUID(), supplier1));

        // teste para registro de pessoa física que deve lançar exceção
        supplier1.setE_pf(true);
        supplier1.setRg(null);
        supplier1.setBirth(null);
        assertThrows(AppException.class, () -> {
            supplierUseCase.update(UUID.randomUUID(), supplier1);
        });

        verify(supplierGateway, times(2)).get(any(UUID.class));
        verify(supplierGateway, times(1)).save(any(Supplier.class));
    }

    @Test
    void delete() {
        supplier1.setCompanies(List.of(company1, company2));
        when(supplierGateway.delete(any(UUID.class))).thenReturn(supplier1);

        assertEquals(supplier1, supplierUseCase.delete(UUID.randomUUID()));
        assertDoesNotThrow(() -> supplierUseCase.delete(UUID.randomUUID()));

        verify(supplierGateway, times(2)).delete(any(UUID.class));
    }

    @Test
    void validatesUnderageSuppliers() {
        assertTrue(supplierUseCase.validatesUnderageSuppliers(LocalDate.now()));
        assertFalse(supplierUseCase.validatesUnderageSuppliers(LocalDate.of(1996, 10, 28)));
        assertThrows(
            DateTimeParseException.class,
            () -> supplierUseCase.validatesUnderageSuppliers(LocalDate.parse("string"))
        );
    }
}
