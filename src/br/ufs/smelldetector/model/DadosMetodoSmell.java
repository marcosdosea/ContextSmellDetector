package br.ufs.smelldetector.model;

public class DadosMetodoSmell {
	
	private String diretorioDaClasse;
	private String nomeClasse;
	private String nomeMetodo;
	private int linhaInicial;
	private int linesOfCode;
	private int complexity;
	private int numberOfParameters;
	private int efferent;
	private int charInicial;
	private int charFinal;
	private String mensagem;
	private String type;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getDiretorioDaClasse() {
		return diretorioDaClasse;
	}
	
	public void setDiretorioDaClasse(String diretorioDaClasse) {
		this.diretorioDaClasse = diretorioDaClasse;
	}
	
	public String getNomeClasse() {
		return nomeClasse;
	}
	
	public void setNomeClasse(String nomeClasse) {
		this.nomeClasse = nomeClasse;
	}
	
	public String getNomeMetodo() {
		return nomeMetodo;
	}
	
	public void setNomeMetodo(String nomeMetodo) {
		this.nomeMetodo = nomeMetodo;
	}
	
	public int getLinhaInicial() {
		return linhaInicial;
	}
	
	public void setLinhaInicial(int linhaInicial) {
		this.linhaInicial = linhaInicial;
	}
	
	public int getLinesOfCode() {
		return linesOfCode;
	}
	
	public void setLinesOfCode(int numeroLinhas) {
		this.linesOfCode = numeroLinhas;
	}
	
	public int getCharInicial() {
		return charInicial;
	}
	
	public void setCharInicial(int charInicial) {
		this.charInicial = charInicial;
	}
	
	public int getCharFinal() {
		return charFinal;
	}
	
	public void setCharFinal(int charFinal) {
		this.charFinal = charFinal;
	}

	public int getComplexity() {
		return complexity;
	}

	public void setComplexity(int complexity) {
		this.complexity = complexity;
	}

	public int getNumberOfParameters() {
		return numberOfParameters;
	}

	public void setNumberOfParameters(int numberOfParameters) {
		this.numberOfParameters = numberOfParameters;
	}

	public int getEfferent() {
		return efferent;
	}

	public void setEfferent(int efferent) {
		this.efferent = efferent;
	}
	
	
}
