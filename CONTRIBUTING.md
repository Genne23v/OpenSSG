## Project Setup

1. JDK must be installed in your computer [JDK Download](https://www.oracle.com/java/technologies/downloads/). The
   current JDK version is `18.0.2.1`. <br />
2. Add dependencies
    - Search Maven modules `com.googlecode.json-simple.json:1.1.1`, `org.commonmark:commonmark:0.17.1`
      in `Project Structure` and add them to dependency module list.
3. Project Build
    - Using *IntelliJ* or other IDE: Open the root of the project folder. Build the project and run with available
      option argument

If you are using terminal, add dependency files to `/lib` folder. Then, go to `/src` folder and compile the program by
running `javac -cp ./libs/* OpenSSG.java FileUtilities.java Options.java ParsingUtils.java Release.java DOMNode.java` in
command line.

## Before Submitting a PR

Please add `google-java-format` and `spotbugs` plugin as per below instruction. I will request to update the PR if there
is any suggestion that I find during PR review. Also, run the unit test by running `gradle test` in terminal. Thank you!

## Code Formatting and Linting

This project uses [google-java-format plugin](https://github.com/google/google-java-format) for consistent code
formatting. Also, [SpotBugs](https://spotbugs.github.io/) plugin is necessary to lint the code. You can
download `google-java-format` jar file to run in your terminal, but IDE plugin is recommended for your convenience. You
can refer to [SpotBugs Bug Description](https://spotbugs.readthedocs.io/en/stable/bugDescriptions.html) to fix as per
SpotBugs suggestion.

### Using IntelliJ Plugin

Go to `File→Settings...→google-java-format Settings` (
or `IntelliJ IDEA→Preferences...→Other Settings→google-java-format Settings` on macOS) and check
the `Enable google-java-format` checkbox.

To format the code, run `Reformat Code` in `Code` menu.

### Using google-java-format jar file

Download `jar` file [HERE](https://github.com/google/google-java-format/releases). You can run below command to format
your code.
`java -jar /path/to/google-java-format-${GJF_VERSION?}-all-deps.jar <options> [files...]`
You can find more details on [google-java-format repository](https://github.com/google/google-java-format)

  