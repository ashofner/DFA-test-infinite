/* Alyssa Shofner
 * java version "1.8.0_102"
 * Java(TM) SE Runtime Environment (build 1.8.0_102-b14)
 * Java HotSpot(TM) 64-Bit Server VM (build 25.102-b14, mixed mode)
 */
import java.util.LinkedList;
import java.nio.file.*;
import java.nio.file.Files;
import java.nio.charset.Charset;
import java.io.IOException;
import java.util.Scanner;
public class DFAInfinite {

	public static void main(String[] args) throws IOException{
		for(String p : args){
			Path pIn = Paths.get(p); //TODO: check args
			DFA first = DFA.readDFA(pIn);
			if(first.languageIsNonEmpty()){
				System.out.print("nonempty ");
			}else{
				System.out.print("empty ");
			}
			if(first.DFSLanguageIsInfinite()){
				System.out.println("infinite");
			}else{
				System.out.println("finite");
			}
			boolean[] accepting = first.getAcceptingStates();
			//for(int i=0; i<accepting.length; i++){
			//	if(accepting[i]){
			//		System.out.print(i + " ");
			//	}
			//}
			//System.out.println();
		}
	}

	public static boolean test(DFA currDFA){//TODO: make asserts.. find accepting string
		System.out.println("epsi: " + currDFA.accepts(""));
		System.out.println("111:  " + currDFA.accepts("111"));
		System.out.println("00:   " + currDFA.accepts("00"));
		System.out.println("000:  " + currDFA.accepts("000"));
		System.out.println("1111: " + currDFA.accepts("1111"));
		System.out.println("00011:" + currDFA.accepts("00011"));
		System.out.println("100:  " + currDFA.accepts("100"));
		System.out.println("1000: " + currDFA.accepts("1000"));
		return true;
	}

	private static DFA readDFA(Path path) throws IOException{
		Charset charset = Charset.forName("US-ASCII"); //from https://docs.oracle.com/javase/tutorial/essential/io/file.html#textfiles
		try (Scanner scanner = new Scanner(path)){
			int numberOfStates = 0;
			while(scanner.hasNextInt()==false){ //TODO:try skip() or look back
				scanner.next();
			}
			numberOfStates = scanner.nextInt();
			boolean[] acceptingStates = new boolean[numberOfStates];
			String alphabet = "";
			String line = scanner.nextLine();
			for (int i = 0; i < 2; i++){
				if ((line = scanner.nextLine())!= null){
					int semiColon = line.indexOf(':');
					switch (i){
						case 0: String sub = line.substring(semiColon+2);
										String[] charStates = sub.split(" ");
										for(String charState:charStates){
											int aState = new Integer(charState);
											acceptingStates[aState] = true;
										}
										break;
						case 1: alphabet = line.substring(semiColon+2);
										break;
					}
				}
			}
			int[][] deltaFunction = new int[numberOfStates][alphabet.length()];
			for(int i = 0; i<numberOfStates; i++){
				for(int j = 0; j<alphabet.length(); j++){
					while(scanner.hasNextInt()==false){scanner.next();}
					deltaFunction[i][j] = scanner.nextInt();
				}
			}
			scanner.close();
			return new DFA(acceptingStates, alphabet, deltaFunction);
		
		} catch (IOException x){
			System.err.format("IOException: %s%n", x);
		}
	return null;	
	}
}
