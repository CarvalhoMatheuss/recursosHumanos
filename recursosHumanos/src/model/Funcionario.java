package model;

public class Funcionario {
	private int id;
	private String nome;
	private String dataNascimento;
	private int id_departamento;
	
	public Funcionario(int i, String n, String d, int id_d) {
		this.id = i;
		this.nome = n;
		this.dataNascimento = d;
		this.id_departamento = id_d;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(String dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public int getId_departamento() {
		return id_departamento;
	}

	public void setId_departamento(int id_departamento) {
		this.id_departamento = id_departamento;
	}

	@Override
	public String toString() {
		return id + "\t" + nome + "\t" + dataNascimento + "\t" + id_departamento;
	}
	

}
