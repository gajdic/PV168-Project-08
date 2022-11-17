package cz.muni.fi.pv168.cashflowevidence.model.transaction;

import cz.muni.fi.pv168.cashflowevidence.model.category.Category;

import java.time.LocalDateTime;

public class RepeatingTransactionEverySecond extends RepeatingTransaction {



    public RepeatingTransactionEverySecond(TransactionType transactionType, String name, String description,
                                           double amount, Category category, LocalDateTime dateFrom, LocalDateTime dateTo,
                                           int frequencySeconds) {
        super(transactionType, name, description, amount, category, dateFrom.toLocalDate(), dateTo.toLocalDate(),
                frequencySeconds);
        this.setDateTimeFrom(dateFrom);
        this.setDateTimeTo(dateTo);
        this.nextCreateAt = dateFrom;
    }

    @Override
    protected void shiftNextCreateAt() {
        this.nextCreateAt = this.nextCreateAt.plusSeconds(this.getDays());
    }


}
