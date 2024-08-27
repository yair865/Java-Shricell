package api;

import sheetimpl.utils.CellType;

public interface Expression
{
        EffectiveValue evaluate(SheetReadActions spreadsheet);
        CellType getFunctionResultType();
}

