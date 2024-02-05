package ie.atu.sw;

import java.util.Arrays;//import static java.lang.System.out;
public class ADFGVX {
	// This is the Cipher class. An ADFGVX object will have following functions:
	// encrypt, decrypt
	// Key and text are inputs.
	// Polybius Square is defined at the bottom for user to customize, there is no
	// external handle for user to modify the Polybius Square outside this script

	public String encrypt(String plainText, String key) throws Exception {
		return doCipher(plainText, true, key);

	}

	public String decrypt(String ciphertext, String key) throws Exception {
		return doCipher(ciphertext, false, key);

	}

	public String doCipher(String plainText, boolean encrypt, String key) {

		char[] cipherkey = key.toCharArray();
		// now get the key characters array sorted and get an integer array storing the
		// original indexes
		int keylength = cipherkey.length;
		int[] keyindex = new int[keylength]; // new array created for storing the transition array
		char[] sortedkey = new char[keylength]; // The mathematical representation of the process: OriginalKey ^
												// TransitionArray = SortedKey
		for (int i = 0; i < keylength; i++) { // If the transition array is figured out and apply it to the intermediate
												// cipher, the final encrypted cipher could ge got(the column transition
												// is done)
			keyindex[i] = i; // Create an array to record the original index of each element in key array
			sortedkey[i] = cipherkey[i]; // Copy the key array
		}
		Arrays.sort(sortedkey); // Sort the copied key array to get a sorted key array

		for (int i = 0; i < keylength; i++) { // loop through the original key array and the sorted key array, find what
												// is the order of original index of each element in sorted array
			for (int j = 0; j < keylength; j++) {
				if (cipherkey[j] == sortedkey[i]) {
					keyindex[i] = j;
				}
			}

		}
		//

		/*
		 * Below is pre-processing the text for encryption, dealing with what if the
		 * cipher text is not the length that can fill the transition matrix (aligning
		 * to the key's length). After the process, the encrypted text should be just
		 * the correct length filling the transition matrix, so if user is using this
		 * program to decrypt an encrypted text that is produced by this very same
		 * program, no need to worry about length.
		 */
		int taillengthCipher = (2 * (plainText.length())) % keylength;
		/*
		 * the plain text's length times two would be the cipher text's length because
		 * one letter in plain text would be represented by two letters in cipher, and
		 * we are aligning the cipher text with the key to make a box instead of the
		 * plain text
		 */
		int spacesneededtofill = 0;
		if (taillengthCipher == 0) { // If the cipher text will be just fit in the matrix (no modular dividing by the
										// length of key, then we don't need to have any "tail" needed filling
			spacesneededtofill = 0;
		} else {
			spacesneededtofill = (keylength - taillengthCipher) / 2; // Otherwise, the spaces in plain text needed to be
																		// filled would be half of the difference
																		// between key length and tail length, because
																		// one charactor in plain text translates to two
																		// characters in cipher text.
		}

		String newplaintext = plainText + " ".repeat(spacesneededtofill); // Put empty space as place holder for the
																			// tail that originally could not fill a
																			// full cipher matrix row

		String ciphertext = "";
		char[][] matrix = encrypt ? new char[2 * newplaintext.length() / keylength +1][keylength] // The matrix having
																								// number of columns
																								// same as key length,
																								// the row number should
																								// be text length
																								// divided by key
																								// length. For
																								// encryption, it is
																								// cipher text going to
																								// be in the matrix
																								// which will be twice
																								// length as plain text
				: new char[plainText.length() / keylength][keylength];
		
		//out.println("The number of rows:"+String.valueOf(matrix.length));
		//out.println("The number of columns:"+String.valueOf(matrix[0].length));
		if (encrypt == true) { // Now start the encryption, note that even though using the same polyius
								// square, the encryption/decryption process is quite different so it is
								// separated here for code readability and unit test & maintenance simplicity
			for (int i = 0; i < newplaintext.length(); i++) { // Loop through the whole plain text to encrypt each
																// character
				char[] temp = getEncryptedCharacter(newplaintext.charAt(i)); // each plain character will be encrypted
																				// to a pair of characters so an array
																				// of two char would be returned by the
																				// function

				int row = (int) ((2 * i)/ keylength); 
				/*
										 * find the row the encrypted char should be on, because one plain text char
										 * would convert to two cipher chars so the total cipher char number up to i-th
										 * char of plain text would be 2*i
										 */
				int col = (2 * i) % (keylength); // this shall be the column number before the column switch
				int coltran = keyindex[col]; // perform the switch of columns in one loop to save computation time
	
				
				matrix[row][coltran] = temp[0];// put the first char of the encrypted pair into matrix, for the next
												// char, also perform column transition, then put it into the correct
												// position of matrix

				if (col + 1 < keylength) { // If the next column of this intermediate matrix (before key sort
											// transition) is not out of bound, then directly put it on the intermediate
											// matrix and perform the column transition by referencing the keyindex
											// array
					int nextcol = keyindex[col + 1];
					matrix[row][nextcol] = temp[1];
				} /*
					 * place the second character encrypted to the next place if there is still
					 * space, otherwise go to next row
					 */

				else { // however, if there is no space, place it on next row

					int nextcol = keyindex[0];

					matrix[row + 1][nextcol] = temp[1];
				}
			}
			// After above loops, the matrix of encrypted cipher text is produced. Then just
			// need to loop through the matrix and save it in one String
			for (int m = 0; m < matrix.length; m++) {

				for (int n = 0; n < keylength; n++) {
					ciphertext += matrix[m][n];

				}
			}
		}

		else { // when decrypting, first transit the columns to get a matrix then decrypt

			for (int j = 0; j < plainText.length(); j++) {

				int row = (int) (j
						/ keylength); /*
										 * First, think of a matrix before column transition, which row should the
										 * encrypted char be placed with each row having same length as key length, the
										 * row number index can be conveniently represented as j/keylength
										 */
				int col = j % (keylength); // this shall be the column number before the column switch
				int coltran = keyindex[col]; // perform the switch of columns in one loop to save computation time
				try {
					matrix[row][coltran] = plainText.charAt(j); // when decrypting, need to first put the encrypted text
																// into the matrix, make the matrix transition, then use
																// a String to string all the characters in matrix one
																// by one, in order, to prepare for decryption
				} catch (Exception e) {
					System.out.println("An error occurred.");
					e.printStackTrace();
				}

			}

			String tempde = ""; // This string is going to be the encrypted cipher text string. This step is not
								// removable unless a substitution alternative methodology is chosen to make
								// sure all the character pairs in correct order when called and referenced as
								// input for decryption
			for (int m = 0; m < matrix.length; m++) {

				for (int n = 0; n < keylength; n++) {
					tempde += matrix[m][n];

				}
			}
			for (int q = 0; q < tempde.length() - 1; q += 2) { // After storing all characters in the string tempde, now
																// loop through every two characters of the cipher text
																// to get decrypted character

				char[] pairs = { tempde.charAt(q), tempde.charAt(q + 1) };

				char decryptedchar = getDecryptedCharacter(pairs);

				ciphertext += decryptedchar;
			}
		}

		return ciphertext;// At the end, be it encryption or decryption, the function doCipher returns a
							// string of processed text.
	}

