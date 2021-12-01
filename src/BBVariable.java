public class BBVariable {
  String variableName;
  int value;

  public BBVariable(String name) {
    value = 0;
    variableName = name;
  }

  public String getVariableName() {
    return variableName;
  }

  public int getValue() {
    return value;
  }

  public void clearValue() {
    this.value = 0;
  }

  public void incrValue() {
    this.value++;
  }

  public void decrValue() {
    this.value--;
  }

  public void printValue() {
    System.out.println(this.value);
  }

  public void changeValue(int value) {
    this.value = value;
  }
}
