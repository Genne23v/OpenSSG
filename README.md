# OpenSSG
OpenSSG is a static site generator to grep all text files and generate HTML files. Your text file will turn into complete website! You can also specify output folder, and it will generate html files in the same hierarchy from original folder. You can specify CSS files to add too. 


## Usage
Run `OpenSSG -v` or `OpenSSG --version` to check program information

Run `OpenSSG -h` or `OpenSSG --help` to find the usage and how to use options

`OpenSSG -i` or `OpenSSG --input <FILE OR FOLDER>` will generate `.html` file in `./dist` folder. If you specify a foldername, it will generate matching `.html` files in the same hierarchy to original folder. 

`OpenSSG -o` or `OpenSSG --output <FOLDER NAME>` will override default folder and create a new folder to generate HTML files. 

`OpenSSG -s` or `OpenSSG --stylesheet <CSS LINKS>` will add CSS files to each HTML file's head. 


## Release
| Version | 0.2.0         |
|---------|---------------|
| Date    | Sep, 13, 2022 |


## License
Eclipse Public License 2.0
Refer to the [License Document](https://github.com/Genne23v/wk-ssg/blob/master/LICENSE)
