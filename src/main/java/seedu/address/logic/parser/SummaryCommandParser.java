package seedu.address.logic.parser;

import seedu.address.logic.commands.SearchCommand;
import seedu.address.logic.commands.SummaryCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.applicant.IdentifierPredicate;

import java.util.List;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.ParserUtil.extractPredicates;
import static seedu.address.logic.parser.ParserUtil.predicateMapping;

public class SummaryCommandParser implements Parser<SummaryCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the {@code SearchCommand}
     * and returns a {@code SearchCommand} object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format.
     */

    @Override
    public SummaryCommand parse(String args) throws ParseException {
        // Note: There can be no predicates i.e. Summarize all
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args, predicateMapping.keySet().toArray(new Prefix[0]));
        List<IdentifierPredicate> predicates = extractPredicates(argMultimap);
        return new SummaryCommand(predicates);
    }
}
