import { FornecedorEmpResponseDTO, FornecedorPagEmpResponseDTO, FornecedorRequestDTO } from "@/interfaces/Fornecedor/FornecedorDTO";

export class FornecedoresService {
   public static async fetchFornecedores(
      page: number, size: number, name: string, cnpj_cpf: string
   ): Promise<FornecedorPagEmpResponseDTO> {
      const response: Response = await fetch(
         `http://localhost:8080/api/fornecedor?page=${page - 1}&size=${size}&nome=${name}&cnpj_cpf=${cnpj_cpf}`,
         { method: 'GET' }
      );
      if (response.status != 200) 
         throw new Error('Não foi possível buscar fornecedores');
      return await response.json();
   }

   public static async registraFornecedor(fornecedor: FornecedorRequestDTO): Promise<FornecedorEmpResponseDTO> {
      const response: Response = await fetch('http://localhost:8080/api/fornecedor/registrar', {
         method: 'POST',
         headers: { 'Content-Type': 'application/json', },
         body: JSON.stringify(fornecedor),
      });
      if (response.status != 201) 
         throw new Error('Não foi possível registrar o fornecedor');
      return await response.json();
   }

   public static async atualizarFornecedor(id: string, fornecedor: FornecedorRequestDTO): Promise<FornecedorEmpResponseDTO> {
      const url: string = `http://localhost:8080/api/fornecedor/atualizar/${id}`;
      const response: Response = await fetch(url, {
         method: 'PATCH',
         headers: { 'Content-Type': 'application/json', },
         body: JSON.stringify(fornecedor)
      });
      if (response.status != 200) 
         throw new Error('Não foi possível atualizar o fornecedor');
      return await response.json();
   }

   public static async deletarFornecedor(id: string): Promise<FornecedorEmpResponseDTO> {
      const url: string = `http://localhost:8080/api/fornecedor/deletar/${id}`;
      const response: Response = await fetch(url, {
         method: 'DELETE',
         headers: { 'Content-Type': 'application/json', }
      });
      if (response.status != 200) 
         throw new Error('Não foi possível deletar o fornecedor');
      return await response.json();
   }
}
