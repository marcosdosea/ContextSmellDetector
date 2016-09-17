package br.ufs.smelldetector.negocio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import br.ufs.smelldetector.model.DadosClasse;
import br.ufs.smelldetector.model.DadosComponentesArquiteturais;

public class GerenciadorProjetoExemploTest {

	private GerenciadorProjetoExemplo gpe = new GerenciadorProjetoExemplo();
	
	/* Projeto de exemplo tem 3 classes que estende mesma classe, 2 que implementa interface da api 
	 * e duas que não se encaixam em nenhuma regra. */
	private String projetoExemplo = "C:/Users/Kekeu/Documents/GitHub/ContextSmellDetector/TestesProjetoExemplo";
	private String projetoExemploGrupo1 = "C:/Users/Kekeu/Documents/GitHub/ContextSmellDetector/TestesProjetoExemplo/src/Grupo1";
	private String projetoExemploGrupo3 = "C:/Users/Kekeu/Documents/GitHub/ContextSmellDetector/TestesProjetoExemplo/src/Grupo3";
	private String projetoExemploGrupo4 = "C:/Users/Kekeu/Documents/GitHub/ContextSmellDetector/TestesProjetoExemplo/src/Grupo4";
	private final int porcentagemLimiar = 75; 

	/* 
	 * A tabela deve conter 3 componentes arquiteturais como segue: 
	 * 1 - implementa a regra 1 (estende a mesma classe - "AbstractDocument")
	 * 2 - implementa a regra 3 (implementa interface da API - "Serializable")
	 * 3 - implementa a regra 4 (Não se encaixa em nenhuma das outras regras)
	 */
	@Test
	public void criarTabelaCompArquiteturais() {
		LinkedList<DadosComponentesArquiteturais> tabela = gpe
				.criarTabelaCompArquiteturais(projetoExemplo, porcentagemLimiar);
		imprimeTabela(tabela);
		assertEquals(tabela.size(), 3);
		
		assertTrue(GerenciadorProjetoExemplo.getClasseComponente(tabela.get(0)).equals("AbstractDocument") ||
				GerenciadorProjetoExemplo.getClasseComponente(tabela.get(0)).equals("Serializable") || 
				GerenciadorProjetoExemplo.getClasseComponente(tabela.get(0)).equals(
						GerenciadorProjetoExemplo.componenteNaoClassificado));
		
		assertTrue(GerenciadorProjetoExemplo.getClasseComponente(tabela.get(1)).equals("AbstractDocument") ||
				GerenciadorProjetoExemplo.getClasseComponente(tabela.get(1)).equals("Serializable") || 
				GerenciadorProjetoExemplo.getClasseComponente(tabela.get(1)).equals(
						GerenciadorProjetoExemplo.componenteNaoClassificado));
		
		assertTrue(GerenciadorProjetoExemplo.getClasseComponente(tabela.get(2)).equals("AbstractDocument") ||
				GerenciadorProjetoExemplo.getClasseComponente(tabela.get(2)).equals("Serializable") || 
				GerenciadorProjetoExemplo.getClasseComponente(tabela.get(2)).equals(
						GerenciadorProjetoExemplo.componenteNaoClassificado));
	}

	@Test
	public void criarGruposComponentesArquiteturais() {
		List<DadosClasse> listaClasses = gpe.obterListaClassesProjetoExemplo(true, projetoExemplo);
		LinkedList<List<DadosClasse>> grupos = new LinkedList<List<DadosClasse>>();
		assertEquals(grupos.size(), 0);
		assertEquals(listaClasses.size(), 7);
		for (DadosClasse classe: listaClasses) {
			gpe.classificarClassesGrupos(classe, grupos);
		}
		gpe.formarGrupoClassesNaoClassificadas(grupos);
		
		// Quantidade de classes por grupo 
		assertTrue((grupos.get(0).size() == 3 && grupos.get(1).size() == 2 && grupos.get(2).size() == 2) ||
				(grupos.get(0).size() == 2 && grupos.get(1).size() == 3 && grupos.get(2).size() == 2) ||
				(grupos.get(0).size() == 2 && grupos.get(1).size() == 2 && grupos.get(2).size() == 3));
		
		// Quantidade de grupos
		assertEquals(grupos.size(), 3);
	}
	
	@Test
	public void grupoEhRegra1() {
		LinkedList<List<DadosClasse>> grupos = gpe.criarGruposComponentesArquiteturais(projetoExemploGrupo1);
		assertTrue(gpe.grupoEhRegra1(grupos.get(0)));
	}
	
	@Test
	public void grupoEhRegra3() {
		LinkedList<List<DadosClasse>> grupos = gpe.criarGruposComponentesArquiteturais(projetoExemploGrupo3);
		assertTrue(gpe.grupoEhRegra3(grupos.get(0)));
	}
	
