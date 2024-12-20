package com.accenture.test.application.usecase.empresa;

import com.accenture.test.application.gateways.empresa.EmpresaGateway;
import com.accenture.test.domain.cep.Cep;
import com.accenture.test.domain.empresa.Empresa;
import com.accenture.test.domain.empresa.EmpresaPag;
import com.accenture.test.domain.fornecedor.Fornecedor;
import com.accenture.test.application.exception.AppException;
import com.accenture.test.application.usecase.cep.CepUseCase;
import com.accenture.test.application.usecase.fornecedor.FornecedorUseCase;

import java.util.UUID;

public class EmpresaUseCase {

    private final EmpresaGateway empresaGateway;
    private final FornecedorUseCase fornecedorUseCase;
    private final CepUseCase cepUseCase;

    public EmpresaUseCase(EmpresaGateway empresaGateway, FornecedorUseCase fornecedorUseCase, CepUseCase cepUseCase) {
        this.empresaGateway = empresaGateway;
        this.fornecedorUseCase = fornecedorUseCase;
        this.cepUseCase = cepUseCase;
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
        Cep response = cepUseCase.buscarCep(cep);
        if (response == null) throw new AppException("Erro ao buscar o CEP");
        return response.getUf().equalsIgnoreCase("PR");
    }

    public void vincularEmpresaFornecedor(Empresa empresa, Fornecedor fornecedor) {
        empresa.getFornecedores().add(fornecedor);
        fornecedor.getEmpresas().add(empresa);
        empresaGateway.save(empresa);
        fornecedorUseCase.save(fornecedor);
    }

    public void desvincularEmpresaFornecedor(Empresa empresa, Fornecedor fornecedor) {
        Empresa empresaManaged = buscar_um(empresa.getId());
        Fornecedor fornecedorManaged = fornecedorUseCase.buscar_um(fornecedor.getId());
        empresaManaged.getFornecedores().remove(fornecedor);
        fornecedorManaged.getEmpresas().remove(empresa);
        empresaGateway.save(empresaManaged);
        fornecedorUseCase.save(fornecedorManaged);
    }
}
