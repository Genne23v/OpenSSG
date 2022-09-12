import java.lang.String;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WKStaticSiteGenerator {
    public static void main(String[] args) throws IOException{
        if (args.length > 0){
            switch(args[0]){
                case "--version":
                case "-v":
                    System.out.println("wk-SSG version x.x.x, date of last version");
                    break;
                case "--help":
                case "-h":
                    System.out.println("standard help/usage message showing how to run the tool, which command line flags and arguments can be used, etc.");
                    break;
                case "--input":
                case "-i":
                    createHTMLFile(args[1]);
                    break;
                case "--output":
                case "-o":
                    createHTMLFile(args[1], args[2]);
                    break;
                case "--sytlesheet":
                case "-s":
                //place css file in the head
            }
        }
    }

    public static void createHTMLFile(String txtFile) throws IOException{
        //remove /dist folder if exists
        String fileName = txtFile.split("\\.")[0];
        String pathName = "./dist/" + fileName + ".html";

        Path outPath = Paths.get("./dist");
        File htmlFile = new File(pathName);

        if (Files.exists(outPath, new LinkOption[]{LinkOption.NOFOLLOW_LINKS})){
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

        //create /dist folder
        Files.createDirectories(outPath);

        //set up a header 
        String header = "<!doctype html>\n<html lang=\"en\">\n<head>\n\s\s<meta charset=\"utf-8\">\n\s\s<title>Filename</title>\n\s\s<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n</head>\n<body>\n\s\s<p>";

        //count the line number to initialize string array, and read txt file
        int lineNumber=0;
        Scanner fileScannerToCount = new Scanner(new File(txtFile));
        while(fileScannerToCount.hasNextLine()){
            fileScannerToCount.nextLine();
            lineNumber++;
        }

        String[] linesFromInputFile = new String[lineNumber];
        Scanner fileScannerToRead = new Scanner(new File(txtFile));
        int i=0;
        while(fileScannerToRead.hasNextLine()){
            linesFromInputFile[i] = fileScannerToRead.nextLine();
            i++;
        }

        //add it to html file
        FileWriter fileWriter = new FileWriter(htmlFile);
        fileWriter.write(header);

        String bodyContent = "";
        for (String line : linesFromInputFile){
            if (line.isEmpty()){
                // fileWriter.write("</p>\n\s\s<p>");
                bodyContent = bodyContent.substring(0, bodyContent.length()-1);
                bodyContent += "</p>\n\s\s<p>";
            } else {
                // fileWriter.write(line + "\n");   
                bodyContent += line + "\n";
            }
        }
        bodyContent = bodyContent.substring(0, bodyContent.length()-1);
        fileWriter.write(bodyContent);

        //add closing tags
        String closingTags = "</p>\n</body>\n</html>";
        fileWriter.write(closingTags);
        fileWriter.close();
    };

    public static void createHTMLFile(String txtFile, String overridenFileLocation){

    }
}
