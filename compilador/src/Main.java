import java.io.*;

import lexer.*;
import node.*;
import analysis.*;

public class Main {
	public static void main(String[] args) { 

		if (args.length > 0) { 
			try {
	
				Comment lexer = new Comment (new PushbackReader(new FileReader(args[0])));
				Token t = lexer.next();
				
				// Impressão do enter, tab e espaço
				String enter ="TEnter";
				String tab ="TTab";
				String espaco ="TEspaco";
				
				while (t.getClass().getSimpleName().compareTo("EOF") != 0) { 
					
					//se não for nem espaço, nem tab, nem enter, imprime o nome da classe
					if (espaco.compareTo(t.getClass().getSimpleName()) == 0) {
						System.out.print(" ");
					} else if (enter.compareTo(t.getClass().getSimpleName()) == 0) { 
						System.out.print("\n");
						t = lexer.next(); // para comer o CR e o LR
					} else if (tab.compareTo(t.getClass().getSimpleName()) == 0) {
						System.out.print("\t");
					} else {				
						if (t.getClass().getSimpleName().compareTo("TComentarioBlocoAbre") == 0) 
							System.out.print("TComentarioBloco");
						else 
							System.out.print(t.getClass().getSimpleName());
					}
					t = lexer.next(); // sempre passa para o próximo
				}
				
			} 
			catch (Exception e) {
				System.out.println (e) ; 
			}
		}
	}
}

/* // Se é um comentário de bloco, não printa o conteúdo de dentro
					if (t.getClass().getSimpleName().compareTo("TComentarioBlocoAbre") == 0) {
						System.out.print(t.getClass().getSimpleName());	
						 commentblock = 1;
					}
					
					// Se fecha o comentário de bloco, então pode printar o que tem dentro
					if (t.getClass().getSimpleName().compareTo("TComentarioBlocoFecha") == 0) {
						 commentblock = 0;
					}*/
