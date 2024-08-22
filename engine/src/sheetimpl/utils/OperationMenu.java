package sheetimpl.utils;

import api.Expression;
import sheetimpl.expression.function.general.Ref;
import sheetimpl.expression.function.math.*;
import sheetimpl.expression.function.string.Concat;
import sheetimpl.expression.function.string.Sub;

import java.lang.reflect.Constructor;
import java.util.List;

public enum OperationMenu {
    DIVIDE {
        @Override
        public Expression createExpression(List<Expression> parameters) {
            validateParameters(Divide.class, parameters);
            return new Divide(parameters.get(0), parameters.get(1));
        }
    },
    MINUS {
        @Override
        public Expression createExpression(List<Expression> parameters) {
            validateParameters(Minus.class, parameters);
            return new Minus(parameters.get(0), parameters.get(1));
        }
    },
    MOD {
        @Override
        public Expression createExpression(List<Expression> parameters) {
            validateParameters(Mod.class, parameters);
            return new Mod(parameters.get(0), parameters.get(1));
        }
    },
    PLUS {
        @Override
        public Expression createExpression(List<Expression> parameters) {
            validateParameters(Plus.class, parameters);
            return new Plus(parameters.get(0), parameters.get(1));
        }
    },
    POW {
        @Override
        public Expression createExpression(List<Expression> parameters) {
            validateParameters(Pow.class, parameters);
            return new Pow(parameters.get(0), parameters.get(1));
        }
    },
    ABS {
        @Override
        public Expression createExpression(List<Expression> parameters) {
            validateParameters(Abs.class, parameters);
            return new Abs(parameters.getFirst());
        }
    },
    CONCAT {
        @Override
        public Expression createExpression(List<Expression> parameters) {
            validateParameters(Concat.class, parameters);
            return new Concat(parameters.get(0), parameters.get(1));
        }
    },
    SUB {
        @Override
        public Expression createExpression(List<Expression> parameters) {
            validateParameters(Sub.class, parameters);
            return new Sub(parameters.get(0), parameters.get(1), parameters.get(2));
        }
    },
    REF {
        @Override
        public Expression createExpression(List<Expression> parameters) {
            validateParameters(Ref.class, parameters);
            return new Ref(parameters.getFirst());
        }
    };

    public abstract Expression createExpression(List<Expression> parameters);

    private static void validateParameters(Class<?> operationClazz, List<Expression> parameters) {
        boolean matchFound = false;
        int constructorParameterCount = 0;
        Constructor<?>[] constructors = operationClazz.getDeclaredConstructors();

        for (Constructor<?> constructor : constructors) {
            constructorParameterCount = constructor.getParameterCount();
            if (constructorParameterCount == parameters.size()) {
                matchFound = true;
                break;
            }
        }

        if (!matchFound) {
            throw new IllegalArgumentException("No compatible constructor found for class: " + operationClazz.getName());
        }
    }
}

