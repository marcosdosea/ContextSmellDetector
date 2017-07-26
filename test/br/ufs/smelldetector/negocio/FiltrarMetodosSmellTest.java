package br.ufs.smelldetector.negocio;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.junit.Test;

import br.ufs.smelldetector.model.DadosClasse;
import br.ufs.smelldetector.model.DadosComponentesArquiteturais;
import br.ufs.smelldetector.model.DadosMetodoSmell;
import br.ufs.smelldetector.model.LimiarMetrica;
import br.ufs.smelldetector.model.LimiarMetricaKey;

public class FiltrarMetodosSmellTest {

	private final int porcentagem = 75;
	private final int LIMIAR_MAX_LOC = 10;
	private final int LIMIAR_MAX_CC = 10;
	private final int LIMIAR_MAX_EFFERENT = 20;
	private final int LIMIAR_MAX_NOP = 5;

	private String projetoExemplo = "D:/Projetos/mobilemedia/MobileMedia01_OO";

	private ArrayList<String> getProjetosParaAnalise() {
		ArrayList<String> projetosParaAnalise = new ArrayList<String>();
		projetosParaAnalise.add(projetoExemplo);
		return projetosParaAnalise;
	}

	private void exibeMetodosLongos(ArrayList<DadosMetodoSmell> metodosSmell, String descricaoMetodo) {
		System.out.println("\n\n\n" + descricaoMetodo);
		System.out.println("Classe   |     Método       |     Linhas Código     |  CC  | Efferent |  NOP");
		for (DadosMetodoSmell metodoSmell : metodosSmell) {
			System.out.println(metodoSmell.getNomeClasse() + "  | " + metodoSmell.getNomeMetodo() + " | "
					+ metodoSmell.getLinesOfCode() + "|" + metodoSmell.getComplexity() + " | "
					+ metodoSmell.getEfferent() + " | " + metodoSmell.getNumberOfParameters());
		}
		System.out.println("Total de métodos longos: " + metodosSmell.size());
	}

	@Test
	public void testFiltrarPorValorLimiarFixo() {
		AnalisadorProjeto analisadorProjeto = new AnalisadorProjeto();
		ArrayList<DadosClasse> classes = analisadorProjeto.getInfoMetodosPorProjetos(getProjetosParaAnalise(), false);
		HashMap<LimiarMetricaKey, LimiarMetrica> mapLimiarMetrica = GerenciadorLimiares
				.obterLimiarPreDefinidoGlobal(LIMIAR_MAX_LOC, LIMIAR_MAX_CC, LIMIAR_MAX_EFFERENT, LIMIAR_MAX_NOP);
		FiltrarMetodosSmell filtrarMetodosSmell = new FiltrarMetodosSmell();
		ArrayList<DadosMetodoSmell> metodosSmell = filtrarMetodosSmell.filtrar(classes, mapLimiarMetrica);

		exibeMetodosLongos(metodosSmell, "Filtrar por valor limiar");

		assertTrue(metodosSmell.size() > 0);
	}

	@Test
	public void testFiltrarPorProjetoExemploGeral() {
		AnalisadorProjeto analisadorProjeto = new AnalisadorProjeto();
		ArrayList<DadosClasse> classes = analisadorProjeto.getInfoMetodosPorProjetos(getProjetosParaAnalise(), false);

		GerenciadorLimiares gerenciadorLimiares = new GerenciadorLimiares();
		int valorLimiarGlobal = gerenciadorLimiares.obterValorLimiarGlobal(projetoExemplo, porcentagem);
		int medianaGlobal = gerenciadorLimiares.obterMedianaGlobal(projetoExemplo, porcentagem);

		FiltrarMetodosSmell filtrarML = new FiltrarMetodosSmell();
		ArrayList<DadosMetodoSmell> metodosLongos = filtrarML.filtrarPorProjetoExemploGeral(classes, valorLimiarGlobal,
				medianaGlobal);

		exibeMetodosLongos(metodosLongos, "Filtrar Por Projeto Exemplo Geral");

		assertTrue(metodosLongos.size() > 0);
	}

	@Test
	public void testFiltrarPorProjetoExemploPreocupacaoArquitetural() {
		AnalisadorProjeto analisadorProjeto = new AnalisadorProjeto();
		ArrayList<DadosClasse> classes = analisadorProjeto.getInfoMetodosPorProjetos(getProjetosParaAnalise(), true);

		GerenciadorLimiares gerenciadorLimiares = new GerenciadorLimiares();
		LinkedList<DadosComponentesArquiteturais> dadosCA = gerenciadorLimiares
				.criarTabelaCompArquiteturais(projetoExemplo, porcentagem);

		FiltrarMetodosSmell filtrarML = new FiltrarMetodosSmell();
		ArrayList<DadosMetodoSmell> metodosLongos = filtrarML.filtrarPorProjetoExemploPreocupacaoArquitetural(classes,
				dadosCA);

		exibeMetodosLongos(metodosLongos, "Filtrar por projeto exemplo preocupação arquitetural");

		assertTrue(metodosLongos.size() > 0);
	}

}
