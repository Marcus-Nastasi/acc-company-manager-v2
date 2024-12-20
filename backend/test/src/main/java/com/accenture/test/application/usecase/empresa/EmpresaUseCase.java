package com.accenture.test.application.usecase.empresa;

import com.accenture.test.adapter.output.cep.CepResponseDTO;
import com.accenture.test.application.gateways.empresa.EmpresaGateway;
import com.accenture.test.application.gateways.fornecedor.FornecedorGateway;
import com.accenture.test.domain.empresa.Empresa;
import com.accenture.test.domain.empresa.EmpresaPag;
import com.accenture.test.domain.fornecedor.Fornecedor;
import com.accenture.test.application.exception.AppException;
import com.accenture.test.application.usecase.cep.CepService;
import com.accenture.test.application.usecase.fornecedor.FornecedorUseCase;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class EmpresaUseCase {

    private final EmpresaGateway empresaGateway;
    private final FornecedorGateway fornecedorGateway;
    private final FornecedorUseCase fornecedorUseCase;
    private final CepService cepService;

    public EmpresaUseCase(EmpresaGateway empresaGateway, FornecedorGateway fornecedorGateway, FornecedorUseCase fornecedorUseCase, CepService cepService) {
        this.empresaGateway = empresaGateway;
        this.fornecedorGateway = fornecedorGateway;
        this.fornecedorUseCase = fornecedorUseCase;
        this.cepService = cepService;
    }

    public EmpresaPag buscar_tudo(int page, int size, String nome_fantasia, String cnpj, String cep) {
        return empresaGateway.buscar_tudo(page, size, nome_fantasia, cnpj, cep);
    }

    public Empresa buscar_um(UUID id) {
        return empresaGateway.buscar_um(id);
    }

    public Empresa registrar(Empresa data) {
        return empresaGateway.save(data);
    }

    public Empresa atualizar(UUID id, Empresa data) {
        Empresa empresa = buscar_um(id);
        empresa.setCnpj(data.getCnpj());
        empresa.setNome_fantasia(data.getNome_fantasia());
        empresa.setCep(data.getCep());
        return empresaGateway.save(empresa);
    }

    public Empresa deletar(UUID id) {
        return empresaGateway.deletar(id);
    }

    public Empresa associarFornecedor(UUID id_fornecedor, UUID id) {
        Empresa empresa = buscar_um(id);
        Fornecedor fornecedor = fornecedorUseCase.buscar_um(id_fornecedor);
        if (fornecedor.isE_pf() && isPr(empresa.getCep()) && fornecedorUseCase.validaFornecedorMenor(fornecedor.getNascimento())) {
            throw new AppException("Não é permitido cadastrar um fornecedor menor de idade no Paraná");
        }
        vincularEmpresaFornecedor(empresa, fornecedor);
        return empresa;
    }

    public Empresa desassociarFornecedor(UUID id_fornecedor, UUID id) {
        Empresa empresa = buscar_um(id);
        Fornecedor fornecedor = fornecedorUseCase.buscar_um(id_fornecedor);
        desvincularEmpresaFornecedor(empresa, fornecedor);
        return empresa;
    }

    public boolean isPr(String cep) {
        Mono<ResponseEntity<CepResponseDTO>> response = cepService.buscarCep(cep);
        if (response == null) throw new AppException("Erro ao buscar o CEP");
        CepResponseDTO c = response.map(ResponseEntity::getBody).block();
        if (c == null) throw new AppException("Erro ao resolver resposta do CEP");
        return c.uf().equalsIgnoreCase("PR");
    }

    public void vincularEmpresaFornecedor(Empresa empresa, Fornecedor fornecedor) {
        empresa.getFornecedores().add(fornecedor);
        fornecedor.getEmpresas().add(empresa);
        empresaGateway.save(empresa);
        fornecedorGateway.save(fornecedor);
    }

    public void desvincularEmpresaFornecedor(Empresa empresa, Fornecedor fornecedor) {
        empresa.getFornecedores().remove(fornecedor);
        fornecedor.getEmpresas().remove(empresa);
        empresaGateway.save(empresa);
        fornecedorGateway.save(fornecedor);
    }
}
