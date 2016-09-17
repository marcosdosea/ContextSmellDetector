package br.ufs.smelldetector.negocio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.junit.Test;

import br.ufs.smelldetector.model.DadosComponentesArquiteturais;

public class GerenciadorProjetoExemploTest {

	/* Projeto de exemplo tem 3 classes que estende mesma classe, 2 que implementa interface da api 
	 * e duas que não se encaixam em nenhuma regra. */
	private String projetoExemplo = "C:/workspace/TestesProjetoExemplo";
	private final int porcentagemLimiar = 75; 

	/* 
	 * A tabela deve conter 3 componentes arquiteturais como segue: 
	 * 1 - implementa a regra 1 (estende a mesma classe)
	 * 2 - implementa a regra 3 (implementa interface da API)
	 * 3 - implementa a regra 4 (Não se encaixa em nenhuma das outras regras)
	 */
	@Test
	public void criarTabelaCompArquiteturais() {
		GerenciadorProjetoExemplo gpe = new GerenciadorProjetoExemplo();
		LinkedList<DadosComponentesArquiteturais> tabela = gpe
				.criarTabelaCompArquiteturais(projetoExemplo, porcentagemLimiar);
		imprimeTabela(tabela);
		assertEquals(tabela.size(), 3);
		assertTrue((tabela.get(0).getExtendsClass().equals("AbstractDocument") && tabela.get(0).getImplementsAPIJava() == null && tabela.get(0).getImplementsArquitecture() == null) ||
				(tabela.get(1).getExtendsClass().equals("AbstractDocument") && tabela.get(1).getImplementsAPIJava() == null && tabela.get(1).getImplementsArquitecture() == null) ||
				(tabela.get(2).getExtendsClass().equals("AbstractDocument") && tabela.get(2).getImplementsAPIJava() == null && tabela.get(2).getImplementsArquitecture() == null));
		
		assertTrue((tabela.get(0).getExtendsClass() == null && tabela.get(0).getImplementsAPIJava().equals("Serializable") && tabela.get(0).getImplementsArquitecture() == null) ||
				(tabela.get(1).getExtendsClass() == null && tabela.get(1).getImplementsAPIJava().equals("Serializable") && tabela.get(1).getImplementsArquitecture() == null) ||
				(tabela.get(2).getExtendsClass() == null && tabela.get(2).getImplementsAPIJava().equals("Serializable") && tabela.get(2).getImplementsArquitecture() == null));
		
		assertTrue((tabela.get(0).getExtendsClass() == null && tabela.get(0).getImplementsAPIJava() == null && tabela.get(0).getImplementsArquitecture() == null) ||
				(tabela.get(1).getExtendsClass() == null && tabela.get(1).getImplementsAPIJava() == null && tabela.get(1).getImplementsArquitecture() == null) ||
				(tabela.get(2).getExtendsClass() == null && tabela.get(2).getImplementsAPIJava() == null && tabela.get(2).getImplementsArquitecture() == null));
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
