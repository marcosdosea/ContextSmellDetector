package br.ufs.smelldetector.negocio;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.LinkedList;

import org.junit.Test;

import br.ufs.smelldetector.model.DadosClasse;
import br.ufs.smelldetector.model.DadosComponentesArquiteturais;
import br.ufs.smelldetector.model.DadosMetodoSmell;

public class FiltrarMetodosSmellTest {

	private final int porcentagem = 75;
	private final int valorLimiar = 10;
	
	private String projetoExemplo = "D:/Projetos/mobilemedia/MobileMedia01_OO"; 
	
	private ArrayList<String> getProjetosParaAnalise() {
		ArrayList<String> projetosParaAnalise = new ArrayList<String>();
		projetosParaAnalise.add(projetoExemplo);
		return projetosParaAnalise;
	}
	
	private void exibeMetodosLongos(ArrayList<DadosMetodoSmell> metodosSmell, 
			String descricaoMetodo) {
		System.out.println("\n\n\n"+descricaoMetodo);
		System.out.println("Classe   |     Método       |     Linhas Código     |  CC  | Efferent |  NOP");
		for (DadosMetodoSmell metodoSmell: metodosSmell) {
			System.out.println(metodoSmell.getNomeClasse() + "  | " + 
					metodoSmell.getNomeMetodo() + " | " + metodoSmell.getLinesOfCode() + "|" + 
					metodoSmell.getComplexity() + " | "  + metodoSmell.getEfferent()   + " | " +
					metodoSmell.getNumberOfParameters());
		}
		System.out.println("Total de métodos longos: "+metodosSmell.size());
	}
	
	@Test
	public void testFiltrarPorValorLimiar() {
		AnalisadorProjeto analisadorProjeto = new AnalisadorProjeto();
		ArrayList<DadosClasse> classes = analisadorProjeto.getInfoMetodosPorProjetos(
			getProjetosParaAnalise(), false);
		
		FiltrarMetodosSmell filtrarML = new FiltrarMetodosSmell();
		ArrayList<DadosMetodoSmell> metodosLongos = filtrarML.filtrarPorValorLimiarPreDefinido(
			classes, valorLimiar);
		
		exibeMetodosLongos(metodosLongos, "Filtrar por valor limiar");
		
		assertTrue(metodosLongos.size() > 0);
	}

	@Test
	public void testFiltrarPorProjetoExemploGeral() {
		AnalisadorProjeto analisadorProjeto = new AnalisadorProjeto();
		ArrayList<DadosClasse> classes = analisadorProjeto.getInfoMetodosPorProjetos(
			getProjetosParaAnalise(), false);
		
		GerenciadorProjetoExemplo gpe = new GerenciadorProjetoExemplo();
		int valorLimiarGlobal = gpe.obterValorLimiarGlobal(projetoExemplo, porcentagem);
		int medianaGlobal = gpe.obterMedianaGlobal(projetoExemplo, porcentagem);
		
		FiltrarMetodosSmell filtrarML = new FiltrarMetodosSmell();
		ArrayList<DadosMetodoSmell> metodosLongos = filtrarML.filtrarPorProjetoExemploGeral(
				classes, valorLimiarGlobal, medianaGlobal);
		
		exibeMetodosLongos(metodosLongos, "Filtrar Por Projeto Exemplo Geral");

		assertTrue(metodosLongos.size() > 0);
	}

	@Test
	public void testFiltrarPorProjetoExemploPreocupacaoArquitetural() {
		AnalisadorProjeto analisadorProjeto = new AnalisadorProjeto();
		ArrayList<DadosClasse> classes = analisadorProjeto.getInfoMetodosPorProjetos(
			getProjetosParaAnalise(), true);
		
		GerenciadorProjetoExemplo gpe = new GerenciadorProjetoExemplo();
		LinkedList<DadosComponentesArquiteturais> dadosCA = gpe.
			criarTabelaCompArquiteturais(projetoExemplo, porcentagem);
		
		FiltrarMetodosSmell filtrarML = new FiltrarMetodosSmell();
		ArrayList<DadosMetodoSmell> metodosLongos = filtrarML.
				filtrarPorProjetoExemploPreocupacaoArquitetural(classes, dadosCA);
		
		exibeMetodosLongos(metodosLongos, "Filtrar por projeto exemplo preocupação arquitetural");
		
		assertTrue(metodosLongos.size() > 0);
	}

}
