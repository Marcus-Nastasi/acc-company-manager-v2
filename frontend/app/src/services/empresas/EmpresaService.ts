import { EmpresaFornResponseDTO, EmpresaPagFornResponseDTO, EmpresaRequestDTO, EmpresaResponseDTO } from "@/interfaces/Empresa/EmpresaDTO";

export class EmpresaService {
   public static async fetchEmpresas(
      page: number, size: number, name: string, cnpj: string, cep: string
   ): Promise<EmpresaPagFornResponseDTO> {
      const url: string = `http://localhost:8080/api/empresa?page=${page - 1}&size=${size}&nome=${encodeURIComponent(name)}&cnpj=${cnpj}&cep=${cep}`;
      const response: Response = await fetch(url, { 
         method: 'GET',
         headers: { 'Content-Type': 'application/json' } 
      });
      if (response.status != 200) 
         throw new Error('Não foi possível buscar as empresas');
      const data: EmpresaPagFornResponseDTO = await response.json();
      return data;
   }

   public static async fetchEmpresa(id: string): Promise<EmpresaFornResponseDTO> {
      const response: Response = await fetch(`http://localhost:8080/api/empresa/${id}`, { 
         method: 'GET',
         headers: { 'Content-Type': 'application/json' }
      });
      if (response.status != 200) 
         throw new Error('Erro ao buscar a empresa');
      const data: EmpresaFornResponseDTO = await response.json();
      return data;
   }

   public static async deleteEmpresa(item: EmpresaResponseDTO): Promise<EmpresaFornResponseDTO> {
      if (!item) return null;
      const url: string = `http://localhost:8080/api/empresa/deletar/${item.id}`;
      const response: Response = await fetch(url, {
         method: 'DELETE',
         headers: { 'Content-Type': 'application/json' },
      });
      if (response.status != 200) 
         throw new Error('status diferente de 200, checar logs');
      return await response.json();
   }

   public static async registrarEmpresa(empresa: EmpresaRequestDTO): Promise<EmpresaFornResponseDTO> {
      const response: Response = await fetch('http://localhost:8080/api/empresa/registrar', {
         method: 'POST',
         headers: { 'Content-Type': 'application/json' },
         body: JSON.stringify(empresa),
      });
      if (response.status != 201) 
         throw new Error('Não foi possível registrar a empresa');
      return await response.json();
   }

   public static async atualizarEmpresa(empresa: EmpresaRequestDTO): Promise<EmpresaFornResponseDTO> { 
      const response: Response = await fetch(`http://localhost:8080/api/empresa/atualizar/${empresa.id}`, {
         method: 'PATCH',
         headers: { 'Content-Type': 'application/json' },
         body: JSON.stringify(empresa),
      });
      if (response.status != 200) 
         throw new Error('Não foi possível atualizar a empresa');
      return await response.json();
   }
}
