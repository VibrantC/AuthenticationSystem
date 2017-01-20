package authenticationsystem;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.IOException;
/**
 * IT 145 Zoo Authentication System
 * @author Carol McLaughlin
 * 12/11/2016
 */
public class FileInfo {
    private String fileName;
    private Scanner fileScanner;
    private FileInputStream inFile;
    
    public FileInfo() {
        fileName = "none";
        fileScanner = null;
        inFile = null;
    }
    
    public void setFileName(String input) {
        fileName = input;
    }
    public String getFileName() {
        return fileName;
    }
    
    public void setFileScanner(FileInputStream input) {
        fileScanner = new Scanner(input);
    }
    public Scanner getFileScanner() {
        return fileScanner;
    }
    
    public void setFileInputStream(String input) throws IOException {
        inFile = new FileInputStream(input);
    }
    public FileInputStream getFileInputStream() {
        return inFile;
    }
    
}
