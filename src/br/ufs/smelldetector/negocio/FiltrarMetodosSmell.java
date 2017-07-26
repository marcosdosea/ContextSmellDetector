package br.ufs.smelldetector.negocio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import br.ufs.smelldetector.model.DadosClasse;
import br.ufs.smelldetector.model.DadosComponentesArquiteturais;
import br.ufs.smelldetector.model.DadosMetodo;
import br.ufs.smelldetector.model.DadosMetodoSmell;
import br.ufs.smelldetector.model.LimiarMetrica;
import br.ufs.smelldetector.model.LimiarMetricaKey;

public class FiltrarMetodosSmell {

	
	public static ArrayList<DadosMetodoSmell> filtrar(ArrayList<DadosClasse> dadosClasse,
			HashMap<LimiarMetricaKey, LimiarMetrica> mapLimiarMetrica) {
		ArrayList<DadosMetodoSmell> listaMetodosSmell = new ArrayList<>();
		
		for (DadosClasse classe : dadosClasse) {
			// identifica design role da classe que será usado
			String classDesignRole = null;
			if (mapLimiarMetrica.size() == LimiarMetrica.NUMERO_METRICAS_DISPONIVEIS)
				classDesignRole = LimiarMetrica.DESIGN_ROLE_UNDEFINED;
			else
				classDesignRole = getDesignRole(classe);
			
			// identifica limiares de acordo com o design role da classe
			LimiarMetrica limiarLOC = mapLimiarMetrica.get(new LimiarMetricaKey(LimiarMetrica.LOC, LimiarMetrica.DESIGN_ROLE_UNDEFINED));
			LimiarMetrica limiarCC = mapLimiarMetrica.get(new LimiarMetricaKey(LimiarMetrica.CC, LimiarMetrica.DESIGN_ROLE_UNDEFINED));
			LimiarMetrica limiarEfferent = mapLimiarMetrica.get(new LimiarMetricaKey(LimiarMetrica.Efferent, LimiarMetrica.DESIGN_ROLE_UNDEFINED));
			LimiarMetrica limiarNOP = mapLimiarMetrica.get(new LimiarMetricaKey(LimiarMetrica.NOP, LimiarMetrica.DESIGN_ROLE_UNDEFINED));
			
			for (DadosMetodo metodo : classe.getMetodos()) {
				if (metodo.getLinesOfCode() > limiarLOC.getLimiarMaximo()) {
					String mensagem = "Methods in this system have on maximum " + limiarLOC.getLimiarMaximo()
							+ " lines of code. " + "\nMake sure refactoring could be applied.";
					String type = "Long Method";
					addMetodoSmell(metodo, classe, type, mensagem, listaMetodosSmell);
				}
				if (metodo.getComplexity() > limiarCC.getLimiarMaximo()) {
					String mensagem = "Methods in this type class have on maximum " + limiarCC.getLimiarMaximo()
							+ " cyclomatic complexity. " + "\nMake sure refactoring could be applied.";
					String type = "High Complexity";
					addMetodoSmell(metodo, classe, type, mensagem, listaMetodosSmell);
				}
				if (metodo.getEfferent() > limiarEfferent.getLimiarMaximo()) {
					String mensagem = "Methods in this type class have on maximum " + limiarEfferent.getLimiarMaximo()
							+ " efferent coupling. " + "\nMake sure refactoring could be applied.";
					String type = "High Efferent Coupling";
					addMetodoSmell(metodo, classe, type, mensagem, listaMetodosSmell);
				}
				if (metodo.getNumberOfParameters() > limiarNOP.getLimiarMaximo()) {
					String mensagem = "Methods in this type class have on maximum " + limiarNOP.getLimiarMaximo()
							+ " number of parameters. " + "\nMake sure refactoring could be applied.";
					String type = "High Number of Parameters";
					addMetodoSmell(metodo, classe, type, mensagem, listaMetodosSmell);
				}
			}
		}
		return listaMetodosSmell;
	}
	
	
	
    private static String getDesignRole(DadosClasse classe) {
    	return LimiarMetrica.DESIGN_ROLE_UNDEFINED;
    }
    
    
	public ArrayList<DadosMetodoSmell> filtrarPorValorLimiarPreDefinido(ArrayList<DadosClasse> dadosClasse,
			int valorLimiarLOC) {
		ArrayList<DadosMetodoSmell> listaMetodosSmell = new ArrayList<>();
		for (DadosClasse classe : dadosClasse) {
			for (DadosMetodo metodo : classe.getMetodos()) {
				if (metodo.getLinesOfCode() > valorLimiarLOC) {
					String mensagem = "Long method. Methods in this system have on maximum " + valorLimiarLOC
							+ " lines of code. " + "\nMake sure refactoring could be applied.";
					String type = "Long Method";
					addMetodoSmell(metodo, classe, type, mensagem, listaMetodosSmell);
				}
			}
		}
		return listaMetodosSmell;
	}

	public ArrayList<DadosMetodoSmell> filtrarPorProjetoExemploGeral(ArrayList<DadosClasse> dadosClasse,
			int valorLimiarGlobal, int medianaGlobal) {
		ArrayList<DadosMetodoSmell> listaMetodosSmell = new ArrayList<>();
		for (DadosClasse classe : dadosClasse) {
			for (DadosMetodo metodo : classe.getMetodos()) {
				if (metodo.getLinesOfCode() > valorLimiarGlobal) {
					String mensagem = "Long method. Methods in this system have between " + medianaGlobal
							+ " and " + valorLimiarGlobal + " lines of code. "
							+ "\nMake sure refactoring could be applied.";
					String type = "Long Method";
					addMetodoSmell(metodo, classe, type, mensagem, listaMetodosSmell);
				}
				
			}
		}
		return listaMetodosSmell;
	}

