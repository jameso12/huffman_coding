package prj02;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import HashTable.*;
import List.*;
import SortedList.*;
import Tree.*;



/**
 * The Huffman Encoding Algorithm
 *
 * This is a data compression algorithm designed by David A. Huffman and published in 1952
 *
 * What it does is it takes a string and by constructing a special binary tree with the frequencies of each character.
 * This tree generates special prefix codes that make the size of each string encoded a lot smaller, thus saving space.
 *
 * @author Fernando J. Bermudez Medina (Template)
 * @author A. ElSaid (Review)
 * @author James O Rodriguez Feliciano (Implementation)
 * @version 2.0
 * @since 10/16/2021
 */
public class HuffmanCoding {
	
	public static void main(String[] args) {
		HuffmanEncodedResult();
	}

	/* This method just runs all the main methods developed or the algorithm */
	private static void HuffmanEncodedResult() {
		String data = load_data("input1.txt"); //You can create other test input files and add them to the inputData Folder

		/*If input string is not empty we can encode the text using our algorithm*/
		if(!data.isEmpty()) {
			Map<String, Integer> fD = compute_fd(data);
			BTNode<Integer,String> huffmanRoot = huffman_tree(fD);
			Map<String,String> encodedHuffman = huffman_code(huffmanRoot);
			String output = encode(encodedHuffman, data);
			process_results(fD, encodedHuffman,data,output);
		} else {
			System.out.println("Input Data Is Empty! Try Again with a File that has data inside!");
		}

	}

	/**
	 * Receives a file named in parameter inputFile (including its path),
	 * and returns a single string with the contents.
	 *
	 * @param inputFile name of the file to be processed in the path inputData/
	 * @return String with the information to be processed
	 */
	public static String load_data(String inputFile) {
		BufferedReader in = null;
		String line = "";

		try {
			/*We create a new reader that accepts UTF-8 encoding and extract the input string from the file, and we return it*/
			in = new BufferedReader(new InputStreamReader(new FileInputStream("inputData/" + inputFile), "UTF-8"));

			/*If input file is empty just return an empty string, if not just extract the data*/
			String extracted = in.readLine();
			if(extracted != null)
				line = extracted;

		} catch (FileNotFoundException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

		}
		return line;
	}

	/**
	 * Processes inputString character by character and returns the
	 * corresponding frequency of occurrence of each character.
	 *
	 * @param inputString single line of string to determine symbol frequency
	 * @return Map of the symbols and their corresponding frequencies
	 */
	public static Map<String, Integer> compute_fd(String inputString) {
		Map<String, Integer> map = new HashTableSC<>(new SimpleHashFunction<String>());
		
		for(int i = 0; i < inputString.length(); i++)
		{
			String c = String.valueOf(inputString.charAt(i));
			//If value is in the map, then add one to its frequency.
			if(map.containsKey(c))
			{
				map.put(c, map.get(c)+1);

			}
			else // value not in the map, thus this is its first appearance
			{
				map.put(c, 1);
			}
		}
		return map; // map of the frequencies is returned
	}

