package sheetimpl.utils;

import api.Expression;
import sheetimpl.expression.type.Bool;
import sheetimpl.expression.type.Number;
import sheetimpl.expression.type.Text;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ExpressionUtils {
    public static void main(String[] args) {
//        System.out.println(tokenizeExpression("{PLUS,2,3}")); // Output: 5
//        System.out.println(tokenizeExpression("{MINUS,{PLUS,4,5},{POW,2,3}}")); // Output: 1
//        System.out.println(tokenizeExpression("{CONCAT,Hello,World}")); // Output: HelloWorld
//        System.out.println(tokenizeExpression("{ABS,{MINUS,4,5}}")); // Output: 1
//        System.out.println(tokenizeExpression("{POW,2,3}")); // Output: 8
//        System.out.println(tokenizeExpression("{SUB,hello,2,3}")); // Output: 8
//        System.out.println(tokenizeExpression("{MOD,4, 2}")); // Output: 0
//        System.out.println(tokenizeExpression("5"));
//        System.out.println(tokenizeExpression("BLABLBALLBA"));
//        Expression exp = buildExpressionFromString("{CONCAT,Hello,World}");
//        System.out.println(exp.evaluate().extractValueWithExpectation(exp.evaluate().getCellType().getType()));

    }
    public static Expression buildExpressionFromString(String someExpression){
        Node tokenized = tokenizeExpression(someExpression.trim());
        return buildExpression(tokenized);
    }


    private static class Node {
        String value;
        List<Node> children;

        Node(String value, List<Node> children) {
            this.value = value;
            this.children = children;
        }

        public String getValue() {
            return value;
        }

        public List<Node> getChildren() {
            return children;
        }

    }

    public static Expression buildExpression(Node expression) {
        Expression resultExpression;
        List<Expression> argumentsList = new LinkedList<>();

        if((expression.children != null && !expression.children.isEmpty())) { // IM AN OPERATION
            for(int i = 0; i < expression.children.size(); i++) {
                argumentsList.add(buildExpression(expression.children.get(i)));
            }

            OperationMenu operation = OperationMenu.valueOf(expression.value.toUpperCase());
            resultExpression = operation.createExpression(argumentsList);
        } else { // IM A LEAF
            resultExpression = parseExpression(expression.value);
        }

        return resultExpression;
    }

    public static Expression parseExpression(String expression) {
        String numberPattern = "^\\d+(\\.\\d+)?$";
        Expression parsingResult;

        try {
            if(expression.matches(numberPattern)) {
                parsingResult = new Number(
                        Double.parseDouble(expression));
            } else if (expression.equalsIgnoreCase("true") || expression.equalsIgnoreCase("false")) {
                parsingResult = new Bool(
                        Boolean.parseBoolean(expression));
            } else {
                parsingResult = new Text(expression);
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }

        return parsingResult;
    }

    public static Node tokenizeExpression(String input) {
        input = input;

        if (input.startsWith("{") && input.endsWith("}")) {
            int commaIndex = input.indexOf(',');
            String function = input.substring(1, commaIndex);
            Node root = new Node(function, new ArrayList<>());

            return tokenizeSubExpression(root, input.substring(1, input.length() - 1));
        } else {
            return new Node(input, null);
        }
    }

    private static Node tokenizeSubExpression(Node root, String input) {
        int commaIndex = input.indexOf(','), bracketCounter = 0;
        String argument;

        for(int i = commaIndex + 1; i < input.length(); i++) {
            if(input.charAt(i) == '{') {
                bracketCounter++;
            } else if (input.charAt(i) == '}') {
                bracketCounter--;
            } else if (input.charAt(i) == ',' && bracketCounter == 0) {
                argument = input.substring(commaIndex + 1, i);
                commaIndex = i;
                root.children.add(tokenizeExpression(argument));
            }
        }

        argument = input.substring(commaIndex + 1);
        root.children.add(tokenizeExpression(argument));

        return root;
    }
}


