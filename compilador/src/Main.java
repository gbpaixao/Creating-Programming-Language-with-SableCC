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
				
				// Impressão da Árvore Sintática em texto, mas formatado
				//tree.apply(new ASTPrinter());
				tree.apply(new AnaliseSemantica()); 
				
				System.out.println("Análise semântica realizada!");
				
			} 
			catch (Exception e) {
				System.out.println (e) ; 
			}
		}
	}
}