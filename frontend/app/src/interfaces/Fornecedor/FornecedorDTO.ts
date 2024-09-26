import { EmpresaResponseDTO } from "../Empresa/EmpresaDTO"

export interface FornecedorEmpResponseDTO {
   id: string,
   cnpj_cpf: string,
   rg: string,
   nascimento: Date,
   nome: string,
   email: string,
   cep: string,
   e_pf: boolean,
   empresas: EmpresaResponseDTO[]
}

export interface FornecedorPagEmpResponseDTO {
   dados: FornecedorEmpResponseDTO[],
   paginaAtual: number,
   totalPaginas: number,
   paginasRestantes: number
}

export interface FornecedorResponseDTO {
   id: string,
   cnpj_cpf: string,
   rg: string,
   nascimento: Date,
   nome: string,
   email: string,
   cep: string,
   e_pf: boolean
}

export interface FornecedorRequestDTO {
   cnpj_cpf: string,
   rg: string,
   nascimento: Date,
   nome: string,
   email: string,
   cep: string,
   e_pf: boolean
}
