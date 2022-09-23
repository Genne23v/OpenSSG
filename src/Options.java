import java.util.ArrayList;

public class Options {
    private String input;
    private String output = "./dist";
    private ArrayList<String> stylesheetLinks;
    private String language = "en-CA";

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
}
