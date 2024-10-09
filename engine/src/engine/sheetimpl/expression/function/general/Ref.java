package engine.sheetimpl.expression.function.general;

import engine.sheetimpl.api.SheetReadActions;
import engine.sheetimpl.cellimpl.api.CellReadActions;
import engine.sheetimpl.cellimpl.api.EffectiveValue;
import engine.sheetimpl.cellimpl.coordinate.Coordinate;
import engine.sheetimpl.expression.Expression;
import engine.sheetimpl.expression.type.UnaryExpression;
import engine.sheetimpl.utils.CellType;

import static engine.sheetimpl.cellimpl.coordinate.CoordinateFactory.createCoordinate;

public class Ref extends UnaryExpression {

    public Ref(Expression cellId) {
    super(cellId);
    }

    @Override
    public EffectiveValue evaluate(Expression expression, SheetReadActions spreadsheet) {
        // Evaluate the expression to get the cell ID as a string
        EffectiveValue cellIdValue = expression.evaluate(spreadsheet);
        String cellId = cellIdValue.extractValueWithExpectation(String.class);

        // Validate that the cell ID is not null
        if (cellId == null) {
            throw new IllegalArgumentException("Invalid cell ID in the expression. Expected a" + String.class.getSimpleName() + "but got "
                    + cellIdValue.getCellType() + ".");
        }

        Coordinate cellCoordinate;
        try {
            cellCoordinate = createCoordinate(cellId);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create coordinate from cell ID: " + cellId, e);
        }

        CellReadActions cell = spreadsheet.getCell(cellCoordinate);

        // If the cell exists, return its effective value
        if (cell != null) {
            return cell.getEffectiveValue();
        }

        // If the cell doesn't exist, throw an exception
        throw new IllegalArgumentException("Cell with ID " + cellId + " not found in the spreadsheet.");
    }

    @Override
    public CellType getFunctionResultType() {
        return CellType.UNKNOWN;
    }
}
