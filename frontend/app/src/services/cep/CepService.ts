export class CepService {
   public static async isPr(cep: string): Promise<boolean> {
      const url: string = `https://viacep.com.br/ws/${cep}/json`;
      const response: Response = await fetch(url, {
         method: 'GET',
         headers: { "Content-Type": "application/json" }
      });
      if (response.status != 200) throw new Error("error while fetching");
      const data = await response.json();
      return data.uf == 'PR';
   }
}