	/**
	 * Constructs a huffman tree from a frequency destribution map.
	 *
	 * @param fD frequency distribution map.
	 * @return Binary tree node of the constructed huffman tree.
	 */
	public static BTNode<Integer, String> huffman_tree(Map<String, Integer> fD) {

		/* given map, construct a sorted linked list
		 * initialize SLL, get keys of fD for easy iteration
		 * add keys and values to the sorted linked list
		 * */
		SortedLinkedList<BTNode<Integer,String>> sll = new SortedLinkedList<>();
		for(String e: fD.getKeys()) 
		{
			sll.add(new BTNode<Integer,String>(fD.get(e),e));
		}
		/* construct huffman tree
		 * Taking advantage of using a sorted list, you will get the 2 items with the
		 * lowest frequency and "combine" them into a node.
		 * Those 2 node would be remove from the list, the new node would be added.
		 * The new node would be the concatenation of those 3 symbols and will have a 
		 * frequency of the addition of the 2 nodes.*/
		BTNode<Integer,String> rootNode = null;
		for(int i = 1; sll.size()!=1; i++) 
		{
			BTNode<Integer,String> n = new BTNode<>(); // make new node
			BTNode<Integer,String> l = sll.removeIndex(0); // remove lowest element
			BTNode<Integer,String> r =sll.removeIndex(0); // remove second lowest 
			/* tie-breaker in case that two symbols have the frequency
			 * smaller symbol must be the left child
			 * */
//			if(l.getKey() == r.getKey())
//			{
//				if(l.getValue().compareTo(r.getValue())>0) // tie-breaker condition
//				{
//					// swap so that smallest is on the left
//					BTNode<Integer,String> tempNode = l;
//					l = r;
//					r = tempNode;
//				}
//			}
			n.setLeftChild(l); // set lowest to left child
			n.setRightChild(r); // set second lowest to right child
			n.setKey(l.getKey()+r.getKey()); // add their frequencies and assign them to n
			n.setValue(l.getValue()+r.getValue()); // add their symbols and assign them to n
			sll.add(n); // add the combination to sll
			
		}
		rootNode = sll.removeIndex(0);
		return rootNode;
	}

	/**
	 * Creates a symbol to huffman code map.
	 *
	 * @param huffmanRoot the root of the created huffman tree.
	 * @return Map of each symbol mapped to their huffman code.
	 */
	public static Map<String, String> huffman_code(BTNode<Integer,String> huffmanRoot) {
		Map<String, String> huffmanCode = new HashTableSC<>(new SimpleHashFunction<String>());
		return huffman_codeAux("",huffmanRoot, huffmanCode);
	}

	/**
	 * Encodes the string using each symbol's corresponding huffman code
	 *
	 * @param encodingMap map that contains the symbols mapped to their huffman code
	 * @param inputString the string to be encoded
	 * @return Encoded String
	 */
	public static String encode(Map<String, String> encodingMap, String inputString) {
		String encodedString = "";
		for (int i = 0; i<inputString.length(); i++) 
		{
			String c = String.valueOf(inputString.charAt(i));
			encodedString += encodingMap.get(c);
		}

		return encodedString;
	}

