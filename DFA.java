//TODO: comment
/*
 * https://en.wikipedia.org/wiki/Deterministic_finite_automaton
 * https://cse.sc.edu/~fenner/csce355/prog-proj/programming-assignment.pdf
 *
 * @author	Alyssa Shofner
 * @version %I%, %G%
 * @since		1.0
 *
 * using java version 1.8.0_102
*/
//for DFAInfinite
import java.util.LinkedHashSet;
import java.util.HashSet;
import java.util.Arrays; //also in DFAsubstring

//for DFAsubstring
import java.util.ArrayList;
import java.util.BitSet;
//and java.util.Arrays 

//for input
import java.util.LinkedList; 
import java.nio.file.*;
import java.nio.file.Files;
import java.nio.charset.Charset;
import java.io.IOException;
import java.util.Scanner;
public class DFA{
	private boolean[] accepting;//true in states count in output is numerical	
	private String alphabet; 
	private int[][] deltaTable; //holds transitory delta function where 
											//1st dimention (row)is the state, number starts from 0
											//2nd dimension (column) is the indexOf the character in the alphabet String
											//contains the state reached from starting at the row state and going through the column character
	private int startState = 0; //state where DFA starts, deemed by assignment to be 0...
	private int currState;  //

	public DFA(boolean[] acceptingStates, String alphabet, int[][] deltaTable){
		this.accepting = acceptingStates;
		this.alphabet = alphabet;
		this.deltaTable = deltaTable;
	}

	public boolean accepts(String inputString){
		if(inputString==""){
			return accepting[startState]; //TODO: check if " " is in every alphabet and is seen as epsilon
		}
		currState = startState;
		for(int i = 0; i<inputString.length(); i++){ //not doing for each as would need to change String to charArray to make the string mutable assumes charAt does not need to convert string to charArray
			char r = inputString.charAt(i);
			int check = alphabet.indexOf(r);
			if(check > -1){
				currState = deltaTable[currState][alphabet.indexOf(r)];
			}else{
				System.out.print(r + " is not in alphabet: ");
				return false;
			}
		}
		return accepting[currState];  //assignment requires if true prints "accepting"
																	//						if false prints "rejecting"
	}

	//TODO: check this is complement
	public DFA complement(){
		boolean[] compAccepting = new boolean[accepting.length];
		int index = 0;
		for(boolean curr : accepting)
		{
			compAccepting[index] = !curr;
			index++;
		}
		return new DFA(compAccepting, alphabet, deltaTable);
	}

	//checks if alphabet follows ASCII order
	public boolean errorCheckAlphabetOrder(){
		//ensures alphabet goes smallest to largest ASCII order
		for(int i = 1; i < alphabet.length(); i++){
			int before = (int) alphabet.charAt(i-1);
			int curr = (int) alphabet.charAt(i); 
		  if (before > curr){
				return false; //note does not check for duplicates
			}
		}	
		return true;
	}
	public String getAlphabet(){
		return alphabet;
	}
	public int getNumberOfStates(){
		return accepting.length;
	}
	public boolean[] getAcceptingStates(){
		return accepting;
	}
	public boolean isAccepting(int state){
		return accepting[state];
	}

	public int[][] getDeltaTable(){
		return deltaTable;
	}

	public int delta(int currState, char a){
		int charColumn = alphabet.indexOf(a);
		if(charColumn > -1){
			return deltaTable[currState][alphabet.indexOf(a)];
		}
		return -1;
	}
//	@Overload
	public int delta(int currState, int a){
		return deltaTable[currState][a];
	}

	public DFA intersection(DFA second){
		if(!alphabet.equals(second.getAlphabet())){
			System.out.println("give same alphabet");
		}
		int thisSize = this.accepting.length;
		int secondSize = second.getNumberOfStates();
		int numberOfStates = thisSize * secondSize;
		boolean[] interAccepting = new boolean[numberOfStates]; 
		int[][] interDT = new int[numberOfStates][alphabet.length()];
		//each i*secondS + j
		for(int i = 0; i<thisSize; i++){
			boolean thisIsAccepting = this.accepting[i];
			for(int j = 0; j<secondSize; j++)
			{
				if(thisIsAccepting){
					if(second.isAccepting(j)){
						interAccepting[i*secondSize+j] = true;
					}
				}
				for(int k = 0; k<alphabet.length(); k++)
				{
					int rowState = i*secondSize + j;
					int thisNextState = this.delta(i, k);
					int secondNextState = second.delta(j, k);
					interDT[rowState][k] = thisNextState*secondSize + secondNextState; 
				}
			}
		}
		return new DFA(interAccepting,alphabet, interDT);

	}

