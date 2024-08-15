package enginePackage.implementation.utils;

import enginePackage.api.Expression;
import enginePackage.implementation.expression.function.math.*;
import enginePackage.implementation.expression.function.string.Concat;
import enginePackage.implementation.expression.function.string.Sub;

import java.util.List;

public enum OperationMenu {
    PLUS {
        @Override
        public Expression createExpression(List<Expression> arguments) {
            return new Plus(arguments.get(0), arguments.get(1));
        }
    },
    MINUS {
        @Override
        public Expression createExpression(List<Expression> arguments) {
            return new Minus(arguments.get(0), arguments.get(1));
        }
    },
    TIMES {
        @Override
        public Expression createExpression(List<Expression> arguments) {
            return new Times(arguments.get(0), arguments.get(1));
        }
    },
    DIVIDE {
        @Override
        public Expression createExpression(List<Expression> arguments) {
            return new Divide(arguments.get(0), arguments.get(1));
        }
    },
    POW {
        @Override
        public Expression createExpression(List<Expression> arguments) {
            return new Pow(arguments.get(0), arguments.get(1));
        }
    },
    ABS {
        @Override
        public Expression createExpression(List<Expression> arguments) {
            return new Abs(arguments.getFirst());
        }
    },
    MOD {
        @Override
        public Expression createExpression(List<Expression> arguments) {
            return new Mod(arguments.get(0), arguments.get(1));
        }
    },
    CONCAT {
        @Override
        public Expression createExpression(List<Expression> arguments) {
            return new Concat(arguments.get(0), arguments.get(1));
        }
    },
    SUB {
        @Override
        public Expression createExpression(List<Expression> arguments) {
            return new Sub(arguments.get(0), arguments.get(1), arguments.get(2));
        }
    },
    REF {
        @Override
        public Expression createExpression(List<Expression> arguments) {
            throw new UnsupportedOperationException("REF operation is not yet implemented.");
        }
    };

    public abstract Expression createExpression(List<Expression> arguments);
}

