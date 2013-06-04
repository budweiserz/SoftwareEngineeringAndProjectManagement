package at.ticketline.kassa.ui;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

public class UIUtilities {

    /**
     * Generates german constraint-violation message for user output.
     * 
     * @param c
     *            != null
     * @return constraint violations, separated by "\n", occurences of
     *         "may not be null" replaced by "muss ausgefüllt werden"
     */
    public static String getReadableConstraintViolations(ConstraintViolationException c) {

        StringBuilder sb = new StringBuilder("Die eingegebenen Daten weisen folgende Fehler auf:\n");

        for (ConstraintViolation<?> cv : c.getConstraintViolations()) {
            sb.append(cv.getPropertyPath().toString().toUpperCase()).append(" ").append(cv.getMessage() + "\n");
        }

        return sb.toString().replaceAll("may not be null", "muss ausgefüllt sein");
    }
}