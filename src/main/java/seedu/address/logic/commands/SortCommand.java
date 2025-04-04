package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_CRITERIA_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDED_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB_POSITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.Prefix;
import seedu.address.model.Model;

/**
 * Sort the applicant list in the address book.
 */
public class SortCommand extends Command {
    public static final String COMMAND_WORD = "sort";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sort the applicants based on [CRITERIA].\n"
            + "Accepted [CRITERIA]:   "
            + PREFIX_NAME + ": Sort by name.   "
            + PREFIX_EMAIL + ": Sort by email address.   "
            + PREFIX_ADDED_TIME + ": Sort by added time.   "
            + PREFIX_JOB_POSITION + ": Sort by job position.   "
            + PREFIX_STATUS + ": Sort by application status.\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_NAME + "\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_EMAIL;

    public static final String MESSAGE_SUCCESS = "Applicant list has been sorted successfully based on %1$s";

    private final Prefix prefix;
    private final String criteria;



    /**
     * Creates an SortCommand to add the specified {@code Prefix}
     */
    public SortCommand(Prefix prefix) throws CommandException {
        requireNonNull(prefix);
        this.prefix = prefix;
        if (prefix.equals(PREFIX_NAME)) {
            this.criteria = "Name";
        } else if (prefix.equals(PREFIX_EMAIL)) {
            this.criteria = "Email Address";
        } else if (prefix.equals(PREFIX_ADDED_TIME)) {
            this.criteria = "Added Time";
        } else if (prefix.equals(PREFIX_JOB_POSITION)) {
            this.criteria = "Job Position";
        } else if (prefix.equals(PREFIX_STATUS)) {
            this.criteria = "Application Status";
        } else {
            throw new CommandException(String.format(
                    MESSAGE_INVALID_CRITERIA_FORMAT, "sorting", SortCommand.MESSAGE_USAGE));
        }

    }

    /**
     * Returns a string describing the chosen sorting criteria.
     */
    public String getCriteria() {
        return criteria;
    }

    /**
     * Executes the sort command and updates the model's filtered list order.
     *
     * @param model The application's model.
     * @return A {@code CommandResult} indicating successful sorting.
     */
    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        model.sortPersons(this.prefix);

        return new CommandResult(String.format(MESSAGE_SUCCESS, this.criteria));
    }

    /**
     * Returns true if both SortCommands sort by the same prefix.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof SortCommand)) {
            return false;
        }

        SortCommand otherSortCommand = (SortCommand) other;
        return prefix.equals(otherSortCommand.prefix);
    }

    /**
     * Returns a string representation of this command for debugging.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("prefix", prefix)
                .toString();
    }
}

