package seedu.address.model.applicant;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents an Applicant's rating
 * Has value of an integer in range 1 to 5, unless not assigned yet (represented by value of -1)
 * Guarantees: immutable; has integer in range 1 to 5, or -1, as value
 */
public class Rating {
    public static final String MESSAGE_CONSTRAINTS =
            "Ratings should be on a scale of 1 to 5, with 5 being the most promising. (-1 can be given for a Unassigned rating)";

    /*
     * Assigned rating should be an integer in range 1 to 5.
     * Accepts -1 as representation of unassigned rating
     */
    public static final String VALIDATION_REGEX = "^[1-5]$|^-1$";

    public final String value;

    /**
     * Constructs a {@code Rating}.
     *
     * @param value A valid rating.
     */
    public Rating(String value) {
        requireNonNull(value);
        checkArgument(isValidRating(value), MESSAGE_CONSTRAINTS);
        this.value = value;
    }

    /**
     * Returns true if a given string is a valid rating.
     */
    public static boolean isValidRating(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Compares this rating, ratings with value -1 are treated as lowest and always placed last.
     */
    public int compareTo(Rating otherRating, boolean isAscending) {
        int thisValue = Integer.parseInt(this.value);
        int otherValue = Integer.parseInt(otherRating.value);
        if (thisValue == -1 || otherValue == -1) {
            return thisValue == otherValue ? 0 : (thisValue == -1 ? 1 : -1);
        }
        return isAscending ? Integer.compare(thisValue, otherValue) : Integer.compare(otherValue, thisValue);
    }
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Rating otherRating)) {
            return false;
        }

        return this.value.equals(otherRating.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        if (value.equals("-1")) {
            return "Unassigned";
        }
        return value + " / 5";
    }

}
