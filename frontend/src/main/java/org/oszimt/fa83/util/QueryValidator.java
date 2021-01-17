package org.oszimt.fa83.util;

import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;
import org.oszimt.fa83.ValidationException;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validator class to validate Frontend input.
 */
final public class QueryValidator {

    private static final Pattern EMAIL_PATTERN;
    private static final String CITY = "city";
    private static final String EMAIL = "email";
    private static final String NAME = "queryName";
    private static final String PRICE_TO = "priceTo";
    private static final String RADIUS = "radius";
    private static final String SPACE = "space";

    private StringBuilder builder;
    static {
        EMAIL_PATTERN = Pattern.compile("^[\\+\\w\\.=-]+@((([\\w-]+\\.)+([\\w]{2,})))$");
    }

    public QueryValidator(StringBuilder stringBuilder) {
        this.builder = stringBuilder;
    }

    /**
     * validate list of given frontend fields.
     * @param allFields the fields to validate.
     * @throws ValidationException thrown when invalid input was given.
     */
    public void validate(Collection<TextField> allFields) throws ValidationException {
        for (TextField field : allFields){
            String fieldId = field.getId();
            if (fieldId.equals(CITY)) {
                if (StringUtils.isEmpty(field.getText())){
                    this.builder.append("Bitte eine Stadt angeben.");
                    this.builder.append("\n");
                }
            }
            if (fieldId.equals(NAME)) {
                if (StringUtils.isEmpty(field.getText())){
                    builder.append("Bitte einen Namen für den Auftrag angeben.");
                    this.builder.append("\n");
                }
            }
            if (fieldId.equals(PRICE_TO) || fieldId.equals(RADIUS) || fieldId.equals(SPACE)) {
                parseDouble(field.getText(), field.getPromptText());
            }
            if (fieldId.equals(EMAIL)) {
                if (!StringUtils.isEmpty(field.getText())){
                    checkEmail(field.getText());
                }
            }
            }
        if (!StringUtils.isEmpty(builder.toString())) {
            throw new ValidationException(builder.toString());
        }
    }

    /**
     * validate a given email.
     * @param email the email to validate.
     */
    public void checkEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (!matcher.matches()) {
            this.builder.append("Bitte korrekte Email angeben");
            this.builder.append("\n");
        }
    }

    /**
     * static implementation to validate email string.
     * @param email the email to validate.
     * @throws ValidationException thrown is invalid email was given.
     */
    public static void validateEmail(String email) throws ValidationException {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if (!matcher.matches()) {
            throw new ValidationException("Bitte korrekte Email angeben");
        }
    }

    private void parseDouble(String toBeParsed, String field) {
        Double parsed = null;
        try {
            if (!StringUtils.isEmpty(toBeParsed)) {
                parsed = Double.parseDouble(toBeParsed);
            }
        } catch (Exception e) {
            this.builder.append(field).append(" muss eine Nummer enthalten.");
            this.builder.append("\n");
        }
    }

}
