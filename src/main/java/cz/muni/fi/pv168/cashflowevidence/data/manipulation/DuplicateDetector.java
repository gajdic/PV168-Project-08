package cz.muni.fi.pv168.cashflowevidence.data.manipulation;

import cz.muni.fi.pv168.cashflowevidence.data.storage.repository.Repository;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.RepeatingTransaction;
import cz.muni.fi.pv168.cashflowevidence.model.transaction.Transaction;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class DuplicateDetector {

    public final Repository<Transaction> transactionRepository;

    public DuplicateDetector(Repository<Transaction> transactionRepository) {
        this.transactionRepository = transactionRepository;
    }


    private List<Transaction> getTransactionsToImport(Collection<Transaction> importedTransactions) {
        List<Transaction> duplicates = findDuplicate(importedTransactions);
        List<Transaction> transactionsToImport = new ArrayList<>(importedTransactions
                .stream()
                .filter(t -> !duplicates.contains(t))
                .toList());

        outerloop:
        for (Transaction transaction : duplicates) {
            int option = confirmDuplicateImports(
                    String.format(
                            "You are trying to import duplicate transaction %s. How do you want to continue?",
                            transaction.getName()));
            switch (option) {
                case 0:
                    transactionsToImport.add(transaction);
                    break;
                case 1:
                    transactionsToImport.addAll(duplicates);
                    break outerloop;
                case 2:
                    transactionsToImport.removeAll(duplicates);
                    break outerloop;
                case 3:
                    transactionsToImport.clear();
                    break outerloop;
            }
        }
        return transactionsToImport;
    }

    private List<Transaction> findDuplicate(Collection<Transaction> importedTransactions) {
        List<Transaction> storedTransactions = transactionRepository.findAll();
        return  storedTransactions.stream()
                .filter(st -> importedTransactions.stream().anyMatch(it -> Objects.equals(st, it)))
                .toList();
    }

    private int confirmDuplicateImports(String message) {
        Object[] options = {"Import", "Import all","Import only non duplicate", "Do not import any"};

        return  JOptionPane.showOptionDialog(
                null,
                message,
                "Warning",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[2]);
    }
}
