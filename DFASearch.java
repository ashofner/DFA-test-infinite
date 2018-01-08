/////
//@author - Alyssa Shofner
//java 8
//DFASubstring made to run and test DFA.substring(String input)
//  which creates a DFA output from string input 
//    that accepts string x iff input is substring of x
//

import java.io.IOException;
public class DFASearch{

	public static void main(String[] args){
		for(String input : args)
		{
			String alphabet = "abcdefghijklmnopqrstuvwxyz";
/*			for(int i=0; i<input.length();i++){
				char currChar = input.charAt(i);
				if(alphabet.indexOf(currChar)==-1){
					alphabet += currChar;	
				}
			}*/
      
//			System.out.println(alphabet);
			DFA outputDFA = DFA.search(input, alphabet);
//  		System.out.println(input);
			System.out.println(outputDFA);

      //testing positives

      //testing random
		}
	}
}

