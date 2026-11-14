package utils;


import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;

import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@UtilityClass
public class DateParser {

    private LocalDate formatPolicyStartDateAsString(String dateString) {
        if (dateString == null || dateString.isEmpty() || dateString.isBlank()) {
            throw new InvalidParameterException("Date is not provided");
        }

        try {
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("dd MMMM yyyy");
            return LocalDate.parse(dateString, formatter);
        } catch (DateTimeException e) {
            throw new InvalidParameterException(
                    "Invalid date format: " + dateString
            );
        }
    }

    private Period parsePolicyPeriod(String policyPeriodString) {
        if (policyPeriodString == null || policyPeriodString.isEmpty()) {
            throw new IllegalArgumentException(
                    "Policy period string for renewal is null or empty"
            );
        }

        int months = 0;
        int days = 0;

        Pattern pattern = Pattern.compile(
                "(?<months>\\d+)\\smonth(s)?(?:\\s(?<days>\\d+)\\sday(s)?)?"
        );
        Matcher matcher = pattern.matcher(policyPeriodString);

        if (matcher.find()) {
            months = Integer.parseInt(matcher.group("months"));
            String daysGroup = matcher.group("days");
            if (daysGroup != null && !daysGroup.isEmpty()) {
                days = Integer.parseInt(daysGroup);
            }
        } else {
            throw new IllegalArgumentException(
                    "Invalid policy period format: " + policyPeriodString
            );
        }

        return Period.of(0, months, days);
    }

    public static String calculateEndPolicyDate(
            String startPolicyDateString,
            String policyPeriodString
    ) {
        LocalDate startPolicyDate =
                formatPolicyStartDateAsString(startPolicyDateString);

        LocalDate endPolicyDate =
                startPolicyDate.plus(parsePolicyPeriod(policyPeriodString));

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd MMMM yyyy");

        return endPolicyDate.format(formatter);
    }

    public static String formatDateMDYToString(LocalDate date) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return date.format(formatter);
    }

    public static String formatDateDMYToString(LocalDate date) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }

    public static String formatDateYMDToString(LocalDate date) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return date.format(formatter);
    }

    public static LocalDate formatStringToDateMDY(String dateString) {
        try {
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("MM/dd/yyyy");
            return LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            throw new DateTimeParseException(
                    e.getMessage(),
                    e.getParsedString(),
                    e.getErrorIndex()
            );
        }
    }

    public static LocalDate formatStringToDateDMY(String dateString) {
        try {
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            throw new DateTimeParseException(
                    e.getMessage(),
                    e.getParsedString(),
                    e.getErrorIndex()
            );
        }
    }

    public static String timeStampText() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String formattedNow = now.format(formatter);

        StringBuilder charConvert = new StringBuilder();
        for (char c : formattedNow.toCharArray()) {
            if (Character.isLetter(c)) {
                charConvert.append(c);
            } else {
                charConvert.append((char) ('A' + (c - '0')));
            }
        }

        return charConvert.toString();
    }

    public static String timeStampNumber(int requiredLength) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String formattedNow = now.format(formatter);

        if (requiredLength >= formattedNow.length() - 1) {
            formattedNow =
                    formattedNow +
                            formattedNow.repeat(
                                    formattedNow.length() % requiredLength
                            );
        }

        return formattedNow.substring(
                formattedNow.length() - requiredLength
        );
    }

    public static String timeStampNumber() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return now.format(formatter);
    }

    public boolean verifyDate(
            String expectedDateStr,
            WebElement creationDateElement
    ) {
        try {
            String actualDateStr =
                    creationDateElement.getText();

            System.out.println(actualDateStr);

            SimpleDateFormat expectedFormat =
                    new SimpleDateFormat("dd/MM/yyyy");

            return expectedFormat.parse(expectedDateStr)
                    .equals(expectedFormat.parse(actualDateStr));

        } catch (Exception e) {
            return false;
        }
    }
}