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
				
				// Impress�o do enter, tab e espa�o
				String enter ="TEnter";
				String tab ="TTab";
				String espaco ="TEspaco";
				
				while (t.getClass().getSimpleName().compareTo("EOF") != 0) { 
					
					//se n�o for nem espa�o, nem tab, nem enter, imprime o nome da classe
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
					t = lexer.next(); // sempre passa para o pr�ximo
				}
				
			} 
			catch (Exception e) {
				System.out.println (e) ; 
			}
		}
	}
}

/* // Se � um coment�rio de bloco, n�o printa o conte�do de dentro
					if (t.getClass().getSimpleName().compareTo("TComentarioBlocoAbre") == 0) {
						System.out.print(t.getClass().getSimpleName());	
						 commentblock = 1;
					}
					
					// Se fecha o coment�rio de bloco, ent�o pode printar o que tem dentro
					if (t.getClass().getSimpleName().compareTo("TComentarioBlocoFecha") == 0) {
						 commentblock = 0;
					}*/
