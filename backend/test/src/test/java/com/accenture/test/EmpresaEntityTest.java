package com.accenture.test;

import com.accenture.test.adapter.output.cep.CepResponseDTO;
import com.accenture.test.adapter.input.empresa.AtualizarEmpresaDTO;
import com.accenture.test.adapter.input.empresa.RegistrarEmpresaDTO;
import com.accenture.test.adapter.output.empresa.EmpPagResponseDTO;
import com.accenture.test.adapter.output.empresa.EmpresaFornResponseDTO;
import com.accenture.test.adapter.output.empresa.EmpresaResponseDTO;
import com.accenture.test.infrastructure.entity.EmpresaEntity;
import com.accenture.test.infrastructure.entity.FornecedorEntity;
import com.accenture.test.application.exception.AppException;
import com.accenture.test.infrastructure.persistence.EmpresaRepo;
import com.accenture.test.infrastructure.persistence.FornecedorRepo;
import com.accenture.test.application.usecase.cep.CepService;
import com.accenture.test.application.usecase.empresa.EmpresaService;
import com.accenture.test.application.usecase.fornecedor.FornecedorUseCase;
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
public class EmpresaEntityTest {

    @Mock
    private EmpresaRepo empresaRepo;
    @Mock
    private FornecedorRepo fornecedorRepo;
    @Mock
    private CepService cepService;
    @Mock
    private FornecedorUseCase fornecedorUseCase;
    @InjectMocks
    private EmpresaService empresaService;

    // entidades de empresa
    EmpresaEntity empresaEntity1 = new EmpresaEntity();
    EmpresaEntity empresaEntity2 = new EmpresaEntity();
    EmpresaResponseDTO empresaResponseDTO = new EmpresaResponseDTO(
            UUID.randomUUID(), "cnpj", "nome_fantasia", "cep"
    );

    // entidades de fornecedor
    FornecedorEntity fornecedorEntity1 = new FornecedorEntity();
    FornecedorEntity fornecedorEntity2 = new FornecedorEntity();

    List<EmpresaEntity> empresaEntityList = List.of(empresaEntity1, empresaEntity2);
    Page<EmpresaEntity> empresaPage = new PageImpl<>(empresaEntityList);

    @Test
    void buscar_tudo_test() {
        when(empresaRepo.filtrarEmpresa(anyString(), anyString(), anyString(), any(Pageable.class)))
            .thenReturn(empresaPage);
        empresaEntity1.setFornecedores(new ArrayList<>(List.of(fornecedorEntity1, fornecedorEntity2)));
        empresaEntity2.setFornecedores(new ArrayList<>(List.of(fornecedorEntity1, fornecedorEntity2)));
        fornecedorEntity1.setEmpresaEntityEntities(new ArrayList<>(List.of(empresaEntity1, empresaEntity2)));
        fornecedorEntity2.setEmpresaEntityEntities(new ArrayList<>(List.of(empresaEntity1, empresaEntity2)));
        EmpPagResponseDTO<EmpresaFornResponseDTO> result = empresaService
            .buscar_tudo(1, 1, "nome", "cnpjCpf", "cep");
        assertDoesNotThrow(() -> result);
        verify(empresaRepo, times(1))
            .filtrarEmpresa("nome", "cnpjCpf", "cep", PageRequest.of(1, 1));
    }

    @Test
    void buscar_um_test() {
        empresaEntity1.setFornecedores(List.of(fornecedorEntity1, fornecedorEntity2));
        EmpresaFornResponseDTO e = empresaService
            .mapToEmpresaFornResponseDTO(empresaEntity1);
        when(empresaRepo.findById(any(UUID.class)))
            .thenReturn(Optional.of(empresaEntity1));
        assertDoesNotThrow(() -> empresaService.buscar_um(UUID.randomUUID()));
        assertEquals(empresaService.buscar_um(UUID.randomUUID()), e);
        verify(empresaRepo, times(2)).findById(any(UUID.class));
    }

    @Test
    void registrar_test() {
        RegistrarEmpresaDTO registrarEmpresaDTO = new RegistrarEmpresaDTO("cnpj_cpf", "rg", "nome_fantasia");
        when(empresaRepo.save(any(EmpresaEntity.class))).thenReturn(null);
        assertDoesNotThrow(() -> empresaService.registrar(registrarEmpresaDTO));
        verify(empresaRepo, times(1)).save(any(EmpresaEntity.class));
    }

    @Test
    void atualizar_test() {
        AtualizarEmpresaDTO atualizarEmpresaDTO = new AtualizarEmpresaDTO("cnpj_cpf", "rg", "nome_fantasia");
        empresaEntity1.setFornecedores(List.of(fornecedorEntity1, fornecedorEntity2));
        when(empresaRepo.findById(any(UUID.class)))
            .thenReturn(Optional.of(empresaEntity1));
        when(empresaRepo.save(any(EmpresaEntity.class)))
            .thenReturn(null);
        assertDoesNotThrow(() -> {
            empresaService.atualizar(UUID.randomUUID(), atualizarEmpresaDTO);
        });
        verify(empresaRepo, times(1)).findById(any(UUID.class));
        verify(empresaRepo, times(1)).save(any(EmpresaEntity.class));
    }

