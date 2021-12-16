package be.unamur.info.b314.compiler.visitors;

import be.unamur.info.b314.compiler.GrammarParser;
import be.unamur.info.b314.compiler.listeners.Context;
import be.unamur.info.b314.compiler.local.UnitTestTemplate;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import be.unamur.info.b314.compiler.pils.exceptions.InvalidNumberOfIndexesException;
import be.unamur.info.b314.compiler.pils.values.references.VariableReference;
import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Hadrien BAILLY
 */
public class VariableReferenceVisitorTest extends UnitTestTemplate {

    @Test
    public void given_scalar_reference_when_visit_variable_then_expect_scalar_arrayElement() {

        final Context tables = new Context();
        final Variable variable = Variable.builder()
              .name("x")
              .type(Type.BOOLEAN)
              .build();
        tables.put(variable);

        final GrammarParser parser = getParser("x");
        final GrammarParser.VariableContext ctx = parser.variable();

        assertThat(ctx.exception).isNull();
        final VariableReferenceVisitor variableReferenceVisitor = new VariableReferenceVisitor(tables);
        final VariableReference element = variableReferenceVisitor.visitVariable(ctx);

        assertThat(element).isNotNull();
        assertThat(element.isValid()).isTrue();
        assertThat(element.getVariable().getDimensions().size()).isEqualTo(0);

        final Variable elementVariable = element.getVariable();
        assertThat(elementVariable.getName()).isEqualTo(variable.getName());
        assertThat(elementVariable.getType()).isEqualTo(variable.getType());
        assertThat(elementVariable.getDimensions().size()).isEqualTo(0);
        assertThat(elementVariable.getLevel()).isEqualTo(0);

    }

    @Test
    public void given_array_reference_when_visit_variable_then_expect_indexed_arrayElement() {

        final Context tables = new Context();
        final Variable variable = Variable.builder()
              .name("x")
              .type(Type.BOOLEAN)
              .dimensions(Arrays.asList(2, 2))
              .build();
        tables.put(variable);

        final GrammarParser parser = getParser("x[1,1]");
        final GrammarParser.VariableContext ctx = parser.variable();

        assertThat(ctx.exception).isNull();
        final VariableReferenceVisitor variableReferenceVisitor = new VariableReferenceVisitor(tables);
        final VariableReference element = variableReferenceVisitor.visitVariable(ctx);

        assertThat(element).isNotNull();
        assertThat(element.isValid()).isTrue();
        assertThat(element.getVariable().getDimensions().size()).isEqualTo(2);

        final Variable elementVariable = element.getVariable();
        assertThat(elementVariable.getName()).isEqualTo(variable.getName());
        assertThat(elementVariable.getType()).isEqualTo(variable.getType());
        assertThat(elementVariable.getDimensions().size()).isEqualTo(2);
        assertThat(elementVariable.getLevel()).isEqualTo(0);

    }

    @Test
    public void given_array_reference_without_index_when_visit_variable_then_expect_exception() {

        final Context tables = new Context();
        final Variable variable = Variable.builder()
              .name("x")
              .type(Type.BOOLEAN)
              .dimensions(Arrays.asList(2, 2))
              .build();
        tables.put(variable);

        final GrammarParser parser = getParser("x");
        final GrammarParser.VariableContext ctx = parser.variable();

        assertThat(ctx.exception).isNull();
        final VariableReferenceVisitor variableReferenceVisitor = new VariableReferenceVisitor(tables);
        assertThatThrownBy(() -> variableReferenceVisitor.visitVariable(ctx)).isInstanceOf(InvalidNumberOfIndexesException.class);

    }

    @Test
    public void given_scalar_reference_with_indexes_when_visit_variable_then_expect_exception() {

        final Context tables = new Context();
        final Variable variable = Variable.builder()
              .name("x")
              .type(Type.BOOLEAN)
              .build();
        tables.put(variable);

        final GrammarParser parser = getParser("x[1,1]");
        final GrammarParser.VariableContext ctx = parser.variable();

        assertThat(ctx.exception).isNull();
        final VariableReferenceVisitor variableReferenceVisitor = new VariableReferenceVisitor(tables);
        assertThatThrownBy(() -> variableReferenceVisitor.visitVariable(ctx)).isInstanceOf(InvalidNumberOfIndexesException.class);

    }

    @Test
    public void given_scalar_reference_with_invalid_number_of_indexes_when_visit_variable_then_expect_exception() {

        final Context tables = new Context();
        final Variable variable = Variable.builder()
              .name("x")
              .type(Type.BOOLEAN)
              .dimensions(Arrays.asList(2, 2))
              .build();
        tables.put(variable);

        final GrammarParser parser = getParser("x[1]");
        final GrammarParser.VariableContext ctx = parser.variable();

        assertThat(ctx.exception).isNull();
        final VariableReferenceVisitor variableReferenceVisitor = new VariableReferenceVisitor(tables);
        assertThatThrownBy(() -> variableReferenceVisitor.visitVariable(ctx)).isInstanceOf(InvalidNumberOfIndexesException.class);

    }
}