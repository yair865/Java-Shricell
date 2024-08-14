package enginePackage.implementation.utils;

import enginePackage.api.Expression;
import enginePackage.implementation.expression.type.Bool;
import enginePackage.implementation.expression.type.Number;
import enginePackage.implementation.expression.type.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ExpressionParser {

    public Expression parse(String expressionString) {
        expressionString = expressionString.trim().substring(1, expressionString.length() - 1).trim();
        return parseExpression(expressionString);
    }

    private Expression parseExpression(String expressionString) {
        List<String> tokens = tokenize(expressionString);
        if (tokens.isEmpty()) {
            throw new IllegalArgumentException("Invalid expression.");
        }

        String operation = tokens.getFirst().trim();
        List<Expression> arguments = new ArrayList<>();

        for (int i = 1; i < tokens.size(); i++) {
            String token = tokens.get(i).trim();
            if (isNumber(token)) {
                arguments.add(new Number(Double.parseDouble(token)));
            } else if (isBoolean(token)) {
                arguments.add(new Bool(Boolean.parseBoolean(token)));
            } else {
                // Recursively parse nested expressions
                arguments.add(parseExpression(token));
            }
        }

        return createOperation(operation, arguments);
    }

    private List<String> tokenize(String expressionString) {
        List<String> tokens = new ArrayList<>();
        Stack<Character> stack = new Stack<>();
        StringBuilder currentToken = new StringBuilder();

        for (char c : expressionString.toCharArray()) {
            if (c == '{') {
                stack.push(c);
                currentToken.append(c);
            } else if (c == '}') {
                stack.pop();
                currentToken.append(c);
                if (stack.isEmpty()) {
                    tokens.add(currentToken.toString().trim());
                    currentToken = new StringBuilder();
                }
            } else if (c == ',' && stack.isEmpty()) {
                tokens.add(currentToken.toString().trim());
                currentToken = new StringBuilder();
            } else {
                currentToken.append(c);
            }
        }
        if (!currentToken.isEmpty()) {
            tokens.add(currentToken.toString().trim());
        }

        return tokens;
    }

    private boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isBoolean(String token) {
        return "true".equalsIgnoreCase(token) || "false".equalsIgnoreCase(token);
    }

    private Expression createOperation(String operation, List<Expression> arguments) {
        OperationMenu opMenu = OperationMenu.valueOf(operation.toUpperCase());
        return opMenu.createExpression(arguments);
    }
}
