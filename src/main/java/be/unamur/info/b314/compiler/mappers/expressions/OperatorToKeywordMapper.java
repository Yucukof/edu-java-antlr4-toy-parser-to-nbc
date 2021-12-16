package be.unamur.info.b314.compiler.mappers.expressions;

import be.unamur.info.b314.compiler.nbc.keywords.Comparison;
import be.unamur.info.b314.compiler.nbc.keywords.Keyword;
import be.unamur.info.b314.compiler.nbc.keywords.Logic;
import be.unamur.info.b314.compiler.nbc.keywords.Math;
import be.unamur.info.b314.compiler.pils.expressions.Operator;
import lombok.NonNull;

import java.util.function.Function;

/**
 * @author Hadrien BAILLY
 */
public class OperatorToKeywordMapper implements Function<Operator, Keyword> {

    @Override
    public Keyword apply(@NonNull final Operator operator) {
        switch (operator) {
            case ADD:
                return Math.ADD;
            case SUB:
                return Math.SUB;
            case MUL:
                return Math.MUL;
            case DIV:
                return Math.DIV;
            case MOD:
                return Math.MOD;
            case OR:
                return Logic.OR;
            case AND:
                return Logic.AND;
            case EQ:
            case GRT:
            case LSS:
                return Comparison.CMP;
            default:
                throw new IllegalArgumentException(String.format("Cannot map operator [%s] to NBC keyword", operator));
        }
    }
}
