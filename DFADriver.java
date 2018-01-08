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
public class DFADriver {

	public static void main(String[] args) throws IOException{
		Path pIn = Paths.get(args[0]); //TODO: check args
		DFA first = readDFA(pIn);

		Path pOut = Paths.get(args[1]);
		LinkedList<String> linesOut = new LinkedList<String>();
			linesOut.add(first.toString());
		Files.write(pOut, linesOut, Charset.forName("UTF-8"));//https://stackoverflow.com/questions/2885173/how-do-i-create-a-file-and-write-to-it-in-java
		System.out.println("Testing first");
		test(first);

		//getting Comp of pIn
		DFA comp = first.complement();
		Path pComp = Paths.get(args[2]);
		LinkedList<String> linesComp = new LinkedList<String>();
		linesComp.add(comp.toString());
		Files.write(pComp, linesComp, Charset.forName("UTF-8"));
	
		System.out.println("Testing first's complement");
		test(comp);

		//if flag
		Path p2In = Paths.get(args[3]);
		DFA second = readDFA(p2In);
		System.out.println("Testing second");
		test(second);

		DFA inter = first.intersection(second);
		Path pInter = Paths.get(args[4]);
		LinkedList<String> linesInter = new LinkedList<String>();
		linesInter.add(inter.toString());
		Files.write(pInter, linesInter, Charset.forName("UTF-8"));
		System.out.println("Testing intersection");
		test(inter);
	}

	public static boolean test(DFA currDFA){//TODO: make asserts.. find accepting string
		System.out.println("epsi: " + currDFA.doesAccept(""));
		System.out.println("111:  " + currDFA.doesAccept("111"));
		System.out.println("00:   " + currDFA.doesAccept("00"));
		System.out.println("000:  " + currDFA.doesAccept("000"));
		System.out.println("1111: " + currDFA.doesAccept("1111"));
		System.out.println("00011:" + currDFA.doesAccept("00011"));
		System.out.println("100:  " + currDFA.doesAccept("100"));
		System.out.println("1000: " + currDFA.doesAccept("1000"));
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
