package com.accenture.test;

import com.accenture.test.Domain.Cep.DTO.CepResponseDTO;
import com.accenture.test.Domain.Empresa.DTO.*;
import com.accenture.test.Domain.Empresa.Empresa;
import com.accenture.test.Domain.Fornecedor.Fornecedor;
import com.accenture.test.Exception.AppException;
import com.accenture.test.Repository.Empresa.EmpresaRepo;
import com.accenture.test.Repository.Fornecedor.FornecedorRepo;
import com.accenture.test.Service.Cep.CepService;
import com.accenture.test.Service.Empresa.EmpresaService;
import com.accenture.test.Service.Fornecedor.FornecedorService;
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
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class EmpresaTest {

    @Mock
    private EmpresaRepo empresaRepo;
    @Mock
    private FornecedorRepo fornecedorRepo;
    @Mock
    private CepService cepService;
    @Mock
    private FornecedorService fornecedorService;
    @InjectMocks
    private EmpresaService empresaService;

    // entidades de empresa
    Empresa empresa1 = new Empresa();
    Empresa empresa2 = new Empresa();
    EmpresaResponseDTO empresaResponseDTO = new EmpresaResponseDTO(
            UUID.randomUUID(), "cnpj", "nome_fantasia", "cep"
    );

    // entidades de fornecedor
    Fornecedor fornecedor1 = new Fornecedor();
    Fornecedor fornecedor2 = new Fornecedor();

    List<Empresa> empresaList = List.of(empresa1, empresa2);
    Page<Empresa> empresaPage = new PageImpl<>(empresaList);

    @Test
    void buscar_tudo_test() {
        when(empresaRepo.filtrarEmpresa(anyString(), anyString(), anyString(), any(Pageable.class)))
            .thenReturn(empresaPage);
        empresa1.setFornecedores(new ArrayList<>(List.of(fornecedor1, fornecedor2)));
        empresa2.setFornecedores(new ArrayList<>(List.of(fornecedor1, fornecedor2)));
        fornecedor1.setEmpresas(new ArrayList<>(List.of(empresa1)));
        fornecedor1.setEmpresas(new ArrayList<>(List.of(empresa2)));
        fornecedor2.setEmpresas(new ArrayList<>(List.of(empresa1)));
        fornecedor2.setEmpresas(new ArrayList<>(List.of(empresa2)));
        EmpPagResponseDTO<EmpresaFornResponseDTO> result = empresaService
            .buscar_tudo(1, 1, "nome", "cnpjCpf", "cep");
        assertDoesNotThrow(() -> result);
        verify(empresaRepo, times(1))
            .filtrarEmpresa("nome", "cnpjCpf", "cep", PageRequest.of(1, 1));
    }

    @Test
    void buscar_um_test() {
        empresa1.setFornecedores(List.of(fornecedor1, fornecedor2));
        EmpresaFornResponseDTO e = empresaService
            .mapToEmpresaFornResponseDTO(empresa1);
        when(empresaRepo.findById(any(UUID.class)))
            .thenReturn(Optional.of(empresa1));
        assertDoesNotThrow(() -> empresaService.buscar_um(UUID.randomUUID()));
        assertEquals(empresaService.buscar_um(UUID.randomUUID()), e);
        verify(empresaRepo, times(2)).findById(any(UUID.class));
    }

    @Test
    void registrar_test() {
        RegistrarEmpresaDTO registrarEmpresaDTO = new RegistrarEmpresaDTO("cnpj_cpf", "rg", "nome_fantasia");
        when(empresaRepo.save(any(Empresa.class))).thenReturn(null);
        assertDoesNotThrow(() -> empresaService.registrar(registrarEmpresaDTO));
        verify(empresaRepo, times(1)).save(any(Empresa.class));
    }

    @Test
    void atualizar_test() {
        AtualizarEmpresaDTO atualizarEmpresaDTO = new AtualizarEmpresaDTO("cnpj_cpf", "rg", "nome_fantasia");
        empresa1.setFornecedores(List.of(fornecedor1, fornecedor2));
        when(empresaRepo.findById(any(UUID.class)))
            .thenReturn(Optional.of(empresa1));
        when(empresaRepo.save(any(Empresa.class)))
            .thenReturn(null);
        assertDoesNotThrow(() -> {
            empresaService.atualizar(UUID.randomUUID(), atualizarEmpresaDTO);
        });
        verify(empresaRepo, times(1)).findById(any(UUID.class));
        verify(empresaRepo, times(1)).save(any(Empresa.class));
    }

    @Test
    void deletar_test() {
        fornecedor1.setEmpresas(new ArrayList<>(List.of(empresa1, empresa2)));
        fornecedor2.setEmpresas(new ArrayList<>(List.of(empresa1, empresa2)));
        empresa1.setFornecedores(new ArrayList<>(List.of(fornecedor1, fornecedor2)));
        when(empresaRepo.findById(any(UUID.class)))
            .thenReturn(Optional.of(empresa1));
        when(fornecedorRepo.save(any(Fornecedor.class)))
            .thenReturn(fornecedor1);
        EmpresaFornResponseDTO result = empresaService.deletar(UUID.randomUUID());
        verify(empresaRepo, times(1)).findById(any(UUID.class));
        assertFalse(fornecedor1.getEmpresas().contains(empresa1));
        assertFalse(fornecedor2.getEmpresas().contains(empresa1));
        verify(fornecedorRepo, times(2)).save(any(Fornecedor.class));
        verify(empresaRepo, times(1)).deleteById(any(UUID.class));
        assertNotNull(result);
    }

    @Test
    void associa_fornecedor_test() {
        empresa1.setFornecedores(new ArrayList<>(List.of(fornecedor1, fornecedor2)));
        fornecedor1.setEmpresas(new ArrayList<>(List.of(empresa1, empresa2)));
        when(empresaRepo.findById(any(UUID.class)))
            .thenReturn(Optional.of(empresa1));
        when(fornecedorRepo.findById(any(UUID.class)))
            .thenReturn(Optional.of(fornecedor1));
        when(empresaRepo.save(any(Empresa.class)))
            .thenReturn(empresa1);
        when(fornecedorRepo.save(any(Fornecedor.class)))
            .thenReturn(fornecedor1);

        EmpresaFornResponseDTO result = empresaService.associarFornecedor(UUID.randomUUID(), UUID.randomUUID());
        verify(empresaRepo, times(1)).findById(any(UUID.class));
        verify(fornecedorRepo, times(1)).findById(any(UUID.class));
        assertTrue(fornecedor1.getEmpresas().contains(empresa1));
        verify(fornecedorRepo, times(1)).save(any(Fornecedor.class));
        verify(empresaRepo, times(1)).save(any(Empresa.class));
        assertNotNull(result);

        // não deve permitir a associação de fornecedores pessoa física e menores de idade em empresas de ceps do Paraná
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setRg("321321112");
        fornecedor.setNascimento(LocalDate.now());
        fornecedor.setCep("80000-000");
        Empresa empresa = new Empresa(UUID.randomUUID(), "cnpj", "nome", "80000-000", new ArrayList<>(List.of(fornecedor)));
        fornecedor.setEmpresas(new ArrayList<>(List.of(empresa)));
        assertThrows(AppException.class, () -> {
            empresaService.associarFornecedor(fornecedor.getId(), empresa.getId());
        });
    }

    @Test
    void isPr_test() {
        String cepSp = "05999-999";
        CepResponseDTO spResponse = new CepResponseDTO(
            "05999-999", "", "São Paulo", "", "", "", "SP", "", "", "", "", "", ""
        );
        when(cepService.buscarCep(cepSp))
            .thenReturn(Mono.just(ResponseEntity.ok(spResponse)));

        // simulando resposta para um CEP do Paraná (PR)
        String cepPr = "80000-000";
        CepResponseDTO prResponse = new CepResponseDTO(
            "80000-000", "Curitiba", "PR", "", "", "", "PR", "", "", "", "", "", ""
        );
        when(cepService.buscarCep(cepPr))
            .thenReturn(Mono.just(ResponseEntity.ok(prResponse)));

        assertFalse(empresaService.isPr(cepSp));
        assertTrue(empresaService.isPr(cepPr));
        verify(cepService, times(1)).buscarCep(cepSp);
        verify(cepService, times(1)).buscarCep(cepPr);
    }

    @Test
    void vincula_empresa_fornecedor_test() {
        empresa1.setFornecedores(new ArrayList<>(List.of(fornecedor1, fornecedor2)));
        fornecedor1.setEmpresas(new ArrayList<>(List.of(empresa1)));
        when(empresaRepo.save(any(Empresa.class))).thenReturn(empresa1);
        when(fornecedorRepo.save(any(Fornecedor.class))).thenReturn(fornecedor1);
        assertDoesNotThrow(() -> {
            empresaService.vincularEmpresaFornecedor(empresa1, fornecedor1);
        });
        verify(empresaRepo, times(1)).save(any(Empresa.class));
        verify(fornecedorRepo, times(1)).save(any(Fornecedor.class));
    }
}
