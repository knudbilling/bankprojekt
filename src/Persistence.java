import java.util.List;

/**
 * @author Knud Billing
 */
public interface Persistence {
    /**
     * Update the information about the bank in the persistent storage.
     * @param bank The bank to read information from.
     * @return True if successful, otherwise false.
     */
    boolean updateBank(Bank bank);

    /**
     *  Add a customer to persistent storage.
     * @param customer
     * @return  True if successful, otherwise false.
     */
    boolean addCustomer(Customer customer);

    /**
     *
     * @param customer
     * @return True if successful, otherwise false.
     */
    boolean updateCustomer(Customer customer);

    /**
     * Add an account to the persistent storage.
     * @param account
     * @return True if successful, otherwise false.
     */
    boolean addAccount(Account account);

    /**
     *
     * @param account
     * @return True if successful, otherwise false.
     */
    boolean updateAccount(Account account);

    /**
     * Add a transaction to the persistemt storage.
     * @param transaction
     * @return True if successful, otherwise false.
     */
    boolean addTransaction(Transaction transaction);

    /**
     *
     * @param bank
     * @return True if successful, otherwise false.
     */
    boolean saveAll(Bank bank);

    /**
     *
     * @return
     */
    Bank loadBank();

    /**
     *
     * @return
     */
    Bank loadAll();

    /**
     * Reset persistent storage so that it contains no data.
     * @return True if successful, otherwise false.
     */
    boolean resetPersistance();
}