    @Test
    void deletar_test() {
        fornecedorEntity1.setEmpresaEntityEntities(new ArrayList<>(List.of(empresaEntity1, empresaEntity2)));
        fornecedorEntity2.setEmpresaEntityEntities(new ArrayList<>(List.of(empresaEntity1, empresaEntity2)));
        empresaEntity1.setFornecedores(new ArrayList<>(List.of(fornecedorEntity1, fornecedorEntity2)));
        when(empresaRepo.findById(any(UUID.class)))
            .thenReturn(Optional.of(empresaEntity1));
        when(fornecedorRepo.save(any(FornecedorEntity.class)))
            .thenReturn(fornecedorEntity1);
        EmpresaFornResponseDTO result = empresaService.deletar(UUID.randomUUID());
        verify(empresaRepo, times(1)).findById(any(UUID.class));
        assertFalse(fornecedorEntity1.getEmpresaEntityEntities().contains(empresaEntity1));
        assertFalse(fornecedorEntity2.getEmpresaEntityEntities().contains(empresaEntity1));
        verify(fornecedorRepo, times(2)).save(any(FornecedorEntity.class));
        verify(empresaRepo, times(1)).deleteById(any(UUID.class));
        assertNotNull(result);
    }

    @Test
    void associa_fornecedor_test() {
        empresaEntity1.setFornecedores(new ArrayList<>(List.of(fornecedorEntity1, fornecedorEntity2)));
        fornecedorEntity1.setEmpresaEntityEntities(new ArrayList<>(List.of(empresaEntity1, empresaEntity2)));
        when(empresaRepo.findById(any(UUID.class)))
            .thenReturn(Optional.of(empresaEntity1));
        when(fornecedorRepo.findById(any(UUID.class)))
            .thenReturn(Optional.of(fornecedorEntity1));
        when(empresaRepo.save(any(EmpresaEntity.class)))
            .thenReturn(empresaEntity1);
        when(fornecedorRepo.save(any(FornecedorEntity.class)))
            .thenReturn(fornecedorEntity1);

        EmpresaFornResponseDTO result = empresaService.associarFornecedor(UUID.randomUUID(), UUID.randomUUID());
        verify(empresaRepo, times(1)).findById(any(UUID.class));
        verify(fornecedorRepo, times(1)).findById(any(UUID.class));
        assertTrue(fornecedorEntity1.getEmpresaEntityEntities().contains(empresaEntity1));
        verify(fornecedorRepo, times(1)).save(any(FornecedorEntity.class));
        verify(empresaRepo, times(1)).save(any(EmpresaEntity.class));
        assertNotNull(result);

        // não deve permitir a associação de fornecedores pessoa física e menores de idade em empresas de ceps do Paraná
        FornecedorEntity fornecedorEntity = new FornecedorEntity();
        fornecedorEntity.setRg("321321112");
        fornecedorEntity.setNascimento(LocalDate.now());
        fornecedorEntity.setCep("80000-000");
        EmpresaEntity empresaEntity = new EmpresaEntity(UUID.randomUUID(), "cnpj", "nome", "80000-000", new ArrayList<>(List.of(fornecedorEntity)));
        fornecedorEntity.setEmpresaEntityEntities(new ArrayList<>(List.of(empresaEntity)));
        assertThrows(AppException.class, () -> {
            empresaService.associarFornecedor(fornecedorEntity.getId(), empresaEntity.getId());
        });
    }

    @Test
    void desassocia_fornecedor_test() {
        empresaEntity1.setFornecedores(new ArrayList<>(List.of(fornecedorEntity1, fornecedorEntity2)));
        fornecedorEntity1.setEmpresaEntityEntities(new ArrayList<>(List.of(empresaEntity1, empresaEntity2)));
        when(empresaRepo.findById(any(UUID.class)))
                .thenReturn(Optional.of(empresaEntity1));
        when(fornecedorRepo.findById(any(UUID.class)))
                .thenReturn(Optional.of(fornecedorEntity1));
        when(empresaRepo.save(any(EmpresaEntity.class)))
                .thenReturn(empresaEntity1);
        when(fornecedorRepo.save(any(FornecedorEntity.class)))
                .thenReturn(fornecedorEntity1);

        EmpresaFornResponseDTO result = empresaService.desassociarFornecedor(UUID.randomUUID(), UUID.randomUUID());
        verify(empresaRepo, times(1)).findById(any(UUID.class));
        verify(fornecedorRepo, times(1)).findById(any(UUID.class));
        assertFalse(empresaEntity1.getFornecedores().contains(fornecedorEntity1));
        assertFalse(fornecedorEntity1.getEmpresaEntityEntities().contains(empresaEntity1));
        verify(fornecedorRepo, times(1)).save(any(FornecedorEntity.class));
        verify(empresaRepo, times(1)).save(any(EmpresaEntity.class));
        assertNotNull(result);
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
            "80000-000", "", "Curitiba", "", "", "", "PR", "", "", "", "", "", ""
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
        empresaEntity1.setFornecedores(new ArrayList<>(List.of(fornecedorEntity1, fornecedorEntity2)));
        fornecedorEntity1.setEmpresaEntityEntities(new ArrayList<>(List.of(empresaEntity1)));
        when(empresaRepo.save(any(EmpresaEntity.class))).thenReturn(empresaEntity1);
        when(fornecedorRepo.save(any(FornecedorEntity.class))).thenReturn(fornecedorEntity1);
        assertDoesNotThrow(() -> {
            empresaService.vincularEmpresaFornecedor(empresaEntity1, fornecedorEntity1);
        });
        verify(empresaRepo, times(1)).save(any(EmpresaEntity.class));
        verify(fornecedorRepo, times(1)).save(any(FornecedorEntity.class));
    }
}
