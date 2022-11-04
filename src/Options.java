import java.util.ArrayList;


public class Options {
    private String input;
    private String output = "./dist";
    private ArrayList<String> stylesheetLinks;
    private String language = "en";

    public Options(String input, String output, ArrayList<String> stylesheetLinks, String language) {
        this.input = input;
        this.output = output;
        this.stylesheetLinks = new ArrayList<>(stylesheetLinks);
        this.language = language;
    }

    public Options(String input, String output, ArrayList<String> stylesheetLinks) {
        this.input = input;
        this.output = output;
        this.stylesheetLinks = new ArrayList<>(stylesheetLinks);
    }

    public Options(String input, String output) {
        this.input = input;
        this.output = output;
    }

    public Options() {
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
        if (stylesheetLinks != null) {
            return new ArrayList<>(stylesheetLinks);
        }
        return new ArrayList<>();
    }

    public void setStylesheetLinks(ArrayList<String> stylesheetLinks) {
        this.stylesheetLinks = new ArrayList<>(stylesheetLinks);
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}
