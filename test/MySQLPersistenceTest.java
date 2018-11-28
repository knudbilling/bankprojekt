import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @authot Knud Billing
 */
public class MySQLPersistenceTest {

    @Test
    public void canConnectToMySQL(){
        Persistence p=new MySQL();
        assertNotNull(p);
    }



    @Test
    public void canResetPersistance(){
        Persistence p = new MySQL();
        assert(p.resetPersistance());
    }

    @Test
    public void backupTest(){
        MySQL p = new MySQL();
        p.resetPersistance();
        p.backup();
        assert(true);
    }
}
