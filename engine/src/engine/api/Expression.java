package engine.api;

import engine.sheetimpl.utils.CellType;

public interface Expression
{
        EffectiveValue evaluate(SheetReadActions spreadsheet);
        CellType getFunctionResultType();
}

