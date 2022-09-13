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
import java.util.ArrayList;
import java.util.Scanner;

public class FileUtilities {
    
    static final String TXT = ".txt";
    static final String HTML = ".html";
    static final String HEADER_BEFORE_TITLE = "<!doctype html>\n<html lang=\"en\">\n<head>\n\s\s<meta charset=\"utf-8\">\n\s\s<title>";

    public ArrayList<String> getAllTxtFiles(String inputArg) throws IOException{
        ArrayList<String> fileNames = new ArrayList<String>();

        if (!inputArg.endsWith(TXT)){
            Path path = Paths.get(inputArg);
            
            Files.walkFileTree(path, new SimpleFileVisitor<Path>(){
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs){
                    System.out.println(file.toString());
                    fileNames.add(file.toString());
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
        Files.walkFileTree(outPath, new SimpleFileVisitor<Path>(){
           @Override
           public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException{
            System.out.println("Delete file: " + file.toString());
            Files.delete(file);
            return FileVisitResult.CONTINUE;
           } 

           @Override
           public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
            Files.delete(dir);
            System.out.println("Delete dir: " + dir.toString());
            return FileVisitResult.CONTINUE;
           }
        });
    }

    public void generateHTMLFiles(ArrayList<String> txtFiles) throws IOException{
        String headerAfterTitle = "</title>\n\s\s<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n</head>\n<body>";

        for (String file : txtFiles){
            createSubDirectory(OpenSSG.DIST_FOLDER, file);
            
            String newHtmlFilename = OpenSSG.DIST_FOLDER + file.substring(1, file.length()-4) + HTML;
            File htmlFile = new File(newHtmlFilename);
            FileWriter fileWriter = new FileWriter(htmlFile);

            fileWriter.write(HEADER_BEFORE_TITLE);

            String[] linesFromInputFile = readTxtFile(file);
            String[] linesAfterCheckingTitle;
            
            if (hasTitle(linesFromInputFile)){
                String title = linesFromInputFile[2];
                fileWriter.write(title + headerAfterTitle);
                fileWriter.write("\n\s\s<h1>" + title + "</h1>");
                linesAfterCheckingTitle = new String[linesFromInputFile.length-4];
                System.arraycopy(linesFromInputFile, 4, linesAfterCheckingTitle, 0, linesFromInputFile.length-4);
            } else {
                fileWriter.write(Paths.get(OpenSSG.DIST_FOLDER + file.substring(1)).getFileName().toString() + headerAfterTitle);
                linesAfterCheckingTitle = linesFromInputFile;
            }
            fileWriter.write("\n\s\s<p>");
            
            String bodyContent = "";
            for (String line : linesAfterCheckingTitle){
                if (line.isEmpty() && !bodyContent.isEmpty()){
                    bodyContent = bodyContent.substring(0, bodyContent.length()-1);
                    bodyContent += "</p>\n\s\s<p>";
                } else {
                    bodyContent += line + "\n";
                }
            }
            bodyContent = bodyContent.substring(0, bodyContent.length()-1);
            fileWriter.write(bodyContent);
    
            String closingTags = "</p>\n</body>\n</html>";
            fileWriter.write(closingTags);
            fileWriter.close();
        }
    }

    public void generateHTMLFiles(ArrayList<String> input, String output, ArrayList<String> stylesheetLinks) throws IOException{
        String headerAfterTitle = "</title>\n\s\s<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n";

        for (String file : input){
            createSubDirectory(output, file);

            String newHtmlFilename = output + file.substring(1, file.length()-4) + HTML;
            File htmlFile = new File(newHtmlFilename);
            FileWriter fileWriter = new FileWriter(htmlFile);

            fileWriter.write(HEADER_BEFORE_TITLE);

            String[] linesFromInputFile = readTxtFile(file);
            String[] linesAfterCheckingTitle;

            if (hasTitle(linesFromInputFile)){
                String title = linesFromInputFile[2];
                fileWriter.write(title + headerAfterTitle + getCssLinks(stylesheetLinks) + "</head>\n<body>");
                fileWriter.write("\n\s\s<h1>" + title + "</h1>");
                linesAfterCheckingTitle = new String[linesFromInputFile.length-4];
                System.arraycopy(linesFromInputFile, 4, linesAfterCheckingTitle, 0, linesFromInputFile.length-4);
            } else {
                fileWriter.write(Paths.get(output + file.substring(1)).getFileName().toString() + headerAfterTitle + getCssLinks(stylesheetLinks) + "</head>\n<body>");
                linesAfterCheckingTitle = linesFromInputFile;
            }
            fileWriter.write("\n\s\s<p>");
            
            String bodyContent = "";
            for (String line : linesAfterCheckingTitle){
                if (line.isEmpty() && !bodyContent.isEmpty()){
                    bodyContent = bodyContent.substring(0, bodyContent.length()-1);
                    bodyContent += "</p>\n\s\s<p>";
                } else {
                    bodyContent += line + "\n";
                }
            }
            bodyContent = bodyContent.substring(0, bodyContent.length()-1);
            fileWriter.write(bodyContent);
    
            String closingTags = "</p>\n</body>\n</html>";
            fileWriter.write(closingTags);
            fileWriter.close();
        }
    }

    public String getCssLinks(ArrayList<String> stylesheetLinks){
        String cssLinks = ""; 

        for (String link : stylesheetLinks){
            cssLinks += ("\s\s<link rel=\"stylesheet\" href=\"" + link + "\">\n");
        }

        return cssLinks;
    }

    public void createSubDirectory(String output, String filename) throws IOException{
        Path path = Paths.get(output + filename.substring(1));
        String newDirectory = path.getParent().toString();
        Files.createDirectories(Paths.get(newDirectory));
    }

    public String[] readTxtFile(String file) throws FileNotFoundException{
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
        return linesFromFile[0].isEmpty() && linesFromFile[1].isEmpty();
    }
}
