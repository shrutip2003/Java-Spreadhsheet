package spreadsheet;

import common.api.BasicSpreadsheet;
import common.api.CellLocation;
import common.api.Expression;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.Objects.isNull;

public class Spreadsheet implements BasicSpreadsheet {
  //
  // start replacing
  //

  /**
   * Construct an empty spreadsheet.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   */
  private Map<CellLocation, Cell> map;

  Spreadsheet() {
    map = new HashMap<>();
  }

  /**
   * Parse and evaluate an expression, using the spreadsheet as a context.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   */
  public double evaluateExpression(String expression) throws InvalidSyntaxException {
    Expression exp = Parser.parse(expression);
    return exp.evaluate(this);
  }

  @Override
  public double getCellValue(CellLocation location) {
    Cell c = map.get(location);
    if (isNull(c)) {
      return 0.0;
    }
    return c.getValue();
  }

  @Override
  public void setCellExpression(CellLocation location, String input) throws InvalidSyntaxException {

    Cell cell;
    if (map.containsKey(location)) {
      cell = map.get(location);
    } else {
      cell = new Cell(this, location);
      map.put(location, cell);
    }
    cell.setExpression(input);
    CycleDetector cycleDet = new CycleDetector(this);
    if (cycleDet.hasCycleFrom(location)) {
      cell.setExpression("");
    }

    cell.recalculate();
  }

  //
  // end replacing
  //

  @Override
  public String getCellExpression(CellLocation location) {
    Cell c = map.get(location);
    if (isNull(c)) {
      return "";
    }
    return c.getExpression();
  }

  @Override
  public String getCellDisplay(CellLocation location) {
    Cell c = map.get(location);
    if (isNull(c)) {
      return "";
    }
    return c.toString();
  }

  @Override
  public void addDependency(CellLocation dependent, CellLocation dependency) {
    Cell referredBy = map.get(dependency);
    if (isNull(referredBy)) {
      Cell cNew = new Cell(this, dependency);
      cNew.addDependent(dependent);
      map.put(dependency, cNew);
    } else {
      referredBy.addDependent(dependent);
    }
  }

  @Override
  public void removeDependency(CellLocation dependent, CellLocation dependency) {
    Cell referredBy = map.get(dependency);
    referredBy.removeDependent(dependent);
  }

  @Override
  public void recalculate(CellLocation location) {
    Cell c;
    if (!map.containsKey(location)) {
      c = new Cell(this, location);
      map.put(location, c);
    } else {
      c = map.get(location);
    }
    c.recalculate();
  }

  @Override
  public void findCellReferences(CellLocation subject, Set<CellLocation> target) {
    Cell c = map.get(subject);
    String expStr = c.getExpression();
    if (!expStr.isEmpty()) {
      try {
        Expression e = Parser.parse(expStr);
        e.findCellReferences(target);
      } catch (InvalidSyntaxException ex) {
        ex.printStackTrace();
      }
    }
  }
}
