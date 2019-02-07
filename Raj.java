/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.*;
import java.io.*;

/**
 *
 * @author nraj39
 */
public class App {

    static HashMap<String, String> tags = new HashMap<String, String>();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        if (args == null || args.length == 0 || isNullOrBlank(args[0])) {
            System.out.println("Missing GED file.");
            return;
        }
        String gedFilePath = args[0];

        File f = new File(gedFilePath);
        if (!f.exists()) {
            System.out.println("'" + gedFilePath + "' file not exists.");
            return;            
        }
                
        System.out.println(gedFilePath);  
        
        initialize();
        parseGEDFile(gedFilePath);
    }
    
    private static void initialize() {
        tags.put("INDI", "0");
        tags.put("NAME", "1");
        tags.put("SEX", "1");
        tags.put("BIRT", "1");
        tags.put("DEAT", "1");
        tags.put("FAMC", "1");
        tags.put("FAMS", "1");
        tags.put("FAM", "0");
        tags.put("MARR", "1");
        tags.put("HUSB", "1");
        tags.put("WIFE", "1");
        tags.put("CHIL", "1");
        tags.put("DIV", "1");
        tags.put("DATE", "2");
        tags.put("HEAD", "0");
        tags.put("TSLR", "0");
        tags.put("NOTE", "0");
    }
    
    private static void parseGEDFile(String gedFilePath) throws FileNotFoundException, IOException {
        
        String gedOutFilePath = gedFilePath + ".out";

        FileInputStream fstream = new FileInputStream(gedFilePath);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        FileWriter outfw = new FileWriter(gedOutFilePath);
        PrintWriter writer = new PrintWriter(outfw);
        
        String line;

        //Read File Line By Line
        while ((line = br.readLine()) != null)   {
          // Print the content on the console
          writer.println("--> " + line);
          
          String id;
          String tag;
          String arguments = null;
          boolean isSpecialTag = false;
          
          String[] parts = line.split(" ", 3);
          if (parts.length == 3 || parts.length == 2) {
              if (parts.length == 2) {
                  id = parts[0];
                  tag = parts[1];
              }
              else {
                  id = parts[0];  
                  tag = parts[1];
                  arguments = parts[2];
              }
              
              if (arguments != null && (arguments.equals("INDI") || arguments.equals("FAM"))) {
                  String temp = tag;
                  tag = arguments;
                  arguments = temp;
                  isSpecialTag = true;
              }
              printGEDLine(writer, id, tag, arguments, isSpecialTag);
          }
        }

        //Close the input stream
        fstream.close();
        outfw.close();
    }
   
    private static void printGEDLine(PrintWriter writer, String level, String tag, String arguments, boolean isSpecialTag) {
        boolean isValid = false;
        
        if (tags.containsKey(tag) && tags.get(tag).equals(level)) {
            if (tag.equals("INDI") || tag.equals("FAM"))
                isValid = isSpecialTag;
            else
                isValid = true;
        }
        
        if (arguments == null) {
            writer.println("<-- " + level + "|" + tag + "|" + (isValid ? "Y" : "N") + "|");
        }
        else {
            writer.println("<-- " + level + "|" + tag + "|" + (isValid ? "Y" : "N") + "|" + arguments);
        }
    }
    private static boolean isNullOrBlank(String param) {
        if (param == null || param.trim().length() == 0) {
            return true;
        }
        return false;
    }    
}

