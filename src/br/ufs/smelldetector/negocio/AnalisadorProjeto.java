package br.ufs.smelldetector.negocio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.github.mauricioaniche.ck.CK;
import com.github.mauricioaniche.ck.CKNumber;
import com.github.mauricioaniche.ck.CKReport;

import br.ufs.smelldetector.Activator;
import br.ufs.smelldetector.marker.MarkerFactory;
import br.ufs.smelldetector.model.DadosMetodoSmell;
import br.ufs.smelldetector.model.LimiarTecnica;
import br.ufs.smelldetector.model.ProviderModel;
import br.ufs.smelldetector.views.MetodoLongoView;

public class AnalisadorProjeto {

	/**
	 * Busca todas as classes do projeto e faz o cálculo das métricas de cada
	 * classe.
	 * 
	 * @param projetos
	 * @param AdicionarArquitetura
	 * @return
	 */
	public static ArrayList<CKNumber> getMetricasProjetos(ArrayList<String> projetos) {
		ArrayList<CKNumber> listaClasses = new ArrayList<>();
		for (String path : projetos) {
			CKReport report = new CK().calculate(path);
			listaClasses.addAll(report.all());
			// listaClasses.addAll(obterMetricas(path).all());
		}
		return listaClasses;
	}

	public static void refreshAll() {
		GerenciadorProjeto.validaProjetosAtivos(Activator.projetos);
		if (ProviderModel.INSTANCE.metodosSmell == null) {
			ProviderModel.INSTANCE.metodosSmell = new HashMap<>();
		}
		atulizarDadosProviderModel();
		refreshView();
		refreshMarcadores(ProviderModel.INSTANCE.metodosSmell);
		// refreshallProjects();
	}

	/***
	 * Utiliza todas as técnicas disponíveis para realização do estudo de caso de
	 * comparação
	 */
	private static void atulizarDadosProviderModel() {
		List<LimiarTecnica> listaTecnicas = CarregaSalvaArquivo.carregarLimiares();
		ArrayList<CKNumber> projetosAnalisar = AnalisadorProjeto.getMetricasProjetos(Activator.projetos);
		FiltrarMetodosSmell.filtrar(projetosAnalisar, listaTecnicas, ProviderModel.INSTANCE.metodosSmell);
	}

	public static void refreshMarcadores(HashMap<String, DadosMetodoSmell> metodosSmell) {
		MarkerFactory marcador = new MarkerFactory();
		marcador.deleteTodosMarcadores();
		marcador.adicionarMarcadoresMetodosLongos(metodosSmell.values());
	}

