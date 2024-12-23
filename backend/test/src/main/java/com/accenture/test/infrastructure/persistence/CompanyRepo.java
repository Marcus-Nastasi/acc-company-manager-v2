package com.accenture.test.infrastructure.persistence;

import com.accenture.test.infrastructure.entity.CompanyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CompanyRepo extends JpaRepository<CompanyEntity, UUID> {

    @Query(nativeQuery = true, value = "SELECT e.* FROM Empresa e " +
            "WHERE(:nome_fantasia IS NULL OR LOWER(e.nome_fantasia) LIKE LOWER(CONCAT('%', :nome_fantasia, '%'))) " +
            "AND (:cnpj IS NULL OR e.cnpj LIKE CONCAT('%', :cnpj, '%')) " +
            "AND (:cep IS NULL OR e.cep LIKE CONCAT('%', :cep, '%'));")
    Page<CompanyEntity> filtrarEmpresa(
            @Param("nome_fantasia") String nome_fantasia,
            @Param("cnpj") String cnpj,
            @Param("cep") String cep,
            Pageable pageable
    );
}
