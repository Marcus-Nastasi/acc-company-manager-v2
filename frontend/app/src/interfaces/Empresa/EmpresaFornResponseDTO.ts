import { FornecedorResponseDTO } from "../Fornecedor/FornecedorResponseDTO";

export interface EmpresaFornResponseDTO {
   id: string;
   cnpj: string;
   nome_fantasia: string;
   cep: string;
   fornecedores: FornecedorResponseDTO[]
}

export interface EmpresaPagFornResponseDTO {
   dados: EmpresaFornResponseDTO[],
   paginaAtual: number,
   totalPaginas: number,
   paginasRestantes: number
}
