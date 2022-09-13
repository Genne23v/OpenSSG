import java.lang.String;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class OpenSSG {

    private static final String DIST_FOLDER = "./dist";

    public static void main(String[] args) throws IOException{
        if (args.length > 0){
            switch(args[0]){
                case "--version":
                case "-v":
                    Release release = new Release();
                    System.out.println("wk-SSG version " + release.version + ", " + release.dateOfRelease);
                    break;
                case "--help":
                case "-h":
                    System.out.println("usage: " + new Release().name + " [-v | --version] [-h | --help]\n\t\t\t[-i | --input <filename>] [-o | --output <folder-name>]\n\t\t\t[-s | --stylesheet <CSS-URL>]");
                    break;
                default:
                    var optionArgs = analyzeArgs(args);
                    sortOptionAndCreateFiles(optionArgs);
            }
        }
    }

    public static Dictionary<String, Object> analyzeArgs(String[] args){
        Dictionary<String, Object> optionArgs = new Hashtable<String, Object>();
        
        for (int i=0; i< args.length; i++){
            if (args[i].equals("-i") || args[i].equals("--input")){
                optionArgs.put("-i", args[i+1]);
            } else if (args[i].equals("-o") || args[i].equals("--output") ){
                optionArgs.put("-o", args[i+1]);
            } else if (args[i].equals("-s") || args[i].equals("--stylesheet")){
                int j = i+1;
                ArrayList<String> stylesheetLinks = new ArrayList<String>();

                while (j < args.length && !args[j].startsWith("-") ){
                    stylesheetLinks.add(args[j]);
                    j++;
                }
                optionArgs.put("-s", stylesheetLinks);
            }
        }
        return optionArgs;
    }

    public static void sortOptionAndCreateFiles(Dictionary<String, Object> optionArgs) throws IOException{
        String inputOption = (String) optionArgs.get("-i");

        if (optionArgs.get("-o") != null && optionArgs.get("-s") != null){
            String outputOption = (String) optionArgs.get("-o");
            ArrayList<String> stylesheetOption = (ArrayList<String>) optionArgs.get("-s");

            createHTMLFiles(inputOption, outputOption, stylesheetOption);

        } else if (optionArgs.get("-o") == null && optionArgs.get("-s") != null){
            String outputOption = "./dist";
            ArrayList<String> stylesheetOption = (ArrayList<String>) optionArgs.get("-s");

            createHTMLFiles(inputOption, outputOption, stylesheetOption);

        } else if (optionArgs.get("-o") == null && optionArgs.get("-s") == null){

            createHTMLFiles(inputOption);

        }
    }

    public static void createHTMLFiles(String inputArg) throws IOException{
        FileUtilities fileUtilities = new FileUtilities();

        Path outPath = Paths.get(DIST_FOLDER);
        if (Files.exists(outPath, new LinkOption[]{LinkOption.NOFOLLOW_LINKS})){
            fileUtilities.removeDistFolder();
        }
        
        Files.createDirectories(outPath);
        
        ArrayList<String> txtFiles = fileUtilities.getAllTxtFiles(inputArg);
        fileUtilities.generateHTMLFiles(txtFiles);

    };

    public static void createHTMLFiles(String input, String output, ArrayList<String> stylesheetLinks) throws IOException{
        FileUtilities fileUtilities = new FileUtilities();

        ArrayList<String> txtFiles = fileUtilities.getAllTxtFiles(input);
        
        Path outPath = Paths.get(output);
        if (Files.exists(outPath, new LinkOption[]{LinkOption.NOFOLLOW_LINKS})){
            fileUtilities.removeDistFolder();
        }
        
        Files.createDirectories(outPath);

        fileUtilities.generateHTMLFiles(txtFiles);  
    }
}
