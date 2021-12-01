import java.util.Objects;
import java.util.zip.DataFormatException;

public class BBLoop {
  public static int execute(String[] line, String action) throws DataFormatException {
    int returnIndex = 1;
    if (Objects.equals(action, "WHILE")) {
      BBVariable variable;
      if (BBMain.getVariable(line[1]) != null) {
        variable = BBMain.getVariable(line[1]);
      } else {
        throw new NullPointerException("variable Does Not Exist");
      }
      if (line.length == 5
          && line[4].equalsIgnoreCase("DO")
          && (line[2].equalsIgnoreCase("EQUALS") || line[2].equalsIgnoreCase("NOT"))) {
        BBVariable conditionVariable;
        if (BBMain.getVariable(line[3]) != null) {
          conditionVariable = BBMain.getVariable(line[3]);
        } else {
          throw new NullPointerException("variable Does Not Exist");
        }
        if (line[2].toUpperCase().equals("EQUALS")) { // loops while 2 variables are equal
          if (Objects.requireNonNull(variable).getValue()
              == Objects.requireNonNull(conditionVariable).getValue()) {
            BBMain.addIndent();
          } else {
            BBMain.setSearchingForEnd("LOOP");
          }

        } else if (line[2].toUpperCase().equals("NOT")) { // loops while 2 variables are not equal
          if (Objects.requireNonNull(variable).getValue()
              != Objects.requireNonNull(conditionVariable).getValue()) {
            BBMain.addIndent();
          } else {
            BBMain.setSearchingForEnd("LOOP");
          }
        }
      } else {
        throw new DataFormatException("not a valid expression");
      }

    } else if (Objects.equals(action, "END")) { //sets main to search for end while
      if (line.length == 1) {
        int currentIndent = BBMain.getCurrentIndent();
        if (currentIndent != -1) {
          returnIndex = currentIndent - BBMain.getCurrentLine();
        } else {
          throw new IndexOutOfBoundsException("There are too many END statements");
        }
      }
    }
    return returnIndex;
  }
}
