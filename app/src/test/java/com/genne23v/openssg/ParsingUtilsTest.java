package com.genne23v.openssg;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;

public class ParsingUtilsTest {

    @Test
    public void getCssLinkTest() {
        ArrayList<String> stylesheets = new ArrayList<>();
        stylesheets.add("style");

        Assertions.assertEquals("\s\s<link rel=\"stylesheet\" href=\"style\">\n", ParsingUtils.getCssLinks(stylesheets));

        stylesheets.remove(0);
        Assertions.assertEquals("", ParsingUtils.getCssLinks(stylesheets));
    }

    @Test
    public void hasTitleTest() {
        String[] stringArray1 = new String[]{"title", "", ""};
        String[] stringArray2 = new String[]{"", "", ""};
        String[] stringArray3 = new String[]{"title", "line1", ""};
        String[] stringArray4 = new String[]{"title", "", "line2"};

        Assertions.assertTrue(ParsingUtils.hasTitle(stringArray1));
        Assertions.assertTrue(ParsingUtils.hasTitle(stringArray2));
        Assertions.assertFalse(ParsingUtils.hasTitle(stringArray3));
        Assertions.assertFalse(ParsingUtils.hasTitle(stringArray4));
    }

    @Test
    public void parseToHtmlTest() {
        String[] stringArray1 = new String[]{"line1", "line2"};
        String[] stringArray2 = new String[]{"line1", "", "line2"};

        Assertions.assertEquals("line1\nline2", ParsingUtils.parseToHtmlBody(stringArray1));
        Assertions.assertEquals("line1</p>\n\s\s<p>line2", ParsingUtils.parseToHtmlBody(stringArray2));
    }

    @Test
    public void parseMarkdownTest() {
        String heading = "# h1 heading";
        String strong = "I am *strong*";
        String hr = "---";
        String link = "[link](https://test.com)";
        Parser parser = Parser.builder().build();
        Node document = parser.parse(heading);
        HtmlRenderer renderer = HtmlRenderer.builder().build();

        Assertions.assertEquals("<h1>h1 heading</h1>\n", renderer.render(document));

        document = parser.parse(strong);
        Assertions.assertEquals("<p>I am <em>strong</em></p>\n", renderer.render(document));

        document = parser.parse(hr);
        Assertions.assertEquals("<hr />\n", renderer.render(document));

        document = parser.parse(link);
        Assertions.assertEquals("<p><a href=\"https://test.com\">link</a></p>\n", renderer.render(document));
    }

    @Test
    public void builderSidebarTest() {
        ArrayList<String> files = new ArrayList<>();
        files.add("./test/file.md");

        Assertions.assertEquals("<div class=\"sidebar\"><ul>test<li><a href=\"./test/file.html\">file</a></li>\n</ul>\n</div>", ParsingUtils.buildSidebar(files));
    }

    @Test
    public void renderListTest() {
        DomNode<String> rootNode = new DomNode<>("root");
        rootNode.addChild("child");

        Assertions.assertEquals("<li><a href=\"./root/child.html\">child</a></li>\n", ParsingUtils.renderList(rootNode));
    }

    @Test
    public void removeExtensionTest() {
        Assertions.assertEquals("test", ParsingUtils.removeExtension("test.html"));
        Assertions.assertEquals("./test", ParsingUtils.removeExtension("./test.md"));
        Assertions.assertEquals("./.hidden/test", ParsingUtils.removeExtension("./.hidden/test.txt"));
    }
}