	public static void refreshView() {
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
			public void run() {
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				if (window != null) {
					IWorkbenchPage page = window.getActivePage();
					try {
						if (page != null && page.findView(MetodoLongoView.ID) != null) {
							page.hideView(page.findView(MetodoLongoView.ID));
							page.showView(MetodoLongoView.ID);
							page.activate(page.getActiveEditor());
						}
					} catch (PartInitException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	// Não está sendo utilizado. Quando for usar os preferences então refazer esse
	// método.
	// private static void _atulizarDadosProviderModel() {
	// IPreferenceStore store = Activator.getDefault().getPreferenceStore();
	// HashMap<String, LimiarMetrica> mapLimiarMetrica = null;
	// if (store.getString(PreferenceConstants.USAR_P_EXEMPLO_V_LIMIAR)
	// .equals(ValorMetodoLongoPreferencePage.OPCAOVALORLIMIAR)) {
	// // ProviderModel.INSTANCE.dadosClasses =
	// // analisadorProjeto.getInfoMetodosPorProjetos(Activator.projetos);
	//
	// int limiarLOC =
	// Integer.parseInt(store.getString(PreferenceConstants.VALOR_LIMIAR));
	//
	// mapLimiarMetrica = new HashMap<String, LimiarMetrica>();
	// mapLimiarMetrica =
	// GerenciadorLimiares.obterLimiarPreDefinidoGlobal(limiarLOC, 15, 10, 4);
	//
	// // ProviderModel.INSTANCE.metodosSmell =
	// // FiltrarMetodosSmell.filtrar(Activator.projetos, mapLimiarMetrica,
	// // null, TECNICA_LIMIAR_FIXO);
	//
	// System.out.println("Métodos longos valor limiar: " +
	// ProviderModel.INSTANCE.metodosSmell.size()
	// + " métodos encontrados.");
	//
	// }
	// if (store.getString(PreferenceConstants.USAR_P_EXEMPLO_V_LIMIAR)
	// .equals(ValorMetodoLongoPreferencePage.OPCAOPROJETOEXEMPLO)) {
	// if (store.getBoolean(PreferenceConstants.USAR_PREOCUPACAO_ARQUITETURAL)) {
	// // ProviderModel.INSTANCE.dadosClasses = analisadorProjeto
	// // .getInfoMetodosPorProjetos(Activator.projetos, true);
	// //
	// // if (ProviderModel.INSTANCE.dadosComponentesArquiteturais == null) {
	// // ProviderModel.INSTANCE.dadosComponentesArquiteturais =
	// // gpe.criarTabelaCompArquiteturais(
	// // store.getString(PreferenceConstants.PROJETO_EXEMPLO),
	// // store.getInt(PreferenceConstants.PORCENTAGEM_PROJETO_EXEMPLO));
	// // }
	// // ProviderModel.INSTANCE.metodosSmell = filtrarMetodos
	// //
	// .filtrarPorProjetoExemploPreocupacaoArquitetural(ProviderModel.INSTANCE.dadosClasses,
	// // ProviderModel.INSTANCE.dadosComponentesArquiteturais);
	// System.out.println("Métodos longos valor preocupação arquitetural: "
	// + ProviderModel.INSTANCE.metodosSmell.size() + " métodos encontrados.");
	// } else {
	//
	// ArrayList<String> projetosBenchmark = new ArrayList(
	// Arrays.asList(store.getString(PreferenceConstants.PROJETOS_EXEMPLOS).split(";")));
	// int percentil =
	// store.getInt(PreferenceConstants.PORCENTAGEM_PROJETO_EXEMPLO);
	//
	// // mapLimiarMetrica =
	// // GerenciadorLimiares.obterLimiarBenchMarkPercentil(projetosBenchmark,
	// // percentil);
	// // ProviderModel.INSTANCE.metodosSmell =
	// // FiltrarMetodosSmell.filtrar(Activator.projetos, mapLimiarMetrica,
	// // null, TECNICA_LIMIAR_BENCHMARK);
	//
	// // ProviderModel.INSTANCE.dadosClasses = analisadorProjeto
	// // .getInfoMetodosPorProjetos(Activator.projetos, false);
	// // if (ProviderModel.INSTANCE.valorLimiarGlobal == 0) {
	// // ProviderModel.INSTANCE.valorLimiarGlobal = gpe.obterValorLimiarGlobal(
	// // store.getString(PreferenceConstants.PROJETOS_EXEMPLO),
	// // store.getInt(PreferenceConstants.PORCENTAGEM_PROJETO_EXEMPLO));
	// // }
	// //
	// // if (ProviderModel.INSTANCE.medianaGlobal == 0) {
	// // ProviderModel.INSTANCE.medianaGlobal = gpe.obterMedianaGlobal(
	// // store.getString(PreferenceConstants.PROJETO_EXEMPLO),
	// // store.getInt(PreferenceConstants.PORCENTAGEM_PROJETO_EXEMPLO));
	// // }
	// // Adicionar na lista de métodos longos
	// // ProviderModel.INSTANCE.metodosSmell =
	// // filtrarMetodos.filtrarPorProjetoExemploGeral(
	// // ProviderModel.INSTANCE.dadosClasses,
	// // ProviderModel.INSTANCE.valorLimiarGlobal,
	// // ProviderModel.INSTANCE.medianaGlobal);
	// System.out.println("Métodos longos valor global projeto exemplo: "
	// + ProviderModel.INSTANCE.metodosSmell.size() + " métodos encontrados.");
	// }
	// }

	// }

}
