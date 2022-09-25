import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    public static String getCssLinks(ArrayList<String> stylesheetLinks){
        StringBuilder cssLinks = new StringBuilder();

        for (String link : stylesheetLinks){
            cssLinks.append("\s\s<link rel=\"stylesheet\" href=\"").append(link).append("\">\n");
        }
        return cssLinks.toString();
    }

    public static boolean hasTitle(String[] linesFromFile){
        return linesFromFile[1].isEmpty() && linesFromFile[2].isEmpty();
    }

    public static String parseMarkdownSyntax(String[] linesFromInputFile){
        StringBuilder bodyContent = new StringBuilder();
        boolean wasThereEmptyLine = false;
        boolean isClosed = true;
        Pattern mdLinkPattern = Pattern.compile("\\[([^]]+)]\\(([^)]+)\\)");
        Pattern mdCodePattern = Pattern.compile("`([^`].*?)`");
        Matcher mdLinkMatcher, mdCodeMatcher;

        for (String line : linesFromInputFile){
            if (line.startsWith("# ")){
                if (!isClosed) {
                    bodyContent.append("</p>\n");
                }
                bodyContent.append("\s\s<h1>").append(line.substring(2)).append("</h1>\n");
                isClosed = true;
                wasThereEmptyLine = false;
            } else if (line.startsWith("## ")) {
                if (!isClosed) {
                    bodyContent.append("</p>\n");
                }
                bodyContent.append("\s\s<h2>").append(line.substring(3)).append("</h2>\n");
                isClosed = true;
                wasThereEmptyLine = false;
            } else if (line.equals("---")){
                bodyContent.append("</p>\n\s\s<br>\n");
                isClosed = true;
            } else if (line.isEmpty()) {
                wasThereEmptyLine = true;
            } else {
                if (wasThereEmptyLine && !isClosed){
                    bodyContent.append("</p>\n");
                    wasThereEmptyLine = false;
                    isClosed = true;
                }

                if (isClosed){
                    bodyContent.append("\s\s<p>");
                    isClosed = false;
                    wasThereEmptyLine = false;
                }

                mdLinkMatcher = mdLinkPattern.matcher(line);
                mdCodeMatcher = mdCodePattern.matcher(line);
                if (mdLinkMatcher.find()) {
                    bodyContent.append(line.replaceAll(String.valueOf(mdLinkPattern), "<a href=\"$2\">$1</a>"));
                } else if (mdCodeMatcher.find()) {
                    bodyContent.append(line.replaceAll(String.valueOf(mdCodePattern), "<code>$1</code>"));
                } else {
                    bodyContent.append(line);
                }
            }
        }
        return bodyContent.toString();
    }

    public static String parseToHtmlBody(String[] txtLines){
        StringBuilder bodyContent = new StringBuilder();

        for (String line : txtLines){
            if (line.isEmpty() && (bodyContent.length() > 0)){
                bodyContent = new StringBuilder(bodyContent.substring(0, bodyContent.length() - 1));
                bodyContent.append("</p>\n\s\s<p>");
            } else {
                bodyContent.append(line).append("\n");
            }
        }
        bodyContent = new StringBuilder(bodyContent.substring(0, bodyContent.length() - 1));

        return bodyContent.toString();
    }
}