	//returns String of properties example: 
	//Number of states: 2
	//Accepting states: 1   
	//Alphabet: 01
	//0 1
	//1 0
	public String toString(){
		//grabbing End of line character and number of states
		final String EOL = System.getProperty("line.separator");//TODO: see if works on Linux  
		String returnString = "Number of states: " + accepting.length + EOL;
		
		//adding accepting states
		returnString += "Accepting states: ";
		int index = 0;
		for(boolean isAccepting : accepting){
			if(isAccepting){
				returnString += index + " ";
			}
			index++;
		}
		returnString += EOL;
		returnString += "Alphabet: "+ alphabet + EOL;

		//Delta Table
		int lastColumnIndex = deltaTable[0].length-1;
		for(int i = 0; i<deltaTable.length; i++){
			for(int j = 0; j<lastColumnIndex; j++){
				returnString += deltaTable[i][j] + " ";
			}
			returnString +=deltaTable[i][lastColumnIndex] + EOL;
		}
		return returnString;
	}

	//idea for empty ... make sure final is not state>0
	//idea for cycle reachable by start...
	//			put all start reachable states in set
	//			if start state is found in row of reachable state, infinite
	//
	//			...is reverse i.e find start state in table then see if row n.. Think longer for comp.. double check w paper

	//does not handle looping in recejecting states well... fixable through recursion?
	//

	public boolean DFSInfinite(Integer state, HashSet<Integer> reachable, boolean[] checked){
		if (checked[state]){
			return false;
		}
		for(int i = 0; i<alphabet.length(); i++){
			HashSet<Integer> temp = new HashSet<Integer>(reachable);
		//	System.out.println("i" + i);
		//	System.out.print("state.. "+state);
			Integer childState = deltaTable[state.intValue()][i];
		//	System.out.println("childState.." +childState);
		//	System.out.println(reachable);
			if(reachable.contains(childState)){
				HashSet<Integer> path = new HashSet<Integer>(accepting.length);
				if(DFSaccepting(childState, path)){
					return true;
				}
				else{}//make all of checked dead have to add tag... perhaps go faster
			}else{
				reachable.add(childState);
				if(DFSInfinite(childState, reachable, checked)){
					return true;
				}
				checked[childState] = true;
			}
			reachable = temp;

		}
		return false;
	}
	
	//HashSet<Integer> checked = new HashSet<Integer>(accepting.length);
	private boolean DFSaccepting(int state, HashSet<Integer> checked){
		if(accepting[state]){
			return true;
		}
		for(int i = 0; i<alphabet.length(); i++){
			int next = deltaTable[state][i];
			if (next!=state && !checked.contains(new Integer(next))){
				if(accepting[next]){
						return true;
					}
					checked.add(new Integer(next));
					return DFSaccepting(next, checked);		
			}
		}
		return false;
	}

	//denotes language
	public boolean languageIsNonEmpty(){
		return DFSaccepting(startState, new HashSet<Integer>(accepting.length));	
	}
	public boolean DFSLanguageIsInfinite(){
		boolean[] checked = new boolean[accepting.length];
		Arrays.fill(checked, false);
		return DFSInfinite(startState, new HashSet<Integer>(accepting.length), checked); 
	}

