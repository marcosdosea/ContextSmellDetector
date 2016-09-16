package br.ufs.smelldetector.negocio;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.LinkedList;

import org.junit.Test;

import br.ufs.smelldetector.model.DadosClasse;
import br.ufs.smelldetector.model.DadosComponentesArquiteturais;
import br.ufs.smelldetector.model.DadosMetodoLongo;

public class FiltrarMetodosLongosTest {

	private final int porcentagem = 75;
	private final int valorLimiar = 10;
	
	private String projetoExemplo = "C:/Users/Kekeu/Documents/mobilemedia/MobileMedia01_OO"; 
	
	private ArrayList<String> getProjetosParaAnalise() {
		ArrayList<String> projetosParaAnalise = new ArrayList<String>();
		projetosParaAnalise.add(projetoExemplo);
		return projetosParaAnalise;
	}
	
	private void exibeMetodosLongos(ArrayList<DadosMetodoLongo> metodosLongos, 
			String descricaoMetodo) {
		System.out.println("\n\n\n"+descricaoMetodo);
		System.out.println("Classe   |     Método       |     Linhas Código");
		for (DadosMetodoLongo metodoLongo: metodosLongos) {
			System.out.println(metodoLongo.getNomeClasse() + "  | " + 
					metodoLongo.getNomeMetodo() + " | " + metodoLongo.getNumeroLinhas() );
		}
		System.out.println("Total de métodos longos: "+metodosLongos.size());
	}
	
	@Test
	public void testFiltrarPorValorLimiar() {
		AnalisadorProjeto analisadorProjeto = new AnalisadorProjeto();
		ArrayList<DadosClasse> classes = analisadorProjeto.getInfoMetodosPorProjetos(
			getProjetosParaAnalise(), false);
		
		FiltrarMetodosLongos filtrarML = new FiltrarMetodosLongos();
		ArrayList<DadosMetodoLongo> metodosLongos = filtrarML.filtrarPorValorLimiar(
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
		
		FiltrarMetodosLongos filtrarML = new FiltrarMetodosLongos();
		ArrayList<DadosMetodoLongo> metodosLongos = filtrarML.filtrarPorProjetoExemploGeral(
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
		
		FiltrarMetodosLongos filtrarML = new FiltrarMetodosLongos();
		ArrayList<DadosMetodoLongo> metodosLongos = filtrarML.
				filtrarPorProjetoExemploPreocupacaoArquitetural(classes, dadosCA);
		
		exibeMetodosLongos(metodosLongos, "Filtrar por projeto exemplo preocupação arquitetural");
		
		assertTrue(metodosLongos.size() > 0);
	}

}
