package view;

import java.util.Scanner;
import controller.Departamento_controller;
import controller.Funcionario_controller;
import model.Funcionario;

public class Menu {
    private Departamento_controller deptCtrl;
    private Funcionario_controller funcCtrl;
    private Scanner entrada;

    public Menu() {
        this.deptCtrl = new Departamento_controller();
        this.funcCtrl = new Funcionario_controller();
        this.entrada = new Scanner(System.in);
    }

    public void exibirCapa() {
        System.out.println("=========================================");
        System.out.println("      Recursos Humanos      ");
        System.out.println("=========================================");
        System.out.println("DISCIPLINA: Laboratório de Programação 2");
        System.out.println("PROFESSORA: Renata");
        System.out.println("TURMA:      2025-2 2DC");
        System.out.println("\nINTEGRANTES DO GRUPO:");
        System.out.println("1. Carine ");
        System.out.println("2. Juan ");
        System.out.println("3. Matheus");
        System.out.println("=========================================\n");
        System.out.println("Pressione ENTER para iniciar...");
        entrada.nextLine();
    }

    public void iniciarMenuPrincipal() {
        int opcao = 0;
        do {
            System.out.println("\n=== MENU GERAL ===");
            System.out.println("1. Gestão de Departamentos");
            System.out.println("2. Gestão de Funcionários");
            System.out.println("3. Sair");
            System.out.print("Escolha: ");
            
            try {
                opcao = Integer.parseInt(entrada.nextLine());
            } catch (NumberFormatException e) {
                opcao = 0;
            }

            switch (opcao) {
                case 1:
                    menuDepartamentos();
                    break;
                case 2:
                    menuFuncionarios();
                    break;
                case 3:
                    System.out.println("Saindo do sistema...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 3);
    }
   
    private void menuDepartamentos() {
        int opcao = 0;
        do {
            System.out.println("\n--- GESTÃO DE DEPARTAMENTOS ---");
            System.out.println("1. Inserir Departamento");
            System.out.println("2. Consultar Geral");
            System.out.println("3. Voltar");
            System.out.print("Escolha: ");
            
            try {
                opcao = Integer.parseInt(entrada.nextLine());
            } catch (NumberFormatException e) {
                opcao = 0;
            }

            switch (opcao) {
                case 1:
                    telaInserirDepartamento();
                    break;
                case 2:
                    System.out.println(deptCtrl.consultarGeralDepartamento());
                    break;
                case 3:
                    return; 
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 3);
    }
    
    private void menuFuncionarios() {
        int opcao = 0;
        do {
            System.out.println("\n--- GESTÃO DE FUNCIONÁRIOS ---");
            System.out.println("1. Inserir Funcionário");
            System.out.println("2. Alterar Funcionário");
            System.out.println("3. Excluir Funcionário");
            System.out.println("4. Consultar Geral");
            System.out.println("5. Consultar por Departamento");
            System.out.println("6. Voltar");
            System.out.print("Escolha: ");

            try {
                opcao = Integer.parseInt(entrada.nextLine());
            } catch (NumberFormatException e) {
                opcao = 0;
            }

            switch (opcao) {
                case 1:
                    telaInserirFuncionario();
                    break;
                case 2:
                    telaAlterarFuncionario();
                    break;
                case 3:
                    telaExcluirFuncionario();
                    break;
                case 4:
                    System.out.println(funcCtrl.consultarGeralFuncionario());
                    break;
                case 5:
                    telaConsultaPorDepto();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 6);
    }

    private void telaInserirDepartamento() {
        try {
            System.out.println("\n--- Cadastro de Departamentos ---");
            System.out.println(">>> Preencha os dados conforme solicitado <<<\n");
            System.out.print("ID do departamento: ");
            int id = Integer.parseInt(entrada.nextLine());
            
            System.out.print("Descrição: ");
            String desc = entrada.nextLine();
            
            if (deptCtrl.inserirDadosDepartamento(id, desc)) {
                System.out.println("\nSucesso: Departamento cadastrado!\n");
            } else {
                System.out.println("Erro: ID já existe ou falha na gravação.");
            }
        } catch (Exception e) {
            System.out.println("Erro nos dados digitados. Verifique e tente novamente!");
        }
    }

    private void telaInserirFuncionario() {
        try {
            System.out.println("\n--- Cadastro de Funcionários ---");
            System.out.println(">>> Preencha os dados conforme solicitado <<<\n");
            System.out.print("ID do funcionário: ");
            int id = Integer.parseInt(entrada.nextLine());

            System.out.print("Nome: ");
            String nome = entrada.nextLine();

            System.out.print("Data Nascimento em formato 'DD/MM/AAAA': ");
            String data = entrada.nextLine();

            System.out.println("--- Departamentos Disponíveis ---");
            System.out.println(">>> Infome o Departamento em que o funcionário será alocado <<<\n");
            System.out.println(deptCtrl.consultarGeralDepartamento());
            System.out.print("ID do Departamento: ");
            int idDep = Integer.parseInt(entrada.nextLine());

            Funcionario f = new Funcionario(id, nome, data, idDep);

            if (funcCtrl.inserirDadosFuncionario(f)) {
                System.out.println("\nSucesso: Funcionário cadastrado!\n");
            } else {
                System.out.println("Erro: ID duplicado ou Departamento inexistente.");
            }
        } catch (Exception e) {
            System.out.println("Erro nos dados digitados. Verifique e tente novamente!");
        }
    }

    private void telaAlterarFuncionario() {
        try {
            System.out.println("\n--- Alterar dados Cadastrais ---\n");
            System.out.print("Digite o ID do funcionário para alterar seus dados: ");
            int id = Integer.parseInt(entrada.nextLine());

          
            if (funcCtrl.buscarIdFuncionario(id) == null) {
                System.out.println("Funcionário não encontrado. Verifique e tente novamente!");
                return;
            }

            System.out.print("Novo Nome: ");
            String nome = entrada.nextLine();
            System.out.print("Nova Data Nascimento: ");
            String data = entrada.nextLine();
            
            System.out.println(deptCtrl.consultarGeralDepartamento());
            System.out.print("Novo ID Departamento: ");
            int idDep = Integer.parseInt(entrada.nextLine());

            Funcionario fEditado = new Funcionario(id, nome, data, idDep);

            if (funcCtrl.alterarFuncionario(fEditado)) {
                System.out.println("\nSucesso: Dados atualizados!\n");
            } else {
                System.out.println("Erro ao atualizar.");
            }
        } catch (Exception e) {
            System.out.println("Erro nos dados. Verifique e tente novamente!");
        }
    }

    private void telaExcluirFuncionario() {
        try {
            System.out.println("\n--- Excluir Funcionário ---\n");
            System.out.print("Digite o ID do funcionário para excluí-lo: ");
            int id = Integer.parseInt(entrada.nextLine());

            System.out.print("Tem certeza?\nUse (S) para continuar. Use (N) para sair: ");
            String resp = entrada.nextLine();

            if (resp.equalsIgnoreCase("S")) {
                if (funcCtrl.excluirFuncionario(id)) {
                    System.out.println("Sucesso: Registro excluído.");
                } else {
                    System.out.println("Erro: ID não encontrado.");
                }
            } else {
                System.out.println("Operação cancelada.");
            }
        } catch (Exception e) {
            System.out.println("Erro nos dados.");
        }
    }

    private void telaConsultaPorDepto() {
        try {
            System.out.println("\n--- Consulta por Departamento ---");
            System.out.println(deptCtrl.consultarGeralDepartamento());
            System.out.print("Digite o ID do Departamento para exibir os funcionários à ele vinculados: ");
            int id = Integer.parseInt(entrada.nextLine());

            System.out.println(funcCtrl.consultarFuncionariosPorDepartamento(id));
        } catch (Exception e) {
            System.out.println("Erro nos dados.");
        }
    }
}
