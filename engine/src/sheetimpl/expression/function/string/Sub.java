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
    protected EffectiveValue evaluate(Expression sourceString, Expression startIndex, Expression endIndex , SpreadsheetDTO spreadsheetDTO) {
        EffectiveValue sourceStringValue = sourceString.evaluate(spreadsheetDTO);
        EffectiveValue startIndexValue = startIndex.evaluate(spreadsheetDTO);
        EffectiveValue endIndexValue = endIndex.evaluate(spreadsheetDTO);

        String source = sourceStringValue.extractValueWithExpectation(String.class);
        int start = startIndexValue.extractValueWithExpectation(Double.class).intValue();
        int end = endIndexValue.extractValueWithExpectation(Double.class).intValue();

        if (start < 0 || end > source.length() || start > end) {
            return new EffectiveValueImpl(CellType.STRING, "!UNDEFINED!") ;
        }

        String result = source.substring(start, end);

        return new EffectiveValueImpl(CellType.STRING, result);
    }

}

