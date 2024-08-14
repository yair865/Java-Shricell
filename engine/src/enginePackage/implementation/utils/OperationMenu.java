package enginePackage.implementation.utils;

import enginePackage.implementation.expression.function.math.*;
import enginePackage.implementation.expression.function.string.*;
import enginePackage.api.Expression;

import java.lang.reflect.Constructor;
import java.util.List;

public enum OperationMenu {
    PLUS {
        @Override
        public Expression<?> createExpression(List<Expression<?>> arguments) {
            validateArguments(arguments, Plus.class);
            return new Plus(castArgument(arguments.getFirst(), Number.class), castArgument(arguments.get(1), Number.class));
        }
    },
    MINUS {
        @Override
        public Expression<?> createExpression(List<Expression<?>> arguments) {
            validateArguments(arguments, Minus.class);
            return new Minus(castArgument(arguments.getFirst(), Number.class), castArgument(arguments.get(1), Number.class));
        }
    },
    TIMES {
        @Override
        public Expression<?> createExpression(List<Expression<?>> arguments) {
            validateArguments(arguments, Times.class);
            return new Times(castArgument(arguments.getFirst(), Number.class), castArgument(arguments.get(1), Number.class));
        }
    },
    DIVIDE {
        @Override
        public Expression<?> createExpression(List<Expression<?>> arguments) {
            validateArguments(arguments, Divide.class);
            return new Divide(castArgument(arguments.getFirst(), Number.class), castArgument(arguments.get(1), Number.class));
        }
    },
    POW {
        @Override
        public Expression<?> createExpression(List<Expression<?>> arguments) {
            validateArguments(arguments, Pow.class);
            return new Pow(castArgument(arguments.getFirst(), Number.class), castArgument(arguments.get(1), Number.class));
        }
    },
    ABS {
        @Override
        public Expression<?> createExpression(List<Expression<?>> arguments) {
            validateArguments(arguments, Abs.class);
            return new Abs(castArgument(arguments.getFirst(), Number.class));
        }
    },
    MOD {
        @Override
        public Expression<?> createExpression(List<Expression<?>> arguments) {
            validateArguments(arguments, Mod.class);
            return new Mod(castArgument(arguments.getFirst(), Number.class), castArgument(arguments.get(1), Number.class));
        }
    },
    CONCAT {
        @Override
        public Expression<?> createExpression(List<Expression<?>> arguments) {
            validateArguments(arguments, Concat.class);
            return new Concat(castArgument(arguments.getFirst(), Text.class), castArgument(arguments.get(1), Text.class));
        }
    },
    SUB {
        @Override
        public Expression<?> createExpression(List<Expression<?>> arguments) {
            validateArguments(arguments, Sub.class);
            return new Sub(castArgument(arguments.getFirst(), Text.class), castArgument(arguments.get(1), Text.class), castArgument(arguments.get(2), Text.class));
        }
    },
    REF {
        @Override
        public Expression<?> createExpression(List<Expression<?>> arguments) {
            throw new UnsupportedOperationException("REF operation is not yet implemented.");
        }
    };

    private static void validateArguments(List<Expression<?>> arguments, Class<? extends Expression<?>> clazz) {
        try {
            Constructor<?>[] constructors = clazz.getConstructors();
            boolean validConstructor = false;

            for (Constructor<?> constructor : constructors) {
                Class<?>[] parameterTypes = constructor.getParameterTypes();

                if (parameterTypes.length == arguments.size()) {
                    validConstructor = true;
                    for (int i = 0; i < parameterTypes.length; i++) {
                        if (!parameterTypes[i].isInstance(arguments.get(i))) {
                            validConstructor = false;
                            break;
                        }
                    }
                    if (validConstructor) {
                        break;
                    }
                }
            }

            if (!validConstructor) {
                throw new IllegalArgumentException("No valid constructor found for " + clazz.getSimpleName());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error validating constructor arguments", e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> Expression<T> castArgument(Expression<?> arg, Class<T> clazz) {
        if (!clazz.isInstance(arg.evaluate())) {
            throw new IllegalArgumentException("Argument is not of type " + clazz.getSimpleName());
        }
        return (Expression<T>) arg;
    }

    public abstract Expression<?> createExpression(List<Expression<?>> arguments);
}
