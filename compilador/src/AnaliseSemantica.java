import java.util.Hashtable;
import analysis.DepthFirstAdapter;
import node.*;

public class AnaliseSemantica extends DepthFirstAdapter {

	// ------------------------------------ Fun��es auxiliares ------------------------------------ //

	// Checa se j� existe a chave na tabela. Caso exista, lan�a exce��o. Caso n�o exista, pode inserir
	private void inserirNaTabela(String nome_variavel, String tipo) {

		boolean a = checarTabela(nome_variavel);
		if (a == true) // pode inserir
			tabelaDeSimbolos.put(nome_variavel, tipo);
		else
			throw new MyException("A vari�vel '" + nome_variavel + "' j� foi declarada previamente."); // lan�a exce��o
	}

	// Busca o tipo da chave (nome_variavel) passada
	private String getTipoTabela(String chave) {
		String a = tabelaDeSimbolos.get(chave);
		return a;
	}

	
	// Checa se � vetor ou n�o, e extrai o tipo da vari�vel
	private String tratarTipoTabela (String str, String tipo) {
		if (tipo.compareTo("var") == 0) { // � vari�vel
			String aux = getTipoTabela(str).toString().substring(0, 3);
			if (aux.compareTo("vet") == 0 || aux.compareTo("cst") == 0) {
				str = getTipoTabela(str).substring(3,6);
			} else
				str = getTipoTabela(str);
		} else { // se n�o for vari�vel
			str = checarTipo(str);
		}
		return str;
	}
	
	// Checa se determinada vari�vel j� est� na tabela. Caso esteja, retorna false.
	private boolean checarTabela(String nome_variavel) {

		String aux_key = getTipoTabela(nome_variavel);
		boolean a = false;
		if (aux_key == null)
			a = true;
		return a;
	}

	// Remove os espa�os em branco
	private String removeAllBlankSpaces(String str) {

		str = str.replaceAll("\\s+", "");
		return str;
	}
	
	private String removeFirstBlankSpace (String str) {
		str = str.substring(1); // Removendo o primeiro
		return str;
	}
	
	private String removeLastBlankSpace (String str) {
		str = str.substring(0, str.length() - 1); // removendo o �ltimo
		return str;
	}
	
	// Remove os colchetes
	private String removeBrackets(String str) {

		str = str.replace("[", "");
		str = str.replace("]", "");
		return str;
	}
	
	private String checarTipo (String str) {
		String a;
		if (str.matches ("[0-9]+")) {  // qualquer numero
			a = "int";
		} else if (str.matches ("[0-9]+,[0-9]+")) { // qualquer coisa com virgula no meio
			a = "flo";
		} else if (str.matches ("['].+[']")) // qualquer coisa entre aspas
			a = "str";
		else
			a = "var";
		return a;
	}
	
	// retorna true se for vetor
	private boolean testeVetor (String str) {
		int idx = str.indexOf(" "); // vai retornar -1 caso n�o exista espa�o, e s� existe espa�o em vetores
		if (idx != -1) // � vetor
			return true;
		else 
			return false;
	}

	// Cria��o da Tabela Hash
	private Hashtable<String, String> tabelaDeSimbolos = new Hashtable<>();

	/*
	 * FORMATO DE INSER��O NA TABELA
	 * 
	 * Constante: [nome_constante] = cst+[tipo]+[valor], onde [tipo] = int, flo ou str 
	 * 	Ex: 
	 * 		const a 1; a=cstint1 
	 * Vari�vel: [nome_variavel] = [tipo_variavel]
	 * 	Ex: 
	 * 		inteiro a1 a1=inteiro 
	 * Vetor: [nome_vetor] = vet+[tipo_vetor]+[tamanho_vetor] 
	 * 	Ex: 
	 * 		inteiro a[1]; a=vetint1
	 * 
	 */

	// ------------------------------------------ SOBREPOSI��O DE M�TODOS ------------------------------------------ //

