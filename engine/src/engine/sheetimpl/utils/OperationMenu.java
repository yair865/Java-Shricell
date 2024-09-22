package engine.sheetimpl.utils;

import engine.api.Expression;
import engine.sheetimpl.expression.function.general.Ref;
import engine.sheetimpl.expression.function.logic.And;
import engine.sheetimpl.expression.function.logic.Equal;
import engine.sheetimpl.expression.function.logic.Bigger;
import engine.sheetimpl.expression.function.logic.Less;
import engine.sheetimpl.expression.function.logic.Not;
import engine.sheetimpl.expression.function.logic.Or;
import engine.sheetimpl.expression.function.math.*;
import engine.sheetimpl.expression.function.math.range.Avg;
import engine.sheetimpl.expression.function.math.range.Sum;
import engine.sheetimpl.expression.function.string.Concat;
import engine.sheetimpl.expression.function.string.Sub;

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
    TIMES {
        @Override
        public Expression createExpression(List<Expression> parameters) {
            validateParameters(Times.class, parameters);
            return new Times(parameters.get(0), parameters.get(1));
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
   SUM {
        @Override
        public Expression createExpression(List<Expression> parameters) {
            validateParameters(Sum.class, parameters);
            return new Sum(parameters.getFirst());
        }
    },
   PERCENT{
        @Override
        public Expression createExpression(List<Expression> parameters) {
            validateParameters(Percent.class, parameters);
            return new Percent(parameters.getFirst(), parameters.get(1));
        }
    },
    AVG{
        @Override
        public Expression createExpression(List<Expression> parameters) {
            validateParameters(Sum.class, parameters);
            return new Avg(parameters.getFirst());
        }
    },
    REF {
        @Override
        public Expression createExpression(List<Expression> parameters) {
            validateParameters(Ref.class, parameters);
            return new Ref(parameters.getFirst());
        }
    },
    EQUAL {
        @Override
        public Expression createExpression(List<Expression> parameters) {
            validateParameters(Equal.class, parameters);
            return new Equal(parameters.get(0), parameters.get(1));
        }
    },
    BIGGER {
        @Override
        public Expression createExpression(List<Expression> parameters) {
            validateParameters(Bigger.class, parameters);
            return new Bigger(parameters.get(0), parameters.get(1));
        }
    },
    LESS {
        @Override
        public Expression createExpression(List<Expression> parameters) {
            validateParameters(Less.class, parameters);
            return new Less(parameters.get(0), parameters.get(1));
        }
    },
    NOT {
        @Override
        public Expression createExpression(List<Expression> parameters) {
            validateParameters(Not.class, parameters);
            return new Not(parameters.getFirst());
        }
    },
    AND {
        @Override
        public Expression createExpression(List<Expression> parameters) {
            validateParameters(And.class, parameters);
            return new And(parameters.get(0), parameters.get(1));
        }
    },
    OR {
        @Override
        public Expression createExpression(List<Expression> parameters) {
            validateParameters(Or.class, parameters);
            return new Or(parameters.get(0), parameters.get(1));
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
                    ", expected " + constructorParameterCount + " but received " + parameters.size());
        }
    }
}
