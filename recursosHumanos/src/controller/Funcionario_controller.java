package controller;

import java.io.*;
import model.Funcionario; 
import model.Departamento;

public class Funcionario_controller {
    private StringBuffer memoria;
    
    public Funcionario_controller() {
        this.memoria = new StringBuffer();
        iniciarArquivoFuncionario(); 
    }
    private boolean iniciarArquivoFuncionario() {
        String linha;
        try {
            BufferedReader arqEntrada;
            arqEntrada = new BufferedReader(new FileReader("Funcionario.txt"));
            memoria.delete(0, memoria.length());
            
            while ((linha = arqEntrada.readLine()) != null) {
                memoria.append(linha + "\n"); 
            }
            arqEntrada.close();
            return true;
        } catch (FileNotFoundException erro1) {
           
            return false;
        } catch (Exception erro2) {
            return false;
        }
    }
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
    public boolean inserirDadosFuncionario(Funcionario reg) {
        try {
        	if (buscarIdFuncionario(reg.getId()) != null) {
                return false; 
            }
        	
            Departamento_controller deptCtrl = new Departamento_controller();
            if (deptCtrl.buscarIdDepartamento(reg.getId_departamento()) == null) {
               
                return false; 
            }
            
            memoria.append(reg.toString() + "\n");
            
           
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
                fim = memoria.indexOf("\n", inicio);
                if (fim == -1) break;
                inicio = fim + 1;

            } catch (Exception erro2) {
               
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
        while (inicio < memoria.length()) {
            try {
                ultimo = memoria.indexOf("\t", inicio);
                if (ultimo == -1) break;

                idStr = memoria.substring(inicio, ultimo);

                if (Integer.parseInt(idStr) == idProcurado) {
                    fim = memoria.indexOf("\n", inicio);
                    if (fim == -1) fim = memoria.length();
                    memoria.replace(inicio, fim + 1, funcEditado.toString()+ "\n");
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
 
    public String consultarGeralFuncionario() {
        StringBuilder textoSaida = new StringBuilder(); 
        
        int inicio = 0, fim, ultimo, primeiro;
        String idStr, nome, dataNsc, idDepStr;
        if (memoria.length() == 0) {
            return "Nenhum funcionário cadastrado.";
        }

        textoSaida.append("--- Lista de Funcionários ---\n\n");

        while (inicio < memoria.length()) {
            try {
                ultimo = memoria.indexOf("\t", inicio);
                if (ultimo == -1) break;
                idStr = memoria.substring(inicio, ultimo);
                primeiro = ultimo + 1;
                ultimo = memoria.indexOf("\t", primeiro);
                nome = memoria.substring(primeiro, ultimo);
                primeiro = ultimo + 1;
                ultimo = memoria.indexOf("\t", primeiro);
                dataNsc = memoria.substring(primeiro, ultimo);
                primeiro = ultimo + 1;
                fim = memoria.indexOf("\n", primeiro);
                if (fim == -1) fim = memoria.length();
                idDepStr = memoria.substring(primeiro, fim);
                textoSaida.append("ID: ").append(idStr).append("\n");
                textoSaida.append("Nome: ").append(nome).append("\n");
                textoSaida.append("Nascimento: ").append(dataNsc).append("\n");
                textoSaida.append("Depto: ").append(idDepStr).append("\n");
                textoSaida.append("-------------------------------\n");
                if (fim == memoria.length()) break; 
                inicio = fim + 1;

            } catch (Exception e) {
                
                fim = memoria.indexOf("\n", inicio);
                if (fim == -1) break;
                inicio = fim + 1;
            }
        }

        
        return textoSaida.toString();
    }
    public String consultarFuncionariosPorDepartamento(int idDepartamentoPesquisado) {
        StringBuilder textoSaida = new StringBuilder();
        Departamento_controller deptCtrl = new Departamento_controller();
        Departamento deptoEncontrado = deptCtrl.buscarIdDepartamento(idDepartamentoPesquisado);
        if (deptoEncontrado == null) {
            return "Erro: O Departamento código " + idDepartamentoPesquisado + " não existe.";
        }
        textoSaida.append("--- Funcionários do Depto: " + deptoEncontrado.getDescricao() + " ---\n\n");
        int inicio = 0, fim, ultimo, primeiro;
        String idFunc, nome, dataNsc, idDepDoFuncionario;
        boolean achouAlgum = false;
        if (memoria.length() == 0) return "Nenhum funcionário cadastrado.";

        while (inicio < memoria.length()) {
            try {
               
                ultimo = memoria.indexOf("\t", inicio);
                if (ultimo == -1) break;
                idFunc = memoria.substring(inicio, ultimo);
                primeiro = ultimo + 1;
                ultimo = memoria.indexOf("\t", primeiro);
                nome = memoria.substring(primeiro, ultimo);
                primeiro = ultimo + 1;
                ultimo = memoria.indexOf("\t", primeiro);
                dataNsc = memoria.substring(primeiro, ultimo);
                primeiro = ultimo + 1;
                fim = memoria.indexOf("\n", primeiro);
                if (fim == -1) fim = memoria.length();
                idDepDoFuncionario = memoria.substring(primeiro, fim);
                if (Integer.parseInt(idDepDoFuncionario) == idDepartamentoPesquisado) {
                    textoSaida.append("ID Func: ").append(idFunc).append("\n");
                    textoSaida.append("Nome: ").append(nome).append("\n");
                    textoSaida.append("Data Nasc: ").append(dataNsc).append("\n");
                    textoSaida.append("-------------------------\n");
                    achouAlgum = true;
                }
                if (fim == memoria.length()) break;
                inicio = fim + 1;

            } catch (Exception e) { 
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
