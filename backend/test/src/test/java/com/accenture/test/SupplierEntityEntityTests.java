package com.accenture.test;

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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class SupplierEntityEntityTests {

    @Mock
    private SupplierRepo supplierRepo;
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

    List<SupplierEntity> fornecedores = List.of(supplierEntity1, supplierEntity2);
    Page<SupplierEntity> fornecedorPage = new PageImpl<>(fornecedores);

    @Test
    void buscar_tudo_test() {
        when(supplierRepo.filtrarFornecedores(anyString(), anyString(), any(Pageable.class))).thenReturn(fornecedorPage);
        supplierEntity1.setEmpresas(List.of(companyEntity1, companyEntity2));
        supplierEntity2.setEmpresas(List.of(companyEntity1, companyEntity2));
        SupplierPag<Supplier> result = supplierUseCase.buscar_tudo(1, 1, "nome", "cnpjCpf");
        assertDoesNotThrow(() -> result);
        verify(supplierRepo, times(1)).filtrarFornecedores("nome", "cnpjCpf", PageRequest.of(1, 1));
    }

    @Test
    void buscar_um_test() {
        supplierEntity1.setEmpresas(List.of(companyEntity1, companyEntity2));
        Supplier f = supplierUseCase.buscar_um(supplierEntity1.getId());
        when(supplierRepo.findById(any(UUID.class))).thenReturn(Optional.of(supplierEntity1));
        assertDoesNotThrow(() -> supplierUseCase.buscar_um(UUID.randomUUID()));
        assertEquals(supplierUseCase.buscar_um(UUID.randomUUID()), f);
        verify(supplierRepo, times(2)).findById(any(UUID.class));
    }

    @Test
    void registrar_test() {
        when(supplierRepo.save(any(SupplierEntity.class))).thenReturn(null);
        assertDoesNotThrow(() -> supplierUseCase.registrar(supplier1));
        // teste para registro de pessoa física que deve lançar exceção
        assertThrows(AppException.class, () -> {
            supplierUseCase.registrar(supplier1);
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
        supplierEntity1.setEmpresas(List.of(companyEntity1, companyEntity2));
        when(supplierRepo.findById(any(UUID.class))).thenReturn(Optional.of(supplierEntity1));
        when(supplierRepo.save(any(SupplierEntity.class))).thenReturn(null);
        assertDoesNotThrow(() -> supplierUseCase.atualizar(UUID.randomUUID(), supplier1));
        // teste para registro de pessoa física que deve lançar exceção
        assertThrows(AppException.class, () -> {
            supplierUseCase.atualizar(UUID.randomUUID(), supplier1);
        });
        verify(supplierRepo, times(2)).findById(any(UUID.class));
        verify(supplierRepo, times(1)).save(any(SupplierEntity.class));
    }

    @Test
    void deletar_test() {
        supplierEntity1.setEmpresas(List.of(companyEntity1, companyEntity2));
        when(supplierRepo.findById(any(UUID.class))).thenReturn(Optional.of(supplierEntity1));
        assertDoesNotThrow(() -> supplierUseCase.deletar(UUID.randomUUID()));
        verify(supplierRepo, times(1)).deleteById(any(UUID.class));
    }

    @Test
    void valida_menor_test() {
        assertTrue(supplierUseCase.validaFornecedorMenor(LocalDate.now()));
        assertFalse(supplierUseCase.validaFornecedorMenor(LocalDate.of(1996, 10, 28)));
        assertThrows(
            DateTimeParseException.class,
            () -> supplierUseCase.validaFornecedorMenor(LocalDate.parse("string"))
        );
    }
}