	// ------------------------------------------ Inser��o ------------------------------------------ //
	// Inserir o nome do programa na tabela
	@Override
	public void outStart(Start node) {

		String str = node.toString();
		String[] program_id = str.split(" "); // para separar a string em um vetor de strings (crit�rio de corte:
		// v�rgula)
		inserirNaTabela(program_id[0], "String");

		// System.out.println(tabelaDeSimbolos.toString());
	}

	// Inserir vari�vel na tabela
	@Override
	public void outAVarDeclaracao(AVarDeclaracao node) {

		String var = node.getVar().toString();
		String tipo = node.getTipo().toString();
		var = removeBrackets(var); // remover colchetes
		tipo = removeAllBlankSpaces(tipo); // remover espa�os em branco
		
		if (tipo.compareTo("inteiro") == 0)
			tipo = "int";
		else if (tipo.compareTo("real") == 0)
			tipo = "flo";
		else if (tipo.compareTo("caractere") == 0)
			tipo = "str";

		String[] textoseparado = var.split(","); // separar a string em um vetor de strings (crit�rio de corte: v�rgula)

		// Remover primeiro e �ltimo espa�o
		for (int i = 0; i < textoseparado.length; i++) {
			textoseparado[i] = textoseparado[i].substring(0, textoseparado[i].length() - 1); // removendo o �ltimo
			// caractere
			if (i >= 1) { // removendo o primeiro caractere (s� precisa a partir do �ndice 1
				textoseparado[i] = textoseparado[i].substring(1);
			}
		}

		// Inserir na tabela
		for (int i = 0; i < textoseparado.length; i++) {
			
			if (testeVetor(textoseparado[i])) { // � vetor
				String[] aux = textoseparado[i].split(" "); // dividir o vetor em id (aux[0]) e tamanho (aux[1])

				// Checar se o tamanho do vetor � 0 (n�o pode ser menor que 0 pelo sint�tico)
				if (Integer.parseInt(aux[1]) == 0) {
					throw new MyException("O tamanho do vetor n�o pode ser menor ou igual a 0."); // lan�a exce��o
				}

				inserirNaTabela(aux[0], "vet" + tipo + aux[1]);
			} else // n�o � vetor
				inserirNaTabela(textoseparado[i], tipo);
		}
		//System.out.println(tabelaDeSimbolos.toString());
	}

	// Inser��o de Constante (str) na Tabela
	@Override 
	public void outAStringValor(AStringValor node) {
		String aux_class = node.parent().getClass().toString();
		String aux = node.parent().toString();

		// Se for uma declara��o
		if (aux_class.compareTo("class node.AConstDeclaracao") == 0) {
			String[] aux2 = aux.split(" "); // dividir o vetor em id (aux1[0]) e valor (aux1[1])
			inserirNaTabela(aux2[0], "cst"+"str"+aux2[1]);
		} 
		//System.out.println(tabelaDeSimbolos.toString());
	}
	
	// Inser��o de Constante (int) na Tabela
	@Override
	public void outAIntValor(AIntValor node) {
		String aux_class = node.parent().getClass().toString();
		String aux = node.parent().toString();

		// Se for uma declara��o
		if (aux_class.compareTo("class node.AConstDeclaracao") == 0){
			String[] aux2 = aux.split(" "); // dividir o vetor em id (aux1[0]) e valor (aux1[1])
			inserirNaTabela(aux2[0], "cst"+"int"+aux2[1]);
		}
		//System.out.println(tabelaDeSimbolos.toString());
	}
	
	// Inser��o de Constante (flo) na Tabela
	@Override 
	public void outAFloatValor(AFloatValor node) { 
		String aux_class = node.parent().getClass().toString();
		String aux = node.parent().toString();

		// Se for uma declara��o
		if (aux_class.compareTo("class node.AConstDeclaracao") == 0) {
			String[] aux2 = aux.split(" "); // dividir o vetor em id (aux1[0]) e valor (aux1[1])
			inserirNaTabela(aux2[0], "cst"+"flo"+aux2[1]);
		} 
		//System.out.println(tabelaDeSimbolos.toString());
	}
	
