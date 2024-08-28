package engine.sheetimpl.utils;

import engine.api.Expression;
import engine.sheetimpl.expression.function.general.Ref;
import engine.sheetimpl.expression.function.math.*;
import engine.sheetimpl.expression.function.string.Concat;
import engine.sheetimpl.expression.function.string.Sub;

import java.lang.reflect.Constructor;
import java.util.List;

public enum OperationMenu {
    DIVIDE {
        @Override
        public Expression createExpression(List<Expression> parameters) {
            validateParameters(Divide.class, parameters);
            validateTypesForBinaryExpressionNumeric(parameters.getFirst(), parameters.get(1), Divide.class);
            return new Divide(parameters.get(0), parameters.get(1));
        }
    },
    MINUS {
        @Override
        public Expression createExpression(List<Expression> parameters) {
            validateParameters(Minus.class, parameters);
            validateTypesForBinaryExpressionNumeric(parameters.getFirst(), parameters.get(1), Minus.class);
            return new Minus(parameters.get(0), parameters.get(1));
        }
    },
    MOD {
        @Override
        public Expression createExpression(List<Expression> parameters) {
            validateParameters(Mod.class, parameters);
            validateTypesForBinaryExpressionNumeric(parameters.getFirst(), parameters.get(1), Mod.class);
            return new Mod(parameters.get(0), parameters.get(1));
        }
    },
    PLUS {
        @Override
        public Expression createExpression(List<Expression> parameters) {
            validateParameters(Plus.class, parameters);
            validateTypesForBinaryExpressionNumeric(parameters.getFirst(), parameters.get(1), Plus.class);
            return new Plus(parameters.get(0), parameters.get(1));
        }
    },
    POW {
        @Override
        public Expression createExpression(List<Expression> parameters) {
            validateParameters(Pow.class, parameters);
            validateTypesForBinaryExpressionNumeric(parameters.getFirst(), parameters.get(1), Pow.class);
            return new Pow(parameters.get(0), parameters.get(1));
        }
    },
    TIMES {
        @Override
        public Expression createExpression(List<Expression> parameters) {
            validateParameters(Times.class, parameters);
            validateTypesForBinaryExpressionNumeric(parameters.getFirst(), parameters.get(1), Times.class);
            return new Times(parameters.get(0), parameters.get(1));
        }
    },
    ABS {
        @Override
        public Expression createExpression(List<Expression> parameters) {
            validateParameters(Abs.class, parameters);
            validateTypeForAbs(parameters.getFirst());
            return new Abs(parameters.getFirst());
        }
    },
    CONCAT {
        @Override
        public Expression createExpression(List<Expression> parameters) {
            validateParameters(Concat.class, parameters);
            validateTypesForBinaryExpressionText(parameters.getFirst(), parameters.get(1));
            return new Concat(parameters.get(0), parameters.get(1));
        }
    },
    SUB {
        @Override
        public Expression createExpression(List<Expression> parameters) {
            validateParameters(Sub.class, parameters);
            validateTypeForSub(parameters.getFirst(), parameters.get(1), parameters.get(2));
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
            throw new IllegalArgumentException("Not enough arguments for function " + operationClazz.getSimpleName() +
                    "expected " + constructorParameterCount + " but received " + parameters.size());
        }
    }

    private static void validateTypesForBinaryExpressionNumeric(Expression left, Expression right, Class<?> clazz) {

        if (left.getFunctionResultType() != CellType.NUMERIC && left.getFunctionResultType() != CellType.UNKNOWN
                || (right.getFunctionResultType() != CellType.NUMERIC && right.getFunctionResultType() != CellType.UNKNOWN)) {

            throw new IllegalArgumentException("Invalid arguments in function " + clazz.getSimpleName() + ".\n"
                    + "the arguments expected are from type " + CellType.NUMERIC + " but the first argument is from type - " + left.getFunctionResultType()
                    + ", and the second argument is from type - " + right.getFunctionResultType() + ".");
        }
    }

    private static void validateTypesForBinaryExpressionText(Expression left, Expression right) {

        if ((left.getFunctionResultType() != CellType.STRING && left.getFunctionResultType() != CellType.UNKNOWN)
                || (right.getFunctionResultType() != CellType.STRING && right.getFunctionResultType() != CellType.UNKNOWN)) {

            throw new IllegalArgumentException("Invalid arguments in function " + Concat.class.getSimpleName() + ".\n"
                    + "The arguments expected are of type " + CellType.STRING + ", but the first argument is of type "
                    + left.getFunctionResultType() + " and the second argument is of type "
                    + right.getFunctionResultType() + ".");
        }
    }

    private static void validateTypeForAbs(Expression expression) {

        if (expression.getFunctionResultType() != CellType.NUMERIC && expression.getFunctionResultType() != CellType.UNKNOWN) {

            throw new IllegalArgumentException("Invalid argument in function " + Abs.class.getSimpleName() + ".\n"
                    + "The argument expected is of type " + CellType.NUMERIC + " but received an argument of type - "
                    + expression.getFunctionResultType() + ".");
        }
    }

    private static void validateTypeForSub(Expression source, Expression start, Expression end) {
        if (source.getFunctionResultType() != CellType.STRING && source.getFunctionResultType() != CellType.UNKNOWN
                || start.getFunctionResultType() != CellType.NUMERIC && start.getFunctionResultType() != CellType.UNKNOWN
                || end.getFunctionResultType() != CellType.NUMERIC && end.getFunctionResultType() != CellType.UNKNOWN) {
            throw new IllegalArgumentException("Invalid argument types in function " + Sub.class.getSimpleName() + ".\n"
                    + "Expected argument types: " + CellType.STRING + " or " + CellType.UNKNOWN + " for source, and " + CellType.NUMERIC + " or " + CellType.UNKNOWN + " for indices. "
                    + "But received: "
                    + "source - " + source.getFunctionResultType() + ", "
                    + "startIndex - " + start.getFunctionResultType() + ", "
                    + "endIndex - " + end.getFunctionResultType() + ".");
        }
    }

}
