package engine.sheetimpl.expression;

import engine.sheetimpl.cellimpl.api.EffectiveValue;
import engine.sheetimpl.api.SheetReadActions;
import engine.sheetimpl.utils.CellType;

public interface Expression
{
        EffectiveValue evaluate(SheetReadActions spreadsheet);
        CellType getFunctionResultType();
}

