package be.unamur.info.b314.compiler.mappers.values.booleans;

import be.unamur.info.b314.compiler.mappers.Context;
import be.unamur.info.b314.compiler.nbc.structures.StructureSimple;
import be.unamur.info.b314.compiler.pils.declarations.Function;
import be.unamur.info.b314.compiler.pils.declarations.Type;
import be.unamur.info.b314.compiler.pils.declarations.Variable;
import be.unamur.info.b314.compiler.pils.expressions.Expression;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionLeaf;
import be.unamur.info.b314.compiler.pils.expressions.ExpressionNode;
import be.unamur.info.b314.compiler.pils.expressions.Operator;
import be.unamur.info.b314.compiler.pils.values.booleans.Negation;
import be.unamur.info.b314.compiler.pils.values.references.FunctionCall;
import be.unamur.info.b314.compiler.pils.values.references.VariableReference;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hadrien BAILLY
 */
public class NegationPreprocessorTest {

    private Context context;
    private NegationPreprocessor mapper;

    @Before
    public void before() throws Exception {
        context = new Context();
        mapper = context.mappers.getNegationPreprocessor();
    }

    @Test
    public void give_simple_negation_when_apply_preprocessor_then_expect_single_statement() {

        final Negation negation = Negation.builder()
              .expression(ExpressionLeaf.TRUE)
              .build();
        assertThat(negation.isValid()).isTrue();
        assertThat(negation.toString()).isEqualTo("not (true)");

        context.memory.allocate(0,1,0);

        final StructureSimple structure = mapper.apply(negation);
        assertThat(structure).isNotNull();
        assertThat(structure.isValid()).isTrue();
        assertThat(structure.getStatements().size()).isEqualTo(1);
        assertThat(structure.toString()).isEqualTo("neg boolean1, 1");
    }

    @Test
    public void give_complex_constant_negation_when_apply_preprocessor_then_expect_single_statement() {

        final Expression expression = ExpressionNode.builder()
              .operator(Operator.EQ)
              .left(ExpressionLeaf.ONE)
              .right(ExpressionLeaf.ZERO)
              .build();
        assertThat(expression.isValid()).isTrue();

        final Negation negation = Negation.builder()
              .expression(expression)
              .build();
        assertThat(negation.isValid()).isTrue();
        assertThat(negation.toString()).isEqualTo("not (1 = 0)");

        context.memory.allocate(0,1,0);

        final StructureSimple structure = mapper.apply(negation);
        assertThat(structure).isNotNull();
        assertThat(structure.isValid()).isTrue();
        assertThat(structure.getStatements().size()).isEqualTo(1);
        assertThat(structure.toString()).isEqualTo("neg boolean1, 0");
    }

    @Test
    public void give_simple_variable_negation_when_apply_preprocessor_then_expect_single_statement() {

        final Variable variable = Variable.builder()
              .name("x")
              .type(Type.BOOLEAN)
              .build();
        assertThat(variable.isValid()).isTrue();

        final VariableReference reference = VariableReference.builder()
              .variable(variable)
              .build();
        assertThat(reference.isValid()).isTrue();

        final Expression expression = new ExpressionLeaf(reference);
        assertThat(expression.isValid()).isTrue();

        final Negation negation = Negation.builder()
              .expression(expression)
              .build();
        assertThat(negation.isValid()).isTrue();
        assertThat(negation.toString()).isEqualTo("not (x)");

        context.memory.allocate(0,1,0);

        final StructureSimple structure = mapper.apply(negation);
        assertThat(structure).isNotNull();
        assertThat(structure.isValid()).isTrue();
        assertThat(structure.getStatements().size()).isEqualTo(1);
        assertThat(structure.toString()).isEqualTo("neg boolean1, x");
    }

    @Test
    public void give_complex_variable_negation_when_apply_preprocessor_then_expect_double_statement() {

        final Variable variable = Variable.builder()
              .name("x")
              .type(Type.INTEGER)
              .build();
        assertThat(variable.isValid()).isTrue();

        final VariableReference reference = VariableReference.builder()
              .variable(variable)
              .build();
        assertThat(reference.isValid()).isTrue();

        final Expression leaf = new ExpressionLeaf(reference);
        assertThat(leaf.isValid()).isTrue();

        final Expression expression = ExpressionNode.builder()
              .operator(Operator.EQ)
              .left(leaf)
              .right(ExpressionLeaf.ZERO)
              .build();
        assertThat(expression.isValid()).isTrue();

        final Negation negation = Negation.builder()
              .expression(expression)
              .build();
        assertThat(negation.isValid()).isTrue();
        assertThat(negation.toString()).isEqualTo("not (x = 0)");

        context.memory.allocate(0,1,0);

        final StructureSimple structure = mapper.apply(negation);
        assertThat(structure).isNotNull();
        assertThat(structure.isValid()).isTrue();
        assertThat(structure.getStatements().size()).isEqualTo(2);
        assertThat(structure.toString()).isEqualTo("cmp EQ, boolean1, x, 0\nneg boolean1, boolean1");
    }

