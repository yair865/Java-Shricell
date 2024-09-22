package engine.sheetimpl.expression.function.math.range;

import engine.api.Coordinate;
import engine.api.EffectiveValue;
import engine.api.Expression;
import engine.api.SheetReadActions;
import engine.sheetimpl.cellimpl.EffectiveValueImpl;
import engine.sheetimpl.expression.type.UnaryExpression;
import engine.sheetimpl.utils.CellType;

import java.util.List;

public class Avg extends UnaryExpression {

    public Avg(Expression rangeName) {
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
        int numericCount = 0;

        for (Coordinate coordinate : selectedRange) {
            EffectiveValue cellValue = spreadsheet.getCell(coordinate).getEffectiveValue();
            if (cellValue.getCellType() == CellType.NUMERIC) {
                sum += cellValue.extractValueWithExpectation(Double.class);
                numericCount++;
            }
        }

        if (numericCount == 0) {
            return new EffectiveValueImpl(CellType.ERROR, "!NO_NUMERIC_VALUES!");
        }

        double average = sum / numericCount;
        return new EffectiveValueImpl(CellType.NUMERIC, average);
    }

    @Override
    public CellType getFunctionResultType() {
        return CellType.NUMERIC;
    }
}