	//////////////////////////////
	//DFAsubstring - creates DFA from String input (alphebetical only) that only accepts substrings of the string
	//assumes default fill is 0- for BitSet need to intitialize default
	//alphabet should be only characters found in the input string
	//				can be more, but NOT fewer
	//
	//METHOD:
	//	states title are BitSet representations of possible positions the testing string could be from the original input string. For example:
	//				input string:  abracabra
	//				state for a:   100101001   //see if
	//				state for ab:  010000010
	//				start state:   111111111
	//				dead state:		 000000000
	//
	//NumberOfStates: at least 2+input.length (for input strings like aaaa)
	//													abcde	 aaaaa
	//													11111  11111
	//													10000	 11110
	//													01000	 11100
	//													00100	 11000
	//													00010	 10000
	//													00001  00000 accepting
	//													00000  00000 dead (not accepting)
	//Start State: an all set BitSet the length of the input string
	//Accepting States: all states but the dead state
	//Delta Transition:
	//			for every state:
	//				for every letter in alphabet:
	//						mark which positions are possible given the current state positions. 
	//						(i.e. If a 1 is found at BitSet index 3 and the current letter matches letter at the input index 4, set index 4 in BitSet for the letter)
	//						transition to corresponding state
	public static DFA substring(String input, String alphabet){
    final boolean DEBUG = false;
		//if input is empty string
		if(input==null || input==""){
			boolean[] acceptingStates = null; //TODO: checkprotocol for no acceptingStates
			int[][] deltaTable = null;
			if(alphabet.length()>0){
				deltaTable = new int[1][alphabet.length()];
				for(int i = 0; i<alphabet.length(); i++){
					deltaTable[0][i] = 0;
				}
			}
			return new DFA(acceptingStates, alphabet, deltaTable); 
		}
		ArrayList<BitSet> stateList = new ArrayList<BitSet>();
		BitSet zeroth = new BitSet(input.length());
		for(int i = 0; i<input.length(); i++){
			zeroth.set(i);
		}
		stateList.add(zeroth);

		final int deadIndex = 1;
		BitSet deadBit = new BitSet(input.length());
		int[] deadTransitions = new int[alphabet.length()];
    Arrays.fill(deadTransitions, deadIndex);
		for(int i = 0; i<input.length(); i++){
			deadBit.clear(i);
		}
		stateList.add(deadBit);
		assert stateList.indexOf(deadBit) == 1;
		int numOfStandardStates = 2;	

		int[] startTransitions = new int[alphabet.length()];
    int numOfExtraCharacters = 0;
		for(int i = 0; i<alphabet.length(); i++){
			if(input.indexOf(alphabet.charAt(i))>-1){ 
				BitSet charBit = new BitSet(input.length());
				char letter = alphabet.charAt(i);
				for(int j = 0; j<input.length(); j++){
					if(letter == input.charAt(j)){
						charBit.set(j);
					}else{
					charBit.clear(j);
					}
				}
				stateList.add(charBit);
				startTransitions[i] = i+numOfStandardStates-numOfExtraCharacters;
			}else{//char is not in input, so its a dead state
				startTransitions[i] = deadIndex;
        numOfExtraCharacters++;
			}
		}
		LinkedList<int[]> growingDeltaTable = new LinkedList<int[]>();
		growingDeltaTable.add(startTransitions);
		growingDeltaTable.add(deadTransitions);

		int statesInDeltaTable = 2;
		
//adding all other states to growingDeltaTable
		while(statesInDeltaTable<stateList.size()){
			BitSet currState = stateList.get(statesInDeltaTable);
      int[] toAdd = new int[alphabet.length()];
				//for each symbol check all positions in the state to list all the positions the string could now be
					for(int inputSymbol = 0; inputSymbol<alphabet.length(); 
							inputSymbol++){
							BitSet toState = new BitSet(alphabet.length());
							for(int i = 0; i<input.length(); i++){
								toState.clear(i);
							}

							if(DEBUG){	System.out.println("input: " + input);}
				//will never consider the last position as anything after is a dead state
				//from java 7 BitSet doc
							for(int currPosition = currState.length(); 
								(currPosition = currState.previousSetBit(currPosition-1))>=0;){
							if(DEBUG){System.out.print("currPosition: " + currPosition);}
							if(currPosition+1<input.length()){	
								char nextSymbol = input.charAt(currPosition+1);
                if(DEBUG){
                  System.out.print("nextSymbol:" + nextSymbol);
                }
                  if(alphabet.indexOf(nextSymbol)==inputSymbol){
                    if(DEBUG){System.out.println(" Iset:" + (currPosition+1));}
                    toState.set(currPosition+1);//assumes to true by default
                  }else if(DEBUG){System.out.println("is not .." +
                      alphabet.charAt(inputSymbol));}
                  }else if(DEBUG){System.out.println("is last position");	
                }
              }
						  if(!stateList.contains(toState)){
								if(DEBUG){System.out.println("is a new State");}
								stateList.add(toState);
							}
              if(DEBUG){
							System.out.println("toState:" + toState + "=>" + stateList.indexOf(toState));}
						 toAdd[inputSymbol] = stateList.indexOf(toState);	
					}
					growingDeltaTable.addLast(toAdd);			
					statesInDeltaTable++;
					if(DEBUG){
            System.out.println("growingDeltaTable");
          
					for(int i = 0; i<growingDeltaTable.size(); i++){
						if(0 == i){
							System.out.print(0 + " :");
						}else if(1 == i){
							System.out.print(1 + " :");
						}
						else if(i> 1 && i<alphabet.length()+numOfStandardStates){
							System.out.print(alphabet.charAt(i-numOfStandardStates)+ " :");
						}else{
							System.out.print(i + " :");
						}
						for(int j = 0; j<growingDeltaTable.get(0).length; j++){
							System.out.print(growingDeltaTable.get(i)[j] + " ");
						}
						System.out.println();
					}
          }
      if(DEBUG){
				System.out.println("numberOfStates:" + stateList.size());
				System.out.println("statesInDelta:" + statesInDeltaTable);			
      }
		}
		//convert growingDeltaTable to int[][] (growtingDeltaTable used so that copies for expansion would not have to be done while max space (2^(input.length)) would not have to be used in beginning TODO: check this logic for best practice
    int numberOfStates = stateList.size();
    int[][] finalDeltaTable = new int[numberOfStates][alphabet.length()];
		for(int i = 0; i<numberOfStates; i++){
      int[] row = growingDeltaTable.pop();
			for(int j=0; j<alphabet.length(); j++){
				finalDeltaTable[i][j] = row[j]; 
			}
		}
		boolean[] acceptingStates = new boolean[numberOfStates];
		Arrays.fill(acceptingStates, true);
		acceptingStates[deadIndex] = false;
		return new DFA(acceptingStates, alphabet, finalDeltaTable);
	}

