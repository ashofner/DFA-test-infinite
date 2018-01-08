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
public class DFASimulator{

	public static void main(String[] args) throws IOException{
		Path pIn = Paths.get(args[0]);
		DFA first = DFA.readDFA(pIn);
		
		Path pTest = Paths.get(args[1]);
		try(Scanner scanner = new Scanner(pTest)){
			String line = null;
			while(scanner.hasNextLine()==true){
				line = scanner.nextLine();
				if(first.accepts(line)){
					System.out.println("accept");
				}else{
					System.out.println("reject");
				}
			}
		} catch (IOException x){
			System.err.format("IOException: %s%n", x);
		}

	}
}
