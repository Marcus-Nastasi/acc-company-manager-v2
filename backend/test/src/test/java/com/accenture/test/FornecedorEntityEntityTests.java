package com.accenture.test;

import com.accenture.test.infrastructure.entity.EmpresaEntity;
import com.accenture.test.adapter.output.fornecedor.FornecedorEmpResponseDTO;
import com.accenture.test.adapter.output.fornecedor.FornecedorPagResponseDTO;
import com.accenture.test.adapter.input.fornecedor.RegistrarFornecedorDTO;
import com.accenture.test.infrastructure.entity.FornecedorEntity;
import com.accenture.test.application.exception.AppException;
import com.accenture.test.infrastructure.persistence.FornecedorRepo;
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

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class FornecedorEntityEntityTests {

    @Mock
    private FornecedorRepo fornecedorRepo;
    @Mock
    private EmpresaService empresaService;
    @InjectMocks
    private FornecedorUseCase fornecedorUseCase;

    // entidades de empresa
    EmpresaEntity empresaEntity1 = new EmpresaEntity();
    EmpresaEntity empresaEntity2 = new EmpresaEntity();

    // entidades de fornecedor
    FornecedorEntity fornecedorEntity1 = new FornecedorEntity();
    FornecedorEntity fornecedorEntity2 = new FornecedorEntity();

    List<FornecedorEntity> fornecedores = List.of(fornecedorEntity1, fornecedorEntity2);
    Page<FornecedorEntity> fornecedorPage = new PageImpl<>(fornecedores);

    @Test
    void buscar_tudo_test() {
        when(fornecedorRepo.filtrarFornecedores(anyString(), anyString(), any(Pageable.class)))
            .thenReturn(fornecedorPage);
        fornecedorEntity1.setEmpresaEntityEntities(List.of(empresaEntity1, empresaEntity2));
        fornecedorEntity2.setEmpresaEntityEntities(List.of(empresaEntity1, empresaEntity2));
        FornecedorPagResponseDTO<FornecedorEmpResponseDTO> result = fornecedorUseCase
            .buscar_tudo(1, 1, "nome", "cnpjCpf");
        assertDoesNotThrow(() -> result);
        verify(fornecedorRepo, times(1))
            .filtrarFornecedores("nome", "cnpjCpf", PageRequest.of(1, 1));
    }

    @Test
    void buscar_um_test() {
        fornecedorEntity1.setEmpresaEntityEntities(List.of(empresaEntity1, empresaEntity2));
        FornecedorEmpResponseDTO f = fornecedorUseCase
            .mapToFornecedorEmpResponseDTO(fornecedorEntity1);
        when(fornecedorRepo.findById(any(UUID.class)))
            .thenReturn(Optional.of(fornecedorEntity1));
        assertDoesNotThrow(() -> fornecedorUseCase.buscar_um(UUID.randomUUID()));
        assertEquals(fornecedorUseCase.buscar_um(UUID.randomUUID()), f);
        verify(fornecedorRepo, times(2))
            .findById(any(UUID.class));
    }

    @Test
    void registrar_test() {
        RegistrarFornecedorDTO registrarFornecedorDTO = new RegistrarFornecedorDTO(
            "cnpj_cpf", "rg", LocalDate.now(), "nome", "email", "04632011", true
        );
        RegistrarFornecedorDTO registrarFornecedorPFDTO = new RegistrarFornecedorDTO(
                "cnpj_cpf", null, null, "nome", "email", "04632011", true
        );
        when(fornecedorRepo.save(any(FornecedorEntity.class))).thenReturn(null);
        assertDoesNotThrow(() -> fornecedorUseCase.registrar(registrarFornecedorDTO));
        // teste para registro de pessoa física que deve lançar exceção
        assertThrows(AppException.class, () -> {
            fornecedorUseCase.registrar(registrarFornecedorPFDTO);
        });
        verify(fornecedorRepo, times(1)).save(any(FornecedorEntity.class));
    }

    @Test
    void atualizar_test() {
        RegistrarFornecedorDTO registrarFornecedorDTO = new RegistrarFornecedorDTO(
                "cnpj_cpf", "rg", LocalDate.now(), "nome", "email", "04632011", true
        );
        RegistrarFornecedorDTO registrarFornecedorPFDTO = new RegistrarFornecedorDTO(
                "cnpj_cpf", null, null, "nome", "email", "04632011", true
        );
        fornecedorEntity1.setEmpresaEntityEntities(List.of(empresaEntity1, empresaEntity2));
        when(fornecedorRepo.findById(any(UUID.class)))
                .thenReturn(Optional.of(fornecedorEntity1));
        when(fornecedorRepo.save(any(FornecedorEntity.class))).thenReturn(null);
        assertDoesNotThrow(() -> fornecedorUseCase.atualizar(UUID.randomUUID(), registrarFornecedorDTO));
        // teste para registro de pessoa física que deve lançar exceção
        assertThrows(AppException.class, () -> {
            fornecedorUseCase.atualizar(UUID.randomUUID(), registrarFornecedorPFDTO);
        });
        verify(fornecedorRepo, times(2)).findById(any(UUID.class));
        verify(fornecedorRepo, times(1)).save(any(FornecedorEntity.class));
    }

    @Test
    void deletar_test() {
        fornecedorEntity1.setEmpresaEntityEntities(List.of(empresaEntity1, empresaEntity2));
        when(fornecedorRepo.findById(any(UUID.class)))
            .thenReturn(Optional.of(fornecedorEntity1));
        assertDoesNotThrow(() -> fornecedorUseCase.deletar(UUID.randomUUID()));
        verify(fornecedorRepo, times(1)).deleteById(any(UUID.class));
    }

    @Test
    void valida_menor_test() {
        assertTrue(fornecedorUseCase.validaFornecedorMenor(LocalDate.now()));
        assertFalse(fornecedorUseCase.validaFornecedorMenor(LocalDate.of(1996, 10, 28)));
        assertThrows(
            DateTimeParseException.class,
            () -> fornecedorUseCase.validaFornecedorMenor(LocalDate.parse("string"))
        );
    }
}
