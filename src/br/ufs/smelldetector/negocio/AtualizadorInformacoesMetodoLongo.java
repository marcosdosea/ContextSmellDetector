package br.ufs.smelldetector.negocio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import br.ufs.smelldetector.Activator;
import br.ufs.smelldetector.marker.MarkerFactory;
import br.ufs.smelldetector.model.DadosMetodoSmell;
import br.ufs.smelldetector.model.LimiarMetrica;
import br.ufs.smelldetector.model.LimiarMetricaKey;
import br.ufs.smelldetector.model.ProviderModel;
import br.ufs.smelldetector.preferences.PreferenceConstants;
import br.ufs.smelldetector.preferences.ValorMetodoLongoPreferencePage;
import br.ufs.smelldetector.views.MetodoLongoView;

public class AtualizadorInformacoesMetodoLongo {

	public static void refreshAll() {
		//AnalisadorProjeto analisadorProjeto = new AnalisadorProjeto();
		GerenciadorProjeto.validaProjetosAtivos(Activator.projetos);
		atulizarDadosProviderModel(/*analisadorProjeto*/);
		if (ProviderModel.INSTANCE.metodoslongos == null) {
			ProviderModel.INSTANCE.metodoslongos = new ArrayList<>();
		}
		refreshView();
		refreshMarcadores(ProviderModel.INSTANCE.metodoslongos);
		// refreshallProjects();
	}

	private static void atulizarDadosProviderModel(/*AnalisadorProjeto analisadorProjeto*/) {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		FiltrarMetodosSmell filtrarMetodos = new FiltrarMetodosSmell();
		HashMap<LimiarMetricaKey, LimiarMetrica> mapLimiarMetrica = null;
		if (store.getString(PreferenceConstants.USAR_P_EXEMPLO_V_LIMIAR)
				.equals(ValorMetodoLongoPreferencePage.OPCAOVALORLIMIAR)) {
			//ProviderModel.INSTANCE.dadosClasses = analisadorProjeto.getInfoMetodosPorProjetos(Activator.projetos);

			int limiarLOC = Integer.parseInt(store.getString(PreferenceConstants.VALOR_LIMIAR));

			mapLimiarMetrica = new HashMap<LimiarMetricaKey, LimiarMetrica>();
			mapLimiarMetrica = GerenciadorLimiares.obterLimiarPreDefinidoGlobal(limiarLOC, 15, 10, 4);

			ProviderModel.INSTANCE.metodoslongos = filtrarMetodos.filtrar(Activator.projetos,
					mapLimiarMetrica);

			System.out.println("Métodos longos valor limiar: " + ProviderModel.INSTANCE.metodoslongos.size()
					+ " métodos encontrados.");
		} else {
			if (store.getString(PreferenceConstants.USAR_P_EXEMPLO_V_LIMIAR)
					.equals(ValorMetodoLongoPreferencePage.OPCAOPROJETOEXEMPLO)) {
				if (store.getBoolean(PreferenceConstants.USAR_PREOCUPACAO_ARQUITETURAL)) {
//					ProviderModel.INSTANCE.dadosClasses = analisadorProjeto
//							.getInfoMetodosPorProjetos(Activator.projetos, true);
//
//					if (ProviderModel.INSTANCE.dadosComponentesArquiteturais == null) {
//						ProviderModel.INSTANCE.dadosComponentesArquiteturais = gpe.criarTabelaCompArquiteturais(
//								store.getString(PreferenceConstants.PROJETO_EXEMPLO),
//								store.getInt(PreferenceConstants.PORCENTAGEM_PROJETO_EXEMPLO));
//					}
					// ProviderModel.INSTANCE.metodoslongos = filtrarMetodos
					// .filtrarPorProjetoExemploPreocupacaoArquitetural(ProviderModel.INSTANCE.dadosClasses,
					// ProviderModel.INSTANCE.dadosComponentesArquiteturais);
					System.out.println("Métodos longos valor preocupação arquitetural: "
							+ ProviderModel.INSTANCE.metodoslongos.size() + " métodos encontrados.");
				} else {
					
					ArrayList<String> projetosBenchmark = new ArrayList(Arrays.asList(store.getString(PreferenceConstants.PROJETOS_EXEMPLOS).split(";")));
					int percentil = store.getInt(PreferenceConstants.PORCENTAGEM_PROJETO_EXEMPLO);
					
					mapLimiarMetrica = GerenciadorLimiares.obterLimiarBenchMarkPercentil(projetosBenchmark, percentil);
					ProviderModel.INSTANCE.metodoslongos = filtrarMetodos.filtrar(Activator.projetos,
							mapLimiarMetrica);

					
					
//					ProviderModel.INSTANCE.dadosClasses = analisadorProjeto
//							.getInfoMetodosPorProjetos(Activator.projetos, false);
//					if (ProviderModel.INSTANCE.valorLimiarGlobal == 0) {
//						ProviderModel.INSTANCE.valorLimiarGlobal = gpe.obterValorLimiarGlobal(
//								store.getString(PreferenceConstants.PROJETOS_EXEMPLO),
//								store.getInt(PreferenceConstants.PORCENTAGEM_PROJETO_EXEMPLO));
//					}
//					
//					if (ProviderModel.INSTANCE.medianaGlobal == 0) {
//						ProviderModel.INSTANCE.medianaGlobal = gpe.obterMedianaGlobal(
//								store.getString(PreferenceConstants.PROJETO_EXEMPLO),
//								store.getInt(PreferenceConstants.PORCENTAGEM_PROJETO_EXEMPLO));
//					}
					// Adicionar na lista de métodos longos
//					ProviderModel.INSTANCE.metodoslongos = filtrarMetodos.filtrarPorProjetoExemploGeral(
//							ProviderModel.INSTANCE.dadosClasses, ProviderModel.INSTANCE.valorLimiarGlobal,
//							ProviderModel.INSTANCE.medianaGlobal);
					System.out.println("Métodos longos valor global projeto exemplo: "
							+ ProviderModel.INSTANCE.metodoslongos.size() + " métodos encontrados.");
				}
			}
		}
	}

	public static void refreshMarcadores(ArrayList<DadosMetodoSmell> metodosLongos) {
		MarkerFactory marcador = new MarkerFactory();
		marcador.deleteTodosMarcadores();
		marcador.adicionarMarcadoresMetodosLongos(metodosLongos);
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
}
