package br.ufs.smelldetector.negocio;

import java.io.File;
import java.util.ArrayList;

import com.github.mauricioaniche.ck.CK;
import com.github.mauricioaniche.ck.CKNumber;
import com.github.mauricioaniche.ck.CKReport;

public class AnalisadorProjeto {

	/**
	 * Busca todas as classes do projeto e faz o cálculo das métricas de cada classe.
	 * @param projetos
	 * @param AdicionarArquitetura
	 * @return
	 */
	public ArrayList<CKNumber> getInfoMetodosPorProjetos(ArrayList<String> projetos){
//		ArrayList<DadosClasse> dadosTodasClasses = null;
//		ArrayList<String> listaArquivosdiretorios = new ArrayList<>();
//		for (String diretorio : projetos) {
//			listaArquivosdiretorios.addAll(getArquivosPorProjeto(diretorio));
//		}
//		
		ArrayList<CKNumber> listaClasses = new ArrayList<>();
		for (String path : projetos) {
			CKReport report = new CK().calculate(path);
			listaClasses.addAll(report.all());
    	}
		
//		
//		
//	
//		AnalisadorClasseMetodo analisadorClasseMetodo = new AnalisadorClasseMetodo();
//		try {
//			if (AdicionarArquitetura) {
//				dadosTodasClasses = analisadorClasseMetodo
//						.getInfoMetodosDosArquivos(listaArquivosdiretorios, true);
//			} else {
//				dadosTodasClasses = analisadorClasseMetodo
//						.getInfoMetodosDosArquivos(listaArquivosdiretorios, false);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return listaClasses;
	}

	/**
	 * Busca todos os arquivos com extensão JAVA
	 * @param nomeDiretorio onde as classes serão buscadas.
	 * @return
	 */
	public ArrayList<String> getArquivosPorProjeto(String nomeDiretorio){
		//System.out.println();
		//System.out.println();
		ArrayList<String> retorno = new ArrayList<>();
		ArrayList<File> listaArquivos = new ArrayList<>();
		File directory = new File(nomeDiretorio);
		listaArquivos.add(directory);
		while (!isAllFile(listaArquivos)) {
			//get all the files from a directory
			for (int i = 0; i < listaArquivos.size(); i++) {
				if (listaArquivos.get(i).isDirectory()) {
					File[] fList = listaArquivos.get(i).listFiles();
					listaArquivos.remove(i);
					for (int j = 0; j < fList.length; j++) {
						if (fList[j].isDirectory()) {
							listaArquivos.add(fList[j]);
						} else {
							if (fList[j].getPath().endsWith(".java")) {
								//System.out.println(fList[j].getAbsolutePath());
								retorno.add(fList[j].getAbsolutePath());
							}
						}
					}
				}
			}
		}
		//System.out.println();
		//System.out.println();
		return retorno;
	}

	public boolean isAllFile(ArrayList<File> listaFiles) {
		for (File file : listaFiles) {
			if (file.isDirectory()) {
				return false;
			}
		}
		return true;
	}

}
