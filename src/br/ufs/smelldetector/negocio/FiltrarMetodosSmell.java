package br.ufs.smelldetector.negocio;

import java.util.ArrayList;
import java.util.HashMap;

import com.github.mauricioaniche.ck.CKNumber;
import com.github.mauricioaniche.ck.MethodData;
import com.github.mauricioaniche.ck.MethodMetrics;

import br.ufs.smelldetector.model.DadosMetodoSmell;
import br.ufs.smelldetector.model.LimiarMetrica;
import br.ufs.smelldetector.model.LimiarMetricaKey;

public class FiltrarMetodosSmell {

	public static ArrayList<DadosMetodoSmell> filtrar(ArrayList<String> projetosAnalisar,
			HashMap<LimiarMetricaKey, LimiarMetrica> mapLimiarMetrica) {

		AnalisadorProjeto analisadorProjeto = new AnalisadorProjeto();
		ArrayList<CKNumber> classesAnalisar = analisadorProjeto.getInfoMetodosPorProjetos(projetosAnalisar);

		ArrayList<DadosMetodoSmell> listaMetodosSmell = new ArrayList<>();

		for (CKNumber classe : classesAnalisar) {
			LimiarMetrica limiarLOC = mapLimiarMetrica.get(new LimiarMetricaKey(LimiarMetrica.LOC, classe.getDesignRole()));
			if (limiarLOC == null) 
				limiarLOC = mapLimiarMetrica.get(new LimiarMetricaKey(LimiarMetrica.LOC, LimiarMetrica.DESIGN_ROLE_UNDEFINED));
			
			LimiarMetrica limiarCC = mapLimiarMetrica.get(new LimiarMetricaKey(LimiarMetrica.CC, classe.getDesignRole()));
			if (limiarCC == null)
				limiarCC = mapLimiarMetrica.get(new LimiarMetricaKey(LimiarMetrica.CC, LimiarMetrica.DESIGN_ROLE_UNDEFINED));
			
			LimiarMetrica limiarEfferent = mapLimiarMetrica
					.get(new LimiarMetricaKey(LimiarMetrica.Efferent, LimiarMetrica.DESIGN_ROLE_UNDEFINED));
			
			LimiarMetrica limiarNOP = mapLimiarMetrica
					.get(new LimiarMetricaKey(LimiarMetrica.NOP, LimiarMetrica.DESIGN_ROLE_UNDEFINED));

			for (MethodData metodo : classe.getMetricsByMethod().keySet()) {

				MethodMetrics metodoMetrics = classe.getMetricsByMethod().get(metodo);

				if (metodoMetrics.getLinesOfCode() > limiarLOC.getLimiarMaximo()) {
					String mensagem = "Methods in this system have on maximum " + limiarLOC.getLimiarMaximo()
							+ " lines of code. " + "\nMake sure refactoring could be applied.";
					String type = "Long Method";
					addMetodoSmell(classe, metodo, metodoMetrics, type, mensagem, listaMetodosSmell);
				}
				if (metodoMetrics.getComplexity() > limiarCC.getLimiarMaximo()) {
					String mensagem = "Methods in this type class have on maximum " + limiarCC.getLimiarMaximo()
							+ " cyclomatic complexity. " + "\nMake sure refactoring could be applied.";
					String type = "High Complexity";
					addMetodoSmell(classe, metodo, metodoMetrics, type, mensagem, listaMetodosSmell);
				}
				if (metodoMetrics.getEfferentCoupling() > limiarEfferent.getLimiarMaximo()) {
					String mensagem = "Methods in this type class have on maximum " + limiarEfferent.getLimiarMaximo()
							+ " efferent coupling. " + "\nMake sure refactoring could be applied.";
					String type = "High Efferent Coupling";
					addMetodoSmell(classe, metodo, metodoMetrics, type, mensagem, listaMetodosSmell);
				}
				if (metodoMetrics.getNumberOfParameters() > limiarNOP.getLimiarMaximo()) {
					String mensagem = "Methods in this type class have on maximum " + limiarNOP.getLimiarMaximo()
							+ " number of parameters. " + "\nMake sure refactoring could be applied.";
					String type = "High Number of Parameters";
					addMetodoSmell(classe, metodo, metodoMetrics, type, mensagem, listaMetodosSmell);
				}
			}
		}
		return listaMetodosSmell;
	}

	// public ArrayList<DadosMetodoSmell>
	// filtrarPorProjetoExemploGeral(ArrayList<DadosClasse> dadosClasse,
	// int valorLimiarGlobal, int medianaGlobal) {
	// ArrayList<DadosMetodoSmell> listaMetodosSmell = new ArrayList<>();
	// for (DadosClasse classe : dadosClasse) {
	// for (DadosMetodo metodo : classe.getMetodos()) {
	// if (metodo.getLinesOfCode() > valorLimiarGlobal) {
	// String mensagem = "Long method. Methods in this system have between " +
	// medianaGlobal
	// + " and " + valorLimiarGlobal + " lines of code. "
	// + "\nMake sure refactoring could be applied.";
	// String type = "Long Method";
	// addMetodoSmell(metodo, classe, type, mensagem, listaMetodosSmell);
	// }
	//
	// }
	// }
	// return listaMetodosSmell;
	// }

