package enginePackage.implementation.expression.type;

import enginePackage.interfaces.Expression;

public class Text implements Expression<String> {

    private String text;

    public Text(String text) {
        this.text = text;
    }

    @Override
    public String evaluate() {return text;}

}