	// ------------------------------------------ Checagem de tipos ------------------------------------------ //
	// atribui��o
	@Override
	public void outAAtribuicaoComando(AAtribuicaoComando node) {
		String id_var = node.getVar().toString();
		String exp = node.getExp().toString();
		id_var = removeLastBlankSpace(id_var);

		// Testa se ID_VAR � um vetor (x[i]) ou uma vari�vel (x)
		if (testeVetor(id_var)) { // � vetor
			String[] aux = id_var.split(" "); // dividir o vetor em id (aux[0]) e �ndice (aux[1])
			id_var = aux[0]; // pega s� o id
		} else // n�o � vetor
			id_var = removeAllBlankSpaces(id_var);
		
		// Testa se EXP � um vetor (x[i]) ou uma vari�vel (x)
		if (testeVetor(exp)) {
			String[] aux1 = exp.split(" "); // dividir o vetor em id (aux1[0]) e �ndice (aux1[1])
			exp = aux1[0]; // pega s� o id
		} else
			exp = removeAllBlankSpaces(exp);
		
		// Se a exp for uma vari�vel, checar se ela j� foi declarada
		String a1 = node.getExp().getClass().toString();
		if (a1.compareTo("class node.AVarExp") == 0)		
			if (!tabelaDeSimbolos.containsKey(exp)) 
				throw new MyException("A vari�vel '" + exp + "' n�o foi declarada previamente."); // lan�a exce��o
		
		// Pra checar se tem na tabela ou n�o
		if (!tabelaDeSimbolos.containsKey(id_var))
			throw new MyException("A vari�vel '" + id_var + "' n�o foi declarada previamente."); // lan�a exce��o
		
		// se for constante
		if (getTipoTabela(id_var).substring(0, 3).compareTo("cst") == 0)
			throw new MyException("Vari�vel do tipo Constante n�o pode ter seu valor alterado."); // lan�a exce��o
	}

