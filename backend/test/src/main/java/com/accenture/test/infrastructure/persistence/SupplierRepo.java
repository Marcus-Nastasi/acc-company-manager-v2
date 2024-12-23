package com.accenture.test.infrastructure.persistence;

import com.accenture.test.infrastructure.entity.SupplierEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SupplierRepo extends JpaRepository<SupplierEntity, UUID> {

    @Query(nativeQuery = true, value = "SELECT s.* FROM supplier s " +
            "WHERE (:name IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:cnpj_cpf IS NULL OR s.cnpj_cpf LIKE CONCAT('%', :cnpj_cpf, '%'));")
    Page<SupplierEntity> filter(
            @Param("name") String name,
            @Param("cnpj_cpf") String cnpj_cpf,
            Pageable pageable
    );
}
