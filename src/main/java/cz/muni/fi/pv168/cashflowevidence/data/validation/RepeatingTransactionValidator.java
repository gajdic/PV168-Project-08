package cz.muni.fi.pv168.cashflowevidence.data.validation;

import cz.muni.fi.pv168.cashflowevidence.data.validation.RepeatingTransaction.FrequencyValidator;
import cz.muni.fi.pv168.cashflowevidence.data.validation.RepeatingTransaction.TwoDatesValidator;
import cz.muni.fi.pv168.cashflowevidence.data.validation.common.DateTimeValidator;
import cz.muni.fi.pv168.cashflowevidence.data.validation.common.DoubleMinValueValidator;
import cz.muni.fi.pv168.cashflowevidence.data.validation.common.StringLengthValidator;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.RepeatingTransaction;

import static cz.muni.fi.pv168.cashflowevidence.data.validation.Validator.extracting;

import java.util.List;

public class RepeatingTransactionValidator implements Validator<RepeatingTransaction>{

    @Override
    public ValidationResult validate(RepeatingTransaction repeatingTransaction) {
        var validators = List.of(
                extracting(RepeatingTransaction::getName, new StringLengthValidator(1, 100, "Transaction name")),
                extracting(RepeatingTransaction::getDescription, new StringLengthValidator(0, 250, "Transaction description")),
                extracting(RepeatingTransaction::getAmount, new DoubleMinValueValidator(0, "Transaction amount")),
                extracting(RepeatingTransaction::getDateTime, new DateTimeValidator("Transaction date")),
                extracting(RepeatingTransaction::getFrequency, new FrequencyValidator("Transaction frequency", repeatingTransaction.getDays())),
                extracting(RepeatingTransaction::getDateFrom, new TwoDatesValidator("Transaction from, to", repeatingTransaction.getDateTo()))
        );

        return Validator.compose(validators).validate(repeatingTransaction);
    }
}
