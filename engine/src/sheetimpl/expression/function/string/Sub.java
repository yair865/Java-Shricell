package sheetimpl.expression.function.string;

import api.EffectiveValue;
import dtoPackage.SpreadsheetDTO;
import sheetimpl.expression.type.TrinaryExpression;
import api.Expression;
import sheetimpl.cellimpl.EffectiveValueImpl;
import sheetimpl.utils.CellType;

public class Sub extends TrinaryExpression {

    public Sub(Expression sourceString, Expression startIndex, Expression endIndex) {
        super(sourceString, startIndex, endIndex);
    }

    @Override
    public EffectiveValue evaluate(Expression sourceString, Expression startIndex, Expression endIndex, SpreadsheetDTO spreadsheetDTO) {
        // Evaluate expressions to get EffectiveValues
        EffectiveValue sourceStringValue = sourceString.evaluate(spreadsheetDTO);
        EffectiveValue startIndexValue = startIndex.evaluate(spreadsheetDTO);
        EffectiveValue endIndexValue = endIndex.evaluate(spreadsheetDTO);

        // Extract the expected types
        String source = sourceStringValue.extractValueWithExpectation(String.class);
        Double start = startIndexValue.extractValueWithExpectation(Double.class);
        Double end = endIndexValue.extractValueWithExpectation(Double.class);

        // Validate extracted values
        if (source == null || start == null || end == null) {
            throw new IllegalArgumentException("Invalid arguments in function " + this.getClass().getSimpleName() + ".\n"
                    + "Expected argument types are " +sourceStringValue.getCellType() +" for the source, and "+ startIndexValue.getCellType() +" for the indices. "
                    + "But received types: "
                    + "source - " + sourceStringValue.getCellType() + ", "
                    + "startIndex - " + startIndexValue.getCellType() + ", "
                    + "endIndex - " + endIndexValue.getCellType() + ".");
        }

        // Ensure start and end are actually integers
        if (start % 1 != 0 || end % 1 != 0) {
            throw new IllegalArgumentException("Start and end indices must be integers. "
                    + "Received values: startIndex = " + start + ", endIndex = " + end + ".");
        }

        int startIndexInt = start.intValue();
        int endIndexInt = end.intValue();

        // Additional validation for index bounds
        if (startIndexInt < 0 || endIndexInt > source.length() || startIndexInt > endIndexInt) {
            return new EffectiveValueImpl(CellType.STRING, "!UNDEFINED!");
        }

        // Perform the substring operation
        String result = source.substring(startIndexInt, endIndexInt);

        return new EffectiveValueImpl(CellType.STRING, result);
    }
}

