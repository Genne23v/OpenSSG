import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.LinkOption;
import java.util.ArrayList;
import java.util.Scanner;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class FileUtilities {
    static final String MD = ".md";
    static final String TXT = ".txt";
    static final String HTML = ".html";
    static final String DOCTYPE = "<!doctype html>\n";
    static final String META_TAGS = "<head>\n\s\s<meta charset=\"utf-8\">\n\s\s<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n";
    static final String HEADER_CLOSING_TAG =  "\n</head>\n<body>\n";
    static final String MAIN_OPENING_TAG = "<div class=\"main\">";
    static final String BODY_CLOSING_TAGS = "</p>\n</div>\n</body>\n</html>";
    static final String DEFAULT_CSS_LINKS = "\s\s<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/tufte-css/1.8.0/tufte.min.css\">\n\s\s<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/water.css@2/out/water.css\">";

    public static ArrayList<String> getAllTxtAndMdFiles(String inputArg) throws IOException{
        ArrayList<String> fileNames = new ArrayList<>();

        if (!(inputArg.endsWith(TXT) && inputArg.endsWith(MD))){
            Path path = Paths.get(inputArg);
            
            Files.walkFileTree(path, new SimpleFileVisitor<>(){
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs){
                    System.out.println("Found " + file.toString());

                    if (file.toString().endsWith(TXT) || file.toString().endsWith(MD)){
                        fileNames.add(file.toString());
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } else {
            fileNames.add(inputArg);
        }

        return fileNames;
    }

    public static void removeExistingFolder(String folderName) throws IOException{
        Path outPath = Paths.get(folderName);
        Files.walkFileTree(outPath, new SimpleFileVisitor<>(){
           @Override
           public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException{
            System.out.println("Delete file: " + file.toString());
            Files.delete(file);
            return FileVisitResult.CONTINUE;
           } 

           @Override
           public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
            Files.delete(dir);
            System.out.println("Delete dir: " + dir);
            return FileVisitResult.CONTINUE;
           }
        });
    }

    public static void createIndexFile(Options options, ArrayList<String> linkHTMLFiles) throws IOException{
        File indexFile = new File(options.getOutput() + "/index.html");
        FileWriter fileWriter = new FileWriter(indexFile);

        fileWriter.write(DOCTYPE);
        fileWriter.write("<html lang=\"" + options.getLanguage() + "\">\n");
        fileWriter.write((META_TAGS));
        fileWriter.write(DEFAULT_CSS_LINKS);
        fileWriter.write("\n\s\s<title>Index Page</title>");
        fileWriter.write(HEADER_CLOSING_TAG + "\s\s<ul>");

        for (String file : linkHTMLFiles){
            String linkOfFile = file.substring(0, file.lastIndexOf("."));
            String encodedLink = linkOfFile.replaceAll(" ", "%20");
            String titleOfLink = Paths.get(file).getFileName().toString().split("\\.")[0];
            fileWriter.write("\n\s\s\s\s<li><a href=\"" + encodedLink + HTML + "\">" + titleOfLink + "</a></li>");
        }

        fileWriter.write("\n\s\s</ul>\n</body>\n</html>");
        fileWriter.close();

        System.out.println("index.html has been created");
    }

    public static String trimFilename(String filename){
        String fileWithoutExt = filename.substring(0, filename.lastIndexOf("."));
        String trimmedFilename;

        if (fileWithoutExt.indexOf("/") > 0){
            trimmedFilename = fileWithoutExt.substring(fileWithoutExt.indexOf("/"));
        } else {
            trimmedFilename = "/" + fileWithoutExt;
        }
        return trimmedFilename;
    }

    public static void createSubDirectory(String output, String filename) throws IOException{
        Path path = Paths.get(output + "/" + filename);
        String newDirectory = path.getParent().toString();
        Files.createDirectories(Paths.get(newDirectory));
    }

    public static String[] readFile(String file) throws FileNotFoundException{
        int lineNumber=0;
        Scanner scannerToCountLines = new Scanner(new File(file));

        while(scannerToCountLines.hasNextLine()){
            scannerToCountLines.nextLine();
            lineNumber++;
        }
        
        String[] linesFromInputFile = new String[lineNumber];
        Scanner scannerToRead = new Scanner(new File(file));

        int i=0;
        while(scannerToRead.hasNextLine()){
            linesFromInputFile[i] = scannerToRead.nextLine();
            i++;
        }

        return linesFromInputFile;
    }

    private static String readMdFile(String file) throws FileNotFoundException {
        StringBuilder bodyContent = new StringBuilder();
        Scanner scanner = new Scanner(new File(file));

        while(scanner.hasNextLine()){
            bodyContent.append(scanner.nextLine());
            bodyContent.append("\n");
        }

        return bodyContent.toString();
    }

    public void generateHtmlFiles(Options options) throws IOException {

        Path outPath = Paths.get(options.getOutput());
        if (Files.exists(outPath, LinkOption.NOFOLLOW_LINKS)) {
            FileUtilities.removeExistingFolder(options.getOutput());
        }
        Files.createDirectories(outPath);

        ArrayList<String> convertingFiles = FileUtilities.getAllTxtAndMdFiles(options.getInput());
        FileUtilities.parseFilesIntoHtml(convertingFiles, options);
    }

    private static void parseFilesIntoHtml(ArrayList<String> convertingFiles, Options options) throws IOException {
        //CONFIGURE SIDEBAR HERE
        String sideBar = Parser.buildSidebar(convertingFiles);

        for (String file : convertingFiles){
            FileUtilities.createSubDirectory(options.getOutput(), file);

            switch (file.substring(file.lastIndexOf("."))) {
                case ".txt" -> FileUtilities.convertTxtFile(file, options, sideBar);
                case ".md" -> FileUtilities.convertMdFile(file, options, sideBar);
            }
        }

        if (!(options.getInput().endsWith(TXT) || options.getInput().endsWith(MD))){
            createIndexFile(options, convertingFiles);
        }
    }

    public static void convertTxtFile(String file, Options options, String sidebar) throws IOException {
        String newHtmlFilename = options.getOutput() + trimFilename(file) + HTML;
        File htmlFile = new File(newHtmlFilename);
        FileWriter fileWriter = new FileWriter(htmlFile);

        fileWriter.write(DOCTYPE);
        fileWriter.write("<html lang=\"" + options.getLanguage() + "\">\n");
        fileWriter.write(META_TAGS);

        if ( options.getStylesheetLinks() != null ){
            if (!options.getStylesheetLinks().isEmpty()){
                fileWriter.write(ParsingUtils.getCssLinks(options.getStylesheetLinks()));
            }
        }

        String[] linesFromInputFile = readFile(file);
        String[] linesAfterCheckingTitle;
        String title;

        if (ParsingUtils.hasTitle(linesFromInputFile)){
            title = linesFromInputFile[0];
            linesAfterCheckingTitle = new String[linesFromInputFile.length-3];
            System.arraycopy(linesFromInputFile, 3, linesAfterCheckingTitle, 0, linesFromInputFile.length-3);
        } else {
            title = Paths.get(options.getOutput() + file.substring(1)).getFileName().toString();
            linesAfterCheckingTitle = linesFromInputFile;
        }
        fileWriter.write("\s\s<title>" + title + "</title>");
        fileWriter.write(HEADER_CLOSING_TAG);
        fileWriter.write(sidebar);
        fileWriter.write(MAIN_OPENING_TAG);

        if (ParsingUtils.hasTitle(linesFromInputFile)) {
            fileWriter.write("\s\s<h1>" + title + "</h1>");
        }
        fileWriter.write("\n\s\s<p>");

        fileWriter.write(ParsingUtils.parseToHtmlBody(linesAfterCheckingTitle));

        fileWriter.write(BODY_CLOSING_TAGS);
        fileWriter.close();

        System.out.println(newHtmlFilename + " has been created");
    }

    public static void convertMdFile(String file, Options options, String sidebar) throws IOException {
        String newHtmlFilename = options.getOutput() + trimFilename(file) + HTML;
        File htmlFile = new File(newHtmlFilename);
        FileWriter fileWriter = new FileWriter(htmlFile);

        fileWriter.write(DOCTYPE);
        fileWriter.write("<html lang=\"" + options.getLanguage() + "\">\n");
        fileWriter.write(META_TAGS);

        if (options.getStylesheetLinks() != null){
            fileWriter.write(ParsingUtils.getCssLinks(options.getStylesheetLinks()));
        }
        fileWriter.write("\s\s<title>" + Paths.get(file).getFileName().toString() + "</title>");
        fileWriter.write(HEADER_CLOSING_TAG);
        fileWriter.write(sidebar);
        fileWriter.write(MAIN_OPENING_TAG);

        String bodyContent = readMdFile(file);
        String parsedBodyContent = ParsingUtils.parseMarkdownSyntax(bodyContent);

        fileWriter.write(parsedBodyContent);
        fileWriter.write(BODY_CLOSING_TAGS);
        fileWriter.close();

        System.out.println(newHtmlFilename + " has been created");
    }


}

