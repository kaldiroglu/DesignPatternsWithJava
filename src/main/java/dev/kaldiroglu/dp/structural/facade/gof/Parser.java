package dev.kaldiroglu.dp.structural.facade.gof;

/**
 * Subsystem class (GoF, p. 185): the parser. It reads tokens from a
 * {@link Scanner} and drives a {@link ProgramNodeBuilder} to construct the parse
 * tree.
 *
 * <p>Grammar of the tiny demo language (one statement per line):</p>
 * <pre>
 *   statement  := IDENT '=' expression | 'return' expression
 *   expression := term (('+' | '-') term)*
 *   term       := INT | IDENT
 * </pre>
 */
public class Parser {

    /** Parses everything the scanner produces into {@code builder}. */
    public void parse(Scanner scanner, ProgramNodeBuilder builder) {
        while (scanner.peek().kind() != Token.Kind.EOF) {
            if (scanner.peek().kind() == Token.Kind.NEWLINE) {
                scanner.next();
                continue;
            }
            builder.addStatement(parseStatement(scanner, builder));
        }
    }

    private ProgramNode parseStatement(Scanner scanner, ProgramNodeBuilder builder) {
        Token token = scanner.peek();
        if (token.kind() == Token.Kind.RETURN) {
            scanner.next();
            return builder.newReturn(parseExpression(scanner, builder));
        }
        Token identifier = expect(scanner, Token.Kind.IDENT);
        expect(scanner, Token.Kind.ASSIGN);
        return builder.newAssignment(identifier.text(), parseExpression(scanner, builder));
    }

    private ProgramNode parseExpression(Scanner scanner, ProgramNodeBuilder builder) {
        ProgramNode left = parseTerm(scanner, builder);
        while (scanner.peek().kind() == Token.Kind.PLUS
                || scanner.peek().kind() == Token.Kind.MINUS) {
            char operator = scanner.next().kind() == Token.Kind.PLUS ? '+' : '-';
            ProgramNode right = parseTerm(scanner, builder);
            left = builder.newBinaryOperator(operator, left, right);
        }
        return left;
    }

    private ProgramNode parseTerm(Scanner scanner, ProgramNodeBuilder builder) {
        Token token = scanner.next();
        return switch (token.kind()) {
            case INT -> builder.newConstant(token.asInt());
            case IDENT -> builder.newVariable(token.text());
            default -> throw new IllegalStateException("Expected a number or variable but found " + token);
        };
    }

    private Token expect(Scanner scanner, Token.Kind kind) {
        Token token = scanner.next();
        if (token.kind() != kind) {
            throw new IllegalStateException("Expected " + kind + " but found " + token);
        }
        return token;
    }
}
