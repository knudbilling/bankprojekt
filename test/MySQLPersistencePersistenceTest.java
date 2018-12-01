import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MySQLPersistencePersistenceTest {

    @Test
    public void canConnectToMySQL(){
        Persistence p=new MySQLPersistence("localhost",3306,"bank","user","1234");
        assertNotNull(p);
    }

 }
