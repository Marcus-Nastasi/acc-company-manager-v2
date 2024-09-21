package com.accenture.test;

import com.accenture.test.Domain.Empresa.Empresa;
import com.accenture.test.Domain.Fornecedor.DTO.FornecedorEmpResponseDTO;
import com.accenture.test.Domain.Fornecedor.DTO.FornecedorPagResponseDTO;
import com.accenture.test.Domain.Fornecedor.DTO.RegistrarFornecedorDTO;
import com.accenture.test.Domain.Fornecedor.Fornecedor;
import com.accenture.test.Exception.AppException;
import com.accenture.test.Repository.Fornecedor.FornecedorRepo;
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

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class FornecedorTests {

    @Mock
    private FornecedorRepo fornecedorRepo;
    @Mock
    private EmpresaService empresaService;
    @InjectMocks
    private FornecedorService fornecedorService;

    // entidades de empresa
    Empresa empresa1 = new Empresa();
    Empresa empresa2 = new Empresa();

    // entidades de fornecedor
    Fornecedor fornecedor1 = new Fornecedor();
    Fornecedor fornecedor2 = new Fornecedor();

    List<Fornecedor> fornecedores = List.of(fornecedor1, fornecedor2);
    Page<Fornecedor> fornecedorPage = new PageImpl<>(fornecedores);

    @Test
    void buscar_tudo_test() {
        when(fornecedorRepo.filtrarFornecedores(anyString(), anyString(), any(Pageable.class)))
            .thenReturn(fornecedorPage);
        fornecedor1.setEmpresas(List.of(empresa1, empresa2));
        fornecedor2.setEmpresas(List.of(empresa1, empresa2));
        FornecedorPagResponseDTO<FornecedorEmpResponseDTO> result = fornecedorService
            .buscar_tudo(1, 1, "nome", "cnpjCpf");
        assertDoesNotThrow(() -> result);
        verify(fornecedorRepo, times(1))
            .filtrarFornecedores("nome", "cnpjCpf", PageRequest.of(1, 1));
    }

    @Test
    void buscar_um_test() {
        fornecedor1.setEmpresas(List.of(empresa1, empresa2));
        FornecedorEmpResponseDTO f = fornecedorService
            .mapToFornecedorEmpResponseDTO(fornecedor1);
        when(fornecedorRepo.findById(any(UUID.class)))
            .thenReturn(Optional.of(fornecedor1));
        assertDoesNotThrow(() -> fornecedorService.buscar_um(UUID.randomUUID()));
        assertEquals(fornecedorService.buscar_um(UUID.randomUUID()), f);
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
        when(fornecedorRepo.save(any(Fornecedor.class))).thenReturn(null);
        assertDoesNotThrow(() -> fornecedorService.registrar(registrarFornecedorDTO));
        // teste para registro de pessoa física que deve lançar exceção
        assertThrows(AppException.class, () -> {
            fornecedorService.registrar(registrarFornecedorPFDTO);
        });
        verify(fornecedorRepo, times(1)).save(any(Fornecedor.class));
    }

    @Test
    void atualizar_test() {
        RegistrarFornecedorDTO registrarFornecedorDTO = new RegistrarFornecedorDTO(
                "cnpj_cpf", "rg", LocalDate.now(), "nome", "email", "04632011", true
        );
        RegistrarFornecedorDTO registrarFornecedorPFDTO = new RegistrarFornecedorDTO(
                "cnpj_cpf", null, null, "nome", "email", "04632011", true
        );
        fornecedor1.setEmpresas(List.of(empresa1, empresa2));
        when(fornecedorRepo.findById(any(UUID.class)))
                .thenReturn(Optional.of(fornecedor1));
        when(fornecedorRepo.save(any(Fornecedor.class))).thenReturn(null);
        assertDoesNotThrow(() -> fornecedorService.atualizar(UUID.randomUUID(), registrarFornecedorDTO));
        // teste para registro de pessoa física que deve lançar exceção
        assertThrows(AppException.class, () -> {
            fornecedorService.atualizar(UUID.randomUUID(), registrarFornecedorPFDTO);
        });
        verify(fornecedorRepo, times(2)).findById(any(UUID.class));
        verify(fornecedorRepo, times(1)).save(any(Fornecedor.class));
    }

    @Test
    void deletar_test() {
        fornecedor1.setEmpresas(List.of(empresa1, empresa2));
        when(fornecedorRepo.findById(any(UUID.class)))
            .thenReturn(Optional.of(fornecedor1));
        assertDoesNotThrow(() -> fornecedorService.deletar(UUID.randomUUID()));
        verify(fornecedorRepo, times(1)).deleteById(any(UUID.class));
    }

    @Test
    void valida_menor_test() {
        assertTrue(fornecedorService.validaFornecedorMenor(LocalDate.now()));
        assertFalse(fornecedorService.validaFornecedorMenor(LocalDate.of(1996, 10, 28)));
        assertThrows(
            DateTimeParseException.class,
            () -> fornecedorService.validaFornecedorMenor(LocalDate.parse("string"))
        );
    }
}