	@Test
	public void grupoEhRegra4() {
		LinkedList<List<DadosClasse>> grupos = gpe.criarGruposComponentesArquiteturais(projetoExemploGrupo4);
		assertTrue(!gpe.grupoEhRegra1(grupos.get(0)) && !gpe.grupoEhRegra2(grupos.get(0)) 
				&& !gpe.grupoEhRegra3(grupos.get(0)));
	}
	
	@Test
	public void extendMesmaClasse() {
		LinkedList<List<DadosClasse>> grupos1 = gpe.criarGruposComponentesArquiteturais(projetoExemploGrupo1);
		assertTrue(gpe.extendMesmaClasse(grupos1.get(0).get(0), grupos1.get(0).get(1)));
		LinkedList<List<DadosClasse>> grupos4 = gpe.criarGruposComponentesArquiteturais(projetoExemploGrupo4);
		assertFalse(gpe.extendMesmaClasse(grupos1.get(0).get(0), grupos4.get(0).get(0)));
	}

	@Test
	public void implementaMesmaClasse() {
		LinkedList<List<DadosClasse>> grupos3 = gpe.criarGruposComponentesArquiteturais(projetoExemploGrupo3);
		assertTrue(gpe.implementaMesmaClasse(grupos3.get(0).get(0), grupos3.get(0).get(1)));
		LinkedList<List<DadosClasse>> grupos4 = gpe.criarGruposComponentesArquiteturais(projetoExemploGrupo4);
		assertFalse(gpe.implementaMesmaClasse(grupos3.get(0).get(0), grupos4.get(0).get(0)));
	}

	@Test
	public void temClasseImplementada() {
		LinkedList<List<DadosClasse>> grupos3 = gpe.criarGruposComponentesArquiteturais(projetoExemploGrupo3);
		assertTrue(gpe.temClasseImplementada(grupos3.get(0).get(0)));
	}

	@Test
	public void implementaInterfaceDaAPI() {
		LinkedList<List<DadosClasse>> grupos3 = gpe.criarGruposComponentesArquiteturais(projetoExemploGrupo3);
		assertFalse(gpe.implementaInterfaceDaArquitetura(grupos3.get(0).get(0)));
	}

	@Test
	public void obterClasseImplementadaEmComum() {
		LinkedList<List<DadosClasse>> grupos3 = gpe.criarGruposComponentesArquiteturais(projetoExemploGrupo3);
		assertTrue(gpe.obterClasseImplementadaEmComum(
				grupos3.get(0).get(0), grupos3.get(0).get(1)).equals("Serializable"));
	}

	@Test
	public void formarGrupoClassesNaoClassificadas() {
		List<DadosClasse> listaClasses = gpe.obterListaClassesProjetoExemplo(true, projetoExemploGrupo4);
		LinkedList<List<DadosClasse>> grupos = new LinkedList<List<DadosClasse>>();
		for (DadosClasse classe: listaClasses) {
			gpe.classificarClassesGrupos(classe, grupos);
		}
		/* 
		 * Dois grupos pois cada classe forma um grupo e os grupos que são formados por apenas uma classe 
		 * se formam um grupo.  
		 * */
		assertEquals(grupos.size(), 2);
		gpe.formarGrupoClassesNaoClassificadas(grupos);
		assertEquals(grupos.size(), 1);
		/* 
		 * Utilizando o projeto de testes exemplo geral, serão formados 4 grupos e após 
		 * organizar os não classificados são formados 3.
		 * */
		listaClasses = gpe.obterListaClassesProjetoExemplo(true, projetoExemplo);
		grupos = new LinkedList<List<DadosClasse>>();
		for (DadosClasse classe: listaClasses) {
			gpe.classificarClassesGrupos(classe, grupos);
		}
		assertEquals(grupos.size(), 4);
		gpe.formarGrupoClassesNaoClassificadas(grupos);
		assertEquals(grupos.size(), 3);
	}
	
	public void imprimeTabela(LinkedList<DadosComponentesArquiteturais> tabelaComponentes) {
		for (DadosComponentesArquiteturais dca : tabelaComponentes) {
			System.out.print("Classe extendinda: "+dca.getExtendsClass());
			System.out.print("   --  Implements API: "+dca.getImplementsAPIJava());
			System.out.print("   --  Implements Arquitetura: "+dca.getImplementsArquitecture());
			System.out.print("   --  Mediana: " + dca.getMediana() + "   --  1º Quartil: " + 
					dca.getPrimeiroQuartil() + "   --  3º Quartil: " +dca.getTerceiroQuartil());
			System.out.println();
			System.out.println();
		}
	}
}
