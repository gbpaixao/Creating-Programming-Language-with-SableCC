/* O intuito desse c�digo � criar uma extens�o da classe Lexer, e com isso possuir todos os m�todos desta.
 * Com isso, poderemos alterar o m�todo Filter, que � utilizado para os coment�rios. 
 * Para criar o balanceamento, devemos criar uma pilha que incrementa quando acha um abrecomentario, e decrementa
 * quando encontra um fechacomentario. Se ao terminar o arquivo, a pilha n�o estiver vazia, ent�o deve-se produzir um
 * erro.
*/

import node.*;
import java.io.PushbackReader;
import lexer.Lexer;

public class Comment extends Lexer {
	
	// Construtor automaticamente gerado
	public Comment(PushbackReader pushbackReader) {
		super(pushbackReader);
	}
	
	private TComentarioBlocoAbre comentario;
	private StringBuffer text; // buffer que acumula tudo que est� dentro de um coment�rio
	private int count; // contador que representa uma pilha
	private int line;
	private int pos;
	
	protected void filter() {
		
		line = token.getLine()/2 + 1;
		pos = token.getPos();
		
		// Se come�ar com ComentarioFecha, d� erro direto
		if (state.equals(State.NORMAL) && token.getText().compareTo("*/") == 0) {
			System.out.print("Erro de coment�rio na posi��o: ("+ line +","+ pos +")");
			token = null;
		}
		
		if (state.equals(State.COMENTARIO)) {		

			// Controla se est� entrando ou j� est� dentro do estado coment�rio
			if(comentario == null) // se � a primeira vez que entra no estado coment�rio, o objeto "comentario" est� nulo
			{ 
				comentario = (TComentarioBlocoAbre) token;
				text = new StringBuffer(comentario.getText()); // acumula '/*' em text
				count = 1; // empilha
				token = null; // continue to scan the input.
			}  
			else // n�o � a primeira vez que entra no estado coment�rio
			{ 		    	  
				text.append(token.getText()); // acumula o texto de dentro do coment�rio		       

				// PILHA
				if(token instanceof TComentarioBlocoAbre)
					count++; // empilha
				else if(token instanceof TComentarioBlocoFecha)
					count--; // desempilha
				
				// Se a pilha n�o est� vazia, e � o final do arquivo
				if(count != 0 && token.getClass().getSimpleName().compareTo("EOF") == 0) {
					System.out.print("Erro de coment�rio na posi��o: ("+ (line) +","+ (pos) +")");
				} 
				else {
					// Se a pilha n�o est� vazia, e n�o � o final do arquivo
					if(count != 0 && token.getClass().getSimpleName().compareTo("EOF") != 0) {		
						token = null; // continua a escanear a entrada
					} 
					else { // Se a pilha est� vazia
						token = comentario; // token recebe '/*' - serve para que o nome do token seja TComentarioBloco
						state = State.NORMAL; //volta para o estado normal
						comentario = null; // libera essa refer�ncia
					} 
				}
			}
		}
	} 
}
