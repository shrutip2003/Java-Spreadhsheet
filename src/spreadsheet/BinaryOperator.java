package spreadsheet;

public enum BinaryOperator {
    ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION, EXPONENTIAL;

    @Override
    public String toString() {
        switch (this) {
            case ADDITION -> {
                return "+";
            }
            case SUBTRACTION -> {
                return "-";
            }
            case MULTIPLICATION -> {
                return "*";
            }
            case DIVISION -> {
                return "/";
            }
            case EXPONENTIAL -> {
                return "^";
            }
        }
        return null;
    }

    public int toNum() {
        switch (this) {
            case ADDITION, SUBTRACTION -> {
                return 1;
            }
            case MULTIPLICATION, DIVISION -> {
                return 2;
            }
            case EXPONENTIAL -> {
                return 3;
            }
        }
        return 0;
    }

    public Boolean isLeftAssociative() {
        switch (this) {
            case EXPONENTIAL -> {
                return false;
            }
            case DIVISION,MULTIPLICATION,ADDITION,SUBTRACTION -> {return true;}
        }
        return null;
    }

}
