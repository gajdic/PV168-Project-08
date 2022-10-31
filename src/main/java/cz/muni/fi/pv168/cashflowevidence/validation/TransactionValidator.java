package cz.muni.fi.pv168.cashflowevidence.validation;

import cz.muni.fi.pv168.cashflowevidence.model.Transaction;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TransactionValidator {

    public static boolean isValidName(String name) {
        return name != null && !name.isBlank();
    }

    public static boolean isValidAmount(double amount) {
        return amount > 0;
    }

    public static boolean isValidDateTime(LocalDateTime dateTime) {
        return dateTime.isBefore(LocalDateTime.now());
    }

    public static boolean isValidRepeat(LocalDate repeatFrom, LocalDate repeatTo) {
        return repeatFrom != null && repeatFrom.isBefore(repeatTo);
    }

    public static boolean isValidFrequency(int frequency) {
        return frequency > 0;
    }

    public static boolean isValid(Transaction transaction) {
        return isValidName(transaction.getName()) &&
                isValidAmount(transaction.getAmount()) &&
                isValidDateTime(transaction.getDateTime()) &&
                (!transaction.isRepeating() ||
                        (isValidRepeat(transaction.getRepeatFrom(), transaction.getRepeatTo()) &&
                                isValidFrequency(transaction.getFrequency())));
    }
}
