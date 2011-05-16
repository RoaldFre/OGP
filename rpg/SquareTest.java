package rpg;

import static org.junit.Assert.*;
import org.junit.*;

/**
 * A class collecting tests for the abstract class of squares.
 *
 * @author Roald Frederickx
 */
public class SquareTest {

    public void setHeatDamageThreshold_LegalCase() {
        Square.setHeatDamageThreshold(new Temperature(100));
        assertEquals(100, Square.getHeatDamageThreshold().temperature(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setHeatDamageThreshold_Null() {
        Square.setHeatDamageThreshold(null);
    }

    public void setHeatDamageStep_LegalCase() {
        Square.setHeatDamageThreshold(new Temperature(100));
        assertEquals(100, Square.getHeatDamageStep(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setHeatDamageStep_Zero() {
        Square.setHeatDamageStep(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setHeatDamageStep_Negative() {
        Square.setHeatDamageStep(-1);
    }

    @Test
    public void setMergeTemperatureWeight_LegalCase() {
        Square.setMergeTemperatureWeight(0.3);
        assertEquals(0.3, Square.getMergeTemperatureWeight(), 0); 
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMergeTemperatureWeight_IllegalCase() {
        Square.setMergeTemperatureWeight(-1);
    }
}

// vim: ts=4:sw=4:expandtab:smarttab

