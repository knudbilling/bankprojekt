import java.util.List;

public interface Persistance {
    public boolean updateBank(Bank bank);
    public boolean addCustomer(Customer customer);
    public boolean updateCustomer(Customer customer);
    public boolean addAccount(Account account);
    public boolean updateAccount(Account account);
    public boolean addTransaction(Transaction transaction);
    public boolean saveAll(Bank bank);

    public Bank loadBank();
    public Bank loadAll();

    public boolean resetPersistance();
}
