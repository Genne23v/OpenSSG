package com.genne23v.openssg;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;

public class OptionsTest {

    @Test
    public void fullOptionBuildTest() {
        ArrayList<String> stylesheets = new ArrayList<>();
        stylesheets.add("style1");
        stylesheets.add("style2");

        Options options = new Options("input", "output", stylesheets, "en-CA");

        Assertions.assertEquals("input", options.getInput());
        Assertions.assertEquals("output", options.getOutput());
        Assertions.assertEquals("style1", options.getStylesheetLinks().get(0));
        Assertions.assertEquals("en-CAA", options.getLanguage());
    }

    @Test
    public void optionsWithoutLangTest() {
        ArrayList<String> stylesheets = new ArrayList<>();
        stylesheets.add("style1");
        stylesheets.add("style2");

        Options options = new Options("input", "output", stylesheets);
        options.setOutput("new output");

        Assertions.assertEquals("input", options.getInput());
        Assertions.assertNotEquals("output", options.getOutput());
        Assertions.assertEquals("new output", options.getOutput());
        Assertions.assertEquals("style2", options.getStylesheetLinks().get(1));
        Assertions.assertEquals("en", options.getLanguage());
    }

    @Test
    public void optionsWithInputOutputTest() {
        Options options = new Options("input", "output");

        Assertions.assertTrue(options.getStylesheetLinks().isEmpty());

        ArrayList<String> stylesheet = new ArrayList<String>();
        stylesheet.add("style1");
        options.setStylesheetLinks(stylesheet);

        Assertions.assertFalse(options.getStylesheetLinks().isEmpty());
        Assertions.assertEquals(1, options.getStylesheetLinks().size());
    }
}
