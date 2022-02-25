package spreadsheet;

import common.api.CellLocation;
import common.api.EvaluationContext;
import common.api.Expression;

import java.util.Set;

public class BinaryOperatorExpression implements Expression {
    private final Expression lhs;
    private final BinaryOperator op;
    private final Expression rhs;

    public BinaryOperatorExpression(Expression lhs, BinaryOperator op, Expression rhs) {
        this.lhs = lhs;
        this.op = op;
        this.rhs = rhs;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        sb.append(lhs.toString());
        sb.append(op.toString());
        sb.append(rhs.toString());
        sb.append(")");
        return sb.toString();
        //return "(" + lhs.toString() + op.toString() + rhs.toString() + ")";
    }

    @Override
    public double evaluate(EvaluationContext context) {
        switch (op) {
            case ADDITION -> {
                return lhs.evaluate(context) + rhs.evaluate(context);
            }
            case SUBTRACTION -> {
                return lhs.evaluate(context) - rhs.evaluate(context);
            }
            case MULTIPLICATION -> {
                return lhs.evaluate(context) * rhs.evaluate(context);
            }
            case DIVISION -> {
                return lhs.evaluate(context) / rhs.evaluate(context);
            }
            case EXPONENTIAL -> {
                return Math.pow(lhs.evaluate(context), rhs.evaluate(context));
            }
        }
        return 0;
    }

    @Override
    public void findCellReferences(Set<CellLocation> dependencies) {
        lhs.findCellReferences(dependencies);
        rhs.findCellReferences(dependencies);
    }

}
