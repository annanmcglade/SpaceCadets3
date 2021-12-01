import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;

public class BBMain {
  static ArrayList<BBVariable> variables; //contains all variables used
  static ArrayList<Integer> indentStack; //keeps track of where while loops start on a stack
  static int currentLine;
  static String searchingForEnd;

  public static String getSearchingForEnd() {
    return searchingForEnd;
  }

  public static void setSearchingForEnd(String value) {
    searchingForEnd = value;
  }

  public static int getCurrentLine() {
    return currentLine;
  }

  public static int getCurrentIndent() {
    if (indentStack.size() == 0) {
      return -1;
    }
    return indentStack.remove(indentStack.size() - 1);
  }

  public static void addIndent() {
    indentStack.add(currentLine);
  }

  public static BBVariable getVariable(String variableName) {
    for (BBVariable v : variables) {
      if (Objects.equals(v.getVariableName(), variableName.toUpperCase(Locale.ROOT))) {
        return v;
      }
    }
    return null;
  }

  public static void addVariable(String variableName) {
    variables.add(new BBVariable(variableName.toUpperCase(Locale.ROOT)));
  }

  private static String stripComments(String line) {
    if (line.contains("#")) {
      line = line.substring(0, line.indexOf("#") - 1);
    }
    return line;
  }

  private static String[] readFile() {
    StringBuilder code = new StringBuilder(); // String for all code to be read into
    try {
      File Script = new File("src\\BBcode.txt");
      Scanner myReader = new Scanner(Script);
      while (myReader.hasNextLine()) {
        code.append(myReader.nextLine());
      }
      myReader.close();
    } catch (FileNotFoundException e) {
      System.err.println("An error occurred.");
      e.printStackTrace();
    }
    return code.toString().split(";"); //splits code into its lines using the character ;
  }

  public static void main(String[] args) {

    String[] lines = readFile();
    variables = new ArrayList<BBVariable>();
    indentStack = new ArrayList<Integer>();
    searchingForEnd = "";
    int newIndex = 0; // variable that updates program counter
    int indentTracker = 0; // int that tracks in how many indents there are when the program is searching for the end of a loop or if statement
    int indexShift = 0; //updates newIndex
    for (int programCounter = 0; programCounter < lines.length; programCounter = newIndex) {
      currentLine = programCounter;
      String line = lines[programCounter];
      line = stripComments(line);
      line = line.trim(); // get rid of white space
      String[] keyWords = line.split(" "); // split statement into words
      if (!Objects.equals(searchingForEnd, "")) { //doesn't run main code and only goes through code line by line until an end statement is found
        switch (searchingForEnd) {
          case "LOOP":
            if (Objects.equals(keyWords[0].toUpperCase(), "END") && keyWords.length == 1) {
              if (indentTracker != 0) { //the end statement found is not for this IF
                indentTracker--;
              } else {
                searchingForEnd = "";
                indexShift = 1;
              }
            } else if (Objects.equals(keyWords[0].toUpperCase(), "WHILE")) {
              indentTracker++; //add another indent
            }
            break;
          case "IF":
            if (Objects.equals(keyWords[0].toUpperCase(), "ELSE") && indentTracker == 0) {
              searchingForEnd = "";
              try {
                indexShift = BBConditional.execute(keyWords); // else or else if found
              } catch (Exception e) {
                System.err.println("ERROR" + currentLine);
              }
            }
            if (Objects.equals(keyWords[0].toUpperCase(), "END")
                && Objects.equals(keyWords[1].toUpperCase(), "IF")) {
              if (indentTracker != 0) {
                indentTracker--;
              } else {
                searchingForEnd = "";
                indexShift = 1;
              }
            } else if (Objects.equals(keyWords[0].toUpperCase(), "IF")) {
              indentTracker++;
            }
            break;
          case "END":
            if (Objects.equals(keyWords[0].toUpperCase(), "END")
                && keyWords.length == 2) {
              if (indentTracker != 0) {
                indentTracker--;
              } else {
                searchingForEnd = "";
                indexShift = 1;
              }
            } else if (Objects.equals(keyWords[0].toUpperCase(), "IF")) {
              indentTracker++;
            }

            break;
        }

      } else if (keyWords.length != 0) {
        switch (keyWords[0].toUpperCase(Locale.ROOT)) {
          case "PRINT":
            indexShift = BBAction.execute(keyWords, "PRINT");
            break;
          case "CLEAR":
            indexShift = BBAction.execute(keyWords, "CLEAR");
            break;
          case "INCR":
            indexShift = BBAction.execute(keyWords, "INCR");
            break;
          case "DECR":
            indexShift = BBAction.execute(keyWords, "DECR");
            break;
          case "WHILE":
            try {
              indexShift = BBLoop.execute(keyWords, "WHILE");
            } catch (Exception e) {
              System.err.println("ERROR" + currentLine);
            }
            break;
          case "END":
            try {
              indexShift = BBLoop.execute(keyWords, "END");
            } catch (Exception e) {
              System.err.println("ERROR" + currentLine);
            }
            break;
          case "IF":
            try {
              indexShift = BBConditional.execute(keyWords);
            } catch (Exception e) {
              System.err.println("ERROR" + currentLine);
            }
            break;
          case "ELSE":
            searchingForEnd = "END";
            indexShift = 1;
            break;
        }
        if (getVariable(keyWords[0]) != null) { // variable assignment
          try {
            indexShift = BBArithmetic.execute(keyWords);
          } catch (Exception e) {
            System.err.println("ERROR" + currentLine);
          }
        }
      }
      newIndex += indexShift;
    }
  }
}
