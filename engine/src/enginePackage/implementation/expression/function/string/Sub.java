package enginePackage.implementation.expression.function.string;

import enginePackage.implementation.expression.type.TrinaryExpression;
import enginePackage.api.Expression;

public class Sub extends TrinaryExpression<Text> {

    public Sub(Expression<Text> expression1, Expression<Text> expression2, Expression<Text> expression3) {
        super(expression1, expression2, expression3);
    } // TODO

    @Override
    protected Text evaluate(Text evaluate, Text evaluate2, Text evaluate3) {
        return null;
    }
}
