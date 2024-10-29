package engine.sheetimpl.expression;

import dto.dtoPackage.effectivevalue.EffectiveValue;
import engine.sheetimpl.api.SheetReadActions;
import dto.dtoPackage.CellType;

public interface Expression
{
        EffectiveValue evaluate(SheetReadActions spreadsheet);
        CellType getFunctionResultType();
}

