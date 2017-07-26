package br.ufs.smelldetector.model;

public class LimiarMetrica {
	
	public static final int NUMERO_METRICAS_DISPONIVEIS = 4;
	public static final String LOC = "LOC";
	public static final String CC = "CC";
	public static final String Efferent = "Efferent";
	public static final String NOP = "NOP";
	public static final String DESIGN_ROLE_UNDEFINED = "UNDEFINED";
	
	private String metrica;
	private String designRole;
	private int limiarMedio;
	private int limiarMaximo;
	private int limiarMaximo2;
	public String getMetrica() {
		return metrica;
	}
	public void setMetrica(String metrica) {
		this.metrica = metrica;
	}
	public String getDesignRole() {
		return designRole;
	}
	public void setDesignRole(String designRole) {
		this.designRole = designRole;
	}
	public int getLimiarMedio() {
		return limiarMedio;
	}
	public void setLimiarMedio(int limiarMedio) {
		this.limiarMedio = limiarMedio;
	}
	public int getLimiarMaximo() {
		return limiarMaximo;
	}
	public void setLimiarMaximo(int limiarMaximo) {
		this.limiarMaximo = limiarMaximo;
	}
	public int getLimiarMaximo2() {
		return limiarMaximo2;
	}
	public void setLimiarMaximo2(int limiarMaximo2) {
		this.limiarMaximo2 = limiarMaximo2;
	}
	@Override
	public boolean equals(Object obj) {
		LimiarMetrica limiarMetrica = (LimiarMetrica) obj;
		return getDesignRole().equals(limiarMetrica.getDesignRole()) &&
				getMetrica().equals(limiarMetrica.getMetrica());
	}

}
