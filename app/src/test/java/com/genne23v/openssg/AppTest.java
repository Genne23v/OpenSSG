/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.genne23v.openssg;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.*;

public class AppTest {
    @Test
    public void mainTest() throws IOException {
        String errMessage = "Cannot process other option or argument. Check the usage by running java OpenSSG -h or --help.\n";
        ByteArrayOutputStream outContent1 = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outContent1));
        String[] args = {"-v", "-h"};
        App.main(args);

        Assertions.assertEquals(errMessage, outContent1.toString());

        ByteArrayOutputStream outContent2 = new ByteArrayOutputStream();
        String[] args2 = {"--version"};
        App.main(args2);
        Assertions.assertEquals("", outContent2.toString());

        /* Implement file exception error handling
        ByteArrayOutputStream outContent3 = new ByteArrayOutputStream();
        String[] args3 = {"--config", "src/test/resources/notExistConfig.json"};
        App.main(args3);
        Assertions.assertEquals("", outContent3.toString());
         */
    }

    @Test
    public void createConfigOptionTest() throws FileNotFoundException, UnsupportedEncodingException {
        Options options;
        String configFile = "src/test/resources/config.json";

        options = App.createOptionFromFile(configFile);

        Assertions.assertEquals("./Sherlock Homes", options.getInput());
        Assertions.assertEquals("tmp", options.getOutput());
        Assertions.assertEquals("fr", options.getLanguage());
    }

    @Test
    public void createOptionsTest() {
        String[] args = {"-i", "input", "-o", "output", "-l", "fr-CA"};
        Options options = App.createOptions(args);

        Assertions.assertEquals("input", options.getInput());
        Assertions.assertEquals("output", options.getOutput());
        Assertions.assertEquals("fr-CA", options.getLanguage());
    }

    @Test
    public void argValidityTest() {
        String[] args1 = {"--input", "./test", "--output", "tmp", "--stylesheet", "style1", "style2", "--lang", "fr-CA"};
        String[] args2 = {"-l", "ja-JP", "-s", "style", "-o", "tmp", "-i", "./test.txt"};
        String[] args3 = {"-input", "./test"};
        String[] args4 = {"-i", "-o", "tmp"};

        Assertions.assertTrue(App.areArgsValid(args1));
        Assertions.assertTrue(App.areArgsValid(args2));
        Assertions.assertFalse(App.areArgsValid(args3));

        String errMessage = "Missing option argument. Check out the usage by running java OpenSSG -h or --help.\n";
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outContent));

        Assertions.assertFalse(App.areArgsValid(args4));
        Assertions.assertEquals(errMessage, outContent.toString());
    }
}
