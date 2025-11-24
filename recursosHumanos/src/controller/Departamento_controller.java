package controller;

import java.io.*;
import model.Departamento;

public class Departamento_controller {
    private StringBuffer memoria;

    public Departamento_controller() {
        this.memoria = new StringBuffer();
        iniciarArquivoDepartamento();
    }
    private boolean iniciarArquivoDepartamento() {
        try {
            BufferedReader arquivoEntrada = new BufferedReader(new FileReader("Departamento.txt"));
            String linha = "";
            memoria.delete(0, memoria.length());
            do {
                linha = arquivoEntrada.readLine();
                if (linha != null) {
                    memoria.append(linha + "\n");
                }
            } while (linha != null);
            arquivoEntrada.close();
            return true;
        } catch (FileNotFoundException erro) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    public boolean gravarDadosDepartamento() {
        try {
            BufferedWriter arquivoSaida = new BufferedWriter(new FileWriter("Departamento.txt"));
            arquivoSaida.write(memoria.toString());
            arquivoSaida.flush();
            arquivoSaida.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public Departamento buscarIdDepartamento(int idProcurado) {
        int inicio = 0, fim, ultimo;
        String idStr, nome;
        
        if (!iniciarArquivoDepartamento()) {
            return null; 
        }

        while (inicio < memoria.length()) {
            try {
                ultimo = memoria.indexOf("\t", inicio);
                if (ultimo == -1) break;

                idStr = memoria.substring(inicio, ultimo);

                if (Integer.parseInt(idStr) == idProcurado) {
                    int primeiro = ultimo + 1;
                    fim = memoria.indexOf("\n", primeiro);
                    if (fim == -1) fim = memoria.length(); 

                    nome = memoria.substring(primeiro, fim);

                    return new Departamento(idProcurado, nome);
                }

                fim = memoria.indexOf("\n", inicio);
                if (fim == -1) break;
                inicio = fim + 1;

            } catch (Exception e) {
                break; 
            }
        }
        return null;
    }
    public boolean inserirDadosDepartamento(int codigo, String descricao) {
        try {
            if (buscarIdDepartamento(codigo) != null) {
                return false;
            }
            Departamento registrado = new Departamento(codigo, descricao);
            memoria.append(registrado.toString() + "\n"); 
            return gravarDadosDepartamento();   

        } catch (Exception e) {
            return false;
        }
    }
    public String consultarGeralDepartamento() {
        StringBuilder textoSaida = new StringBuilder(); 
        
        int inicio = 0, fim, ultimo, primeiro;
        String idStr, descricao;
        if (memoria.length() == 0) {
            return "Nenhum departamento cadastrado.";
        }
        textoSaida.append("--- Lista de Departamentos ---\n\n");
        while (inicio < memoria.length()) {
            try {
                ultimo = memoria.indexOf("\t", inicio);
                if (ultimo == -1) break;
                idStr = memoria.substring(inicio, ultimo);
                primeiro = ultimo + 1;
                fim = memoria.indexOf("\n", primeiro);
                
                if (fim == -1) fim = memoria.length();
                
                descricao = memoria.substring(primeiro, fim);

                textoSaida.append("ID: ").append(idStr).append("\n");
                textoSaida.append("Descrição: ").append(descricao).append("\n");
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
}
