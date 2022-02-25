package spreadsheet;

import common.api.CellLocation;
import common.api.EvaluationContext;
import common.api.Expression;

import java.util.Set;

public class EmptyExpression implements Expression {
  @Override
  public double evaluate(EvaluationContext context) {
    return 0;
  }

  @Override
  public void findCellReferences(Set<CellLocation> dependencies) {}
}
