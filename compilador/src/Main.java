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
				
				// Impress�o da �rvore Sint�tica em string
				//System.out.println(tree.toString());
				
				// Impress�o da �rvore Sint�tica em texto, mas formatado
				tree.apply(new ASTPrinter());
				
				// Impress�o da �rvore Sint�tica com o JFrame
				tree.apply(new ASTDisplay()); 
				
				System.out.println("\nAn�lise sint�tica realizada!");
				
			} 
			catch (Exception e) {
				System.out.println (e) ; 
			}
		}
	}
}