package ie.atu.sw;

//import java.io.BufferedReader;

import java.io.File;
//import java.io.FileInputStream;
//import java.io.InputStreamReader;
import java.util.Scanner;

public class Parse {
	/*
	 * Parse the texts files because the encryption Polyius Square covers only
	 * letters and numbers. This Parse class has two function: parsing one single
	 * file and return a String storing the parsed contents from the input file; and
	 * a function to unparse a specified input file assuming using the parse
	 * function in this very same class to parse the file. Both function reads one
	 * single txt file and outputs a String Dependencies: BufferedReader, File,
	 * FileInputStream, InputStreamReader
	 */
	public String parse(String file) throws Exception { // The input String file should be the absolute path (/**.txt)

		Scanner sc = new Scanner(new File(file));
		String line = "";// Initiate a String variable when looping through each line in the file
		String parsed = "";// Initiate a String variable to add up all the contents looped through and
							// parsed. Not using String Builder here because of capacity

		while (sc.hasNext()) { // Using while loop to loop through all the non null lines:
			// after major grammar signs replacement,
																				// abandon the rest of non letter non
																			// number elements in text
			line += (sc.nextLine() + "BRLINE"); // add a distinguisher of line break, used for unparsing later. If not using
											// the unparse function below, this could be removed to avoid confusion
		}

		sc.close(); // close the buffered reader at the end
		parsed = line.replace(" ", "SpaceX");// Replace all the spaces, stop signs etc with a special word that avoid
											// misleading/mixing with ordinary word "space" but retains the
											// readability after unparsing
		parsed = parsed.replace(",", "Commahere");
		parsed = parsed.replace(".", "fullstop");
		parsed = parsed.replace("?", "questionmark");
		parsed = parsed.replace("!", "escalationmark");
		parsed = parsed.trim().replaceAll("[^a-zA-Z0-9]", "").toUpperCase(); 
		return parsed; // return a String containing all the contents in that txt file
	}

	public String[] unparse(String file) throws Exception { // Reverse engineer the parse function above, enhance the
															// readability.
		// Return an array of String, each element in the String array is a line.

		Scanner sc = new Scanner(new File(file));
		String lines = "";
		while (sc.hasNext()) {
			lines += sc.nextLine(); // After parsing using the parse(String file) function in this
			// class, the line separator is BRLINE
		}
		sc.close();
		String[] theselines = lines.split("BRLINE");
		for (int i = 0; i < theselines.length; i++) {// loop through all the lines collected in the String array
//	System.out.println("Original line:");
//	System.out.println(line);
			String line = theselines[i];
			line = line.replace("SPACEX", " ");
//	System.out.println("after first step:");
//	System.out.println(line);
			line = line.replace("COMMAHERE", ",");
			line = line.replace("FULLSTOP", ".");
			line = line.replace("QUESTIONMARK", "?");
			line = line.replace("ESCALATIONMARK", "!");
			theselines[i] = line;

		}

		return theselines;
	}

	

	public static void main(String[] args) throws Exception {
		// Input some handle/script for unit test
	}
}
