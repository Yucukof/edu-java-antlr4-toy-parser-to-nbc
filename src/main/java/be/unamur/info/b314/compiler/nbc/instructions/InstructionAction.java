package be.unamur.info.b314.compiler.nbc.instructions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * @author Anthony DI STASIO
 */
@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class InstructionAction extends Instruction {
}