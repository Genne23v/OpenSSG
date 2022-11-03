# OpenSSG
OpenSSG is a static site generator to grep all text and markdown files in a designated folder, and generate matching HTML files in a separate folder. 

And index.html file will be created to link all your generated html files. Each page also will have the auto-configured sidebar to navigate pages easily. Your text file will turn into complete website! You can also specify output folder, and it will generate html files in the same hierarchy from original folder. You can specify CSS files to add too. 

## Installation
1. JDK must be installed in your computer [JDK Download](https://www.oracle.com/java/technologies/downloads/). The current JDK version is `18.0.2.1`. <br />
2. Add Maven dependency modules `com.googlecode.json-simple.json:1.1.1`, `org.commonmark:commonmark:0.17.1`
3. Build and run in IDE

If you want to run in terminal, create `/lib` folder and add dependency jar files to `/lib` folder. 
Then, go to `/src` folder and compile the program by running `javac -cp ./libs/* OpenSSG.java FileUtilities.java Options.java ParsingUtils.java Release.java DOMNode.java` in command line.  

## Usage
Run `java OpenSSG -v` or `java OpenSSG --version` to check program information

Run `java OpenSSG -h` or `java OpenSSG --help` to find the usage and how to use options

`java OpenSSG -i <FILE OR FOLDER>` or `java OpenSSG --input <FILE OR FOLDER>` will generate `.html` file in `./dist` folder. If you specify a foldername, it will find all txt files and generate matching `.html` files in the same hierarchy to original folder. Quotes should be surrounded at the beginning and end of the file or folder name to avoid argument error.

`java OpenSSG -o <FOLDER NAME>` or `java OpenSSG --output <FOLDER NAME>` will override default folder and create a new folder to generate HTML files. If you want to add spaces in folder name, folder name should be surrounded by quotation marks. 

`java OpenSSG -s <CSS LINK1> <CSS LINK2>...` or `java OpenSSG --stylesheet <CSS LINK1> <CSS LINK2>...` will add CSS files to each HTML file's head. Multiple CSS file links can be added to the command.

`java OpenSSG -l <LANGUAGE>` or `java OpenSSG --lang <LANGUAGE>` will update the default `<html lang="en">` to specified lang option.

## Config File
You can specify a `-c` or `--config` flag with a JSON file passed as an argument like `-c test.json` and it will search the JSON file for appropriate values such as stylesheets array or lang flag to change the nature of the program's operation. An example valid JSON would look like so:

```
{
    "input": "Silver Blaze.txt",
    "lang": "fr",
    "stylesheets": ["https://cdnjs.cloudflare.com/ajax/libs/tufte-css/1.8.0/tufte.min.css",
    "https://cdn.jsdelivr.net/npm/water.css@2/out/water.css"],
    "This":"Blah",
    "output": "NewWorkingHTML"
}
```

In this example multiple styles will be taken out of stylesheets and used, also "This" will be ignored.

Note that keys in the json are case sensitive. `input` `lang` `stylesheets` and `output` will work.

## Generated Sample Site 
https://genne23v.github.io/sherlock-homes-selected-stories/

## Release
| Version | Date           |
|---------|----------------|
| 0.4.0   | Oct, 26, 2022  |
| 0.3.0   | Oct, 7, 2022   |
| 0.2.0   | Sep, 22, 2022  |
| 0.1.2   | Sep, 18, 2022  |
| 0.1.1   | Sep, 16, 2022  |
| 0.1.0   | Sep, 11, 2022  |



## License
Eclipse Public License 2.0
Refer to the [License Document](https://github.com/Genne23v/wk-ssg/blob/master/LICENSE)
