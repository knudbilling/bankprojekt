import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CustomerTest {

    @Test
    public void CustomerTest(){
        Customer c = new Customer("firstName", "lastname","home", "22222222");

        assertEquals("firstName",c.firstName);
        assertEquals(0,c.idNo);



    }


}
