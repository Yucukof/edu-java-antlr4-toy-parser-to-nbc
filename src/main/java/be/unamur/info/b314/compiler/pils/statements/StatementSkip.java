package be.unamur.info.b314.compiler.pils.statements;

import be.unamur.info.b314.compiler.mappers.SpaceRequirement;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * @author Hadrien BAILLY
 */
@Slf4j
public class StatementSkip implements Statement {

    @Override
    public String toString() {
        return "skip";
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public SpaceRequirement getSpaceRequirement() {
        return SpaceRequirement.NONE;
    }

    @Override
    public Optional<StatementNext> run() {
        log.trace("Running [{}]", this);
        return Optional.empty();
    }
}
