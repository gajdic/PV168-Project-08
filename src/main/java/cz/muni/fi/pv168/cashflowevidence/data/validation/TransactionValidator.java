package cz.muni.fi.pv168.cashflowevidence.data.validation;

import cz.muni.fi.pv168.cashflowevidence.data.validation.common.DateTimeValidator;
import cz.muni.fi.pv168.cashflowevidence.data.validation.common.DoubleMinValueValidator;
import cz.muni.fi.pv168.cashflowevidence.data.validation.common.StringLengthValidator;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.RepeatingTransaction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.TransactionFrequency;
import static cz.muni.fi.pv168.cashflowevidence.data.validation.Validator.extracting;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class TransactionValidator implements Validator<Transaction> {
    // TODO: REMOVE ALL BUT validate()
    private String errorMessage = null;

    public boolean isValidName(String name) {
        if (name != null && !name.isBlank()) {
        return true;
        }

        errorMessage = "Specified name is invalid. Name cannot be empty.";
        return false;
    }

    public boolean isValidAmount(double amount) {
        if (amount > 0) {
            return true;
        }
        errorMessage = "Amount cannot be less than 0.";
        return false;
    }

    public boolean isValidDateTime(LocalDateTime dateTime) {
        if (dateTime.isBefore(LocalDateTime.now()) || dateTime.isEqual(LocalDateTime.now())) {
            return true;
        }

        errorMessage = "Specified date is invalid. Date cannot be after today.";
        return true;
    }

    public boolean isValidRepeat(LocalDate repeatFrom, LocalDate repeatTo) {
        if (repeatFrom != null && repeatFrom.isBefore(repeatTo)) {
            return true;
        }

        errorMessage = "'Date from' is either empty or after the 'Date to'.";
        return false;
    }

    public boolean isValidFrequency(TransactionFrequency frequency, int days) {
        if (frequency != TransactionFrequency.Custom || days > 0) {
            return true;
        }

        errorMessage = "Months cannot be less than 0.";
        return false;
    }

    public boolean isValidTransaction(Transaction transaction) {
        return isValidName(transaction.getName()) &&
                isValidAmount(transaction.getAmount()) &&
                isValidDateTime(transaction.getDateTime());
    }

    public boolean isValidRepeatingTransaction(RepeatingTransaction transaction) {
        return isValidTransaction(transaction) &&
                isValidRepeat(transaction.getDateFrom(), transaction.getDateTo()) &&
                isValidFrequency(transaction.getFrequency(), transaction.getDays());
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public ValidationResult validate(Transaction transaction) {
        var validators = List.of(
                extracting(Transaction::getName, new StringLengthValidator(1, 100, "Name")),
                extracting(Transaction::getDescription, new StringLengthValidator(0, 250, "Description")),
                extracting(Transaction::getAmount, new DoubleMinValueValidator(0, "Amount")),
                extracting(Transaction::getDateTime, new DateTimeValidator("Date"))
        );

        return Validator.compose(validators).validate(transaction);
    }
}
