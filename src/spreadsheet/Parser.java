package spreadsheet;

import common.api.Expression;
import common.lexer.InvalidTokenException;
import common.lexer.Token;

import java.util.List;
import java.util.Stack;

import static common.lexer.Lexer.tokenize;

public class Parser {

  /**
   * Parse a string into an Expression.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   */

  private static BinaryOperator getOperator(Token t) {
    switch (t.kind) {
      case PLUS -> {
        return BinaryOperator.ADDITION;
      }
      case MINUS -> {
        return BinaryOperator.SUBTRACTION;
      }
      case STAR -> {
        return BinaryOperator.MULTIPLICATION;
      }
      case SLASH -> {
        return BinaryOperator.DIVISION;
      }
      case CARET -> {
        return BinaryOperator.EXPONENTIAL;
      }
    }
    return null;
  }


  static Expression parse(String input) throws InvalidSyntaxException {
    Stack<Expression> operands = new Stack<>();
    Stack<BinaryOperator> operators = new Stack<>();
    List<Token> tokens = null;
    try {
      tokens = tokenize(input);
    } catch (InvalidTokenException e) {
      e.printStackTrace();
    }

    if (input.isEmpty()){
      return new EmptyExpression();
    }
    for (Token t : tokens) {
      switch (t.kind){
        case NUMBER -> operands.push(new Number(t.numberValue));
        case CELL_LOCATION -> operands.push(new CellReference(t.cellLocationValue));
        case LANGLE,RANGLE -> throw new InvalidSyntaxException("Expression not inputted in the correct format");
        default -> {
          if (t.kind.isBinaryOperator()) {
            if (operators.isEmpty()) {
              operators.push(getOperator(t));
            } else {
              BinaryOperator top = operators.peek();
              while ((top.toNum() > t.kind.toNum()) ||
                      (top.toNum() == t.kind.toNum() && top.isLeftAssociative())) {
                Expression exp2 = operands.pop();
                Expression exp1 = operands.pop();
                operands.push(new BinaryOperatorExpression(exp1, top, exp2));
                operators.pop(); // we only peeked at top earlier, now we have to remove it
                if (operators.isEmpty()) {
                  break;
                }
                top = operators.peek();
              }
              // pushing t in the operators stack once/if the condition is no longer fulfilled
              operators.push(getOperator(t));
            }
          }
        }
      }
    }
    // once the processing of all tokens is done
    while (!operators.isEmpty()) {
      BinaryOperator op = operators.pop();
      Expression exp2 = operands.pop();
      Expression exp1 = operands.pop();
      operands.push(new BinaryOperatorExpression(exp1, op, exp2));
    }
    // we are now guaranteed operator stack is empty
    // resulting expression is the one at the top of the stack
    return (operands.pop());
  }


}