	private static char[] getEncryptedCharacter(char plainChar) {

		char[] spaces = { ' ', ' ' };// Prepare an empty pair of spaces to be default return if the plain text having
										// character not in Polyius Square of encryption
		for (int row = 0; row < cypherbox.length; row++) { // Loop through Polyius Square rows

			for (int col = 0; col < cypherbox[row].length; col++) { // Loop through Polyius Square columns to find the
																	// matching char

				if (cypherbox[row][col] == plainChar) {// if the char is found in the Polyius Square, return below,
														// otherwise return empty spaces
					char[] c = { codestrips[row], codestrips[col] }; // Find the row and column representation in ADFGVX
																		// encoding

					return c;

				}

			}
		}

		return spaces;
	}

	private static char getDecryptedCharacter(char[] cipherChar) {

		for (int row = 0; row < codestrips.length; row++) { // Loop through the first encoding letter reference to
															// locate which row of Polyius Square is the encrypted char
															// on

			if (codestrips[row] == cipherChar[0]) {

				for (int col = 0; col < codestrips.length; col++) {// Loop through the second encoding letter reference
																	// to locate which column of Polyius Square is the
																	// encrypted char on

					if (codestrips[col] == cipherChar[1]) { // If the encrypted (original) char is found
						return cypherbox[row][col];	//Return the original representation of char
					}
				}
			}
		}
		return ' ';//If nothing is matched in the Polyius square, return a space char.
	}

	private static final char[] codestrips = { 'A', 'D', 'F', 'G', 'V', 'X' };
	private static final char[][] cypherbox = { { 'P', 'H', '0', 'Q', 'G', '6' }, { '4', 'M', 'E', 'A', '1', 'Y' },
			{ 'L', '2', 'N', 'O', 'F', 'D' }, { 'X', 'K', 'R', '3', 'C', 'V' }, { 'S', '5', 'Z', 'W', '7', 'B' },
			{ 'J', '9', 'U', 'T', 'I', '8' }

	};

}
