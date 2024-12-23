package com.accenture.test.adapter.mapper.company;

import com.accenture.test.adapter.input.company.CompanyRequestDto;
import com.accenture.test.adapter.mapper.supplier.SupplierDtoMapper;
import com.accenture.test.adapter.output.company.CompanyCleanDto;
import com.accenture.test.adapter.output.company.CompanyResponseDto;
import com.accenture.test.domain.company.Company;
import org.springframework.beans.factory.annotation.Autowired;

public class CompanyDtoMapper {

    @Autowired
    private SupplierDtoMapper supplierDtoMapper;

    public Company mapFromRequest(CompanyRequestDto companyRequestDto) {
        return new Company(
                companyRequestDto.id(),
                companyRequestDto.cnpj(),
                companyRequestDto.nome_fantasia(),
                companyRequestDto.cep(),
                companyRequestDto.fornecedores()
        );
    }

    public CompanyResponseDto mapToResponse(Company company) {
        return new CompanyResponseDto(
                company.getId(),
                company.getCnpj(),
                company.getNome_fantasia(),
                company.getCep(),
                company.getFornecedores().stream().map(supplierDtoMapper::mapToClean).toList()
        );
    }

    public CompanyCleanDto mapToClean(Company company) {
        return new CompanyCleanDto(
                company.getId(),
                company.getCnpj(),
                company.getNome_fantasia(),
                company.getCep()
        );
    }
}
