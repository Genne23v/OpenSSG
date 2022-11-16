package com.genne23v.openssg;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileUtilitiesTest {

    @Test
    public void getAllFilesTest() throws IOException {
        ArrayList<String> fileNames = FileUtilities.getAllTxtAndMdFiles("src/test/resources");

        Assertions.assertEquals("src/test/resources/file.txt", fileNames.get(0));
        Assertions.assertEquals("src/test/resources/file.md", fileNames.get(1));
    }

    @Test
    @Ignore("Ignored until further development")
    public void createIndexFileTest() throws IOException {
        Options options = new Options("src/test/resources", "src/out");
        ArrayList<String> file = new ArrayList<>();
        file.add("file.txt");

        FileUtilities.createIndexFile(options, file);
        Path path = Paths.get("src/out/index.html");
        Assertions.assertTrue(Files.exists(path, LinkOption.NOFOLLOW_LINKS));
    }

    @Test
    public void trimFilenameTest() {
        String filename1 = "./test.txt";
        String filename2 = "./folder/test.txt";

        Assertions.assertEquals("/test", FileUtilities.trimFilename(filename1));
        Assertions.assertEquals("/folder/test", FileUtilities.trimFilename(filename2));
    }

    @Test
    public void readMdFileTest() throws FileNotFoundException {
        String path = "src/test/resources/file.md";
        File file = new File(path);
        String absolutePath = file.getAbsolutePath();

        Assertions.assertEquals("# h1 Heading\n", FileUtilities.readMdFile(absolutePath));
    }

    @Test
    public void readFileTest() throws FileNotFoundException {
        String path1 = "src/test/resources/file.txt";
        File file1 = new File(path1);
        String absolutePath1 = file1.getAbsolutePath();
        String[] linesFromFile = FileUtilities.readFile(absolutePath1);

        Assertions.assertEquals("Silver Blaze", linesFromFile[0]);

        /*
        String path2 = "src/test/resources/file.txt";
        File file2 = new File(path2);
        String absolutePath2 = file2.getAbsolutePath();

        //Add test to throw
         */
    }
}
