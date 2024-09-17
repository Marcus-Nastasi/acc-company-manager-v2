import FornecedorEmpResponseDTO from "../Fornecedor/FornecedorDTO";


export interface EmpresaFornResponseDTO {
   id: string;
   cnpj: string;
   nome_fantasia: string;
   cep: string;
   fornecedores: FornecedorEmpResponseDTO[]
}

export interface EmpresaPagFornResponseDTO {
   dados: EmpresaFornResponseDTO[],
   paginaAtual: number,
   totalPaginas: number,
   paginasRestantes: number
}

export interface EmpresaResponseDTO {
   id: string;
   cnpj: string;
   nome_fantasia: string;
   cep: string;
}

export interface EmpresaRequestDTO {
   id?: string;
   cnpj: string;
   nome_fantasia: string;
   cep: string;
}
