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
public class DFACompInter {

	public static void main(String[] args) throws IOException{
		
		Path pIn = Paths.get(args[0]); //TODO: check args
		DFA first = DFA.readDFA(pIn);
		if(args.length == 1){
		//getting Comp of pIn
			DFA comp = first.complement();
			System.out.println(comp.toString());
		}else if(args.length == 2){
		//if flag
		Path p2In = Paths.get(args[1]);
		DFA second = DFA.readDFA(p2In);

		DFA inter = first.intersection(second);
		System.out.println(inter.toString());
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
}
