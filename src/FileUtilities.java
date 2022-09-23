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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtilities {
    static final String MD = ".md";
    static final String TXT = ".txt";
    static final String HTML = ".html";
    static final String DIST_FOLDER = "./dist";
    static final String HEADER_BEFORE_TITLE = "<!doctype html>\n<html lang=\"en\">\n<head>\n\s\s<meta charset=\"utf-8\">\n\s\s<title>";
    static final String HEADER_AFTER_TITLE = "</title>\n\s\s<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n</head>\n<body>";
    static final String HEADER_AFTER_TITLE_WITHOUT_CLOSING_TAG =  "</title>\n\s\s<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n";
    static final String BODY_CLOSING_TAGS = "</p>\n</body>\n</html>";
    static final String CSS_LINKS = "\s\s<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/tufte-css/1.8.0/tufte.min.css\">\n\s\s<link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/water.css@2/out/water.css\">";

    public ArrayList<String> getAllTxtAndMdFiles(String inputArg) throws IOException{
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

    public void removeExistingFolder(String folderName) throws IOException{
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

    public void generateHTMLFiles(ArrayList<String> txtFiles) throws IOException{
        
        for (String file : txtFiles){
            createSubDirectory(DIST_FOLDER, file);
            FileUtilities fileUtilities = new FileUtilities();

            switch (file.substring(file.lastIndexOf("."))) {
                case ".txt" -> fileUtilities.convertTxtFile(file);
                case ".md" -> fileUtilities.convertMdFile(file);
            }
        }
    }

    public void generateHTMLFiles(ArrayList<String> convertingFiles, String output) throws IOException{
        
        for (String file : convertingFiles){
            createSubDirectory(output, file);
            FileUtilities fileUtilities = new FileUtilities();

            switch (file.substring(file.lastIndexOf("."))) {
                case ".txt" -> fileUtilities.convertTxtFile(file, output);
                case ".md" -> fileUtilities.convertMdFile(file, output);
            }
        }
        createIndexFile(output, convertingFiles);
    }

    public void generateHTMLFiles(ArrayList<String> input, String output, ArrayList<String> stylesheetLinks) throws IOException{
        
        for (String file : input){
            createSubDirectory(output, file);
            FileUtilities fileUtilities = new FileUtilities();

            switch (file.substring(file.lastIndexOf("."))) {
                case ".txt" -> fileUtilities.convertTxtFile(file, output, stylesheetLinks);
                case ".md" -> fileUtilities.convertMdFile(file, output, stylesheetLinks);
            }
        }
        createIndexFile(output, input);
    }

    public void createIndexFile(String output, ArrayList<String> convertingFiles) throws IOException{
        File indexFile = new File(output + "/index.html");
        FileWriter fileWriter = new FileWriter(indexFile);

        fileWriter.write(HEADER_BEFORE_TITLE + "Index Page" + HEADER_AFTER_TITLE_WITHOUT_CLOSING_TAG + CSS_LINKS + "\n</head>\n<body>\n\s\s<ul>");

        for (String file : convertingFiles){
            String linkOfFile = file.substring(0, file.lastIndexOf("."));
            String encodedLink = linkOfFile.replaceAll(" ", "%20");
            String titleOfLink = Paths.get(file).getFileName().toString().split("\\.")[0];
            fileWriter.write("\n\s\s\s\s<li><a href=\"" + encodedLink + HTML + "\">" + titleOfLink + "</a></li>");
        }

        fileWriter.write("\n\s\s</ul>\n</body>\n</html>");
        fileWriter.close();

        System.out.println("index.html has been created");
    }

    public String trimFilename(String filename){
        return "/" + filename.replace("./", "").substring(0, filename.lastIndexOf("."));
    }

    public String getCssLinks(ArrayList<String> stylesheetLinks){
        StringBuilder cssLinks = new StringBuilder();

        for (String link : stylesheetLinks){
            cssLinks.append("\s\s<link rel=\"stylesheet\" href=\"").append(link).append("\">\n");
        }
        return cssLinks.toString();
    }

    public void createSubDirectory(String output, String filename) throws IOException{
        Path path = Paths.get(output + "/" + filename);
        String newDirectory = path.getParent().toString();
        Files.createDirectories(Paths.get(newDirectory));
    }

    public String[] readFile(String file) throws FileNotFoundException{
        int lineNumber=0;
        Scanner fileScannerToCount = new Scanner(new File(file));

        while(fileScannerToCount.hasNextLine()){
            fileScannerToCount.nextLine();
            lineNumber++;
        }
        
        String[] linesFromInputFile = new String[lineNumber];
        Scanner fileScannerToRead = new Scanner(new File(file));

        int i=0;
        while(fileScannerToRead.hasNextLine()){
            linesFromInputFile[i] = fileScannerToRead.nextLine();
            i++;
        }

        return linesFromInputFile;
    }

    public boolean hasTitle(String[] linesFromFile){
        return linesFromFile[1].isEmpty() && linesFromFile[2].isEmpty();
    }

    public void createHTMLFiles(String inputArg) throws IOException {
        FileUtilities fileUtilities = new FileUtilities();

        Path outPath = Paths.get(DIST_FOLDER);
        if (Files.exists(outPath, LinkOption.NOFOLLOW_LINKS)) {
            fileUtilities.removeExistingFolder(DIST_FOLDER);
        }

        Files.createDirectories(outPath);

        ArrayList<String> convertingFiles = fileUtilities.getAllTxtAndMdFiles(inputArg);
        fileUtilities.generateHTMLFiles(convertingFiles);
    }

    public void createHTMLFiles(String input, String output) throws IOException {
        FileUtilities fileUtilities = new FileUtilities();

        Path outPath = Paths.get(output);
        if (Files.exists(outPath, LinkOption.NOFOLLOW_LINKS)) {
            fileUtilities.removeExistingFolder(output);
        }

        Files.createDirectories(outPath);

        ArrayList<String> convertingFiles = fileUtilities.getAllTxtAndMdFiles(input);
        fileUtilities.generateHTMLFiles(convertingFiles, output);
    }

    public void createHTMLFiles(String input, String output, ArrayList<String> stylesheetLinks)
            throws IOException {
        FileUtilities fileUtilities = new FileUtilities();

        Path outPath = Paths.get(output);
        if (Files.exists(outPath, LinkOption.NOFOLLOW_LINKS)) {
            fileUtilities.removeExistingFolder(output);
        }

        Files.createDirectories(outPath);

        ArrayList<String> convertingFiles = fileUtilities.getAllTxtAndMdFiles(input);
        fileUtilities.generateHTMLFiles(convertingFiles, output, stylesheetLinks);
    }

    public void convertTxtFile(String file) throws IOException {
        String newHtmlFilename = DIST_FOLDER + trimFilename(file) + HTML;
        File htmlFile = new File(newHtmlFilename);
        FileWriter fileWriter = new FileWriter(htmlFile);

        fileWriter.write(HEADER_BEFORE_TITLE);

        String[] linesFromInputFile = readFile(file);
        String[] linesAfterCheckingTitle;

        if (hasTitle(linesFromInputFile)){
            String title = linesFromInputFile[0];
            fileWriter.write(title + HEADER_AFTER_TITLE);
            fileWriter.write("\n\s\s<h1>" + title + "</h1>");
            linesAfterCheckingTitle = new String[linesFromInputFile.length-3];
            System.arraycopy(linesFromInputFile, 3, linesAfterCheckingTitle, 0, linesFromInputFile.length-3);
        } else {
            fileWriter.write(Paths.get(file).getFileName().toString() + HEADER_AFTER_TITLE);
            linesAfterCheckingTitle = linesFromInputFile;
        }
        fileWriter.write("\n\s\s<p>");

        StringBuilder bodyContent = new StringBuilder();
        for (String line : linesAfterCheckingTitle){
            if (line.isEmpty() && (bodyContent.length() > 0)){
                bodyContent = new StringBuilder(bodyContent.substring(0, bodyContent.length() - 1));
                bodyContent.append("</p>\n\s\s<p>");
            } else {
                bodyContent.append(line).append("\n");
            }
        }
        bodyContent = new StringBuilder(bodyContent.substring(0, bodyContent.length() - 1));
        fileWriter.write(bodyContent.toString());

        fileWriter.write(BODY_CLOSING_TAGS);
        fileWriter.close();

        System.out.println(newHtmlFilename + " has been created");
    }

    public void convertTxtFile(String file, String output) throws IOException {
        String newHtmlFilename = output + trimFilename(file) + HTML;
        File htmlFile = new File(newHtmlFilename);
        FileWriter fileWriter = new FileWriter(htmlFile);

        fileWriter.write(HEADER_BEFORE_TITLE);

        String[] linesFromInputFile = readFile(file);
        String[] linesAfterCheckingTitle;

        if (hasTitle(linesFromInputFile)){
            String title = linesFromInputFile[0];
            fileWriter.write(title);
            fileWriter.write(HEADER_AFTER_TITLE_WITHOUT_CLOSING_TAG + "</head>\n<body>");
            fileWriter.write("\n\s\s<h1>" + title + "</h1>");
            linesAfterCheckingTitle = new String[linesFromInputFile.length-3];
            System.arraycopy(linesFromInputFile, 3, linesAfterCheckingTitle, 0, linesFromInputFile.length-3);
        } else {
            fileWriter.write(Paths.get(output + file.substring(1)).getFileName().toString());
            fileWriter.write(HEADER_AFTER_TITLE_WITHOUT_CLOSING_TAG + "</head>\n<body>");
            linesAfterCheckingTitle = linesFromInputFile;
        }
        fileWriter.write("\n\s\s<p>");

        StringBuilder bodyContent = new StringBuilder();
        for (String line : linesAfterCheckingTitle){
            if (line.isEmpty() && (bodyContent.length() > 0)){
                bodyContent = new StringBuilder(bodyContent.substring(0, bodyContent.length() - 1));
                bodyContent.append("</p>\n\s\s<p>");
            } else {
                bodyContent.append(line).append("\n");
            }
        }
        bodyContent = new StringBuilder(bodyContent.substring(0, bodyContent.length() - 1));
        fileWriter.write(bodyContent.toString());

        fileWriter.write(BODY_CLOSING_TAGS);
        fileWriter.close();

        System.out.println(newHtmlFilename + " has been created");
    }

    public void convertTxtFile(String file, String output, ArrayList<String> stylesheetLinks) throws IOException {
        String newHtmlFilename = output + trimFilename(file) + HTML;
        File htmlFile = new File(newHtmlFilename);
        FileWriter fileWriter = new FileWriter(htmlFile);

        fileWriter.write(HEADER_BEFORE_TITLE);

        String[] linesFromInputFile = readFile(file);
        String[] linesAfterCheckingTitle;

        if (hasTitle(linesFromInputFile)){
            String title = linesFromInputFile[0];
            fileWriter.write(title + HEADER_AFTER_TITLE_WITHOUT_CLOSING_TAG);
            fileWriter.write(getCssLinks(stylesheetLinks) + "</head>\n<body>");
            fileWriter.write("\n\s\s<h1>" + title + "</h1>");
            linesAfterCheckingTitle = new String[linesFromInputFile.length-3];
            System.arraycopy(linesFromInputFile, 3, linesAfterCheckingTitle, 0, linesFromInputFile.length-3);
        } else {
            fileWriter.write(Paths.get(output + file.substring(1)).getFileName().toString() + HEADER_AFTER_TITLE_WITHOUT_CLOSING_TAG);
            fileWriter.write(getCssLinks(stylesheetLinks) + "</head>\n<body>");
            linesAfterCheckingTitle = linesFromInputFile;
        }
        fileWriter.write("\n\s\s<p>");

        StringBuilder bodyContent = new StringBuilder();
        for (String line : linesAfterCheckingTitle){
            if (line.isEmpty() && (bodyContent.length() > 0)){
                bodyContent = new StringBuilder(bodyContent.substring(0, bodyContent.length() - 1));
                bodyContent.append("</p>\n\s\s<p>");
            } else {
                bodyContent.append(line).append("\n");
            }
        }
        bodyContent = new StringBuilder(bodyContent.substring(0, bodyContent.length() - 1));
        fileWriter.write(bodyContent.toString());

        fileWriter.write(BODY_CLOSING_TAGS);
        fileWriter.close();

        System.out.println(newHtmlFilename + " has been created");
    }

    public void convertMdFile(String file) throws IOException {
        String newHtmlFilename = DIST_FOLDER + trimFilename(file) + HTML;
        File htmlFile = new File(newHtmlFilename);
        FileWriter fileWriter = new FileWriter(htmlFile);

        fileWriter.write(HEADER_BEFORE_TITLE);
        fileWriter.write(Paths.get(file).getFileName().toString() + HEADER_AFTER_TITLE + "\n");

        String[] linesFromInputFile = readFile(file);

        String bodyContent = parseMarkdownSyntax(linesFromInputFile);

        fileWriter.write(bodyContent);
        fileWriter.write(BODY_CLOSING_TAGS);
        fileWriter.close();

        System.out.println(newHtmlFilename + " has been created");
    }

    public void convertMdFile(String file, String output) throws IOException {
        String newHtmlFilename = output + trimFilename(file) + HTML;
        File htmlFile = new File(newHtmlFilename);
        FileWriter fileWriter = new FileWriter(htmlFile);

        fileWriter.write(HEADER_BEFORE_TITLE);
        fileWriter.write(Paths.get(file).getFileName().toString() + HEADER_AFTER_TITLE_WITHOUT_CLOSING_TAG + "</head>\n<body>");

        String[] linesFromInputFile = readFile(file);

        String bodyContent = parseMarkdownSyntax(linesFromInputFile);

        fileWriter.write(bodyContent);
        fileWriter.write(BODY_CLOSING_TAGS);
        fileWriter.close();

        System.out.println(newHtmlFilename + " has been created");
    }

    public void convertMdFile(String file, String output, ArrayList<String> stylesheetLinks) throws IOException {
        String newHtmlFilename = output + trimFilename(file) + HTML;
        File htmlFile = new File(newHtmlFilename);
        FileWriter fileWriter = new FileWriter(htmlFile);

        fileWriter.write(HEADER_BEFORE_TITLE);
        fileWriter.write(Paths.get(file).getFileName().toString() + HEADER_AFTER_TITLE_WITHOUT_CLOSING_TAG + getCssLinks(stylesheetLinks) + "</head>\n<body>\n");

        String[] linesFromInputFile = readFile(file);

        String bodyContent = parseMarkdownSyntax(linesFromInputFile);

        fileWriter.write(bodyContent);
        fileWriter.write(BODY_CLOSING_TAGS);
        fileWriter.close();

        System.out.println(newHtmlFilename + " has been created");
    }

    public String parseMarkdownSyntax(String[] linesFromInputFile){
        StringBuilder bodyContent = new StringBuilder();
        boolean wasThereEmptyLine = false;
        boolean isClosed = true;
        Pattern mdLinkPattern = Pattern.compile("\\[([^]]+)]\\(([^)]+)\\)");
        Matcher matcher;

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

                matcher = mdLinkPattern.matcher(line);
                if (matcher.find()) {
                    bodyContent.append(line.replaceAll(String.valueOf(mdLinkPattern), "<a href=\"$2\">$1</a>"));
                } else {
                    bodyContent.append(line);
                }
            }
        }
        return bodyContent.toString();
    }
}

