package br.ufs.smelldetector.negocio;

import java.util.ArrayList;
import java.util.LinkedList;

import br.ufs.smelldetector.model.DadosClasse;
import br.ufs.smelldetector.model.DadosComponentesArquiteturais;
import br.ufs.smelldetector.model.DadosMetodo;
import br.ufs.smelldetector.model.DadosMetodoSmell;

public class FiltrarMetodosSmell {

	public ArrayList<DadosMetodoSmell> filtrarPorValorLimiarPreDefinido(
			ArrayList<DadosClasse> dadosClasse, int valorLimiar) {
		//IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		ArrayList<DadosMetodoSmell> listaMetodosSmell = new ArrayList<>();
		for (DadosClasse classe : dadosClasse) {
			//System.out.println(classe.getNomeClasse() +"  --  "+classe.getMetodos().size());
			//System.out.println();
			for (DadosMetodo metodo : classe.getMetodos()) {
				if (metodo.getLinesOfCode() > valorLimiar) {
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
					dadosMetodoSmell.setMensagem("Long method. Methods in this system have on maximum "
							+ valorLimiar + " lines of code. "
							+ "\nMake sure refactoring could be applied.");
					dadosMetodoSmell.setType("Long Method");
					listaMetodosSmell.add(dadosMetodoSmell);
				}
			}
		}
		return listaMetodosSmell;
	}
	
	public ArrayList<DadosMetodoSmell> filtrarPorProjetoExemploGeral(
			ArrayList<DadosClasse> dadosClasse, int valorLimiarGlobal, int medianaGlobal) {
		ArrayList<DadosMetodoSmell> listaMetodosSmell = new ArrayList<>();
		for (DadosClasse classe : dadosClasse) {
			for (DadosMetodo metodo : classe.getMetodos()) {
				if (metodo.getLinesOfCode() > valorLimiarGlobal) {
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
					dadosMetodoSmell.setMensagem("Long method. Methods in this system have between "
							+ medianaGlobal + " and " + valorLimiarGlobal + " lines of code. "
							+ "\nMake sure refactoring could be applied.");
					dadosMetodoSmell.setType("Long Method");
					listaMetodosSmell.add(dadosMetodoSmell);
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
			if (analisarProximaRegra && gca.temClasseImplementada(classe) && 
					gca.implementaInterfaceDaArquitetura(classe)) {
				analisarProximaRegra = implementaMesmaClasseArquiteturalComponente(classe, listaMetodosSmell, dca);
			}
			if (analisarProximaRegra && gca.temClasseImplementada(classe) && 
					!gca.implementaInterfaceDaArquitetura(classe)) {
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
			if (componente.getExtendsClass() == null && componente.getImplementsArquitecture() == null &&
					componente.getImplementsAPIJava() == null) {
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

	private boolean estendeMesmaClasseComponente(DadosClasse classe, 
			ArrayList<DadosMetodoSmell> listaMetodosSmell, LinkedList<DadosComponentesArquiteturais> dca) {
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
			if (MedianaQuartis.eMaiorQueTerceiroQuartil(metodo.getLinesOfCode(), 
					componente.getTerceiroQuartil())) {
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
				dadosMetodoSmell.setMensagem("Long method. Methods in this architectural concern have between "
						+ componente.getMediana() + " and " + componente.getTerceiroQuartil() 
						+ " lines of code. Make sure refactoring could be applied.");
				dadosMetodoSmell.setType("Long Method");
				listaMetodosSmell.add(dadosMetodoSmell);
			}
		}
	}
}
