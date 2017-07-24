package br.ufs.smelldetector.model;

public class DadosMetodo {
	
	private int linhaInicial;
	private int linesOfCode;
	private int complexity;
	private int numberOfParameters;
	private int efferent;
	private String nomeMetodo;
	private int charInicial;
	private int charFinal;
	
	
	public int getLinesOfCode() {
		return linesOfCode;
	}

	public void setLinesOfCode(int linesOfCode) {
		this.linesOfCode = linesOfCode;
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

	
	public int getLinhaInicial() {
		return linhaInicial;
	}
	
	public void setLinhaInicial(int linhaInicial) {
		this.linhaInicial = linhaInicial;
	}
	
	public String getNomeMetodo() {
		return nomeMetodo;
	}
	
	public void setNomeMetodo(String nomeMetodo) {
		this.nomeMetodo = nomeMetodo;
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
	
}
