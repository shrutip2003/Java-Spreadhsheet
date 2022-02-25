package spreadsheet;

import common.api.BasicSpreadsheet;
import common.api.CellLocation;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/** Detects dependency cycles. */
public class CycleDetector {
  /**
   * Constructs a new cycle detector.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   *
   * @param spreadsheet The parent spreadsheet, used for resolving cell locations.
   */
  private final BasicSpreadsheet spreadsheet;

  CycleDetector(BasicSpreadsheet spreadsheet) {
    this.spreadsheet = spreadsheet;
  }

  /**
   * Checks for a cycle in the spreadsheet, starting at a particular cell.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   *
   * @param start The cell location where cycle detection should start.
   * @return Whether a cycle was detected in the dependency graph starting at the given cell.
   */
  public boolean hasCycleFrom(CellLocation start) {
    Set<CellLocation> seen = new HashSet<>();
    seen.add(start);
    Set<CellLocation> dependencies = new HashSet<>();
    spreadsheet.findCellReferences(start, dependencies);
    for (CellLocation y : dependencies) {
      boolean result = cycleHelper(y, seen);
      if (result) {
        return true;
      }
    }
    return false;
    // throw new UnsupportedOperationException("Not implemented yet");
  }

  private boolean cycleHelper(CellLocation y, Set<CellLocation> cellsSeen) {
    if (cellsSeen.contains(y)) {
      return true;
    } else {
      boolean seen = false;
      cellsSeen.add(y);
      Set<CellLocation> dependencies = new HashSet<>();
      spreadsheet.findCellReferences(y, dependencies);
      if (dependencies.isEmpty()) {
        cellsSeen.remove(y);
        return false;
      }
      for (CellLocation c : dependencies) {
        seen = seen || cycleHelper(c, cellsSeen);
      }
      cellsSeen.remove(y);
      return seen;
    }
  }
}
