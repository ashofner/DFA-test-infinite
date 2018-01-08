/////
//@author - Alyssa Shofner
//java 8
//DFASubstring made to run and test DFA.substring(String input)
//  which creates a DFA output from string input 
//    that accepts string x iff x is substring of input
//
//can use random strings to test using 
//  DFASubstring.randomString(int length, String alphabet), 
//  DFASubstring.randomSubstring(String input), 
//or all Substrings using
//  DFASubstring.allSubstrins(String input)

import java.io.IOException;
//for testing
import java.util.Random;
import java.lang.StringBuilder;
import java.util.LinkedList;
public class DFASubstring{

	public static void main(String[] args){
		for(String input : args)
		{
			String alphabet = "";
			for(int i=0; i<input.length();i++){
				char currChar = input.charAt(i);
				if(alphabet.indexOf(currChar)==-1){
					alphabet += currChar;	
				}
			}
      final int numOfTests = 5; //may want to vary based on input length			
      
//			System.out.println(alphabet);
			DFA outputDFA = DFA.substring(input, alphabet);
  		System.out.println(input);
			System.out.println(outputDFA);

      //testing positives
      System.out.println("testing positives...");
      /*for(int i = 0; i<numOfTests; i++){
        String randomSub = randomSubstring(input);
        System.out.println(randomSub + ": " + outputDFA.accepts(randomSub));
      }*/
      LinkedList<String> allSubOfInput = allSubstrings(input);
      for(String test : allSubOfInput){
        System.out.println(test + ": " + outputDFA.accepts(test));
      }

      //testing random
      System.out.println("testing random");
      for(int i = 0; i<numOfTests; i++){
        String random = randomString(((alphabet.length())), alphabet);
        System.out.println(random + ": " + outputDFA.accepts(random));
      }

		}
	}
  public static LinkedList<String> allSubstrings(String input){
    LinkedList<String> toReturn = new LinkedList<String>();
    toReturn.add(input);
    int length = input.length();
    for(int i = 1; i < length; i++){
      toReturn.add(input.substring(0, length-i));
    }
    for(int i = 0; i<length; i++){
      String toAdd = input.substring(i, length);
      if(!toReturn.contains(toAdd)){
        toReturn.add(toAdd);
      }
    }
    return toReturn;
  }
  public static String randomSubstring(String input){
    Random r = new Random();
    int endIndex = r.nextInt(input.length()); //r exclusive of input.length
    if(0== endIndex){
      return "";
    } else if(1== endIndex){
      return ""+input.charAt(0);
    }else{
      int beginIndex = r.nextInt(endIndex-1);
      return input.substring(beginIndex, endIndex);
    }
  }

  public static String randomString(String alphabet){
    Random r = new Random();
    return randomString(r.nextInt(alphabet.length()), alphabet);
  }
  public static String randomString(int length, String alphabet){
    Random r = new Random(); 
    StringBuilder sb = new StringBuilder(length);
    for(int i = 0; i<length; i++){
      int index = r.nextInt(alphabet.length());
      sb.append(alphabet.charAt(index));
    }
    return sb.toString();
  }
}

