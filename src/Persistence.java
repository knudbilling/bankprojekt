import java.util.List;

public interface Persistence {

    void addBank(Bank bank);

    /**
     * Update the information about the bank in the persistent storage.
     * @param bank The bank to read information from.
     */
    void updateBank(Bank bank);

    void addCustomer(Bank bank, Customer customer);

    void updateCustomer(Bank bank, Customer customer);

    void addAccount(Bank bank, Account account);

    void updateAccount(Bank bank, Account account);

    void addTransaction(Bank bank, Transaction transaction);

    void save(Bank bank);

    Bank load(String registrationNumber);

    /**
     * Reset persistent storage so that it contains no data.
     */
    void resetPersistence();
}
