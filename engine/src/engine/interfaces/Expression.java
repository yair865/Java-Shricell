package engine.interfaces;

public interface Expression {
        /**
         * evaluate the expression and return the result
         *
         * @return the results of the expression
         */
        double evaluate();

        String getOperationSign();
    }

