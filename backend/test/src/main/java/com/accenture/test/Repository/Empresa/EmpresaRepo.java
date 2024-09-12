package com.accenture.test.Repository.Empresa;

import com.accenture.test.Domain.Empresa.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmpresaRepo extends JpaRepository<Empresa, UUID> {
}
