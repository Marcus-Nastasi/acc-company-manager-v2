package com.accenture.test;

import com.accenture.test.application.gateways.supplier.SupplierGateway;
import com.accenture.test.domain.company.Company;
import com.accenture.test.domain.supplier.Supplier;
import com.accenture.test.domain.supplier.SupplierPag;
import com.accenture.test.infrastructure.entity.CompanyEntity;
import com.accenture.test.adapter.input.supplier.SupplierRequestDto;
import com.accenture.test.infrastructure.entity.SupplierEntity;
import com.accenture.test.application.exception.AppException;
import com.accenture.test.infrastructure.persistence.SupplierRepo;
import com.accenture.test.application.usecase.company.CompanyUseCase;
import com.accenture.test.application.usecase.supplier.SupplierUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class SupplierTest {

    @Mock
    private SupplierRepo supplierRepo;
    @Mock
    private SupplierGateway supplierGateway;
    @Mock
    private CompanyUseCase companyUseCase;

    @InjectMocks
    private SupplierUseCase supplierUseCase;

    // entidades de empresa
    CompanyEntity companyEntity1 = new CompanyEntity();
    CompanyEntity companyEntity2 = new CompanyEntity();

    // entidades de fornecedor
    SupplierEntity supplierEntity1 = new SupplierEntity();
    SupplierEntity supplierEntity2 = new SupplierEntity();

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
    void registrar_test() {
        when(supplierRepo.save(any(SupplierEntity.class))).thenReturn(null);
        assertDoesNotThrow(() -> supplierUseCase.register(supplier1));
        // teste para registro de pessoa física que deve lançar exceção
        assertThrows(AppException.class, () -> {
            supplierUseCase.register(supplier1);
        });
        verify(supplierRepo, times(1)).save(any(SupplierEntity.class));
    }

    @Test
    void atualizar_test() {
        SupplierRequestDto supplierRequestDto = new SupplierRequestDto(
                "cnpj_cpf", "rg", LocalDate.now(), "nome", "email", "04632011", true
        );
        SupplierRequestDto registrarFornecedorPFDTO = new SupplierRequestDto(
                "cnpj_cpf", null, null, "nome", "email", "04632011", true
        );
        supplierEntity1.setCompanies(List.of(companyEntity1, companyEntity2));
        when(supplierRepo.findById(any(UUID.class))).thenReturn(Optional.of(supplierEntity1));
        when(supplierRepo.save(any(SupplierEntity.class))).thenReturn(null);
        assertDoesNotThrow(() -> supplierUseCase.update(UUID.randomUUID(), supplier1));
        // teste para registro de pessoa física que deve lançar exceção
        assertThrows(AppException.class, () -> {
            supplierUseCase.update(UUID.randomUUID(), supplier1);
        });
        verify(supplierRepo, times(2)).findById(any(UUID.class));
        verify(supplierRepo, times(1)).save(any(SupplierEntity.class));
    }

    @Test
    void deletar_test() {
        supplierEntity1.setCompanies(List.of(companyEntity1, companyEntity2));
        when(supplierRepo.findById(any(UUID.class))).thenReturn(Optional.of(supplierEntity1));
        assertDoesNotThrow(() -> supplierUseCase.delete(UUID.randomUUID()));
        verify(supplierRepo, times(1)).deleteById(any(UUID.class));
    }

    @Test
    void valida_menor_test() {
        assertTrue(supplierUseCase.validatesUnderageSuppliers(LocalDate.now()));
        assertFalse(supplierUseCase.validatesUnderageSuppliers(LocalDate.of(1996, 10, 28)));
        assertThrows(
            DateTimeParseException.class,
            () -> supplierUseCase.validatesUnderageSuppliers(LocalDate.parse("string"))
        );
    }
}