	// public ArrayList<DadosMetodoSmell>
	// filtrarPorProjetoExemploPreocupacaoArquitetural(
	// ArrayList<DadosClasse> dadosClasse, LinkedList<DadosComponentesArquiteturais>
	// dca) {
	// GerenciadorLimiares gca = new GerenciadorLimiares();
	// ArrayList<DadosMetodoSmell> listaMetodosSmell = new ArrayList<>();
	// for (DadosClasse classe : dadosClasse) {
	// boolean analisarProximaRegra = true;
	// if (analisarProximaRegra && classe.getClassesExtendsImplements().get(0) !=
	// null) {
	// analisarProximaRegra = estendeMesmaClasseComponente(classe,
	// listaMetodosSmell, dca);
	// }
	// if (analisarProximaRegra && gca.temClasseImplementada(classe)
	// && gca.implementaInterfaceDaArquitetura(classe)) {
	// analisarProximaRegra = implementaMesmaClasseArquiteturalComponente(classe,
	// listaMetodosSmell, dca);
	// }
	// if (analisarProximaRegra && gca.temClasseImplementada(classe)
	// && !gca.implementaInterfaceDaArquitetura(classe)) {
	// analisarProximaRegra = implementaMesmaClasseAPIComponente(classe,
	// listaMetodosSmell, dca);
	// }
	// if (analisarProximaRegra) {
	// arquiteturaNaoClassificada(classe, listaMetodosSmell, dca);
	// }
	// }
	// return listaMetodosSmell;
	// }

	// private void arquiteturaNaoClassificada(DadosClasse classe,
	// ArrayList<DadosMetodoSmell> listaMetodosSmell,
	// LinkedList<DadosComponentesArquiteturais> dca) {
	// for (DadosComponentesArquiteturais componente : dca) {
	// if (componente.getExtendsClass() == null &&
	// componente.getImplementsArquitecture() == null
	// && componente.getImplementsAPIJava() == null) {
	// selecionarMetodos(classe, listaMetodosSmell, componente);
	// }
	// }
	// }

	// private boolean implementaMesmaClasseAPIComponente(DadosClasse classe,
	// ArrayList<DadosMetodoSmell> listaMetodosSmell,
	// LinkedList<DadosComponentesArquiteturais> dca) {
	// for (DadosComponentesArquiteturais componente : dca) {
	// for (int i = 1; i < classe.getClassesExtendsImplements().size(); i++) {
	// if
	// (classe.getClassesExtendsImplements().get(i).equals(componente.getImplementsAPIJava()))
	// {
	// selecionarMetodos(classe, listaMetodosSmell, componente);
	// return false;
	// }
	// }
	// }
	// return true;
	// }

	// private boolean implementaMesmaClasseArquiteturalComponente(DadosClasse
	// classe,
	// ArrayList<DadosMetodoSmell> listaMetodosSmell,
	// LinkedList<DadosComponentesArquiteturais> dca) {
	// for (DadosComponentesArquiteturais componente : dca) {
	// for (int i = 1; i < classe.getClassesExtendsImplements().size(); i++) {
	// if
	// (classe.getClassesExtendsImplements().get(i).equals(componente.getImplementsArquitecture()))
	// {
	// selecionarMetodos(classe, listaMetodosSmell, componente);
	// return false;
	// }
	// }
	// }
	// return true;
	// }

	// private boolean estendeMesmaClasseComponente(DadosClasse classe,
	// ArrayList<DadosMetodoSmell> listaMetodosSmell,
	// LinkedList<DadosComponentesArquiteturais> dca) {
	// for (DadosComponentesArquiteturais componente : dca) {
	// if
	// (classe.getClassesExtendsImplements().get(0).equals(componente.getExtendsClass()))
	// {
	// selecionarMetodos(classe, listaMetodosSmell, componente);
	// return false;
	// }
	// }
	// return true;
	// }

	// private void selecionarMetodos(DadosClasse classe,
	// ArrayList<DadosMetodoSmell> listaMetodosSmell,
	// DadosComponentesArquiteturais componente) {
	// for (DadosMetodo metodo : classe.getMetodos()) {
	// if (MedianaQuartis.eMaiorQueTerceiroQuartil(metodo.getLinesOfCode(),
	// componente.getTerceiroQuartil())) {
	// String mensagem = "Long method. Methods in this design role have between "
	// + componente.getMediana() + " and " + componente.getTerceiroQuartil()
	// + " lines of code. Make sure refactoring could be applied.";
	// String type = "Long Method";
	// addMetodoSmell(metodo, classe, type, mensagem, listaMetodosSmell);
	// }
	// }
	// }

	private static void addMetodoSmell(CKNumber classe, MethodData metodo, MethodMetrics metricas, String type,
			String mensagem, ArrayList<DadosMetodoSmell> listaMetodosSmell) {
		DadosMetodoSmell dadosMetodoSmell = new DadosMetodoSmell();
		dadosMetodoSmell.setCharFinal(metodo.getFinalChar());
		dadosMetodoSmell.setCharInicial(metodo.getInitialChar());
		dadosMetodoSmell.setDiretorioDaClasse(classe.getFile());
		dadosMetodoSmell.setLinhaInicial(metodo.getInitialLine());
		dadosMetodoSmell.setNomeClasse(classe.getClassName());
		dadosMetodoSmell.setNomeMetodo(metodo.getNomeMethod());
		dadosMetodoSmell.setLinesOfCode(metricas.getLinesOfCode());
		dadosMetodoSmell.setEfferent(metricas.getEfferentCoupling());
		dadosMetodoSmell.setComplexity(metricas.getComplexity());
		dadosMetodoSmell.setNumberOfParameters(metricas.getNumberOfParameters());
		dadosMetodoSmell.setMensagem(mensagem);
		dadosMetodoSmell.setType(type);
		listaMetodosSmell.add(dadosMetodoSmell);
	}

}
