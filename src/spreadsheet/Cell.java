package spreadsheet;

import common.api.BasicSpreadsheet;
import common.api.CellLocation;
import common.api.Expression;

import java.util.HashSet;
import java.util.Set;

/**
 * A single cell in a spreadsheet, tracking the expression, value, and other parts of cell state.
 */
public class Cell {

  private Double value = 0.0;
  private String exp = "";
  private final BasicSpreadsheet spreadsheet;
  private final CellLocation location;

  private Set<CellLocation> dependents = new HashSet<>();

  /**
   * Constructs a new cell.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   *
   * @param spreadsheet The parent spreadsheet,
   * @param location The location of this cell in the spreadsheet.
   */
  Cell(BasicSpreadsheet spreadsheet, CellLocation location) {
    this.spreadsheet = spreadsheet;
    this.location = location;
  }

  /**
   * Gets the cell's last calculated value.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   *
   * @return the cell's value.
   */
  public double getValue() {
    return value;
  }

  /**
   * Gets the cell's last stored expression, in string form.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   *
   * @return a string that parses to an equivalent expression to that last stored in the cell; if no
   *     expression is stored, we return the empty string.
   */
  public String getExpression() {
    return exp;
  }

  public CellLocation getLocation() {
    return location;
  }

  /**
   * Sets the cell's expression from a string.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   *
   * @param input The string representing the new cell expression.
   * @throws InvalidSyntaxException if the string cannot be parsed.
   */
  public void setExpression(String input) throws InvalidSyntaxException {

    // clearing old dependencies
    System.out.println(input);
    Set<CellLocation> oldDependencies = new HashSet<>();
    Expression e = Parser.parse(exp);
    e.findCellReferences(oldDependencies);

    for (CellLocation l : oldDependencies) {
      // remove this cell as a dependent
      spreadsheet.removeDependency(location, l);
    }

    // changing the expression
    if (input.isEmpty()) {
      exp = "";
      value = 0.0;
      // we don't need to add any new dependencies in this case

    } else {
      Expression e2 = Parser.parse(input);
      exp = e2.toString();

      // adding new dependencies
      Set<CellLocation> dependencyLoc = new HashSet<>();
      e2.findCellReferences(dependencyLoc);
      for (CellLocation l : dependencyLoc) {
        spreadsheet.addDependency(location, l);
      }
    }
  }

  /** @return a string representing the value, if any, of this cell. */
  @Override
  public String toString() {
    if (isCellEmpty()) {
      return "";
    } else {
      return value.toString();
    }
  }

  /**
   * Adds the given location to this cell's dependents.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   *
   * @param location the location to add.
   */
  public void addDependent(CellLocation location) {
    dependents.add(location);
  }

  /**
   * Adds the given location to this cell's dependents.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   *
   * @param location the location to add.
   */
  public void removeDependent(CellLocation location) {
    dependents.remove(location);
  }

  /**
   * Adds this cell's expression's references to a set.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   *
   * @param target The set that will receive the dependencies for this
   */
  public void findCellReferences(Set<CellLocation> target) {
    if (!exp.isEmpty()) {
      try {
        Expression e = Parser.parse(exp);
        e.findCellReferences(target);
      } catch (InvalidSyntaxException ex) {
        ex.printStackTrace();
      }
    }
  }

  /**
   * Recalculates this cell's value based on its expression.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   */
  public void recalculate() {
    if (exp.equals("")) {
      value = 0.0;
    } else {
      try {
        Expression e = Parser.parse(exp);
        value = e.evaluate(spreadsheet);
      } catch (InvalidSyntaxException ex) {
        ex.printStackTrace();
      }
    }
    for (CellLocation l : dependents) {
      spreadsheet.recalculate(l);
    }
  }

  private Boolean isCellEmpty() {
    return exp.isEmpty() && value == 0.0;
  }
}
