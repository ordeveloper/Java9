
package org.acme.wordcount;

public class WordCount{
	
	
	public static void main (String[] args) {
		int i = 0;
		if (args != null && args.length > 0) {
			for (String arg : args) {
				System.out.println("Argomento " +  i + "  vale  " + arg.length());
				i++;
			}
			
		}
	}
}