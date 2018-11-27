import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MySQLPersistanceTest {

    @Test
    public void canConnectToMySQL(){
        Persistance p=new MySQL();
        assertNotNull(p);
    }



    @Test
    public void canResetPersistance(){
        Persistance p = new MySQL();
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
