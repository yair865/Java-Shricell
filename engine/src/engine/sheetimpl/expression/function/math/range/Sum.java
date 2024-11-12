package engine.sheetimpl.expression.function.math.range;

import dto.dtoPackage.coordinate.Coordinate;
import dto.dtoPackage.effectivevalue.EffectiveValue;
import engine.sheetimpl.expression.Expression;
import engine.sheetimpl.api.SheetReadActions;
import dto.dtoPackage.effectivevalue.EffectiveValueImpl;
import engine.sheetimpl.expression.type.UnaryExpression;
import dto.dtoPackage.CellType;

import java.util.List;

public class Sum extends UnaryExpression {

    public Sum(Expression rangeName) {
        super(rangeName);
    }

    @Override
    public EffectiveValue evaluate(Expression expression, SheetReadActions spreadsheet) {
        EffectiveValue rangeEffectiveValue = expression.evaluate(spreadsheet);
        String rangeName = rangeEffectiveValue.extractValueWithExpectation(String.class);

        if (rangeName == null) {
            return new EffectiveValueImpl(CellType.ERROR, Double.NaN);
        }

        List<Coordinate> selectedRange = spreadsheet.getRangeByName(rangeName).getCoordinates();
        double sum = 0;
        boolean hasNumericValue = false;

        for (Coordinate coordinate : selectedRange) {
            EffectiveValue cellValue = spreadsheet.getCell(coordinate).getEffectiveValue();
            if (cellValue.getCellType() == CellType.NUMERIC) {
                sum += cellValue.extractValueWithExpectation(Double.class);
                hasNumericValue = true;
            }
        }

        return new EffectiveValueImpl(CellType.NUMERIC, hasNumericValue ? sum : 0);
    }

    @Override
    public CellType getFunctionResultType() {
        return CellType.NUMERIC;
    }
}
