import java.util.Arrays;
import java.util.Objects;
import java.util.zip.DataFormatException;

public class BBConditional {
  public static int execute(String[] line) throws DataFormatException {
    if (line[line.length - 1].toUpperCase().equals("THEN")) {
      boolean conditional;
      if (line[1].toUpperCase().equals("IF")) { // checks to see if the statement is an else if or an if
        conditional = evaluate(Arrays.copyOfRange(line, 2, line.length - 1));
      } else {
        conditional = evaluate(Arrays.copyOfRange(line, 1, line.length - 1));
      }
      if (!conditional) {
        BBMain.setSearchingForEnd("IF");
      }
    } else if (!(line[0].toUpperCase().equals("ELSE") && line.length == 1)) {
      throw new DataFormatException("not a valid expression");
    }
    return 1;
  }

  private static boolean evaluate(String[] conditional) throws DataFormatException {
    boolean finalValue = false;
    int equalsIndex = findIndex(conditional, "==");
    if (equalsIndex != -1) {
      int expression1 = BBArithmetic.calcValue(Arrays.copyOfRange(conditional, 0, equalsIndex));
      int expression2 =
          BBArithmetic.calcValue(
              Arrays.copyOfRange(conditional, equalsIndex + 1, conditional.length));
      finalValue = (expression1 == expression2);
    } else {
      throw new DataFormatException("not a valid expression");
    }
    return finalValue;
  }

  public static int findIndex(String[] expression, String target) {
    if (expression == null) {
      return -1;
    }
    for (int i = 0; i < expression.length; i++) {
      if (Objects.equals(expression[i], target)) {
        return i;
      }
    }
    return -1;
  }
}