	public static DFA readDFA(String arg) throws IOException{
		Path p = Paths.get(arg);
		try{
			return readDFA(p);
		}catch(IOException e){
			System.out.println(arg + ": file not found");
			return null;
		}
	}

  ///////////
  //search creates DFA which only accepts string w iff input is a substring of w
  //      alphabet consists of all possible characters w could be made up with
  //
  //METHOD
  //  states are titled by possible strings. Since the whole input string must
  //    be in w for w to be accepted (no substrings of input), all states can be constructed before the transition table based on all substrings starting with index 0 of String input
  //    i.e. starting state input[0]
  //         next state     input.substring(0,2) endIndex exclusive
  //         til state      input.substring(0, input.length())
  //
  //RETURNS: DFA that accepts String w iff input is a substring of w
  //  Number of States: input.length() 
  //  accepting States: input.length() (or input if using titling)
  //  deltaTable:
  //  for each state except acceptingstate:
  //    the character at the next index of input transitions to next state
  //    the characters which are in runningAlphabet may have a transition to a previous state not the startState
  //    all other characters in alphabet transition to start state
  //  for each index in input:
  //    createState titled input.substring(0, index)
  //
  //ASSUMES String.substring endIndex is exclusive
  public static DFA search(String input, String alphabet){
    int[][] deltaTable = new int[input.length()+1][alphabet.length()]; 
    char firstChar = input.charAt(0);
    
    deltaTable[0][alphabet.indexOf(firstChar)] = 1;
    String actualAlphabet = "";
    for(int i=0; i<input.length();i++){
      char currChar = input.charAt(i);
      if(actualAlphabet.indexOf(currChar)==-1){
        actualAlphabet+=currChar;
      }
    }
    HashSet<Character> runningAlphabet = new HashSet<Character>(input.length()-1);//don't need the last
    runningAlphabet.add(firstChar);
    for(int inputIndex = 1; inputIndex<input.length(); inputIndex++){
      //tranistion to next if next letter appears
      char nextChar = input.charAt(inputIndex);
      deltaTable[inputIndex][alphabet.indexOf(nextChar)] = inputIndex+1;

      //check for substring. won't occur if it is new letter
      for(int i=0; i<actualAlphabet.length(); i++){
        char currChar = actualAlphabet.charAt(i); 
        if(currChar!=nextChar){  
          if(runningAlphabet.contains(currChar)){
            for(int j=1; j<=inputIndex; j++){
              String test = input.substring(j, inputIndex)+currChar;//substring exclusive  
              //System.out.println(test);
              if(input.regionMatches(0, test, 0, test.length())){
                deltaTable[inputIndex][alphabet.indexOf(currChar)] =
                  inputIndex-j+1;
                //System.out.println("is accepted");
                j= inputIndex;
              } 
            }
          }else{//no point inchecking substrings
          }
        }
      }
      if(!runningAlphabet.contains(nextChar)){
        runningAlphabet.add(nextChar);
      }
    }
    //finalstate
    Arrays.fill(deltaTable[input.length()], input.length());
    boolean[] acceptingStates = new boolean[input.length()+1];
    acceptingStates[input.length()] = true;
    return new DFA(acceptingStates, alphabet, deltaTable);
  }
	public static DFA readDFA(Path path) throws IOException{
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
	//idea for Homomorphism description:
			//stackoverflow.com/questions/11171716/conversion-object-to-int-error
	//	each of sigma denotes combination of Gamma
	//	if 
} 
