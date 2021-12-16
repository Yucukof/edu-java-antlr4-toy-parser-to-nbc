package be.unamur.info.b314.compiler.pils.expressions;

import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.exceptions.DivisionByZeroException;
import be.unamur.info.b314.compiler.pils.exceptions.InvalidDataTypeException;
import be.unamur.info.b314.compiler.pils.exceptions.InvalidOperationException;
import be.unamur.info.b314.compiler.pils.values.Value;
import be.unamur.info.b314.compiler.pils.values.primitives.PrimitiveBoolean;
import be.unamur.info.b314.compiler.pils.values.primitives.PrimitiveInteger;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * Une opération est une fonction acceptant deux arguments de même type et exécutant une opération désignée:
 * <br> - Calcul mathématique sur des entiers ou
 * <br> - Comparaisons (entière, booléenne ou de case).
 * <br> Une opération est caractérisée par un symbole ({@link #token}) et un niveau de précédence ({@link #precedence}).
 *
 * @author Hadrien BAILLY
 * @see Type
 */
@Slf4j
@SuppressWarnings("DuplicatedCode")
public enum Operator {
    /**
     * L'opérateur de multiplication.
     */
    MUL("*", Type.INTEGER, 1, false, Collections.singletonList(Type.INTEGER), Operator::mul),
    /**
     * L'opérateur de division.
     */
    DIV("/", Type.INTEGER, 1, false, Collections.singletonList(Type.INTEGER), Operator::div),
    /**
     * L'opérateur modulo.
     */
    MOD("%", Type.INTEGER, 1, false, Collections.singletonList(Type.INTEGER), Operator::mod),
    /**
     * L'opérateur d'addition.
     */
    ADD("+", Type.INTEGER, 2, false, Collections.singletonList(Type.INTEGER), Operator::add),
    /**
     * L'opérateur de soustraction.
     */
    SUB("-", Type.INTEGER, 2, false, Collections.singletonList(Type.INTEGER), Operator::sub),
    /**
     * L'opérateur de comparaison supérieure.
     */
    GRT(">", Type.BOOLEAN, 3, true, Collections.singletonList(Type.INTEGER), Operator::grt),
    /**
     * L'opérateur de comparaison inférieure.
     */
    LSS("<", Type.BOOLEAN, 3, true, Collections.singletonList(Type.INTEGER), Operator::lss),
    /**
     * L'opérateur d'équivalence.
     */
    EQ("=", Type.BOOLEAN, 4, true, Arrays.asList(Type.INTEGER, Type.BOOLEAN, Type.SQUARE), Operator::eq),
    /**
     * L'opérateur de conjonction.
     */
    AND("and", Type.BOOLEAN, 5, false, Collections.singletonList(Type.BOOLEAN), Operator::and),
    /**
     * L'opérateur de disjonction.
     */
    OR("or", Type.BOOLEAN, 6, false, Collections.singletonList(Type.BOOLEAN), Operator::or);

    /**
     * Symbole représentant l'opération dans une expression.
     */
    private final String token;
    /**
     * Type de résultat retourné par l'opération dans une expression.
     */
    private final Type type;
    /**
     * Indique si l'opérateur est un opérateur de comparaison.
     */
    private final boolean comparator;
    /**
     * Niveau de précédence de l'opération dans une expression.
     * <br> Inspiré de la table de précédence de <a href="https://www.programiz.com/java-programming/operator-precedence#precedence-table">Programiz</a>.
     *
     * @see <a href="https://www.programiz.com/java-programming/operator-precedence#precedence-table">https://www.programiz.com/java-programming/operator-precedence#precedence-table</a>
     */
    private final int precedence;
    /**
     * Liste des types supportés par l'opérateur.
     */
    private final List<Type> supportedTypes;
    /**
     * Paire de valeurs utilisées dans l'évaluation de l'expression.
     */
    private final Function<Pair<Value, Value>, Value> mapper;

    Operator(final String token, final Type type, final int precedence, final boolean comparator, final List<Type> supportedTypes, final Function<Pair<Value, Value>, Value> mapper) {
        this.token = token;
        this.type = type;
        this.comparator = comparator;
        this.supportedTypes = supportedTypes;
        this.precedence = precedence;
        this.mapper = mapper;
    }

    private static Value or(final Pair<Value, Value> values) {
        final Value val1 = values.getLeft();
        final Value val2 = values.getRight();
        if (val1.isBoolean() && val2.isBoolean()) {
            log.trace("Performing [{}] OR [{}]", val1, val2);
            final boolean test = val1.getBoolValue() || val2.getBoolValue();
            return PrimitiveBoolean.builder()
                  .value(test)
                  .build();
        }
        throw new InvalidDataTypeException();
    }

    private static Value and(final Pair<Value, Value> values) {
        final Value val1 = values.getLeft();
        final Value val2 = values.getRight();
        if (val1.isBoolean() && val2.isBoolean()) {
            log.trace("Performing [{}] AND [{}]", val1, val2);
            final boolean test = val1.getBoolValue() && val2.getBoolValue();
            return PrimitiveBoolean.builder()
                  .value(test)
                  .build();
        }
        throw new InvalidDataTypeException();
    }

    private static Value eq(final Pair<Value, Value> values) {
        final Value val1 = values.getLeft();
        final Value val2 = values.getRight();
        if (val1.isInteger() && val2.isInteger()) {
            log.trace("Performing [{}] = [{}] (INTEGER)", val1, val2);
            final boolean test = val1.getIntValue().equals(val2.getIntValue());
            return PrimitiveBoolean.builder()
                  .value(test)
                  .build();
        }
        if (val1.isBoolean() && val2.isBoolean()) {
            log.trace("Performing [{}] = [{}] (BOOLEAN)", val1, val2);
            final boolean test = val1.getBoolValue().equals(val2.getBoolValue());
            return PrimitiveBoolean.builder()
                  .value(test)
                  .build();
        }
        if (val1.isSquare() && val2.isSquare()) {
            log.trace("Performing [{}] = [{}] (SQUARE)", val1, val2);
            final boolean test = val1.getSquareValue().equals(val2.getSquareValue());
            return PrimitiveBoolean.builder()
                  .value(test)
                  .build();
        }
        throw new InvalidDataTypeException();
    }

    private static Value lss(final Pair<Value, Value> values) {
        final Value val1 = values.getLeft();
        final Value val2 = values.getRight();
        if (val1.isInteger() && val2.isInteger()) {
            log.trace("Performing [{}] < [{}]", val1, val2);
            final boolean test = val1.getIntValue() < val2.getIntValue();
            return PrimitiveBoolean.builder()
                  .value(test)
                  .build();
        }
        throw new InvalidDataTypeException();
    }

    private static Value grt(final Pair<Value, Value> values) {
        final Value val1 = values.getLeft();
        final Value val2 = values.getRight();
        if (val1.isInteger() && val2.isInteger()) {
            log.trace("Performing [{}] > [{}]", val1, val2);
            final boolean test = val1.getIntValue() > val2.getIntValue();
            return PrimitiveBoolean.builder()
                  .value(test)
                  .build();
        }
        throw new InvalidDataTypeException();
    }

    private static Value add(final Pair<Value, Value> values) {
        final Value val1 = values.getLeft();
        final Value val2 = values.getRight();
        if (val1.isInteger() && val2.isInteger()) {
            log.trace("Performing [{}] + [{}]", val1, val2);
            final int sum = val1.getIntValue() + val2.getIntValue();
            return PrimitiveInteger.builder()
                  .value(sum)
                  .build();
        }
        throw new InvalidDataTypeException();
    }

    private static Value sub(final Pair<Value, Value> values) {
        final Value val1 = values.getLeft();
        final Value val2 = values.getRight();
        if (val1.isInteger() && val2.isInteger()) {
            log.trace("Performing [{}] - [{}]", val1, val2);
            final int sum = val1.getIntValue() - val2.getIntValue();
            return PrimitiveInteger.builder()
                  .value(sum)
                  .build();
        }
        throw new InvalidDataTypeException();
    }

    private static Value mul(final Pair<Value, Value> values) {
        final Value val1 = values.getLeft();
        final Value val2 = values.getRight();
        if (val1.isInteger() && val2.isInteger()) {
            log.trace("Performing [{}] * [{}]", val1, val2);
            final int sum = val1.getIntValue() * val2.getIntValue();
            return PrimitiveInteger.builder()
                  .value(sum)
                  .build();
        }
        throw new InvalidDataTypeException();
    }

    private static Value div(final Pair<Value, Value> values) {
        final Value val1 = values.getLeft();
        final Value val2 = values.getRight();
        if (val1.isInteger() && val2.isInteger()) {
            final int divisor = val2.getIntValue();
            if (divisor == 0) {
                throw new DivisionByZeroException();
            }
            log.trace("Performing [{}] / [{}]", val1, val2);
            final int sum = val1.getIntValue() / divisor;
            return PrimitiveInteger.builder()
                  .value(sum)
                  .build();
        }
        throw new InvalidDataTypeException();
    }

    private static Value mod(final Pair<Value, Value> values) {
        final Value val1 = values.getLeft();
        final Value val2 = values.getRight();
        if (val1.isInteger() && val2.isInteger()) {
            log.trace("Performing [{}] % [{}]", val1, val2);
            final int sum = val1.getIntValue() % val2.getIntValue();
            return PrimitiveInteger.builder()
                  .value(sum)
                  .build();
        }
        throw new InvalidDataTypeException();
    }

    /**
     * Résout un symbole donné dans l'opération correspondante.
     *
     * @param token un symbole textuel.
     * @return l'opération correspondante au symbole donné.
     * @throws InvalidOperationException si le symbole n'est pas reconnu.
     */
    public static Operator resolve(final String token) throws InvalidOperationException {
        return Arrays.stream(values())
              .filter(operation -> operation.matches(token))
              .findAny()
              .orElseThrow(() -> new InvalidOperationException("Cannot resolve token [" + token + "] into any known operation."));
    }

    /**
     * Vérifie si le symbole donné correspond à l'opération courante.
     *
     * @param token le symbole à évaluer.
     * @return vrai si le symbole correspond à celui de l'opération, faux sinon.
     */
    public boolean matches(final String token) {
        return this.token.equalsIgnoreCase(token);
    }

    public String getToken() {
        return token;
    }

    /**
     * Applique l'opération courante sur les deux valeurs passées en paramètre.
     *
     * @param val1 le premier opérande.
     * @param val2 le second opérande.
     * @return une valeur simple résultant de l'opération.
     * @see Value
     * @see Type
     */
    public Value apply(@NonNull final Value val1, @NonNull final Value val2) {
        return apply(new ImmutablePair<>(val1, val2));
    }

    /**
     * Applique l'opération courante sur les deux valerus passées en paramètre.
     *
     * @param operands les deux opérandes dans l'ordre.
     * @return une valeur simple résultant de l'opération.
     * @see Value
     * @see Type
     */
    public Value apply(@NonNull final Pair<Value, Value> operands) {
        return mapper.apply(operands);
    }

    /**
     * Vérifie si l'opération courante a précédence sur l'opération donné.
     *
     * @param operator l'opération à comparer
     * @return faux si l'opération est strictement moins prioritaire à celle donnée, vrai sinon.
     * @see Operator#comparePrecedence(Operator)
     */
    public boolean hasPrecedence(@NonNull final Operator operator) {
        return this.comparePrecedence(operator) >= 0;
    }

    /**
     * Compare le niveau de précédence entre l'opération courante et une opération donnée.
     *
     * @param operator l'opération à comparer à l'opération courante.
     * @return un entier strictement supérieur à 0 si l'opération courante a précédence,
     *       0 si les deux opérations ont même précédence ou
     *       un entier strictement inférieur à zéro si l'opération courante n'a pas précédence.
     */
    public int comparePrecedence(@NonNull final Operator operator) {
        return operator.precedence - precedence;
    }

    /**
     * Retourne le type effectif de l'opération réalisée par un opérateur.
     *
     * @return un type.
     */
    public Type getType() {
        return type;
    }

    /**
     * Vérifie si l'opérande a un type compatible avec l'opérateur.
     *
     * @param operand l'opérande à contrôler.
     * @return vrai si l'opérande a un type faisant partie des types acceptés par l'opérateur, faux sinon.
     */
    public boolean accept(final Expression operand) {
        return supportedTypes.contains(operand.getType());
    }

    /**
     * Indique si le résultat de l'opération commandée par l'opérateur est de type booléen.
     *
     * @return vrai si l'opérateur renvoie un booléen, faux sinon.
     */
    public boolean isBoolean() {
        return type.equals(Type.BOOLEAN);
    }

    /**
     * Indique si le résultat de l'opération commandée par l'opérateur est de type entier.
     *
     * @return vrai si l'opérateur renvoie un entier, faux sinon.
     */
    public boolean isInteger() {
        return type.equals(Type.INTEGER);
    }

    /**
     * Indique si le résultat de l'opération commandée par l'opérateur est de type case.
     *
     * @return vrai si l'opérateur renvoie une case, faux sinon.
     */
    public boolean isSquare() {
        return type.equals(Type.SQUARE);
    }

    /**
     * Indique si l'opération commandée par l'opérateur induit une comparaison entre les deux membres.
     *
     * @return vrai si l'expression résulte en une comparaison, faux sinon.
     */
    public boolean isComparator() {
        return comparator;
    }
}
