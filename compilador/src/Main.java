import java.io.*;

import lexer.*;
import node.*;
import analysis.*;
import parser.*;

public class Main {
	public static void main(String[] args) { 

		if (args.length > 0) { 
			try {
	
				Comment lexer = new Comment (new PushbackReader(new FileReader(args[0]),1024));
				Parser p = new Parser (lexer);
				Start tree = p.parse();
				
				// Impressão da Árvore Sintática em string
				//System.out.println(tree.toString());
				
				// Impressão da Árvore Sintática em texto, mas formatado
				tree.apply(new ASTPrinter());
				
				// Impressão da Árvore Sintática com o JFrame
				tree.apply(new ASTDisplay()); 
				
				System.out.println("\nAnálise sintática realizada!");
				
			} 
			catch (Exception e) {
				System.out.println (e) ; 
			}
		}
	}
}