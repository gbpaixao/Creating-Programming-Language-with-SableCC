/* O intuito desse código é criar uma extensão da classe Lexer, e com isso possuir todos os métodos desta.
 * Com isso, poderemos alterar o método Filter, que é utilizado para os comentários. 
 * Para criar o balanceamento, devemos criar uma pilha que incrementa quando acha um abrecomentario, e decrementa
 * quando encontra um fechacomentario. Se ao terminar o arquivo, a pilha não estiver vazia, então deve-se produzir um
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
	private StringBuffer text; // buffer que acumula tudo que está dentro de um comentário
	private int count; // contador que representa uma pilha
	private int line;
	private int pos;
	
	protected void filter() {
		
		line = token.getLine()/2 + 1;
		pos = token.getPos();
		
		// Se começar com ComentarioFecha, dá erro direto
		if (state.equals(State.NORMAL) && token.getText().compareTo("*/") == 0) {
			System.out.print("Erro de comentário na posição: ("+ line +","+ pos +")");
			token = null;
		}
		
		if (state.equals(State.COMENTARIO)) {		

			// Controla se está entrando ou já está dentro do estado comentário
			if(comentario == null) // se é a primeira vez que entra no estado comentário, o objeto "comentario" está nulo
			{ 
				comentario = (TComentarioBlocoAbre) token;
				text = new StringBuffer(comentario.getText()); // acumula '/*' em text
				count = 1; // empilha
				token = null; // continue to scan the input.
			}  
			else // não é a primeira vez que entra no estado comentário
			{ 		    	  
				text.append(token.getText()); // acumula o texto de dentro do comentário		       

				// PILHA
				if(token instanceof TComentarioBlocoAbre)
					count++; // empilha
				else if(token instanceof TComentarioBlocoFecha)
					count--; // desempilha
				
				// Se a pilha não está vazia, e é o final do arquivo
				if(count != 0 && token.getClass().getSimpleName().compareTo("EOF") == 0) {
					System.out.print("Erro de comentário na posição: ("+ (line) +","+ (pos) +")");
				} 
				else {
					// Se a pilha não está vazia, e não é o final do arquivo
					if(count != 0 && token.getClass().getSimpleName().compareTo("EOF") != 0) {		
						token = null; // continua a escanear a entrada
					} 
					else { // Se a pilha está vazia
						token = comentario; // token recebe '/*' - serve para que o nome do token seja TComentarioBloco
						state = State.NORMAL; //volta para o estado normal
						comentario = null; // libera essa referência
					} 
				}
			}
		}
	} 
}
