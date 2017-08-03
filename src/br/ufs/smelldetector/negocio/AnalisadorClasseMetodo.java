package br.ufs.smelldetector.negocio;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import br.ufs.smelldetector.model.DadosMetodo;

/**
 * 
 * @author Kekeu
 *
 */
public class AnalisadorClasseMetodo {

//	public ArrayList<CKNumber> getInfoMetodosDosArquivos(
//			ArrayList<String> files, boolean adicionarArquitetura) throws IOException {
//		ArrayList<CKNumber> dadosClasses = new ArrayList<>();
//		for (String localFile : files) {
//			String textoDaClasse = readFileToString(localFile);
//			ASTParser parser = ASTParser.newParser(AST.JLS8);
//			parser.setSource(textoDaClasse.toCharArray());
//			parser.setKind(ASTParser.K_COMPILATION_UNIT);
//			final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
//
//			cu.accept(new ASTVisitor() {
//				@Override
//				public boolean visit(TypeDeclaration node) {
//					DadosClasse classe = new DadosClasse();
//					ArrayList<String> listaClassesEI = null;
//					if (adicionarArquitetura) {
//						listaClassesEI = obterListaClassesEI(node);	
//					}
//					String nomeDaClasse = node.getName().toString();
//					MethodDeclaration[] methodDeclaration = node.getMethods();
//					ArrayList<DadosMetodo> metodosDaClasse = new ArrayList<>();
//					for (int i = 0; i < methodDeclaration.length; i++) {
//						if (!methodDeclaration[i].isConstructor()) {
//							metodosDaClasse.add(obterInfoMetodo(cu, methodDeclaration[i]));
//						}
//					}
//					classe.setClassesExtendsImplements(listaClassesEI);
//					classe.setDiretorioDaClasse(localFile);
//					classe.setMetodos(metodosDaClasse);
//					classe.setNomeClasse(nomeDaClasse);
//					dadosClasses.add(classe);
//					//System.out.println(nomeDaClasse +"  --  "+metodosDaClasse.size());
//					//System.out.println();
//					return true;
//				}
//			});
//		}
//		return dadosClasses;
//	}

	//read file content into a string
	public String readFileToString(String filePath) throws IOException {
		StringBuilder fileData = new StringBuilder(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));

