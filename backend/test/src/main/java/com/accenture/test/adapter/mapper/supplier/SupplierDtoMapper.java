package com.accenture.test.adapter.mapper.supplier;

import com.accenture.test.adapter.input.supplier.SupplierRequestDto;
import com.accenture.test.adapter.mapper.company.CompanyDtoMapper;
import com.accenture.test.adapter.output.supplier.SupplierCleanDto;
import com.accenture.test.adapter.output.supplier.SupplierResponseDto;
import com.accenture.test.domain.supplier.Supplier;
import org.springframework.beans.factory.annotation.Autowired;

public class SupplierDtoMapper {

    @Autowired
    private CompanyDtoMapper companyDtoMapper;

    public SupplierResponseDto mapToResponse(Supplier supplier) {
        return new SupplierResponseDto(
                supplier.getId(),
                supplier.getCnpj_cpf(),
                supplier.getRg(),
                supplier.getBirth(),
                supplier.getName(),
                supplier.getEmail(),
                supplier.getCep(),
                supplier.isE_pf(),
                supplier.getCompanies().stream().map(companyDtoMapper::mapToClean).toList()
        );
    }

    public Supplier mapFromRequest(SupplierRequestDto supplierRequestDto) {
        return new Supplier(
                null,
                supplierRequestDto.cnpj_cpf(),
                supplierRequestDto.rg(),
                supplierRequestDto.nascimento(),
                supplierRequestDto.nome(),
                supplierRequestDto.email(),
                supplierRequestDto.cep(),
                supplierRequestDto.e_pf(),
                null
        );
    }

    public SupplierCleanDto mapToClean(Supplier supplier) {
        return new SupplierCleanDto(
                supplier.getId(),
                supplier.getCnpj_cpf(),
                supplier.getRg(),
                supplier.getBirth(),
                supplier.getName(),
                supplier.getEmail(),
                supplier.getCep(),
                supplier.isE_pf()
        );
    }
}
