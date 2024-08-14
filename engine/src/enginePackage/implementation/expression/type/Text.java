package enginePackage.implementation.expression.type;

import enginePackage.api.EffectiveValue;
import enginePackage.api.Expression;

public class Text implements Expression {

    private final String text;

    public Text(String text) {
        this.text = text;
    }

    @Override
    public EffectiveValue evaluate() {return text;} //FIX
}
