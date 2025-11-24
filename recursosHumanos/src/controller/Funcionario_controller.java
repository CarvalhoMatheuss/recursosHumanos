package controller;

import java.io.*;
import model.Funcionario; 
import model.Departamento;

public class Funcionario_controller {
    private StringBuffer memoria;
    
    public Funcionario_controller() {
        this.memoria = new StringBuffer();
        iniciarArquivoFuncionario(); // Já carrega os dados do disco para a memória ao nascer
    }
    private boolean iniciarArquivoFuncionario() {
        String linha;
        try {
            BufferedReader arqEntrada;
            arqEntrada = new BufferedReader(new FileReader("Funcionario.txt"));
            
            // Limpa a memória antes de carregar
            memoria.delete(0, memoria.length());
            
            while ((linha = arqEntrada.readLine()) != null) {
                memoria.append(linha + "\n"); // Ou System.lineSeparator()
            }
            arqEntrada.close();
            return true;
        } catch (FileNotFoundException erro1) {
            // Arquivo não existe ainda (primeira execução), não é um erro grave
            return false;
        } catch (Exception erro2) {
            return false;
        }
    }

    // Método auxiliar privado para salvar o estado atual da memória no disco
    private boolean gravarDadosFuncionario() {
        try {
            BufferedWriter arqSaida;
            arqSaida = new BufferedWriter(new FileWriter("Funcionario.txt"));
            arqSaida.write(memoria.toString());
            arqSaida.flush();
            arqSaida.close();
            return true;
        } catch (Exception erro3) {
            return false;
        }
    }

    // INSERIR (Agora grava na memória E no arquivo)
    public boolean inserirDadosFuncionario(Funcionario reg) {
        try {
        	if (buscarIdFuncionario(reg.getId()) != null) {
                return false; // Retorna false avisando que já existe (Main avisa o usuário)
            }
        	// 2. NOVO: Verifica se o Departamento existe (Integridade Referencial)
            Departamento_controller deptCtrl = new Departamento_controller();
            if (deptCtrl.buscarIdDepartamento(reg.getId_departamento()) == null) {
                // Se o departamento não existe, não deixa cadastrar o funcionário
                // O ideal aqui seria avisar o usuário, mas retornando false o Main trata.
                return false; 
            }
            // Adiciona na memória RAM primeiro
            memoria.append(reg.toString() + "\n");
            
            // Depois salva tudo no arquivo "Funcionario.txt" (Corrigido de Trem.txt)
            return gravarDadosFuncionario();
            
        } catch (Exception e) {
            return false;
        }
    }

    public Funcionario buscarIdFuncionario(int idProcurado) {
        String idStr, nome, dataNsc, id_dep;
        int inicio = 0, fim, ultimo, primeiro;

        if (memoria.length() == 0) return null;

        while (inicio < memoria.length()) {
            try {
                ultimo = memoria.indexOf("\t", inicio);
                if (ultimo == -1) break;

                idStr = memoria.substring(inicio, ultimo);

                if (Integer.parseInt(idStr) == idProcurado) {
                    // Leitura dos campos
                    primeiro = ultimo + 1;
                    ultimo = memoria.indexOf("\t", primeiro);
                    nome = memoria.substring(primeiro, ultimo);

                    primeiro = ultimo + 1;
                    ultimo = memoria.indexOf("\t", primeiro);
                    dataNsc = memoria.substring(primeiro, ultimo);

                    primeiro = ultimo + 1;
                    fim = memoria.indexOf("\n", primeiro);
                    if (fim == -1) fim = memoria.length();
                    id_dep = memoria.substring(primeiro, fim);

                    return new Funcionario(idProcurado, nome, dataNsc, Integer.parseInt(id_dep));
                }

                // Pula para próxima linha
                fim = memoria.indexOf("\n", inicio);
                if (fim == -1) break;
                inicio = fim + 1;

            } catch (Exception erro2) {
                // Pula linha com erro
                fim = memoria.indexOf("\n", inicio);
                if (fim == -1) break;
                inicio = fim + 1;
            }
        }
        return null;
    }