	@Override
    public void outAOpSumExp(AOpSumExp node) {
		String a = node.getLeft().toString();
		String b = node.getRight().toString();
		String a_class = node.getLeft().getClass().toString();
		String b_class = node.getRight().getClass().toString();
		a = removeLastBlankSpace(a);
		b = removeLastBlankSpace(b);
		
		boolean isVetor = testeVetor(a); // Testa se A � um vetor (x[i]) ou uma vari�vel (x)
		
		if (isVetor) { // se for vetor
			String[] aux = a.split(" "); // dividir o vetor em id (aux1[0]) e �ndice (aux1[1])
			a = aux[0]; // pega s� o id
		} else // se n�o for vetor
			a = removeAllBlankSpaces(a);
		
		// Se a exp for uma vari�vel, checar se ela j� foi declarada
		if (a_class.compareTo("class node.AVarExp") == 0)		
			if (!tabelaDeSimbolos.containsKey(a)) 
				throw new MyException("A vari�vel '" + a + "' n�o foi declarada previamente."); // lan�a exce��o
			else
				if (getTipoTabela(a).substring(0, 3).compareTo("vet") != 0  && isVetor == true)
					throw new MyException(" A vari�vel '" + a + "' n�o � um vetor."); // lan�a exce��o
		
		isVetor = testeVetor(b);
		// Testa se B � um vetor (x[i]) ou uma vari�vel (x)
		if (isVetor) {
			String[] aux1 = b.split(" "); // dividir o vetor em id (aux1[0]) e �ndice (aux1[1])
			b = aux1[0]; // pega s� o id
		} else
			b = removeAllBlankSpaces(b);

		// Se a exp for uma vari�vel, checar se ela j� foi declarada
		if (b_class.compareTo("class node.AVarExp") == 0)		
			if (!tabelaDeSimbolos.containsKey(b)) 
				throw new MyException("A vari�vel '" + b + "' n�o foi declarada previamente."); // lan�a exce��o
			else 
				if (getTipoTabela(b).substring(0, 3).compareTo("vet") != 0 && isVetor == true)
					throw new MyException(" A vari�vel '" + b + "' n�o � um vetor."); // lan�a exce��o
	}
	@Override
	public void outAOpSubExp(AOpSubExp node) {
		String a = node.getLeft().toString();
		String b = node.getRight().toString();
		String a_class = node.getLeft().getClass().toString();
		String b_class = node.getRight().getClass().toString();
		a = removeLastBlankSpace(a);
		b = removeLastBlankSpace(b);
		
		boolean isVetor = testeVetor(a); // Testa se A � um vetor (x[i]) ou uma vari�vel (x)
		
		if (isVetor) { // se for vetor
			String[] aux = a.split(" "); // dividir o vetor em id (aux1[0]) e �ndice (aux1[1])
			a = aux[0]; // pega s� o id
		} else // se n�o for vetor
			a = removeAllBlankSpaces(a);
		
		// Se a exp for uma vari�vel, checar se ela j� foi declarada
		if (a_class.compareTo("class node.AVarExp") == 0)		
			if (!tabelaDeSimbolos.containsKey(a)) 
				throw new MyException("A vari�vel '" + a + "' n�o foi declarada previamente."); // lan�a exce��o
			else
				if (getTipoTabela(a).substring(0, 3).compareTo("vet") != 0  && isVetor == true)
					throw new MyException(" A vari�vel '" + a + "' n�o � um vetor."); // lan�a exce��o
		
		isVetor = testeVetor(b);
		// Testa se B � um vetor (x[i]) ou uma vari�vel (x)
		if (isVetor) {
			String[] aux1 = b.split(" "); // dividir o vetor em id (aux1[0]) e �ndice (aux1[1])
			b = aux1[0]; // pega s� o id
		} else
			b = removeAllBlankSpaces(b);

		// Se a exp for uma vari�vel, checar se ela j� foi declarada
		if (b_class.compareTo("class node.AVarExp") == 0)		
			if (!tabelaDeSimbolos.containsKey(b)) 
				throw new MyException("A vari�vel '" + b + "' n�o foi declarada previamente."); // lan�a exce��o
			else 
				if (getTipoTabela(b).substring(0, 3).compareTo("vet") != 0 && isVetor == true)
					throw new MyException(" A vari�vel '" + b + "' n�o � um vetor."); // lan�a exce��o
    }
	
	@Override
    public void outAOpMultExp(AOpMultExp node) {
		String a = node.getLeft().toString();
		String b = node.getRight().toString();
		String a_class = node.getLeft().getClass().toString();
		String b_class = node.getRight().getClass().toString();
		a = removeLastBlankSpace(a);
		b = removeLastBlankSpace(b);
		
		boolean isVetor = testeVetor(a); // Testa se A � um vetor (x[i]) ou uma vari�vel (x)
		
		if (isVetor) { // se for vetor
			String[] aux = a.split(" "); // dividir o vetor em id (aux1[0]) e �ndice (aux1[1])
			a = aux[0]; // pega s� o id
		} else // se n�o for vetor
			a = removeAllBlankSpaces(a);
		
		// Se a exp for uma vari�vel, checar se ela j� foi declarada
		if (a_class.compareTo("class node.AVarExp") == 0)		
			if (!tabelaDeSimbolos.containsKey(a)) 
				throw new MyException("A vari�vel '" + a + "' n�o foi declarada previamente."); // lan�a exce��o
			else
				if (getTipoTabela(a).substring(0, 3).compareTo("vet") != 0  && isVetor == true)
					throw new MyException(" A vari�vel '" + a + "' n�o � um vetor."); // lan�a exce��o
		
		isVetor = testeVetor(b);
		// Testa se B � um vetor (x[i]) ou uma vari�vel (x)
		if (isVetor) {
			String[] aux1 = b.split(" "); // dividir o vetor em id (aux1[0]) e �ndice (aux1[1])
			b = aux1[0]; // pega s� o id
		} else
			b = removeAllBlankSpaces(b);

		// Se a exp for uma vari�vel, checar se ela j� foi declarada
		if (b_class.compareTo("class node.AVarExp") == 0)		
			if (!tabelaDeSimbolos.containsKey(b)) 
				throw new MyException("A vari�vel '" + b + "' n�o foi declarada previamente."); // lan�a exce��o
			else 
				if (getTipoTabela(b).substring(0, 3).compareTo("vet") != 0 && isVetor == true)
					throw new MyException(" A vari�vel '" + b + "' n�o � um vetor."); // lan�a exce��o
    }
	