	public ArrayList<DadosMetodoSmell> filtrarPorProjetoExemploPreocupacaoArquitetural(
			ArrayList<DadosClasse> dadosClasse, LinkedList<DadosComponentesArquiteturais> dca) {
		GerenciadorLimiares gca = new GerenciadorLimiares();
		ArrayList<DadosMetodoSmell> listaMetodosSmell = new ArrayList<>();
		for (DadosClasse classe : dadosClasse) {
			boolean analisarProximaRegra = true;
			if (analisarProximaRegra && classe.getClassesExtendsImplements().get(0) != null) {
				analisarProximaRegra = estendeMesmaClasseComponente(classe, listaMetodosSmell, dca);
			}
			if (analisarProximaRegra && gca.temClasseImplementada(classe)
					&& gca.implementaInterfaceDaArquitetura(classe)) {
				analisarProximaRegra = implementaMesmaClasseArquiteturalComponente(classe, listaMetodosSmell, dca);
			}
			if (analisarProximaRegra && gca.temClasseImplementada(classe)
					&& !gca.implementaInterfaceDaArquitetura(classe)) {
				analisarProximaRegra = implementaMesmaClasseAPIComponente(classe, listaMetodosSmell, dca);
			}
			if (analisarProximaRegra) {
				arquiteturaNaoClassificada(classe, listaMetodosSmell, dca);
			}
		}
		return listaMetodosSmell;
	}

	private void arquiteturaNaoClassificada(DadosClasse classe, ArrayList<DadosMetodoSmell> listaMetodosSmell,
			LinkedList<DadosComponentesArquiteturais> dca) {
		for (DadosComponentesArquiteturais componente : dca) {
			if (componente.getExtendsClass() == null && componente.getImplementsArquitecture() == null
					&& componente.getImplementsAPIJava() == null) {
				selecionarMetodos(classe, listaMetodosSmell, componente);
			}
		}
	}

	private boolean implementaMesmaClasseAPIComponente(DadosClasse classe,
			ArrayList<DadosMetodoSmell> listaMetodosSmell, LinkedList<DadosComponentesArquiteturais> dca) {
		for (DadosComponentesArquiteturais componente : dca) {
			for (int i = 1; i < classe.getClassesExtendsImplements().size(); i++) {
				if (classe.getClassesExtendsImplements().get(i).equals(componente.getImplementsAPIJava())) {
					selecionarMetodos(classe, listaMetodosSmell, componente);
					return false;
				}
			}
		}
		return true;
	}

	private boolean implementaMesmaClasseArquiteturalComponente(DadosClasse classe,
			ArrayList<DadosMetodoSmell> listaMetodosSmell, LinkedList<DadosComponentesArquiteturais> dca) {
		for (DadosComponentesArquiteturais componente : dca) {
			for (int i = 1; i < classe.getClassesExtendsImplements().size(); i++) {
				if (classe.getClassesExtendsImplements().get(i).equals(componente.getImplementsArquitecture())) {
					selecionarMetodos(classe, listaMetodosSmell, componente);
					return false;
				}
			}
		}
		return true;
	}

	private boolean estendeMesmaClasseComponente(DadosClasse classe, ArrayList<DadosMetodoSmell> listaMetodosSmell,
			LinkedList<DadosComponentesArquiteturais> dca) {
		for (DadosComponentesArquiteturais componente : dca) {
			if (classe.getClassesExtendsImplements().get(0).equals(componente.getExtendsClass())) {
				selecionarMetodos(classe, listaMetodosSmell, componente);
				return false;
			}
		}
		return true;
	}

	private void selecionarMetodos(DadosClasse classe, ArrayList<DadosMetodoSmell> listaMetodosSmell,
			DadosComponentesArquiteturais componente) {
		for (DadosMetodo metodo : classe.getMetodos()) {
			if (MedianaQuartis.eMaiorQueTerceiroQuartil(metodo.getLinesOfCode(), componente.getTerceiroQuartil())) {
				String mensagem = "Long method. Methods in this design role have between "
						+ componente.getMediana() + " and " + componente.getTerceiroQuartil()
						+ " lines of code. Make sure refactoring could be applied.";
				String type = "Long Method";
				addMetodoSmell(metodo, classe, type, mensagem, listaMetodosSmell);
			}
		}
	}
	
	private static void addMetodoSmell(DadosMetodo metodo, DadosClasse classe, String type, String mensagem,
			ArrayList<DadosMetodoSmell> listaMetodosSmell) {
		DadosMetodoSmell dadosMetodoSmell = new DadosMetodoSmell();
		dadosMetodoSmell.setCharFinal(metodo.getCharFinal());
		dadosMetodoSmell.setCharInicial(metodo.getCharInicial());
		dadosMetodoSmell.setDiretorioDaClasse(classe.getDiretorioDaClasse());
		dadosMetodoSmell.setLinhaInicial(metodo.getLinhaInicial());
		dadosMetodoSmell.setNomeClasse(classe.getNomeClasse());
		dadosMetodoSmell.setNomeMetodo(metodo.getNomeMetodo());
		dadosMetodoSmell.setLinesOfCode(metodo.getLinesOfCode());
		dadosMetodoSmell.setEfferent(metodo.getEfferent());
		dadosMetodoSmell.setComplexity(metodo.getComplexity());
		dadosMetodoSmell.setNumberOfParameters(metodo.getNumberOfParameters());
		dadosMetodoSmell.setMensagem(mensagem);
		dadosMetodoSmell.setType(type);
		listaMetodosSmell.add(dadosMetodoSmell);
	}

}