	/**
	 * Receives the frequency distribution map, the Huffman Prefix Code HashTable, the input string,
	 * and the output string, and prints the results to the screen (per specifications).
	 *
	 * Output Includes: symbol, frequency and code.
	 * Also includes how many bits has the original and encoded string, plus how much space was saved using this encoding algorithm
	 *
	 * @param fD Frequency Distribution of all the characters in input string
	 * @param encodedHuffman Prefix Code Map
	 * @param inputData text string from the input file
	 * @param output processed encoded string
	 */
	public static void process_results(Map<String, Integer> fD, Map<String, String> encodedHuffman, String inputData, String output) {
		/*To get the bytes of the input string, we just get the bytes of the original string with string.getBytes().length*/
		int inputBytes = inputData.getBytes().length;

		/**
		 * For the bytes of the encoded one, it's not so easy.
		 *
		 * Here we have to get the bytes the same way we got the bytes for the original one but we divide it by 8,
		 * because 1 byte = 8 bits and our huffman code is in bits (0,1), not bytes.
		 *
		 * This is because we want to calculate how many bytes we saved by counting how many bits we generated with the encoding
		 */
		DecimalFormat d = new DecimalFormat("##.##");
		double outputBytes = Math.ceil((float) output.getBytes().length / 8);

		/**
		 * to calculate how much space we saved we just take the percentage.
		 * the number of encoded bytes divided by the number of original bytes will give us how much space we "chopped off"
		 *
		 * So we have to subtract that "chopped off" percentage to the total (which is 100%)
		 * and that's the difference in space required
		 */
		String savings =  d.format(100 - (( (float) (outputBytes / (float)inputBytes) ) * 100));


		/**
		 * Finally we just output our results to the console
		 * with a more visual pleasing version of both our Hash Tables in decreasing order by frequency.
		 *
		 * Notice that when the output is shown, the characters with the highest frequency have the lowest amount of bits.
		 *
		 * This means the encoding worked and we saved space!
		 */
		System.out.println("Symbol\t" + "Frequency   " + "Code");
		System.out.println("------\t" + "---------   " + "----");

		SortedList<BTNode<Integer,String>> sortedList = new SortedLinkedList<BTNode<Integer,String>>();

		/* To print the table in decreasing order by frequency, we do the same thing we did when we built the tree
		 * We add each key with it's frequency in a node into a SortedList, this way we get the frequencies in ascending order*/
		for (String key : fD.getKeys()) {
			BTNode<Integer,String> node = new BTNode<Integer,String>(fD.get(key),key);
			sortedList.add(node);
		}

		/**
		 * Since we have the frequencies in ascending order,
		 * we just traverse the list backwards and start printing the nodes key (character) and value (frequency)
		 * and find the same key in our prefix code "Lookup Table" we made earlier on in huffman_code().
		 *
		 * That way we get the table in decreasing order by frequency
		 * */
		for (int i = sortedList.size() - 1; i >= 0; i--) {
			BTNode<Integer,String> node = sortedList.get(i);
			System.out.println(node.getValue() + "\t" + node.getKey() + "\t    " + encodedHuffman.get(node.getValue()));
		}

		System.out.println("\nOriginal String: \n" + inputData);
		System.out.println("Encoded String: \n" + output);
		System.out.println("Decoded String: \n" + decodeHuff(output, encodedHuffman) + "\n");
		System.out.println("The original string requires " + inputBytes + " bytes.");
		System.out.println("The encoded string requires " + (int) outputBytes + " bytes.");
		System.out.println("Difference in space requiered is " + savings + "%.");
	}


	/*************************************************************************************
	 ** ADD ANY AUXILIARY METHOD YOU WISH TO IMPLEMENT TO FACILITATE YOUR SOLUTION HERE **
	 *************************************************************************************/
	/**
	 * auxiliary method that makes the huffman code
	 * uses string string parameter to make the huffman code, node parameter to traverse the tree
	 * and root of huffman map to have the huffman code mapping inside the functions scope
	 * @param HCode string which will store the huffman code 
	 * @param n current node used to traverse the tree
	 * @param m map that will conatian symbols mapped to their huffman code
	 * */
	private static Map<String,String> huffman_codeAux(String HCode,BTNode<Integer,String> n, Map<String,String> m)
	{
		if(n.getLeftChild()==null && n.getRightChild()==null)
		{
			m.put(n.getValue() , HCode); // add code entry to map k = string v = huffman code
		}
		else 
		{
			huffman_codeAux(HCode+"0",n.getLeftChild(),m);
			huffman_codeAux(HCode+"1",n.getRightChild(),m);
		}
		return m;
	}
	
	
	/**
	 * Auxiliary Method that decodes the generated string by the Huffman Coding Algorithm
	 *
	 * Used for output Purposes
	 *
	 * @param output - Encoded String
	 * @param lookupTable
	 * @return The decoded String, this should be the original input string parsed from the input file
	 */
	public static String decodeHuff(String output, Map<String, String> lookupTable) {
		String result = "";
		int start = 0;
		List<String>  prefixCodes = lookupTable.getValues();
		List<String> symbols = lookupTable.getKeys();

		/*looping through output until a prefix code is found on map and
		 * adding the symbol that the code that represents it to result */
		for(int i = 0; i <= output.length();i++){

			String searched = output.substring(start, i);

			int index = prefixCodes.firstIndex(searched);

			if(index >= 0) { //Found it
				result= result + symbols.get(index);
				start = i;
			}
		}
		return result;
	}


}
