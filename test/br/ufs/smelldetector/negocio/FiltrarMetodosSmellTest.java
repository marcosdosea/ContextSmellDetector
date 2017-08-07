package br.ufs.smelldetector.negocio;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import com.github.mauricioaniche.ck.CKNumber;

import br.ufs.smelldetector.model.DadosMetodoSmell;
import br.ufs.smelldetector.model.LimiarTecnica;

public class FiltrarMetodosSmellTest {

	private ArrayList<CKNumber> projetosAnalisar = new ArrayList<>();

	@Test
	public void testFiltrarPorValoresPredefinidos() {

		List<LimiarTecnica> listaTecnicas = new ArrayList<>();
		listaTecnicas = CarregaSalvaArquivo.carregarLimiares();

		ArrayList<String> listaPathProjetos = new ArrayList<>();
		listaPathProjetos.add("D:/Projetos/mobilemedia/MobileMedia01_OO");

		projetosAnalisar = AnalisadorProjeto.getMetricasProjetos(listaPathProjetos);

		HashMap<String, DadosMetodoSmell> metodosSmell = null;
		metodosSmell = FiltrarMetodosSmell.filtrar(projetosAnalisar, listaTecnicas, metodosSmell);

		exibeMetodosLongos(metodosSmell.values(), "Filtrar por valor limiar");

		assertTrue(metodosSmell.keySet().size() > 0);
	}

	// @Test
	// public void testFiltrarPorBenchmark() {
	// ArrayList<String> listaPathProjetosAnalisar = new ArrayList<>();
	// listaPathProjetosAnalisar.add("D:/Projetos/mobilemedia/MobileMedia01_OO");
	// projetosAnalisar =
	// AnalisadorProjeto.getMetricasProjetos(listaPathProjetosAnalisar);
	//
	// ArrayList<String> listaPathProjetosBenchmark = new ArrayList<>();
	// listaPathProjetosBenchmark.add("D:/Projetos/mobilemedia/MobileMedia01_OO");
	// projetosBenchMark =
	// AnalisadorProjeto.getMetricasProjetos(listaPathProjetosBenchmark);
	//
	// final int percentil = 90;
	//
	// HashMap<LimiarMetricaKey, LimiarMetrica> mapLimiarMetrica =
	// GerenciadorLimiares
	// .obterLimiarBenchMarkPercentil(projetosBenchMark, percentil);
	//
	// HashMap<String, DadosMetodoSmell> metodosSmell =
	// FiltrarMetodosSmell.filtrar(projetosAnalisar, mapLimiarMetrica,
	// null, "B");
	//
	// exibeMetodosLongos(metodosSmell.values(), "Filtrar Por BenchMark");
	//
	// assertTrue(metodosSmell.size() > 0);
	// }

	// @Test
	// public void testFiltrarPorBenchMarkDesignRole() {
	// ArrayList<String> listaPathProjetosAnalisar = new ArrayList<>();
	// listaPathProjetosAnalisar.add("D:/Projetos/mobilemedia/MobileMedia01_OO");
	// projetosAnalisar =
	// AnalisadorProjeto.getMetricasProjetos(listaPathProjetosAnalisar);
	//
	// ArrayList<String> listaPathProjetosBenchmark = new ArrayList<>();
	// listaPathProjetosBenchmark.add("D:/Projetos/mobilemedia/MobileMedia01_OO");
	// projetosBenchMark =
	// AnalisadorProjeto.getMetricasProjetos(listaPathProjetosBenchmark);
	// final int percentil = 90;
	//
	// HashMap<LimiarMetricaKey, LimiarMetrica> mapLimiarMetrica =
	// GerenciadorLimiares
	// .obterLimiarBenchMarkDesignRole(projetosBenchMark, percentil);
	//
	// HashMap<String, DadosMetodoSmell> metodosLongos =
	// FiltrarMetodosSmell.filtrar(projetosAnalisar,
	// mapLimiarMetrica, null, "C");
	//
	// exibeMetodosLongos(metodosLongos.values(), "Filtrar Por BenchMark");
	//
	// assertTrue(metodosLongos.size() > 0);
	// }

	private void exibeMetodosLongos(Collection<DadosMetodoSmell> metodosSmell, String descricaoMetodo) {
		System.out.println("\n\n\n" + descricaoMetodo);
		System.out.println("Classe   |     Método       |     Linhas Código     |  CC  | Efferent |  NOP");
		for (DadosMetodoSmell metodoSmell : metodosSmell) {
			System.out.println(metodoSmell.getNomeClasse() + "  | " + metodoSmell.getNomeMetodo() + " | "
					+ metodoSmell.getLinesOfCode() + "|" + metodoSmell.getComplexity() + " | "
					+ metodoSmell.getEfferent() + " | " + metodoSmell.getNumberOfParameters());
		}
		System.out.println("Total de métodos longos: " + metodosSmell.size());
	}

}
