package com.accenture.test;

import com.accenture.test.application.gateways.company.CompanyGateway;
import com.accenture.test.domain.cep.Cep;
import com.accenture.test.domain.company.Company;
import com.accenture.test.domain.company.CompanyPag;
import com.accenture.test.domain.supplier.Supplier;
import com.accenture.test.infrastructure.entity.CompanyEntity;
import com.accenture.test.infrastructure.entity.SupplierEntity;
import com.accenture.test.application.exception.AppException;
import com.accenture.test.infrastructure.persistence.CompanyRepo;
import com.accenture.test.infrastructure.persistence.SupplierRepo;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CompanyTest {

    @Mock
    private CompanyRepo companyRepo;
    @Mock
    private CompanyGateway companyGateway;
    @Mock
    private SupplierRepo supplierRepo;
    @Mock
    private CepUseCase cepUseCase;
    @Mock
    private SupplierUseCase supplierUseCase;
    @InjectMocks
    private CompanyUseCase companyUseCase;

    // entidades de empresa
    CompanyEntity companyEntity1 = new CompanyEntity();
    CompanyEntity companyEntity2 = new CompanyEntity();

    Company company1 = new Company(
        UUID.randomUUID(),
        "",
        "",
        "",
        List.of()
    );

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

    List<CompanyEntity> companyEntityList = List.of(companyEntity1, companyEntity2);
    CompanyPag companyPag = new CompanyPag(List.of(), 0, 10, 0);

    @Test
    void getAll() {
        when(companyGateway.getAll(anyInt(), anyInt(), anyString(), anyString(), anyString())).thenReturn(companyPag);
        companyEntity1.setSuppliers(new ArrayList<>(List.of(supplierEntity1, supplierEntity2)));
        companyEntity2.setSuppliers(new ArrayList<>(List.of(supplierEntity1, supplierEntity2)));
        supplierEntity1.setCompanies(new ArrayList<>(List.of(companyEntity1, companyEntity2)));
        supplierEntity2.setCompanies(new ArrayList<>(List.of(companyEntity1, companyEntity2)));
        CompanyPag result = companyUseCase.getAll(1, 1, "nome", "cnpjCpf", "cep");
        assertDoesNotThrow(() -> result);
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
    void registrar_test() {
        when(companyRepo.save(any(CompanyEntity.class))).thenReturn(null);
        assertDoesNotThrow(() -> companyUseCase.register(company1));
        verify(companyRepo, times(1)).save(any(CompanyEntity.class));
    }

    @Test
    void atualizar_test() {
        companyEntity1.setSuppliers(List.of(supplierEntity1, supplierEntity2));
        when(companyRepo.findById(any(UUID.class))).thenReturn(Optional.of(companyEntity1));
        when(companyRepo.save(any(CompanyEntity.class))).thenReturn(null);
        assertDoesNotThrow(() -> {
            companyUseCase.update(UUID.randomUUID(), company1);
        });
        verify(companyRepo, times(1)).findById(any(UUID.class));
        verify(companyRepo, times(1)).save(any(CompanyEntity.class));
    }

    @Test
    void deletar_test() {
        supplierEntity1.setCompanies(new ArrayList<>(List.of(companyEntity1, companyEntity2)));
        supplierEntity2.setCompanies(new ArrayList<>(List.of(companyEntity1, companyEntity2)));
        companyEntity1.setSuppliers(new ArrayList<>(List.of(supplierEntity1, supplierEntity2)));
        when(companyRepo.findById(any(UUID.class))).thenReturn(Optional.of(companyEntity1));
        when(supplierRepo.save(any(SupplierEntity.class))).thenReturn(supplierEntity1);
        verify(companyRepo, times(1)).findById(any(UUID.class));
        assertFalse(supplierEntity1.getCompanies().contains(companyEntity1));
        assertFalse(supplierEntity2.getCompanies().contains(companyEntity1));
        verify(supplierRepo, times(2)).save(any(SupplierEntity.class));
        verify(companyRepo, times(1)).deleteById(any(UUID.class));
//        assertNotNull(result);
    }

    @Test
    void associa_fornecedor_test() {
        companyEntity1.setSuppliers(new ArrayList<>(List.of(supplierEntity1, supplierEntity2)));
        supplierEntity1.setCompanies(new ArrayList<>(List.of(companyEntity1, companyEntity2)));
        when(companyRepo.findById(any(UUID.class)))
            .thenReturn(Optional.of(companyEntity1));
        when(supplierRepo.findById(any(UUID.class)))
            .thenReturn(Optional.of(supplierEntity1));
        when(companyRepo.save(any(CompanyEntity.class)))
            .thenReturn(companyEntity1);
        when(supplierRepo.save(any(SupplierEntity.class)))
            .thenReturn(supplierEntity1);

        Company result = companyUseCase.associateSupplier(UUID.randomUUID(), UUID.randomUUID());
        verify(companyRepo, times(1)).findById(any(UUID.class));
        verify(supplierRepo, times(1)).findById(any(UUID.class));
        assertTrue(supplierEntity1.getCompanies().contains(companyEntity1));
        verify(supplierRepo, times(1)).save(any(SupplierEntity.class));
        verify(companyRepo, times(1)).save(any(CompanyEntity.class));
        assertNotNull(result);

        // não deve permitir a associação de fornecedores pessoa física e menores de idade em empresas de ceps do Paraná
        SupplierEntity supplierEntity = new SupplierEntity();
        supplierEntity.setRg("321321112");
        supplierEntity.setBirth(LocalDate.now());
        supplierEntity.setCep("80000-000");
        CompanyEntity companyEntity = new CompanyEntity(UUID.randomUUID(), "cnpj", "nome", "80000-000", new ArrayList<>(List.of(supplierEntity)));
        supplierEntity.setCompanies(new ArrayList<>(List.of(companyEntity)));
        assertThrows(AppException.class, () -> {
            companyUseCase.associateSupplier(supplierEntity.getId(), companyEntity.getId());
        });
    }

    @Test
    void desassocia_fornecedor_test() {
        companyEntity1.setSuppliers(new ArrayList<>(List.of(supplierEntity1, supplierEntity2)));
        supplierEntity1.setCompanies(new ArrayList<>(List.of(companyEntity1, companyEntity2)));
        when(companyRepo.findById(any(UUID.class)))
                .thenReturn(Optional.of(companyEntity1));
        when(supplierRepo.findById(any(UUID.class)))
                .thenReturn(Optional.of(supplierEntity1));
        when(companyRepo.save(any(CompanyEntity.class)))
                .thenReturn(companyEntity1);
        when(supplierRepo.save(any(SupplierEntity.class)))
                .thenReturn(supplierEntity1);

        Company result = companyUseCase.disassociateSupplier(UUID.randomUUID(), UUID.randomUUID());
        verify(companyRepo, times(1)).findById(any(UUID.class));
        verify(supplierRepo, times(1)).findById(any(UUID.class));
        assertFalse(companyEntity1.getSuppliers().contains(supplierEntity1));
        assertFalse(supplierEntity1.getCompanies().contains(companyEntity1));
        verify(supplierRepo, times(1)).save(any(SupplierEntity.class));
        verify(companyRepo, times(1)).save(any(CompanyEntity.class));
        assertNotNull(result);
    }

    @Test
    void isPr_test() {
        String cepSp = "05999-999";
        Cep spResponse = new Cep("05999-999", "", "São Paulo", "", "", "", "SP", "", "", "", "", "", "");
        when(cepUseCase.getCep(cepSp)).thenReturn(spResponse);

        // simulando resposta para um CEP do Paraná (PR)
        String cepPr = "80000-000";
        Cep prResponse = new Cep("80000-000", "", "Curitiba", "", "", "", "PR", "", "", "", "", "", "");
        when(cepUseCase.getCep(cepPr)).thenReturn(prResponse);

        assertFalse(companyUseCase.isPr(cepSp));
        assertTrue(companyUseCase.isPr(cepPr));
        verify(cepUseCase, times(1)).getCep(cepSp);
        verify(cepUseCase, times(1)).getCep(cepPr);
    }

    @Test
    void vincula_empresa_fornecedor_test() {
        companyEntity1.setSuppliers(new ArrayList<>(List.of(supplierEntity1, supplierEntity2)));
        supplierEntity1.setCompanies(new ArrayList<>(List.of(companyEntity1)));
        when(companyRepo.save(any(CompanyEntity.class))).thenReturn(companyEntity1);
        when(supplierRepo.save(any(SupplierEntity.class))).thenReturn(supplierEntity1);
        assertDoesNotThrow(() -> {
            companyUseCase.linkCompanySupplier(company1, supplier1);
        });
        verify(companyRepo, times(1)).save(any(CompanyEntity.class));
        verify(supplierRepo, times(1)).save(any(SupplierEntity.class));
    }
}
