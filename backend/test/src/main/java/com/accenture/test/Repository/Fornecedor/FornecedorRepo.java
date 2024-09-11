package com.accenture.test.Repository.Fornecedor;

import com.accenture.test.Domain.Fornecedor.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FornecedorRepo extends JpaRepository<Fornecedor, String> {
}
