
package authenticationsystem;
import java.util.Scanner;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.security.MessageDigest;
import javax.swing.JOptionPane;

/**
 * IT 145 Zoo Authentication System
 * @author Carol McLaughlin
 * 12/4/16
 */
public class AuthenticationSystem {

    public static void main(String[] args) throws IOException {
        // create FileInfo objects and populate them using the populateFileInfo method
        FileInfo authFile = new FileInfo();
        FileInfo adminFile = new FileInfo();
        FileInfo vetFile = new FileInfo();
        FileInfo keeperFile = new FileInfo();
        String authFileName = "credentials.txt";
        String adminFileName = "admin.txt";
        String vetFileName = "veterinarian.txt";
        String keeperFileName = "zookeeper.txt";
        
        PopulateFileInfo(authFile, authFileName);
        PopulateFileInfo(adminFile, adminFileName);
        PopulateFileInfo(vetFile, vetFileName);
        PopulateFileInfo(keeperFile, keeperFileName);
        
        // create arrays to hold the information within credentials.txt based on their category
        ArrayList<String> line = new ArrayList<>();
        ArrayList<String> userNames = new ArrayList<>();
        ArrayList<String> passwords = new ArrayList<>();
        ArrayList<String> roles = new ArrayList<>();
        ArrayList<String> MD5pass = new ArrayList<>();
                
        //Connect to file credentials.txt, if connection fails, throws exception
        Scanner inFile = authFile.getFileScanner();
        
        // Read each line of authFile into credential arrays
        // based on their input
        while (inFile.hasNextLine()) {
            userNames.add(inFile.next());
            MD5pass.add(inFile.next());
            line.add(inFile.nextLine());
        }
        for (int i = 0; i < line.size(); i++) {
                passwords.add(line.get(i).substring(line.get(i).indexOf('\"')+1, line.get(i).lastIndexOf('\"')));
                roles.add(line.get(i).substring(line.get(i).lastIndexOf('\"')+1, line.get(i).length()));
        }   
                
        inFile.close(); // close connection to credentials.txt
        
        String userNameInput = JOptionPane.showInputDialog(null, "Enter Username", "Zoo System Login", JOptionPane.PLAIN_MESSAGE);
        String passwordInput = JOptionPane.showInputDialog(null, "Enter Password", "Zoo System Login", JOptionPane.PLAIN_MESSAGE);
        
        // convert user provided password to MD5 hash to use for testing
        String ConvertedPass = "";
        try {
            ConvertedPass = MD5Convert(passwordInput);
        }
        catch(NoSuchAlgorithmException e) {
            System.out.println("Conversion failed");
        }

        // As long as the number of attempts is less than 3, check user provided username and password
        // against authorized credential arrays to determine a match
        boolean match = false;
        int attemptcount = 0;
        String role = "";
        
        while ((!match) && (attemptcount < 2)) {
            for ( String i : userNames) {
                if (i.equals(userNameInput)){
                    if (MD5pass.get(userNames.indexOf(i)).equals(ConvertedPass)) {
                    match = true;
                    // assign role based on same index of matched username and remove any whitespace in 
                    role = roles.get(userNames.indexOf(i)).replaceAll("\\s+", "");
                    }
                }
            }
            // If username and password provided do not trigger a match to authorized user credentials
            // increment number of attempts, and request user reenter input
            if ((!match) && (attemptcount < 2)) {
                attemptcount++;                
                JOptionPane.showMessageDialog(null, "Invalid login, please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                userNameInput = JOptionPane.showInputDialog("Enter Username");
                passwordInput = JOptionPane.showInputDialog("Enter Password");
                try {
                    ConvertedPass = MD5Convert(passwordInput);
                }
                catch(NoSuchAlgorithmException e) {
                    System.out.println("Conversion failed");
                }
            }
            // If provided user credentials match, determine user role based on role variable
            // and display the appropriate file.
            else {
                String messageDialog = "";
                switch (role) {
                    case "admin":
                        Scanner inAdmin = adminFile.getFileScanner();
                        while (inAdmin.hasNextLine()) {
                            messageDialog += inAdmin.nextLine() + "\n";
                        }
                        inAdmin.close();
                        break;
                    case "veterinarian":
                        Scanner inVet = vetFile.getFileScanner();
                        while(inVet.hasNextLine()) {
                            messageDialog += inVet.nextLine() + "\n";
                        }
                        inVet.close();
                        break;
                    case "zookeeper":
                        Scanner inKeeper = keeperFile.getFileScanner();
                        while(inKeeper.hasNextLine()) {
                            messageDialog += inKeeper.nextLine() + "\n";
                        }
                        inKeeper.close();
                        break;                        
                }
                JOptionPane.showMessageDialog(null, messageDialog, "Welcome!", JOptionPane.INFORMATION_MESSAGE);
            }
            
            // if no match is made after 3 attempts, notify user and exit the program
            if (attemptcount >= 2) {
                JOptionPane.showMessageDialog(null, "Too many failed attempts, logging you out.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        System.exit(0);
    } // end main
    
    // method to convert user provided password to an MD5 hash for testing purposes
    public static String MD5Convert(String password) throws java.security.NoSuchAlgorithmException {
        String original = password;  //Replace "password" with the actual password input by the user
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(original.getBytes());
		byte[] digest = md.digest();
        StringBuffer sb = new StringBuffer();
		for (byte b : digest) {
			sb.append(String.format("%02x", b & 0xff));
		}
                return sb.toString();
    } // end MD5Convert
    
    // method to populate individual fileInfo objects
    public static void PopulateFileInfo(FileInfo input, String fileName) throws IOException{
        input.setFileName(fileName);
        input.setFileInputStream(fileName);
        input.setFileScanner(input.getFileInputStream());    
                
    } // end connectToFile

} // end class

