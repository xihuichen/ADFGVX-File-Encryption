package ie.atu.sw;

import static java.lang.System.out;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Runner {
	private DirectorySpecification fp = new DirectorySpecification(); /*
																		 * To specify directory, default no directories
																		 * were specified, so this variable can't be
																		 * static. To reference it in main method
																		 * (static), this needs to be wrapped in a
																		 * class, hence here an object of
																		 * DirectorySpecification is created within
																		 * Runner class.
																		 */
	private Scanner s; // Prepared for Inputs by users, specify it at the class level to make recursive
						// entry scanning
	private boolean keepRunning = true; /*
										 * Prepare the control variable for menu, using this control variable provides
										 * coming back to main menu option each time after one action executed by user,
										 * and also provide an option of quitting the program.
										 */
	private String sbLog = "";/*
								 * The string is trying to log what had been the historical commands user
								 * entered, for future development the log automatically saved even program was
								 * interrupted by exceptions
								 */
	private  Key mykey = new Key();/*
											 * Key instance, default key is LUCKY, user can specify new key and the log
											 * will take record what key was used to encrypt
											 */

	private ADFGVX Cypher = new ADFGVX(); // Cypher class instance.

	public Key getkey() {
		return mykey; // This getKey function here is for using the user specified keys in Cypher
						// class, for encryption/decryption
	}

	public static void main(String[] args) throws Exception {
		Runner myobject = new Runner(); // Using an object of the runner class, using this object to connect and
										// reference input directory, output directory and key entered/set by user
		myobject.s = new Scanner(System.in); // Using one scanner to take user's inputs, keep it open to be interactive
												// until user opted to quit.
		while (myobject.keepRunning) { // using keepRunning as a control variable, default set as true so every time
										// after finished one branch-option the program will return back to main menu

			showOptions();// Wrapping the option display as a function, future development: could add the
							// progress meter

			int choice = Integer.parseInt(myobject.s.next());// Take integer entry as choice otherwise will throw type
																// mismatch exception

			myobject.sbLog += ("User entered choice " + choice + " from main menu");// keep adding user's trace of
																					// action into log

			switch (choice) { // Execute user's choice according to the main menu displayed
			case 1 -> myobject.SpecifyInputDir();
			case 2 -> myobject.SpecifyOutputDir();
			case 3 -> myobject.SetKey();
			case 4 -> myobject.Encrypt();
			case 5 -> myobject.Decrypt();
			case 6 -> myobject.Options();
			case 7 -> myobject.setKeepRunning(false);
			default -> out.println("[Error] Invalid Selection");
			}

		}
		myobject.s.close();
		out.println("[INFO] Exiting...Bye!");
	}

	public void SpecifyInputDir() {

		out.println("[INFO] Specify the file directory of input");

		out.println("Enter input directory>");
		String sdir = s.next();

		try {
			fp.setInputdir(sdir);
			out.println("[INFO]  " + sdir + "Set as input directory");
			sbLog += (sdir + "Set as input directory");
		} catch (Exception e) {
			out.println("[INFO] Cannot find directory " + sdir);
			sbLog += ("attemp to set " + sdir + " as input directory failed");
		}

	}

	private void SpecifyOutputDir() {
		out.println("[INFO] Specify the output directory");
		out.println("Enter desired output directory>");
		String sdirout = s.next();

		try {
			fp.setOutputdir(sdirout);
			out.println("[INFO] Output files will be saved under " + sdirout);

			sbLog += (sdirout + "Set as output directory");
		} catch (Exception e) {
			out.println("[INFO] Cannot find directory " + sdirout);
			sbLog += ("attemp to set " + sdirout + " as output directory failed");
		}
	}

	private void SetKey() {
		out.println("[INFO] Set the key for encryption/decryption");

		out.println("Enter desired key>");
		String skey = s.next();

		mykey.setKey(skey);

		sbLog += ("Encryption/Decryption key set as " + skey);
		out.println("[INFO] Key successfully set as: " + skey);

	}

	private void Encrypt() throws Exception {
		String sdir = fp.getInputdir();
		String sdirout = fp.getOutputdir();
		String key = mykey.getKey();
		out.println("[INFO] All txt files under " + sdir + " will be encrypted");
		out.println("Specified output directory is " + sdirout);
		// Remove below commented code block to add an option for user to confirm input
		// & output directories before encryption:
		// out.println(
		// "Confirm both directories please enter Yes, or enter anything else to return
		// to main menu for altering directories");
		// String confirmation = s.next();
		// if (confirmation == "Yes") {
		// Create an object of txt file parser
		File folder = new File(sdir); // Parse all the txt files saved under that folder, below loop is looping
										// through txt files in the folder, encrypt them and then write the encrypted
										// string to txt files
		File[] listoffiles = folder.listFiles();
		for (int i = 0; i < listoffiles.length; i++) {
			Parse p = new Parse(); 
			File file = listoffiles[i];
			if (file.isFile() && file.getName().endsWith(".txt")) {
				String content = file.getAbsolutePath();
				String temp = p.parse(content);
				// System.out.println(temp);
				temp = Cypher.encrypt(temp,key);
				try {
					FileWriter myWriter = new FileWriter(sdirout + file.getName()); // Encrypted files will be outputted
																					// under the pre-defined output
																					// directory using the same file
																					// name. Hence, if input and output
																					// directory are the same user had
																					// no option to keep both.
					myWriter.write(temp);
					myWriter.close();

				} catch (IOException e) {
					System.out.println("An error occurred.");
					e.printStackTrace();
				}
			}

		}

		System.out.println("Encrypted txt files saved under" + sdirout);
		sbLog += ("Encrypted " + listoffiles.length + " files from " + sdir + " and saved to " + sdirout + "with key "
				+ key);
		// } else {
		out.println("Returning to main menu...");

		// }

	}

	private void Decrypt() throws Exception {
		String sdir = fp.getInputdir();
		String sdirout = fp.getOutputdir();
		String key=mykey.getKey();
		out.println("[INFO] All txt files under " + sdir + " will be decrypted");
		out.println("Specified output directory is " + sdirout);

		// out.println(
		// "Confirm both directories please enter Yes, or enter anything else to return
		// to main menu for altering directories");
		// String confirmation = s.next();
		// if (confirmation == "Yes") {
		// Parse p = new Parse();
		File folder = new File(sdir);
		File[] listoffiles = folder.listFiles();
		for (int i = 0; i < listoffiles.length; i++) {
			File file = listoffiles[i];
			if (file.isFile() && file.getName().endsWith(".txt")) {
				String content = file.getAbsolutePath();
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(content))));
				String line = "";
				String temp = "";

				while ((line = br.readLine()) != null) {
					temp += line;
				}

				br.close();

				temp = Cypher.decrypt(temp,key);
				try {
					FileWriter myWriter = new FileWriter(sdirout + file.getName());
					myWriter.write(temp);
					myWriter.close();
					System.out.println("Successfully wrote to the file");
				} catch (IOException e) {
					System.out.println("An error occurred.");
					e.printStackTrace();
				}
			}
			out.println("Decrypted txt files saved under" + sdirout);
			sbLog += ("Decrypted " + listoffiles.length + " files from " + sdir + " and saved to " + sdirout+"using key: "+key);

		}
		// } else {

		// out.println("Returning to main menu...");

		// }

	}

	private void Options() throws Exception {
		out.println("[INFO] Additional Options to choose");
		out.println("1)Unparse decrypted files");
		out.println("2)Create log of encryption/decryption and corresponding key used ");

		int option = s.nextInt();
		sbLog += ("User chose " + option + " from option menu");
		switch (option) {
		case 1 -> unparse();
		case 2 -> CreateLog();

		default -> out.println("[Error] Invalid Selection");

		}

	}

	private void unparse() throws Exception { // Not using specified input/output directory but asking user to enter new
												// directory

		out.println("Please enter input directory->"); // Potential future enhancement:provide a default option of using
														// current directory where encrypted files are saved
		String sdirin = s.next();
		out.println("Please enter output directory ->");// Potential future enhancement: adding an option for user to
														// choose re-name logic for files, then could have input and
														// output in same directory
		String sdirout = s.next();
		Parse unparsed = new Parse(); // The reverse parsing function is engineered in the Parse class
		File folder = new File(sdirin); // Start to loop over the input directory
		File[] listoffiles = folder.listFiles(); // Files list
		for (File file : listoffiles) {

			if (file.isFile() && file.getName().endsWith(".txt")) { // Only process (unparse) the txt files
				String content = file.getAbsolutePath(); // Get the absolute path to feed in the Parse().unparse(Str)
															// function, output is an array of Strings
				String[] temp = unparsed.unparse(content); // The output of unparse() function is a String array
				try {
					FileWriter fr = new FileWriter(sdirout + file.getName()); // Potential future enhancement: adding an
					BufferedWriter brw = new BufferedWriter(fr); // option for user to choose re-name logic
					for (String line : temp) { 
						brw.write(line); 						//  Open a FileWriter to write
						brw.newLine();
					} 												// unparsed string array to txt file
					brw.close(); 										// using original name, but under output
					fr.close(); 										// directory
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		out.println("Files unparsed and saved in output dir" + sdirout);
		sbLog += ("Unparsed " + listoffiles.length + " files from " + sdirin + "and saved under " + sdirout);
	}

	private void CreateLog() throws IOException { // The log has been created from invoking the program, this option is
													// just to output the log created. Will promt user to enter desired
													// output directory and name for the log
		out.println("Specify output directory and name for log file, including extension name -> ");
		out.println("(sample format: User/JavaProject/ADFGVXCipherProgramLog.txt)");
		String sdirout = s.next();

		try (FileWriter wrlog = new FileWriter(sdirout)) {
			wrlog.write(sbLog);
		}
		out.println("Log created and saved in output dir" + sdirout);

	}

	private static void showOptions() throws Exception {
		// You should put the following code into a menu or Menu class
		System.out.println(ConsoleColour.GREEN);
		System.out.println("************************************************************");
		System.out.println("*       ATU - Dept. Computer Science & Applied Physics     *");
		System.out.println("*                                                          *");
		System.out.println("*                   ADFGVX File Encryption                 *");
		System.out.println("*                                                          *");
		System.out.println("************************************************************");
		System.out.println("(1) Specify Input File Directory");
		System.out.println("(2) Specify Output File Directory");
		System.out.println("(3) Set Key");
		System.out.println("(4) Encrypt");
		System.out.println("(5) Decrypt");
		System.out.println("(6) Options"); 
		System.out.println("(7) Quit");
		System.out.println(
				"Note: Please specify input & output file directory before first time invoking any processing functions");
		// Output a menu of options and solicit text from the user
		System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
		System.out.print("Select Option [1-7]>");
		System.out.println();

	}

	public void setKeepRunning(boolean keepRunning) { //Create a setter for user to change the control variable keepRunning to false, to quit
		this.keepRunning = keepRunning;

	}
}