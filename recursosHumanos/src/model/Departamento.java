package model;

public class Departamento {
	private int id;
	private String descricao;
	
	public Departamento(int i, String d) {
		this.id = i;
		this.descricao = d;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return id + "\t" + descricao;
	}
	

}
