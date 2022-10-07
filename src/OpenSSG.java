import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.String;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.IOException;

public class OpenSSG {
    private static final String OPTION_DESCRIPTION = "\nAvailable options:\n[-v | --version]\t\t\tDisplay program information\n[-h | --help]\t\t\t\tDisplay how to use options\n[-i | --input <file-or-folder>]\t\tSpecify input file or folder\n[-o | --output <folder-name>]\t\tSpecify output folder. Default is ./dist\n[-l | --lang <language-country>]\t\tSpecify language to add it to html tag";

    public static void main(String[] args) throws IOException {
        if (areArgsValid(args)) {
            switch (args[0]) {
                case "--version", "-v" -> {
                    Release release = new Release();
                    System.out.println("OpenSSG version " + release.version + ", " + release.dateOfRelease);
                }
                case "--help", "-h" ->
                        System.out.println("usage: " + new Release().name + " <option>\n" + OPTION_DESCRIPTION);
                case "--config", "-c" -> {
                    System.out.println("Parsing a potential config file.");
                    var optionArgs = config(args[1]);
                    generateHtmlFiles(optionArgs);
                }
                default -> {
                    var optionArgs = createOptions(args);
                    generateHtmlFiles(optionArgs);
                }
            }
        }
    }

    /** Handle opening of JSON or throwing of error and exiting of program.
     * Read from JSON and assign appropriate properties from it while ignoring the ones that do not exist.
     * @param jsonFN This is the filename that should be taken as an argument when user starts the program under -c or --config*/
    public static Options config(String jsonFN)  {
        //TODO: Handle opening of JSON or throwing of error and exiting of program
        //TODO: Read from JSON and assign appropriate properties from it while ignoring the ones that do not exist.

        Options options = new Options();

        JSONParser jsonParser = new JSONParser();

        try(FileReader reader = new FileReader(jsonFN)){
            Object jsonObject = jsonParser.parse(reader);
            JSONObject configProps = (JSONObject) jsonObject;
            JSONArray styleArray = (JSONArray) configProps.get("stylesheets");

            ArrayList <String> stylesheets = new ArrayList<>();

            if(styleArray != null){
                for(int i=0; i<styleArray.size();i++){
                    stylesheets.add((String)styleArray.get(i));
                }
            }


            if(configProps.containsKey("input")) {
                options.setInput((String) configProps.get("input"));
            }
            if(configProps.containsKey("output")){
                options.setOutput((String)configProps.get("output"));
            }
            if(configProps.containsKey("stylesheets")){
                options.setStylesheetLinks(stylesheets);
            }
            if(configProps.containsKey("lang")){
                options.setLanguage((String)configProps.get("lang"));
            }

        }
        catch(FileNotFoundException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.exit(2);
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.exit(2);
        }
        catch(ParseException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.exit(2);
        }

        return options;
    }

    public static Options createOptions(String[] args) {
        Options options = new Options();

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-i", "--input" -> options.setInput(args[i+1]);
                case "-o", "--output" -> options.setOutput(args[i+1]);
                case "-l", "--lang" -> options.setLanguage(args[i+1]);
                case "-s", "--stylesheet" -> {
                    int j = i + 1;
                    ArrayList<String> stylesheetLinks = new ArrayList<>();
                    while (j < args.length && !args[j].startsWith("-")) {
                        stylesheetLinks.add(args[j]);
                        j++;
                    }
                    options.setStylesheetLinks(stylesheetLinks);
                }
            }
        }
        return options;
    }

    public static boolean areArgsValid(String[] args) {
        boolean isValid = true;
        String[] basicOptions = { "-v", "--version", "-h", "--help" };
        String[] singleArgOptions = { "-i", "--input", "-o", "--output", "-l", "-lang"};
        String[] stylesheetOptions = { "-s", "--stylesheet" };

        if (args.length > 0) {
            if (Arrays.asList(basicOptions).contains(args[0])){
                if (args.length > 1){
                    System.out.println("Cannot process other option or argument. Check the usage by running java OpenSSG -h or --help.");    
                    isValid = false;
                }
            }else if(Arrays.asList(args).contains("-c") || Arrays.asList(args).contains("--config")){
                isValid=true;
            }
            else if (!(Arrays.asList(args).contains("-i") || Arrays.asList(args).contains("--input"))){
                isValid = false;
                System.out.println("Input option must be provided with <File> or <Folder> argument. Check the usage by running java OpenSSG -h or --help.");
            } else {
                for (int i = 0; i < args.length; i++) {
                    if (Arrays.asList(singleArgOptions).contains(args[i])) {
                        i++;
                        if (i<args.length){
                            if (args[i].startsWith(("-"))){
                                isValid = false;
                                System.out.println("Missing option argument. Check out the usage by running java OpenSSG -h or --help.");
                            }
                        } else {
                            isValid = false;
                            System.out.println("Missing option argument. Check out the usage by running java OpenSSG -h or --help.");
                        }
                    } else if (Arrays.asList(stylesheetOptions).contains(args[i])){
                        i++;
                        if (i<args.length){
                            while(i<args.length && !args[i].startsWith("-")){
                                i++;
                            }
                        } else {
                            isValid = false;
                            System.out.println("Missing option argument. Please provide CSS links to add.");
                        }
                    }
                }
            }
        } else {
            System.out.println("Please provide an option. Check usage by running OpenSSG -h or OpenSSG --help.");
        }
        return isValid;
    }

    public static void generateHtmlFiles(Options optionArgs) throws IOException {
        FileUtilities fileUtil = new FileUtilities();

        fileUtil.generateHtmlFiles(optionArgs);
    }
}
