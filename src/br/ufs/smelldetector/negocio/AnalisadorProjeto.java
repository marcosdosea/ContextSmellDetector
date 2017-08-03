package br.ufs.smelldetector.negocio;

import java.util.ArrayList;

import com.github.mauricioaniche.ck.CK;
import com.github.mauricioaniche.ck.CKNumber;
import com.github.mauricioaniche.ck.CKReport;

public class AnalisadorProjeto {

	/**
	 * Busca todas as classes do projeto e faz o cálculo das métricas de cada classe.
	 * @param projetos
	 * @param AdicionarArquitetura
	 * @return
	 */
	public ArrayList<CKNumber> getInfoMetodosPorProjetos(ArrayList<String> projetos){
		ArrayList<CKNumber> listaClasses = new ArrayList<>();
		for (String path : projetos) {
			CKReport report = new CK().calculate(path);
			listaClasses.addAll(report.all());
    	}
		return listaClasses;
	}

}