	@Override
    public void outAOpDivExp(AOpDivExp node) {
		String a = node.getLeft().toString();
		String b = node.getRight().toString();
		String a_class = node.getLeft().getClass().toString();
		String b_class = node.getRight().getClass().toString();
		a = removeLastBlankSpace(a);
		b = removeLastBlankSpace(b);
		
		boolean isVetor = testeVetor(a); // Testa se A � um vetor (x[i]) ou uma vari�vel (x)
		
		if (isVetor) { // se for vetor
			String[] aux = a.split(" "); // dividir o vetor em id (aux1[0]) e �ndice (aux1[1])
			a = aux[0]; // pega s� o id
		} else // se n�o for vetor
			a = removeAllBlankSpaces(a);
		
		// Se a exp for uma vari�vel, checar se ela j� foi declarada
		if (a_class.compareTo("class node.AVarExp") == 0)		
			if (!tabelaDeSimbolos.containsKey(a)) 
				throw new MyException("A vari�vel '" + a + "' n�o foi declarada previamente."); // lan�a exce��o
			else
				if (getTipoTabela(a).substring(0, 3).compareTo("vet") != 0  && isVetor == true)
					throw new MyException(" A vari�vel '" + a + "' n�o � um vetor."); // lan�a exce��o
		
		isVetor = testeVetor(b);
		// Testa se B � um vetor (x[i]) ou uma vari�vel (x)
		if (isVetor) {
			String[] aux1 = b.split(" "); // dividir o vetor em id (aux1[0]) e �ndice (aux1[1])
			b = aux1[0]; // pega s� o id
		} else
			b = removeAllBlankSpaces(b);

		// Se a exp for uma vari�vel, checar se ela j� foi declarada
		if (b_class.compareTo("class node.AVarExp") == 0)		
			if (!tabelaDeSimbolos.containsKey(b)) 
				throw new MyException("A vari�vel '" + b + "' n�o foi declarada previamente."); // lan�a exce��o
			else 
				if (getTipoTabela(b).substring(0, 3).compareTo("vet") != 0 && isVetor == true)
					throw new MyException(" A vari�vel '" + b + "' n�o � um vetor."); // lan�a exce��o
	}	
}

// FALTA FAZER
/*
 * 	A quest�o do tipo String (n�o existe). O que existe � vetor de caracteres. Pensar em teste relacionados a isso.
 *  Por exemplo: se uma vari�vel � declarada como caractere, n�o pode atribuir diretamente, ou sei l�.
 *  Se ela for um vetor de strings o formato vai ser vetstr[name]
 *  
 *  Tem na documenta��o:
 *  As express�es em se e enquanto devem avaliar um tipo booleano
 *  Em opera��es entre os tipos inteiro e real, os valores inteiros devem ser
convertidos para reais.

	Valida��o de escopo e de exist�ncia de identificadores
	Verifica��o de tipos
 */
