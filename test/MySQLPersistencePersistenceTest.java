import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Knud Billing
 */
public class MySQLPersistencePersistenceTest {

    @Test
    public void canConnectToMySQL(){
        Persistence p=new MySQLPersistence();
        assertNotNull(p);
    }



    @Test
    public void canResetPersistance(){
        Persistence p = new MySQLPersistence();
        assert(p.resetPersistance());
    }

 }
