ADFGVX File Encryption
@author Xihui Chen
@version Java 19

Description
This is a terminal-based application that  allow user to encrypt & decrypt txt files under specified file directory, using ADFGVX Cipher encryption methodology, output txt files with the same names of input files, so if user want to keep both files need to specify different output directory than input directory.


To Run
From console at the directory where src folder is saved:
javac *.java
After compiling the file, if successfully done then run the Runner class:
java Runner
Then navigate through the console menu to set desired input directory and output directory for the file processing. You can also choose to set key for encryption/decryption, and choose “Option” to output a log since the programme had been invoked to a designated directory with specified name (/***.txt for example). The log will contain information about how many files were executed what action using what key.


Features
•	Specify the directory containing objects to be processed
o	All txt files under entered directory will be processed
o	In future beta versions could experiment being more inclusive, compatible with more types of links and support other file extensions
•	Specify the directory for processed files to be saved
o	Specify the output directory, note that the output files will be saved using the same name as input files, so if the output directory was set the same as input directory all the input files will be overwritten by output files
•	User can encrypt or decrypt txt files using the application
•	Change the key for encryption/decryption
•	Option to output log of historical commands since program called
•	Option to unparse files


