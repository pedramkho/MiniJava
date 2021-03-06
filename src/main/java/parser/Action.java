package parser;

public class Action {
  public Act action;
  public int number;

  public Action(Act action, int number) {
    this.action = action;
    this.number = number;
  }

  public String toString() {
    switch (action) {
      case ACCEPT:
        return "acc";
      case SHIFT:
        return "s" + number;
      case REDUCE:
        return "r" + number;
    }
    return action.toString() + number;
  }
}

enum Act {
  SHIFT,
  REDUCE,
  ACCEPT
}
