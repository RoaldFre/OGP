package rpg.util;

import static org.junit.Assert.*;
import org.junit.*;


/**
 * A class collecting tests for the class of couples.
 *
 * @author Roald Frederickx
 */
public class CoupleTest {

    @Test
    public void constructor_singleArgument() {
        Couple<String> couple = new Couple<String>("Polly");
        assertTrue(couple.contains("Polly"));
    }
    @Test
    public void constructor_twoArguments() {
        Couple<String> couple1 = new Couple<String>("norwegian", null);
        Couple<String> couple2 = new Couple<String>(null, "blue");
        Couple<String> couple3 = new Couple<String>("dead", "parrot");
        assertTrue(couple1.contains("norwegian"));
        assertTrue(couple2.contains("blue"));
        assertTrue(couple3.contains("dead"));
        assertTrue(couple3.contains("parrot"));
    }
    @Test
    public void constructor_clone() {
        Couple<String> couple = new Couple<String>("Dolly");
        Couple<String> clone = new Couple<String>(couple);
        assertTrue(clone.contains("Dolly"));
    }

    @Test
    public void getAnElement_test() {
        Couple<String> couple = new Couple<String>("Zichem");
        assertEquals("Zichem", couple.getAnElement());
    }

    @Test
    public void getPartner_test() {
        Couple<String> couple = new Couple<String>("Laurel", "Hardy");
        assertEquals("Laurel", couple.getPartner("Hardy"));
        assertEquals("Hardy", couple.getPartner("Laurel"));
        assertEquals(null, couple.getPartner("Chaplin"));
    }

    @Test
    public void setPartner_legal() {
        Couple<String> couple1 = new Couple<String>("Nicole");
        couple1.setPartner("Nicole", "Hugo");
        assertEquals("Hugo", couple1.getPartner("Nicole"));

        Couple<String> couple2 = new Couple<String>("Louis", "Verheyden");
        couple2.setPartner("Louis", "de Witte");
        assertEquals("de Witte", couple2.getPartner("Louis"));
    }
    @Test (expected = IllegalArgumentException.class)
    public void setPartner_null() {
        Couple<String> couple = new Couple<String>("null");
        couple.setPartner(null, "segmentation fault");
    }

    @Test
    public void add_test() {
        Couple<String> couple = new Couple<String>("sunshine");
        couple.add("happiness");
        assertEquals("happiness", couple.getPartner("sunshine"));
    }
    
    @Test
    public void delete_legal() {
        Couple<String> couple = new Couple<String>("good", "bad");
        couple.delete("bad");
        assertFalse(couple.contains("bad"));
        assertEquals(1, couple.getNbElements());
    }
    @Test (expected = IllegalArgumentException.class)
    public void delete_notInThere() {
        Couple<String> couple = new Couple<String>("black", "white");
        couple.delete("gray");
    }

    @Test
    public void iterator_test() {
        Couple<String> couple1 = new Couple<String>("a");
        for (String string : couple1)
            assertTrue(string.equals("a"));

        Couple<String> couple2 = new Couple<String>("b", "c");
        for (String string : couple2)
            assertTrue(string.equals("b") || string.equals("c"));
    }
}

// vim: ts=4:sw=4:expandtab:smarttab