    @Test
    public void give_function_call_with_no_arg_negation_when_apply_preprocessor_then_expect_something() {

        final Function function = Function.builder()
              .name("f")
              .type(Type.BOOLEAN)
              .output(ExpressionLeaf.TRUE)
              .build();
        assertThat(function.isValid()).isTrue();

        context.mappers.getFunctionMapper().apply(function);

        final FunctionCall call = FunctionCall.builder()
              .function(function)
              .build();
        assertThat(call.isValid()).isTrue();

        final Expression expression = new ExpressionLeaf(call);
        assertThat(expression.isValid()).isTrue();

        final Negation negation = Negation.builder()
              .expression(expression)
              .build();
        assertThat(negation.isValid()).isTrue();
        assertThat(negation.toString()).isEqualTo("not (f())");

        context.memory.allocate(negation.getSpaceRequirement());

        final StructureSimple structure = mapper.apply(negation);
        assertThat(structure).isNotNull();
        assertThat(structure.isValid()).isTrue();
        assertThat(structure.getStatements().size()).isEqualTo(2);
        assertThat(structure.toString()).isEqualTo("call f\nneg boolean1, f_result");
    }

    @Test
    public void give_function_call_with_simple_arg_negation_when_apply_preprocessor_then_expect_something() {

        final Variable variable = Variable.builder()
              .name("x")
              .type(Type.SQUARE)
              .build();
        variable.set(ExpressionLeaf.DIRT);
        assertThat(variable.isValid()).isTrue();

        final Function function = Function.builder()
              .name("f")
              .type(Type.BOOLEAN)
              .argument(variable)
              .output(ExpressionLeaf.TRUE)
              .build();
        assertThat(function.isValid()).isTrue();

        context.mappers.getFunctionMapper().apply(function);

        final FunctionCall call = FunctionCall.builder()
              .function(function)
              .argument(ExpressionLeaf.DIRT)
              .build();
        assertThat(call.isValid()).isTrue();

        final Expression expression = new ExpressionLeaf(call);
        assertThat(expression.isValid()).isTrue();

        final Negation negation = Negation.builder()
              .expression(expression)
              .build();
        assertThat(negation.isValid()).isTrue();
        assertThat(negation.toString()).isEqualTo("not (f(dirt))");

        context.memory.allocate(negation.getSpaceRequirement());
        final StructureSimple structure = mapper.apply(negation);
        assertThat(structure).isNotNull();
        assertThat(structure.isValid()).isTrue();
        assertThat(structure.getStatements().size()).isEqualTo(3);
        assertThat(structure.toString()).isEqualTo("mov f_x, 0\ncall f\nneg boolean1, f_result");
    }
//
    @Test
    public void give_function_call_with_complex_arg_negation_when_apply_preprocessor_then_expect_something() {

        final Variable variable = Variable.builder()
              .name("x")
              .type(Type.BOOLEAN)
              .build();
        variable.set(ExpressionLeaf.TRUE);
        assertThat(variable.isValid()).isTrue();

        final Function function = Function.builder()
              .name("f")
              .type(Type.BOOLEAN)
              .argument(variable)
              .output(ExpressionLeaf.TRUE)
              .build();
        assertThat(function.isValid()).isTrue();
        context.mappers.getFunctionMapper().apply(function);

        final Expression node = ExpressionNode.builder()
              .operator(Operator.EQ)
              .left(ExpressionLeaf.DIRT)
              .right(ExpressionLeaf.ROCK)
              .build();

        final FunctionCall call = FunctionCall.builder()
              .function(function)
              .argument(node)
              .build();
        assertThat(call.isValid()).isTrue();

        final Expression expression = new ExpressionLeaf(call);
        assertThat(expression.isValid()).isTrue();

        final Negation negation = Negation.builder()
              .expression(expression)
              .build();
        assertThat(negation.isValid()).isTrue();
        assertThat(negation.toString()).isEqualTo("not (f(dirt = rock))");

        context.memory.allocate(negation.getSpaceRequirement());

        final StructureSimple structure = mapper.apply(negation);
        assertThat(structure).isNotNull();
        assertThat(structure.isValid()).isTrue();
        assertThat(structure.getStatements().size()).isEqualTo(3);
        assertThat(structure.toString()).isEqualTo("mov f_x, 0\ncall f\nneg boolean1, f_result");
    }
}