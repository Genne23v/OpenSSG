# OpenSSG
OpenSSG is a static site generator to grep all text files and generate HTML files. Your text file will turn into complete website! You can also specify output folder, and it will generate html files in the same hierarchy from original folder. You can specify CSS files to add too. 

## Installation
JDK must be installed in your computer. [JDK Download](https://www.oracle.com/java/technologies/downloads/)

Compile the program by running `javac OpenSSG.java FileUtilities.java Release.java` in command line. Then it's ready for you to use! 

## Usage
Run `java OpenSSG -v` or `java OpenSSG --version` to check program information

Run `java OpenSSG -h` or `java OpenSSG --help` to find the usage and how to use options

`java OpenSSG -i <FILE OR FOLDER>` or `java OpenSSG --input <FILE OR FOLDER>` will generate `.html` file in `./dist` folder. If you specify a foldername, it will generate matching `.html` files in the same hierarchy to original folder. 

`java OpenSSG -o <FOLDER NAME>` or `java OpenSSG --output <FOLDER NAME>` will override default folder and create a new folder to generate HTML files. 

`java OpenSSG -s <CSS LINKS>` or `java OpenSSG --stylesheet <CSS LINKS>` will add CSS files to each HTML file's head. 


## Release
| Version | 0.1.0         |
|---------|---------------|
| Date    | Sep, 11, 2022 |


## License
Eclipse Public License 2.0
Refer to the [License Document](https://github.com/Genne23v/wk-ssg/blob/master/LICENSE)
