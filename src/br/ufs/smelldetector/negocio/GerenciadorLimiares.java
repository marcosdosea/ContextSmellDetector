package br.ufs.smelldetector.negocio;

import java.util.ArrayList;
import java.util.HashMap;

import com.github.mauricioaniche.ck.CKNumber;
import com.github.mauricioaniche.ck.MethodMetrics;

import br.ufs.smelldetector.model.LimiarMetrica;

public class GerenciadorLimiares {

	public static String componenteNaoClassificado = "Não Classificado";

	public static HashMap<String, LimiarMetrica> obterLimiarPreDefinidoGlobal(int maxLoc, int maxCC,
			int maxEfferent, int maxNOP) {

		HashMap<String, LimiarMetrica> mapLimiarMetrica = new HashMap<String, LimiarMetrica>();

		LimiarMetrica limiarLOC = new LimiarMetrica();
		limiarLOC.setDesignRole(LimiarMetrica.DESIGN_ROLE_UNDEFINED);
		limiarLOC.setMetrica(LimiarMetrica.LOC);
		limiarLOC.setLimiarMaximo(maxLoc);
		mapLimiarMetrica.put(limiarLOC.getKey(), limiarLOC);

		LimiarMetrica limiarCC = new LimiarMetrica();
		limiarCC.setDesignRole(LimiarMetrica.DESIGN_ROLE_UNDEFINED);
		limiarCC.setMetrica(LimiarMetrica.CC);
		limiarCC.setLimiarMaximo(maxCC);
		mapLimiarMetrica.put(limiarCC.getKey(),	limiarCC);

		LimiarMetrica limiarEfferent = new LimiarMetrica();
		limiarEfferent.setDesignRole(LimiarMetrica.DESIGN_ROLE_UNDEFINED);
		limiarEfferent.setMetrica(LimiarMetrica.Efferent);
		limiarEfferent.setLimiarMaximo(maxEfferent);
		mapLimiarMetrica.put(limiarEfferent.getKey(), limiarEfferent);

		LimiarMetrica limiarNOP = new LimiarMetrica();
		limiarNOP.setDesignRole(LimiarMetrica.DESIGN_ROLE_UNDEFINED);
		limiarNOP.setMetrica(LimiarMetrica.NOP);
		limiarNOP.setLimiarMaximo(maxNOP);
		mapLimiarMetrica.put(limiarNOP.getKey(), limiarNOP);

		return mapLimiarMetrica;
	}
	
	public static void gerarPlanilhaDesignRole(ArrayList<CKNumber> classesBenchmark) {

		HashMap<String, LimiarMetrica> mapLimiarMetrica = new HashMap<String, LimiarMetrica>();

		ArrayList<Integer> listLOC = new ArrayList<>();
		ArrayList<Integer> listCC = new ArrayList<>();
		ArrayList<Integer> listEfferent = new ArrayList<>();
		ArrayList<Integer> listNOP = new ArrayList<>();

		for (CKNumber classe : classesBenchmark) {
			for (MethodMetrics metodo : classe.getMetricsByMethod().values()) {
				listLOC.add(metodo.getLinesOfCode());
				listCC.add(metodo.getComplexity());
				listEfferent.add(metodo.getEfferentCoupling());
				listNOP.add(metodo.getNumberOfParameters());
			}
		}
	}
	

	public static HashMap<String, LimiarMetrica> obterLimiarBenchMarkPercentil(
			ArrayList<CKNumber> classesBenchmark, int percentil) {

		HashMap<String, LimiarMetrica> mapLimiarMetrica = new HashMap<String, LimiarMetrica>();

		ArrayList<Integer> listLOC = new ArrayList<>();
		ArrayList<Integer> listCC = new ArrayList<>();
		ArrayList<Integer> listEfferent = new ArrayList<>();
		ArrayList<Integer> listNOP = new ArrayList<>();

		for (CKNumber classe : classesBenchmark) {
			for (MethodMetrics metodo : classe.getMetricsByMethod().values()) {
				listLOC.add(metodo.getLinesOfCode());
				listCC.add(metodo.getComplexity());
				listEfferent.add(metodo.getEfferentCoupling());
				listNOP.add(metodo.getNumberOfParameters());
			}
		}

		listLOC = MedianaQuartis.ordernarOrdemCrescente(listLOC);
		listCC = MedianaQuartis.ordernarOrdemCrescente(listCC);
		listEfferent = MedianaQuartis.ordernarOrdemCrescente(listEfferent);
		listNOP = MedianaQuartis.ordernarOrdemCrescente(listNOP);

		LimiarMetrica limiarLOC = new LimiarMetrica();
		limiarLOC.setDesignRole(LimiarMetrica.DESIGN_ROLE_UNDEFINED);
		limiarLOC.setMetrica(LimiarMetrica.LOC);
		limiarLOC.setLimiarMaximo(MedianaQuartis.percentil(listLOC, percentil));
		mapLimiarMetrica.put(limiarLOC.getKey(), limiarLOC);

		LimiarMetrica limiarCC = new LimiarMetrica();
		limiarCC.setDesignRole(LimiarMetrica.DESIGN_ROLE_UNDEFINED);
		limiarCC.setMetrica(LimiarMetrica.CC);
		limiarCC.setLimiarMaximo(MedianaQuartis.percentil(listCC, percentil));
		mapLimiarMetrica.put(limiarCC.getKey(),	limiarCC);

		LimiarMetrica limiarEfferent = new LimiarMetrica();
		limiarEfferent.setDesignRole(LimiarMetrica.DESIGN_ROLE_UNDEFINED);
		limiarEfferent.setMetrica(LimiarMetrica.Efferent);
		limiarEfferent.setLimiarMaximo(MedianaQuartis.percentil(listEfferent, percentil));
		mapLimiarMetrica.put(limiarEfferent.getKey(), limiarEfferent);

		LimiarMetrica limiarNOP = new LimiarMetrica();
		limiarNOP.setDesignRole(LimiarMetrica.DESIGN_ROLE_UNDEFINED);
		limiarNOP.setMetrica(LimiarMetrica.NOP);
		limiarNOP.setLimiarMaximo(MedianaQuartis.percentil(listNOP, percentil));
		mapLimiarMetrica.put(limiarNOP.getKey(), limiarNOP);

		return mapLimiarMetrica;
	}
}
