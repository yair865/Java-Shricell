package engine.sheetimpl.expression.type;

import dto.dtoPackage.effectivevalue.EffectiveValue;
import engine.sheetimpl.expression.Expression;
import engine.sheetimpl.api.SheetReadActions;
import dto.dtoPackage.effectivevalue.EffectiveValueImpl;
import dto.dtoPackage.CellType;

public class Text implements Expression {

    private final String text;

    public Text(String text) {
        this.text = text;
    }

    @Override
    public EffectiveValue evaluate(SheetReadActions spreadSheet) {
        return new EffectiveValueImpl(CellType.STRING, text);
    }

    @Override
    public CellType getFunctionResultType() {
        return CellType.STRING;
    }
}
