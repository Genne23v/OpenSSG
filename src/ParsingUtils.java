import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class ParsingUtils {

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

    public static String parseMarkdownSyntax(String bodyContent){
        Parser parser = Parser.builder().build();
        Node document = parser.parse(String.valueOf(bodyContent));
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String parsedBodyContent = renderer.render(document);

        return parsedBodyContent;
    }
}
