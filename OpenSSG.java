import java.lang.String;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.io.IOException;

public class OpenSSG {

    static final String DIST_FOLDER = "./dist";
    private static final String OPTION_DESCRIPTION = " [-v | --version] [-h | --help]\n\t\t[-i | --input <file-or-folder>] [-o | --output <folder-name>]\n\t\t[-s | --stylesheet <CSS-URL>]";

    public static void main(String[] args) throws IOException{
        if (args.length > 0){
            switch(args[0]){
                case "--version":
                case "-v":
                    Release release = new Release();
                    System.out.println("OpenSSG version " + release.version + ", " + release.dateOfRelease);
                    break;
                case "--help":
                case "-h":
                    System.out.println("usage: " + new Release().name + OPTION_DESCRIPTION);
                    break;
                default:
                    var optionArgs = analyzeArgs(args);
                    sortOptionAndCreateFiles(optionArgs);
            }
        } else {
            System.out.println("Please provide an option. You can check usage by running OpenSSG -h or OpenSSG --help.");
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
        String outputOption = "";

        if (optionArgs.get("-o") != null && optionArgs.get("-s") != null){
            outputOption = (String) optionArgs.get("-o");
            @SuppressWarnings("unchecked") 
            ArrayList<String> stylesheetOption = (ArrayList<String>) optionArgs.get("-s");

            createHTMLFiles(inputOption, outputOption, stylesheetOption);

        } else if (optionArgs.get("-o") == null && optionArgs.get("-s") != null){
            outputOption = DIST_FOLDER;
            @SuppressWarnings("unchecked") 
            ArrayList<String> stylesheetOption = (ArrayList<String>) optionArgs.get("-s");

            createHTMLFiles(inputOption, outputOption, stylesheetOption);

        } else if (optionArgs.get("-o") != null && optionArgs.get("-s") == null){
            outputOption = (String) optionArgs.get("-o");

            createHTMLFiles(inputOption, outputOption);
        } else if (optionArgs.get("-o") == null && optionArgs.get("-s") == null){

            createHTMLFiles(inputOption);

        }
    }

    public static void createHTMLFiles(String inputArg) throws IOException{
        FileUtilities fileUtilities = new FileUtilities();

        Path outPath = Paths.get(DIST_FOLDER);
        if (Files.exists(outPath, new LinkOption[]{LinkOption.NOFOLLOW_LINKS})){
            fileUtilities.removeExistingFolder(DIST_FOLDER);
        }
        
        Files.createDirectories(outPath);
        
        ArrayList<String> txtFiles = fileUtilities.getAllTxtFiles(inputArg);
        fileUtilities.generateHTMLFiles(txtFiles);
    };

    public static void createHTMLFiles(String input, String output) throws IOException{
        FileUtilities fileUtilities = new FileUtilities();

        Path outPath = Paths.get(output);
        if (Files.exists(outPath, new LinkOption[]{LinkOption.NOFOLLOW_LINKS})){
            fileUtilities.removeExistingFolder(output);
        }
        
        Files.createDirectories(outPath);
        
        ArrayList<String> txtFiles = fileUtilities.getAllTxtFiles(input);
        fileUtilities.generateHTMLFiles(txtFiles, output);
    }

    public static void createHTMLFiles(String input, String output, ArrayList<String> stylesheetLinks) throws IOException{
        FileUtilities fileUtilities = new FileUtilities();
        
        Path outPath = Paths.get(output);
        if (Files.exists(outPath, new LinkOption[]{LinkOption.NOFOLLOW_LINKS})){
            fileUtilities.removeExistingFolder(output);
        }
        
        Files.createDirectories(outPath);
        
        ArrayList<String> txtFiles = fileUtilities.getAllTxtFiles(input);
        fileUtilities.generateHTMLFiles(txtFiles, output, stylesheetLinks);  
    }
}
