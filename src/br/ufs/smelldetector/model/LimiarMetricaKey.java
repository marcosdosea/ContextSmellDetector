package br.ufs.smelldetector.model;

public class LimiarMetricaKey {
	private String metrica;
	private String designRole;

	public LimiarMetricaKey(String metrica, String designRole) {
		super();
		this.metrica = metrica;
		this.designRole = designRole;
	}

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

	@Override
	public boolean equals(Object obj) {
		LimiarMetricaKey limiarMetrica = null;
		if (obj instanceof LimiarMetricaKey)
			limiarMetrica = (LimiarMetricaKey) obj;
		else
			return false;
		return  getDesignRole().equals(limiarMetrica.getDesignRole()) && 
					getMetrica().equals(limiarMetrica.getMetrica());
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return getDesignRole().hashCode() + getMetrica().hashCode();
	}

}