    public boolean alterarFuncionario(Funcionario funcEditado) {
        int idProcurado = funcEditado.getId();
        int inicio = 0, fim, ultimo;
        String idStr;

        // Não precisamos chamar iniciarArquivo() aqui, pois o construtor já fez
        // e as operações de insert/update mantêm a memória atualizada.

        while (inicio < memoria.length()) {
            try {
                ultimo = memoria.indexOf("\t", inicio);
                if (ultimo == -1) break;

                idStr = memoria.substring(inicio, ultimo);

                if (Integer.parseInt(idStr) == idProcurado) {
                    fim = memoria.indexOf("\n", inicio);
                    if (fim == -1) fim = memoria.length();

                    // Substitui na memória
                    // Atenção: O toString() do Funcionario NÃO deve ter \n no final para isso funcionar perfeito
                    // Se tiver, o cálculo muda um pouco. Assumindo que seu toString tem \n:
                    memoria.replace(inicio, fim + 1, funcEditado.toString()+ "\n");

                    // Grava no disco
                    return gravarDadosFuncionario();
                }

                fim = memoria.indexOf("\n", inicio);
                if (fim == -1) break;
                inicio = fim + 1;

            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public boolean excluirFuncionario(int idProcurado) {
        int inicio = 0, fim, ultimo;
        String idStr;

        while (inicio < memoria.length()) {
            try {
                ultimo = memoria.indexOf("\t", inicio);
                if (ultimo == -1) break;

                idStr = memoria.substring(inicio, ultimo);

                if (Integer.parseInt(idStr) == idProcurado) {
                    fim = memoria.indexOf("\n", inicio);
                    
                    if (fim == -1) {
                        fim = memoria.length();
                        memoria.delete(inicio, fim);
                    } else {
                        memoria.delete(inicio, fim + 1);
                    }

                    return gravarDadosFuncionario();
                }

                fim = memoria.indexOf("\n", inicio);
                if (fim == -1) break;
                inicio = fim + 1;

            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
 // 1. Mudamos o retorno de VOID para STRING
    public String consultarGeralFuncionario() {
        
        // StringBuilder é como uma String "bombada" para juntar muito texto
        StringBuilder textoSaida = new StringBuilder(); 
        
        int inicio = 0, fim, ultimo, primeiro;
        String idStr, nome, dataNsc, idDepStr;

        // Verifica se tem dados na memória (lembrando que o Construtor já carregou)
        if (memoria.length() == 0) {
            return "Nenhum funcionário cadastrado.";
        }

        textoSaida.append("--- Lista de Funcionários ---\n\n");

        while (inicio < memoria.length()) {
            try {
                // --- LÓGICA DE LEITURA (Igual ao buscar, mas sequencial) ---
                
                // 1. Pega ID
                ultimo = memoria.indexOf("\t", inicio);
                if (ultimo == -1) break;
                idStr = memoria.substring(inicio, ultimo);

                // 2. Pega Nome
                primeiro = ultimo + 1;
                ultimo = memoria.indexOf("\t", primeiro);
                nome = memoria.substring(primeiro, ultimo);

                // 3. Pega Data
                primeiro = ultimo + 1;
                ultimo = memoria.indexOf("\t", primeiro);
                dataNsc = memoria.substring(primeiro, ultimo);

                // 4. Pega ID Depto (até o final da linha)
                primeiro = ultimo + 1;
                fim = memoria.indexOf("\n", primeiro);
                if (fim == -1) fim = memoria.length();
                idDepStr = memoria.substring(primeiro, fim);

                // --- EM VEZ DE IMPRIMIR, ADICIONA NO TEXTO ---
                
                // Cria o objeto só para garantir que os dados estão sãos (opcional)
                // Funcionario func = new Funcionario(Integer.parseInt(idStr), nome, dataNsc, Integer.parseInt(idDepStr));
                
                textoSaida.append("ID: ").append(idStr).append("\n");
                textoSaida.append("Nome: ").append(nome).append("\n");
                textoSaida.append("Nascimento: ").append(dataNsc).append("\n");
                textoSaida.append("Depto: ").append(idDepStr).append("\n");
                textoSaida.append("-------------------------------\n");

                // Prepara para a próxima volta do loop
                if (fim == memoria.length()) break; // Chegou no fim do arquivo real
                inicio = fim + 1;

            } catch (Exception e) {
                // Se uma linha der erro, pulamos ela e continuamos a listagem das outras
                // Não imprimimos erro aqui, apenas ignoramos a linha podre.
                fim = memoria.indexOf("\n", inicio);
                if (fim == -1) break;
                inicio = fim + 1;
            }
        }

        // Retorna todo o texto acumulado para quem chamou
        return textoSaida.toString();
    }
    public String consultarFuncionariosPorDepartamento(int idDepartamentoPesquisado) {
        StringBuilder textoSaida = new StringBuilder();
        
        // --- PASSO 1: Verificar no PRIMEIRO ARQUIVO (Departamento) ---
        // Criamos uma instância temporária do outro controller só para consultar
        Departamento_controller deptCtrl = new Departamento_controller();
        Departamento deptoEncontrado = deptCtrl.buscarIdDepartamento(idDepartamentoPesquisado);

        // Se retornou null, o departamento não existe. Encerra aqui.
        if (deptoEncontrado == null) {
            return "Erro: O Departamento código " + idDepartamentoPesquisado + " não existe.";
        }

        // Se chegou aqui, o departamento existe. Pegamos o nome dele para o cabeçalho.
        textoSaida.append("--- Funcionários do Depto: " + deptoEncontrado.getDescricao() + " ---\n\n");

        // --- PASSO 2: Varrer o SEGUNDO ARQUIVO (Funcionário) ---
        int inicio = 0, fim, ultimo, primeiro;
        String idFunc, nome, dataNsc, idDepDoFuncionario;
        boolean achouAlgum = false;

        if (memoria.length() == 0) return "Nenhum funcionário cadastrado.";

        while (inicio < memoria.length()) {
            try {
                // 1. Pega ID do Funcionário
                ultimo = memoria.indexOf("\t", inicio);
                if (ultimo == -1) break;
                idFunc = memoria.substring(inicio, ultimo);

                // 2. Pega Nome
                primeiro = ultimo + 1;
                ultimo = memoria.indexOf("\t", primeiro);
                nome = memoria.substring(primeiro, ultimo);

                // 3. Pega Data
                primeiro = ultimo + 1;
                ultimo = memoria.indexOf("\t", primeiro);
                dataNsc = memoria.substring(primeiro, ultimo);

                // 4. Pega ID do Departamento (Chave Estrangeira)
                // É o último campo da linha
                primeiro = ultimo + 1;
                fim = memoria.indexOf("\n", primeiro);
                if (fim == -1) fim = memoria.length();
                idDepDoFuncionario = memoria.substring(primeiro, fim);

                // --- A COMPARAÇÃO MÁGICA ---
                // Verifica se o ID do departamento deste funcionário (arquivo 2)
                // é igual ao ID que pesquisamos (arquivo 1)
                if (Integer.parseInt(idDepDoFuncionario) == idDepartamentoPesquisado) {
                    textoSaida.append("ID Func: ").append(idFunc).append("\n");
                    textoSaida.append("Nome: ").append(nome).append("\n");
                    textoSaida.append("Data Nasc: ").append(dataNsc).append("\n");
                    textoSaida.append("-------------------------\n");
                    achouAlgum = true;
                }

                // Próxima linha
                if (fim == memoria.length()) break;
                inicio = fim + 1;

            } catch (Exception e) {
                // Tratamento de erro de leitura (pula linha)
                fim = memoria.indexOf("\n", inicio);
                if (fim == -1) break;
                inicio = fim + 1;
            }
        }

        if (!achouAlgum) {
            textoSaida.append("Nenhum funcionário alocado neste departamento.");
        }

        return textoSaida.toString();
    }
}