		char[] buf = new char[10];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			//System.out.println(numRead);
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		reader.close();
		return  fileData.toString();	
	}

	public int contarNumeroDeLinhas(MethodDeclaration node) {
		String[] linhasMetodo = node.toString().split("\n"); 
		if (node.getJavadoc() == null) {
			return linhasMetodo.length;
		} else {
			String[] linhasTextoJavadoc = node.getJavadoc().toString().split("\n");
			return linhasMetodo.length - linhasTextoJavadoc.length;
		}
	}

	public int qtdCaracteresJavadoc(MethodDeclaration node) {
		if (node.getJavadoc() == null) {
			return 0;
		}
		return node.getJavadoc().getLength();
	}

	private ArrayList<String> obterListaClassesEI(TypeDeclaration node) {
		Type superClass = node.getSuperclassType();
		ArrayList<String> listaEI = new ArrayList<>();
		if (superClass != null) {
			listaEI.add(superClass.toString());
		} else {
			listaEI.add(null);
		}
		List<?> lista = node.superInterfaceTypes();
		for (Object object : lista) {
			//System.out.println(object.toString());
			listaEI.add(object.toString());
		}
		return listaEI;
	}

	private DadosMetodo obterInfoMetodo(final CompilationUnit cu, MethodDeclaration metodo) {
		VisitMethod visitMethod = new VisitMethod(cu, metodo);
		
		
		return visitMethod.getDadosMetodo();
	}
	
	private class VisitMethod extends ASTVisitor {
		DadosMetodo informacoesMetodo;
		MethodDeclaration metodo;
		Map<String, String> declaredTypes = new HashMap<>();
		Set<String> usedTypes = new HashSet<String>();
		Stack<DadosMetodo> methodStack = new Stack<DadosMetodo>();
		//String currentMethod;
		CompilationUnit cu;

		private VisitMethod(final CompilationUnit cu, MethodDeclaration metodo) {
			this.informacoesMetodo = new DadosMetodo();
			this.metodo = metodo;
			this.cu = cu;
			
			//currentMethod = metodo.getName().getIdentifier();
		}
		
		public DadosMetodo getDadosMetodo() {
			informacoesMetodo.setLinhaInicial(cu.getLineNumber(
					metodo.getName().getStartPosition()));
			informacoesMetodo.setLinesOfCode(contarNumeroDeLinhas(metodo));
			informacoesMetodo.setNumberOfParameters(metodo.parameters().size());
		
			cu.accept(this);
			
			informacoesMetodo.setNomeMetodo(metodo.getName().toString());
			informacoesMetodo.setLinhaInicial(cu.getLineNumber(
					metodo.getName().getStartPosition()));
			informacoesMetodo.setCharInicial(metodo.getStartPosition());
			informacoesMetodo.setCharFinal(metodo.getLength()+
					metodo.getStartPosition());
		
			return informacoesMetodo;
		}

		public boolean visit(MethodDeclaration node) {  	
			
			node.resolveBinding();

			List<SingleVariableDeclaration> currentParameters = node.parameters();
			for (SingleVariableDeclaration parameter: currentParameters) {
				declaredTypes.put(parameter.getName().getIdentifier(), parameter.getType().toString());
			}
			if (!node.isConstructor() &&  (node.getReturnType2() != null) &&!node.getReturnType2().toString().equals("void")) {
				usedTypes.add(node.getReturnType2().toString());
			}
			
//		    	MethodData methodData = getMethodData();
//		    	if (!metricsByMethod.containsKey(methodData))
//		    		metricsByMethod.put(methodData, new MethodMetrics());
//		    	
//		    	MethodMetrics methodMetrics =  metricsByMethod.get(methodData);
//		    	methodMetrics.setLinesOfCode(Utils.countLineNumbers(node.getBody().toString()));
//		    	methodMetrics.setNumberOfParameters(node.parameters().size());
			
			methodStack.push(informacoesMetodo);
			increaseCc();
			return super.visit(node);
		}

		public boolean visit(ClassInstanceCreation node) {
			String typeDeclared =  node.getType().toString();
			usedTypes.add(typeDeclared);
			return super.visit(node);
		}

		public boolean visit(SingleVariableDeclaration node) {
			String typeDeclared =  node.getType().toString();
			usedTypes.add(typeDeclared);
			return super.visit(node);
		}

		public boolean visit(VariableDeclarationStatement node) {
			String typeDeclared =  node.getType().toString();
			usedTypes.add(typeDeclared);
			return super.visit(node);
		}

		public boolean visit(FieldDeclaration node) {
			String typeDeclared =  node.getType().toString();
			
			List<VariableDeclarationFragment> fragments = (List<VariableDeclarationFragment>) node.fragments();
			
			for (VariableDeclarationFragment fragment : fragments ) {
				declaredTypes.put(fragment.getName().getIdentifier(), typeDeclared);
			}
			return super.visit(node);
		}

		public boolean visit(FieldAccess node) {
			String identifier = node.getName().getIdentifier();
			String type = declaredTypes.get(identifier);
			//String type = node.getExpression().resolveTypeBinding().getName();
				 
		   	if (type != null) {
				usedTypes.add(type);
			} else {
				System.out.println("problema em encontrar o tipo no campo "+node.toString());
			}
			return super.visit(node);
		}

		public boolean visit(MethodInvocation node) {
			List<Expression> arguments = (List<Expression>) node.arguments();
			
			Expression exp = node.getExpression();
			if (exp != null) {
				String type = declaredTypes.get(exp.toString());
				
			   	if (type != null) {
			   		usedTypes.add(type);
			   	} else if (!exp.toString().contains("(") &&  Character.isUpperCase(exp.toString().charAt(0)))	{
		   			// chamada estática de método
			   		usedTypes.add(exp.toString());
			   	}
			}
			
			for(Expression argument: arguments) {
				if (argument instanceof SimpleName) {
					String identifier = node.getName().getIdentifier();
		   	   		
			    	String type = declaredTypes.get(identifier);
			   		if (type != null) {
			   			usedTypes.add(type);
			   		}
				}
			}
			return super.visit(node);
		}

		public void endVisit(MethodDeclaration node) {
			//MethodMetrics methodMetrics =  metricsByMethod.get(getMethodData());
			//methodMetrics.setEfferentCoupling(usedTypes.size());
			informacoesMetodo.setEfferent(usedTypes.size());
			usedTypes.clear();
			methodStack.pop();
		}

		@Override
		public boolean visit(ForStatement node) {
			increaseCc();
			
			return super.visit(node);
		}

		@Override
		public boolean visit(EnhancedForStatement node) {
			increaseCc();
			return super.visit(node);
		}

		@Override
		public boolean visit(ConditionalExpression node) {
			increaseCc();
			return super.visit(node);
		}

		@Override
		public boolean visit(DoStatement node) {
			increaseCc();
			return super.visit(node);
		}

		@Override
		public boolean visit(WhileStatement node) {
			increaseCc();
			return super.visit(node);
		}

		@Override
		public boolean visit(SwitchCase node) {
			if(!node.isDefault())
				increaseCc();
			return super.visit(node);
		}

		@Override
		public boolean visit(Initializer node) {
			//currentMethod = "(static_block)";
		
			methodStack.push(informacoesMetodo);
			increaseCc();
			return super.visit(node);
		}

		@Override
		public void endVisit(Initializer node) {
			methodStack.pop();
		}

		@Override
		public boolean visit(CatchClause node) {
			increaseCc();
			String typeDeclared =  node.getException().getType().toString();
			usedTypes.add(typeDeclared);
			return super.visit(node);
		}

		public boolean visit(IfStatement node) {
			
			String expr = node.getExpression().toString().replace("&&", "&").replace("||", "|");
			int ands = StringUtils.countMatches(expr, "&");
			int ors = StringUtils.countMatches(expr, "|");
			
			increaseCc(ands + ors);
			increaseCc();
			
			return super.visit(node);
		}

		private void increaseCc() {
			increaseCc(1);
		}

		private void increaseCc(int qtd) {
			// i dont know the method... ignore!
			if(methodStack.isEmpty()) return;
			//MethodData methodData = getMethodData();
			
			//if (!metricsByMethod.containsKey(methodData))
				//metricsByMethod.put(methodData, new MethodMetrics());
			//MethodMetrics methodMetrics =  metricsByMethod.get(methodData);
			informacoesMetodo.setComplexity(informacoesMetodo.getComplexity() + qtd);
		}
	}

}
