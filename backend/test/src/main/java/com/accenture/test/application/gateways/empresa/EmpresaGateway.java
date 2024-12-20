package com.accenture.test.application.gateways.empresa;

import com.accenture.test.domain.empresa.Empresa;
import com.accenture.test.domain.empresa.EmpresaPag;

import java.util.UUID;

public interface EmpresaGateway {

    EmpresaPag buscar_tudo(int page, int size, String nome_fantasia, String cnpj, String cep);

    Empresa buscar_um(UUID id);

    Empresa save(Empresa data);

    Empresa deletar(UUID id);
}
