package api;

import dtoPackage.SpreadsheetDTO;

public interface Expression
{
        EffectiveValue evaluate(SpreadsheetDTO spreadsheetDTO);
}

