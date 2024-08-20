package sheetimpl.expression.function.general;

import api.EffectiveValue;
import api.Expression;
import dtoPackage.CellDTO;
import dtoPackage.SpreadsheetDTO;
import sheetimpl.cellimpl.coordinate.Coordinate;
import sheetimpl.expression.type.UnaryExpression;
import static sheetimpl.cellimpl.coordinate.CoordinateFactory.createCoordinate;

public class Ref extends UnaryExpression {

    public Ref(Expression cellId) {
    super(cellId);
    }

    @Override
    public EffectiveValue evaluate(Expression expression, SpreadsheetDTO spreadsheet) {
        // Evaluate the expression to get the cell ID as a string
        EffectiveValue cellIdValue = expression.evaluate(spreadsheet);
        String cellId = cellIdValue.extractValueWithExpectation(String.class);

        // Validate that the cell ID is not null
        if (cellId == null) {
            throw new IllegalArgumentException("Invalid cell ID in the expression. Expected a String but got "
                    + cellIdValue.getCellType() + ".");
        }

        // Convert the cell ID string to a Coordinate
        Coordinate cellCoordinate;
        try {
            cellCoordinate = createCoordinate(cellId);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to create coordinate from cell ID: " + cellId, e);
        }

        // Retrieve the cell from the spreadsheet using the coordinate
        CellDTO cell = spreadsheet.cells().get(cellCoordinate);

        // If the cell exists, return its effective value
        if (cell != null) {
            return cell.effectiveValue();
        }

        // If the cell doesn't exist, throw an exception
        throw new IllegalArgumentException("Cell with ID " + cellId + " not found in the spreadsheet.");
    }
}
