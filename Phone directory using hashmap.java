import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * This program lets the user keep a file named "phone book" that contains 2
 * categories that are names and phone numbers. The data for the phone book is
 * stored in a file in the user's home directory.
 *
 * The program is meant as a demonstration of file for storing phone book
 * information using the data structure "HashMap". The phone book data is stored
 * in the form of Name/Number pairs, with following defined conditions checking.
 */
public class PhoneDirectoryDemo {

    /**
     * The name of the file in which the phone book data is kept. The file is
     * stored in the user's home directory.
     */
    private static final String DATA_FILE_NAME = "phone_book"; //initializing variable for file name. you can create many new files just by changing the name 
    public static void main(String[] args) {
        String name, number;       // Name and number of an entry in the directory.
        HashMap<String, String> phoneBook;   // Phone directory data structure.
        // keys are name and values are number.
        phoneBook = new HashMap<>();   //allocating memory for hashmaps.
        /* Create a dataFile variable of type File to represent the
       * data file that is stored in the user's home directory.
         */
        File userHomeDirectory = new File(System.getProperty("user.home"));    //Creating the file in user homedirectory.
        File dataFile = new File(userHomeDirectory, DATA_FILE_NAME);          //allocating the above file name in user homedirectoy.
        /* If the data file already exists, then the data in the file is
       * read and is used to initialize the phone directory.  The format
       * of the file must be as follows:  Each line of the file represents
       * one entry(number and name as pair).If a file exists but does not
       * have this format, then the program terminates; this is done to
       * avoid overwriting a file that is being used for another purpose.
         */
        if (!dataFile.exists()) {
            System.out.println("No phone book data file found.");
            System.out.println("A new one will be created.");// creating a new file
            System.out.println("File name:  " + dataFile.getAbsolutePath()); //allocating the above file as new one in userhomedirectory as hidden file.
        } else {
            System.out.println("Reading phone book data...");      // if a file existed.. then it will read and capture the data from the file. will perform functions on user requirements.
            
            //if a file does not exist with same format.
            try {
                Scanner scanner = new Scanner(dataFile);
                while (scanner.hasNextLine()) {
                    String phoneEntry = scanner.nextLine();
                    int separatorPosition = phoneEntry.indexOf('%');
                    if (separatorPosition == -1) {
                        throw new IOException("File is not a phonebook data file.");
                    }
                    name = phoneEntry.substring(0, separatorPosition);
                    number = phoneEntry.substring(separatorPosition + 1);
                    phoneBook.put(name, number);
                }
            } catch (IOException e) {
                System.out.println("Error in phone book data file.");
                System.out.println("File name:  " + dataFile.getAbsolutePath());
                System.out.println("This program cannot continue.");
                System.exit(1);
            }
        }
        /* ask commands from the user and carry them out, until the
       * user gives the "Exit from program" command.
         */
        Scanner in = new Scanner(System.in);
        boolean changed = false;  // Have any changes been made to the directory?
        mainLoop:
        while (true) {           //A main loop that just sits waiting for someone to terminate it.
            System.out.println("\nSelect the action that you want to perform:");
            System.out.println("   1.  Look up a phone number."); //Search a number by name.
            System.out.println("   2.  Add or change a phone number.");  //ADD an entry.
            System.out.println("   3.  Remove an entry from your phone directory.");  //remove or delete an entry.
            System.out.println("   4.  List the entire phone directory.");    //print the all entries present in file.
            System.out.println("   5.  Exit from the program.");   //terminate the loop.
            System.out.println("Enter action number (1-5):  ");     //user input.
            int command;
            if (in.hasNextInt()) {
                command = in.nextInt();
                in.nextLine();
            } else {
                System.out.println("\nILLEGAL RESPONSE.  YOU MUST ENTER A NUMBER."); //if user input other then the required(1-5) input.
                in.nextLine();
                continue;
            }
            // using switch cases for the user command. we have allocated the 1 to 5 cases..
            switch (command) {
                case 1:
                    System.out.print("\nEnter the name whose number you want to look up: ");
                    name = in.nextLine().trim().toLowerCase();
                    number = phoneBook.get(name);
                    if (number == null) //if number not found!!
                    {
                        System.out.println("\nSorry, NO Number found for " + name);
                    } else {
                        System.out.println("\nNumber for " + name + ":  " + number);
                    }
                    break;
                case 2:
                    System.out.print("\nEnter the name: ");
                    name = in.nextLine().trim().toLowerCase();
                    if (name.length() == 0) {
                        System.out.println("\nNAME CANNOT BE BLANK.");
                    } else if (name.indexOf('%') >= 0) {
                        System.out.println("\nNAME CANNOT CONTAIN THE CHARACTER \"%\".");
                    } else {
                        System.out.print("Enter phone number: ");
                        number = in.nextLine().trim();
                        if (number.length() == 0) {
                            System.out.println("\nPHONE NUMBER CANNOT BE BLANK!!!");
                        } else {
                            phoneBook.put(name, number);
                            changed = true;
                        }
                    }
                    break;
                case 3:
                    System.out.print("\nEnter the name whose entry you want to remove: ");
                    name = in.nextLine().trim().toLowerCase();
                    number = phoneBook.get(name);
                    if (number == null) {
                        System.out.println("\nSORRY, there is no entry for " + name);
                    } else {
                        phoneBook.remove(name);
                        changed = true;
                        System.out.println("\nDirectory entry removed for " + name);
                    }
                    break;
                case 4:
                    System.out.println("\nList of entries in your PHONE BOOK:\n");
                    phoneBook.entrySet().forEach(entry -> {
                        System.out.println("   " + entry.getKey() + ": " + entry.getValue());
                    });
                    break;
                case 5:
                    System.out.println("\nExiting the program.");
                    break mainLoop;
                default:
                    System.out.println("\nIllegal action number.");
            }
        }
        /* Before ending the program, write the current contents of the
       * phone directory, but only if some changes have been made to
       * the directory.
         */
        if (changed) {
            System.out.println("Saving phone directory changes to file "
                    + dataFile.getAbsolutePath() + " ...");
            PrintWriter out;
            try {
                out = new PrintWriter(new FileWriter(dataFile));
            } catch (IOException e) {
                System.out.println("ERROR: Can't open data file for output.");
                return;
            }
            for (Map.Entry<String, String> entry : phoneBook.entrySet()) {
                out.println(entry.getKey() + "%" + entry.getValue());
            }
            out.close();
            if (out.checkError()) {
                System.out.println("ERROR: Some error occurred while writing data file.");
            } else {
                System.out.println("Done.");
            }
        }
    }
}
