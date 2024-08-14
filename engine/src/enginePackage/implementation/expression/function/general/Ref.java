package enginePackage.implementation.expression.function.general;

import enginePackage.api.Expression;
import enginePackage.implementation.expression.type.UnaryExpression;

public class Ref extends UnaryExpression {

    public Ref(Expression cellId) {
    super(cellId);
    }

    @Override
    protected Text evaluate(Text evaluate) {
        return null;
    }
}
