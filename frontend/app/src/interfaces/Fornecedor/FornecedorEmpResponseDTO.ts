import { EmpresaResponseDTO } from "../Empresa/EmpresaResponseDTO";

export default interface FornecedorEmpResponseDTO {
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
