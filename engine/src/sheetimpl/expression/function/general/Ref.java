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
        String cellId = expression.evaluate(spreadsheet).extractValueWithExpectation(String.class);
        Coordinate cellCoordinate = createCoordinate(cellId);

        //TODO : if the coordinate inbound return String "" ?

        CellDTO cell = spreadsheet.cells().get(cellCoordinate);
        if (cell != null) {
            return cell.effectiveValue();
        }

        throw new IllegalArgumentException("Cell with ID " + cellId + " not found in the spreadsheet.");
    }
}
