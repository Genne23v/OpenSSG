import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Options {
    private String input;
    private String output = "./dist";
    private ArrayList<String> stylesheetLinks;
    private String language = "en";

    public Options(String input, String output, ArrayList<String> stylesheetLinks, String language){
        this.input = input;
        this.output = output;
        this.stylesheetLinks = stylesheetLinks;
        this.language = language;
    }

    public Options(String input, String output, ArrayList<String> stylesheetLinks){
        this.input = input;
        this.output = output;
        this.stylesheetLinks = stylesheetLinks;
    }

    public Options(String input, String output){
        this.input = input;
        this.output = output;
    }

    public Options(){
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public ArrayList<String> getStylesheetLinks() {
        return stylesheetLinks;
    }

    public void setStylesheetLinks(ArrayList<String> stylesheetLinks) {
        this.stylesheetLinks = stylesheetLinks;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    /** Handle opening of JSON or throwing of error and exiting of program.
     * Read from JSON and assign appropriate properties from it while ignoring the ones that do not exist.
     * @param jsonFN This is the filename that should be taken as an argument when user starts the program under -c or --config*/
    public void config(String jsonFN)  {
        //TODO: Handle opening of JSON or throwing of error and exiting of program
        //TODO: Read from JSON and assign appropriate properties from it while ignoring the ones that do not exist.

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
                setInput((String) configProps.get("input"));
            }
            if(configProps.containsKey("output")){
                setOutput((String)configProps.get("output"));
            }
            if(configProps.containsKey("stylesheet")){
                setStylesheetLinks(stylesheets);
            }
            if(configProps.containsKey("lang")){
                setLanguage((String)configProps.get("lang"));
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
    }

}
