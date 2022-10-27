import java.util.ArrayList;
import java.util.List;
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

    public static String buildSidebar(ArrayList<String> convertingFiles) {
        StringBuilder sidebar = new StringBuilder();
        DOMNode<String> rootDom = null;
        sidebar.append("<div class=\"sidebar\"><ul>");

        for (String file : convertingFiles){
            String trimmedFilename = removeExtension(file.replace("./", ""));
            String[] filePathSplit = trimmedFilename.split("/");

            List<DOMNode<String>> domNodes = new ArrayList<>(filePathSplit.length-1);
            for (int i=0; i<filePathSplit.length; i++){
                if (rootDom == null && i == 0){
                    rootDom = new DOMNode<>(filePathSplit[0]);
                    sidebar.append(filePathSplit[0]);
                } else {
                    domNodes.add(new DOMNode<>(filePathSplit[i]));

                    if (i == 1) {
                        if (!rootDom.doesChildrenHaveData(filePathSplit[i])) {
                            rootDom.addChild(filePathSplit[i]);
                        }
                    } else {
                        DOMNode<String> tempNode = rootDom;
                        int j = 1;

                        while (j <= i) {
                            if (tempNode.getChild(filePathSplit[j]) == null) {
                                tempNode.addChild(filePathSplit[j]);
                            }
                            tempNode = tempNode.getChild(filePathSplit[j]);
                            j++;
                        }
                    }
                }
            }
        }
        sidebar.append(renderList(rootDom));
        sidebar.append("</ul>\n</div>");
        return sidebar.toString();
    }

    private static String renderList(DOMNode<String> rootDom) {
        StringBuilder renderedList = new StringBuilder();

        for (DOMNode<String> child : rootDom.getChildren()){
            if (child.isLeaf()){
                renderedList.append("<li><a href=\"" + child.getUrl() + "\">" + child.getData() + "</a></li>\n");
            } else {
                renderedList.append("<li>" + child.getData());
                renderedList.append("<ul>" + renderList(child) + "</ul>");
                renderedList.append("</li>");
            }
        }
        return renderedList.toString();
    }

    private static String removeExtension(String filename) {
        return filename.substring(0, filename.lastIndexOf("."));
    }
}
