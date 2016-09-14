package br.ufs.smelldetector.negocio;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.Test;

import br.ufs.smelldetector.model.DadosClasse;
import br.ufs.smelldetector.model.DadosMetodoLongo;

public class FiltrarMetodosLongosTest {

	@Test
	public void testFiltrarPorValorLimiar() {
		ArrayList<String> projetos = new ArrayList<String>();
		projetos.add("D:/Projetos/mobilemedia/MobileMedia01_OO");
		
		AnalisadorProjeto analisadorProjeto = new AnalisadorProjeto();
		ArrayList<DadosClasse> classes = analisadorProjeto.getInfoMetodosPorProjetos(projetos, false);
		
		FiltrarMetodosLongos filtrarMetodosLongos = new FiltrarMetodosLongos();
		ArrayList<DadosMetodoLongo> metodosLongos = filtrarMetodosLongos.filtrarPorValorLimiar(classes, 40);
		
		System.out.println("Classe   |     Método       |     Linhas Código");
		for (DadosMetodoLongo metodoLongo: metodosLongos) {
			System.out.println(metodoLongo.getNomeClasse() + "  | " + metodoLongo.getNomeMetodo() + " | " + metodoLongo.getNumeroLinhas() );
		}
		assertTrue(metodosLongos.size() > 0);
	}

	@Test
	public void testFiltrarPorProjetoExemploGeral() {
		fail("Not yet implemented");
	}

	@Test
	public void testFiltrarPorProjetoExemploPreocupacaoArquitetural() {
		fail("Not yet implemented");
	}

}
