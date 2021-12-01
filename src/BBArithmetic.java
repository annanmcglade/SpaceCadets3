import java.util.Arrays;
import java.util.Objects;
import java.util.zip.DataFormatException;

public class BBArithmetic {
    static String[] expressionList = {"+", "-", "*", "/"};
    public static int execute(String[] line) throws DataFormatException {
        BBVariable variable = BBMain.getVariable(line[0]);
        if (!Objects.equals(line[1], "=")){
            throw new DataFormatException("not a valid expression");
        } else {
            String[] expression = Arrays.copyOfRange(line, 2, line.length);
            Objects.requireNonNull(variable).changeValue(calcValue(expression));
        }
        return 1;
    }
    public static int calcValue(String[] expression) throws DataFormatException { // recursive function
        int value = 0;
        BBVariable variable;
        if (BBMain.getVariable(expression[0]) != null){
            variable = BBMain.getVariable(expression[0]);
        } else {
            throw new DataFormatException("not a valid expression");
        } if (expression.length == 1){
            value = Objects.requireNonNull(variable).getValue();
        } else if (Arrays.stream(expressionList).anyMatch(x -> expression[1].equals(x))){
            value = Objects.requireNonNull(variable).getValue();
            switch (expression[1]) {
                case "+" -> value += calcValue(Arrays.copyOfRange(expression, 2, expression.length));
                case "-" -> value -= calcValue(Arrays.copyOfRange(expression, 2, expression.length));
                case "*" -> value *= calcValue(Arrays.copyOfRange(expression, 2, expression.length));
                case "/" -> value /= calcValue(Arrays.copyOfRange(expression, 2, expression.length));
            }
        } else {
            throw new DataFormatException("not a valid expression");
        }
        return value;
    }
}
