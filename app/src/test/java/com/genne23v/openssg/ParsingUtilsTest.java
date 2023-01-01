package com.genne23v.openssg;

import org.commonmark.internal.util.Parsing;
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
        String expected = "\s\s<link rel=\"stylesheet\" href=\"style\">\n";

        Assertions.assertEquals(expected, ParsingUtils.getCssLinks(stylesheets));

        stylesheets.add("style1");
        expected += "\s\s<link rel=\"stylesheet\" href=\"style1\">\n";

        Assertions.assertEquals(expected, ParsingUtils.getCssLinks(stylesheets));

        stylesheets.clear();

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
        String[] stringArray3 = new String[]{"line1"};

        Assertions.assertEquals("line1\nline2", ParsingUtils.parseToHtmlBody(stringArray1));
        Assertions.assertEquals("line1</p>\n\s\s<p>line2", ParsingUtils.parseToHtmlBody(stringArray2));
        Assertions.assertEquals("line1", ParsingUtils.parseToHtmlBody(stringArray3));
    }

    @Test
    public void parseMarkdownTest() {
        String heading = "# h1 heading";
        String strong = "I am *strong*";
        String hr = "---";
        String link = "[link](https://test.com)";

        Assertions.assertEquals("<h1>h1 heading</h1>\n", ParsingUtils.parseMarkdownSyntax(heading));

        Assertions.assertEquals("<p>I am <em>strong</em></p>\n", ParsingUtils.parseMarkdownSyntax(strong));

        Assertions.assertEquals("<hr />\n", ParsingUtils.parseMarkdownSyntax(hr));

        Assertions.assertEquals("<p><a href=\"https://test.com\">link</a></p>\n", ParsingUtils.parseMarkdownSyntax(link));
    }

    @Test
    public void builderSidebarTest() {
        ArrayList<String> files = new ArrayList<>();
        files.add("./test/file.md");
        String expected = "<div class=\"sidebar\"><ul>test<li><a href=\"./test/file.html\">file</a></li>\n</ul>\n</div>";

        Assertions.assertEquals(expected, ParsingUtils.buildSidebar(files));

        files.add("./test/file1.md");
        expected = "<div class=\"sidebar\"><ul>test<li><a href=\"./test/file.html\">file</a></li>\n" +
                "<li><a href=\"./test/file1.html\">file1</a></li>\n" +
                "</ul>\n" +
                "</div>";

        Assertions.assertEquals(expected, ParsingUtils.buildSidebar(files));

        files.clear();

        Assertions.assertEquals("<div class=\"sidebar\"><ul></ul>\n</div>", ParsingUtils.buildSidebar(files));
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
