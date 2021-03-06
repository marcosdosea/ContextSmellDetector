package br.ufs.smelldetector.negocio;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.PlatformUI;

import br.ufs.smelldetector.Activator;
import br.ufs.smelldetector.marker.MarkerFactory;

public class GerenciadorProjeto {

	public static void validaProjetosAtivos(ArrayList<String> projetos) {
		for (int i = 0; i < projetos.size(); i++) {
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			IProject iProject = workspace.getRoot().getProject(
					nomeProjetoPorCaminho(projetos.get(i)));
			
			String nome = nomeProjetoPorCaminho(projetos.get(i));
			boolean existe = iProject.exists();
			boolean opened = iProject.isOpen();
			
			if (!(iProject.exists() && iProject.isOpen())) {
				projetos.remove(i);
			}
		}
		Activator.projetos = projetos;
	}

	public static String nomeProjetoPorCaminho(String caminho) {
		String[] partes = caminho.split("/");
		return partes[partes.length-1];
	}

	public static String getCurrentProject() {    
		IProject project = null;    
		ISelectionService selectionService = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getSelectionService();;    
		ISelection selection = selectionService.getSelection();    
		if(selection instanceof IStructuredSelection) {    
			Object element = ((IStructuredSelection)selection).getFirstElement();    

			if (element instanceof IResource) {    
				project= ((IResource)element).getProject();    

			}  
			else if (element instanceof IJavaElement) {    
				IJavaProject jProject= ((IJavaElement)element).getJavaProject();    
				project = jProject.getProject();
			}
		}
		if(project == null)
			return null;
		else 
			return project.getLocation().toString();
	}

	public static void addProjectAnalysis() {
		String projetoSelecionado = GerenciadorProjeto.getCurrentProject();
		MessageDialog dialog = null;
		if (projetoSelecionado != null) {
			Activator.projetos.add(projetoSelecionado);
			AnalisadorProjeto.refreshAll();
			dialog = new MessageDialog(null, "ContextSmell", null, 
					"Project Added To Review", 
					MessageDialog.INFORMATION, new String[] {"OK"}, 0);
			
		} else {
			dialog = new MessageDialog(null, "ContextSmell", null, 
					"N�o foi poss�vel adicionar o projeto.\n"
							+ "Pois, n�o foi poss�vel obter o projeto selecionado", 
					MessageDialog.INFORMATION, new String[] {"OK"}, 0);
		}
		dialog.open();
	}

	public static void removeProjectAnalysis() {
		String projetoSelecionado = GerenciadorProjeto.getCurrentProject();
		MessageDialog dialog = null;
		if (projetoSelecionado != null) {
			ArrayList<String> projetos = Activator.projetos; 
			for (int i = 0; i < projetos.size(); i++) {
				if (projetos.get(i).equals(projetoSelecionado)) {
					System.out.println("Removeu Projeto: " + projetos.get(i));
					projetos.remove(i);
				}
			}
			Activator.projetos = projetos;
			new MarkerFactory().deleteMarcadorPorProjeto(projetoSelecionado);
			AnalisadorProjeto.refreshAll();
			dialog = new MessageDialog(null, "ContextLongMethod", null, 
					"Removed Analysis Project", 
					MessageDialog.INFORMATION, new String[] {"OK"}, 0);
		} else {
			dialog = new MessageDialog(null, "ContextLongMethod", null, 
					"N�o foi poss�vel remover o projeto. "
							+ "Pois, n�o foi poss�vel obter o projeto selecionado", 
					MessageDialog.INFORMATION, new String[] {"OK"}, 0);
		}
		dialog.open();
	}
	
	public static boolean projetoEstaNaAnalise(String verificaProjeto) {
		ArrayList<String> projetos = Activator.projetos;
		for (int i = 0; i < projetos.size(); i++) {
			if (projetos.get(i).equals(verificaProjeto)) {
				return true;
			}
		}
		return false;
	}
	
	public static void removerProjetoPorId(int posicao) {
		Activator.projetos.remove(posicao);
	}
	
	public static void alterarNameProjetoAnalisadoPorPosicao(String novoProjeto, int posicao) {
		Activator.projetos.remove(posicao);
		Activator.projetos.add(novoProjeto);
	}
}
