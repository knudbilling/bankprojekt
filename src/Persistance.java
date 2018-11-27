import java.util.List;

public interface Persistance {
    boolean updateBank(Bank bank);
    boolean addCustomer(Customer customer);
    boolean updateCustomer(Customer customer);
    boolean addAccount(Account account);
    boolean updateAccount(Account account);
    boolean addTransaction(Transaction transaction);
    boolean saveAll(Bank bank);

    Bank loadBank();
    Bank loadAll();

    boolean resetPersistance();
}
