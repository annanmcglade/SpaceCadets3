import java.util.Objects;

public class BBAction {

    public static int execute(String[] line, String action) {
        if (line.length != 2){
            throw new ArithmeticException("Expected 1 Argument, Got " + (line.length - 1));
        } else if (BBMain.getVariable(line[1]) == null) {
            if (Objects.equals(action, "CLEAR")){
                BBMain.addVariable(line[1]); // creates new variable
            } else {
                throw new NullPointerException("variable Does Not Exist");
            }
        } else {
            BBVariable variable = BBMain.getVariable(line[1]);
            switch (action) {
                case "CLEAR" -> Objects.requireNonNull(variable).clearValue();
                case "INCR" -> Objects.requireNonNull(variable).incrValue();
                case "DECR" -> Objects.requireNonNull(variable).decrValue();
                case "PRINT" -> Objects.requireNonNull(variable).printValue();
            }
        }
        return 1;
    }
}
