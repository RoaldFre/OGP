package rpg;

import static org.junit.Assert.*;
import org.junit.*;

import rpg.exceptions.*;
import rpg.util.Direction;
import rpg.util.Temperature;

/**
 * A class collecting tests for the abstract class of squares.
 *
 * @author Roald Frederickx
 */
public class SquareImplTest {

    /**
     * Instance variable referencing squares that may change during 
     * individual tests.
     */
    private SquareImpl squareDefault;
    private SquareImpl square_T100_H50;
    private SquareImpl square_Tneg5_H20;
    private SquareImpl square_Tneg4p99_H5;
    private SquareImpl square_Tneg15_H100;
    private SquareImpl square_Tneg15p01_H0;
    private SquareImpl square_Tneg100_H80;
    private SquareImpl square_T35_H50;
    private SquareImpl square_T40_H100;
    private SquareImpl squareTemp49p99;
    private SquareImpl squareTemp50;
    private SquareImpl square_T20_Tmin0_Tmax100_H20;

    private SquareImpl connectedSquare1; //max temperature 1000C
    private SquareImpl connectedSquare2; //max temperature 100C


    /**
     * Set up a mutable test fixture.
     */
    @Before
    public void setUpMutableFixture() {
        squareDefault = new RegularSquare();

        square_T100_H50 = new RegularSquare(new Temperature(100), 5000);

        square_Tneg5_H20    = new RegularSquare(new Temperature(-5), 1000);
        square_Tneg4p99_H5  = new RegularSquare(new Temperature(-14.99), 500);
        square_Tneg15_H100  = new RegularSquare(new Temperature(-15), 10000);
        square_Tneg15p01_H0 = new RegularSquare(new Temperature(-15.01), 0);
        square_Tneg100_H80  = new RegularSquare(new Temperature(-100), 8000);

        square_T35_H50  = new RegularSquare(new Temperature(35), 5000);
        square_T40_H100 = new RegularSquare(new Temperature(40), 10000);
        squareTemp49p99 = new RegularSquare(new Temperature(49.99), 5000);
        squareTemp50    = new RegularSquare(new Temperature(50), 5000);

        square_T20_Tmin0_Tmax100_H20 = new RegularSquare(
                    new Temperature(25),
                    new Temperature(-10), new Temperature(200),
                    3000,
                    true);


        connectedSquare1 = new RegularSquare(
                    new Temperature(0),
                    new Temperature(0), new Temperature(1000),
                    5000,
                    true);
        connectedSquare2 = new RegularSquare(
                    new Temperature(0),
                    new Temperature(0), new Temperature(100),
                    5000,
                    true);
        connectedSquare1.mergeWith(connectedSquare2, Direction.NORTH);
    }

    /** 
     * Asserts the class invariants of the given square.
     *
     * @param square 
     * The square to test.
     */
    public static void assertClassInvariants(SquareImpl square) {
        assertTrue(square.isNotRaw());
    }

    @Test
    public void setTemperature_LegalCase() {
        square_T100_H50.setTemperature(new Temperature(200));
        assertEquals(new Temperature(200), square_T100_H50.getTemperature());
        assertClassInvariants(square_T100_H50);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setTemperature_TooHigh() throws Exception {
        double T = Double.MAX_VALUE;
        if (!square_T100_H50.canHaveAsTemperature(new Temperature(T)))
            square_T100_H50.setTemperature(new Temperature(T));
        else
            throw new IllegalArgumentException();
    }

    @Test(expected = IllegalArgumentException.class)
    public void setTemperature_TooLow() throws Exception {
        double T = Double.MIN_VALUE;
        if (!square_T100_H50.canHaveAsTemperature(new Temperature(T)))
            square_T100_H50.setTemperature(new Temperature(T));
        else
            throw new IllegalArgumentException();
    }

    @Test
    public void setTemperature_properEquilibration() {
        Temperature T100 = new Temperature(100);
        Temperature T50 = new Temperature(50);
        connectedSquare1.setTemperature(T100);
        assertEquals(T50, connectedSquare1.getTemperature());
        assertEquals(T50, connectedSquare2.getTemperature());
    }
    @Test 
    public void setTemperature_equilibrationViolatesLimits() {
        Temperature T1000 = new Temperature(1000);
        Temperature T0 = new Temperature(0);
        try {
            connectedSquare1.setTemperature(T1000);
            assertTrue(false);
        } catch (EquilibratingSquaresViolatesLimitsException e) {
            assertEquals(T0, connectedSquare1.getTemperature());
            assertEquals(T0, connectedSquare2.getTemperature());
        }
    }


    @Test
    public void setMaxTemperature_LegalCase() {
        Temperature temperature = new Temperature(1000);
        square_T100_H50.setMaxTemperature(temperature);
        assertEquals(temperature, square_T100_H50.getMaxTemperature());
        assertClassInvariants(square_T100_H50);
    }

    @Test
    public void setMinTemperature_LegalCase() {
        Temperature temperature = new Temperature(-1000);
        square_T100_H50.setMinTemperature(temperature);
        assertEquals(temperature, square_T100_H50.getMinTemperature());
        assertClassInvariants(square_T100_H50);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMaxTemperature_BelowTemp() {
        square_T100_H50.setMaxTemperature(new Temperature(99));
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMinTemperature_AboveTemp() {
        square_T100_H50.setMinTemperature(new Temperature(101));
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMaxTemperature_BelowMin() {
        Temperature lower = new Temperature(
                square_T100_H50.getMinTemperature().temperature() - 1);
        square_T100_H50.setMaxTemperature(lower);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMinTemperature_AboveMax() {
        Temperature higher = new Temperature(
                square_T100_H50.getMaxTemperature().temperature() + 1);
        square_T100_H50.setMinTemperature(higher);
    }

    @Test
    public void coldDamage(){
        assertEquals(0, square_T100_H50.coldDamage());
        assertEquals(1, square_Tneg5_H20.coldDamage());
        assertEquals(1, square_Tneg4p99_H5.coldDamage());
        assertEquals(2, square_Tneg15_H100.coldDamage());
        assertEquals(2, square_Tneg15p01_H0.coldDamage());
        assertEquals(10,square_Tneg100_H80.coldDamage());
    }

    @Test
    public void heatDamage(){
        assertEquals(0, square_Tneg5_H20.heatDamage());
        assertEquals(1, square_T35_H50.heatDamage());
        assertEquals(1, square_T40_H100.heatDamage());
        assertEquals(1, squareTemp49p99.heatDamage());
        assertEquals(2, squareTemp50.heatDamage());
    }

    @Test
    public void rustDamage_Test() {
        assertEquals(0, square_Tneg15p01_H0.rustDamage());
        assertEquals(0, square_Tneg5_H20.rustDamage());
        assertEquals(2, square_T100_H50.rustDamage());
        assertEquals(7, square_Tneg100_H80.rustDamage());
    }

    @Test
    public void slippery_Test() {
        assertTrue(square_T20_Tmin0_Tmax100_H20.isSlippery());

        assertTrue(square_T40_H100.isSlippery());
        assertTrue(square_Tneg15_H100.isSlippery());

        assertFalse(square_Tneg15p01_H0.isSlippery());
        assertFalse(square_Tneg4p99_H5.isSlippery());
    }
    
    @Test
    public void inhabitability_Test() {
        double heatDam = squareDefault.heatDamage();
        double heatDamCubed = heatDam * heatDam * heatDam;
        double coldDam = squareDefault.coldDamage();
        double humidityPercent = squareDefault.getHumidity() / 100.0;

        double expected = -1 * Math.sqrt(heatDamCubed / (101 - humidityPercent))
                    - Math.sqrt(coldDam);
        assertEquals(expected, squareDefault.inhabitability(), 0);
    }

    @Test
    public void getBorderAt_Test() {
        Border newBorder = new Wall(
                squareDefault.getBorderAt(Direction.NORTH), false);
        assertEquals(newBorder, squareDefault.getBorderAt(Direction.NORTH));
        assertClassInvariants(squareDefault);
    }

    @Test(expected = IllegalArgumentException.class)
    public void changeBorderAt_NonProperBorder() {
        squareDefault.changeBorderAt(Direction.NORTH, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void changeBorderAt_Loop() {
        squareDefault.changeBorderAt(Direction.NORTH,
                squareDefault.getBorderAt(Direction.SOUTH));
    }
    
    @Test
    public void isProperBorderAt_Test() {
        Border terminatedBorder = squareDefault.getBorderAt(Direction.NORTH);
        new Wall(terminatedBorder, false);

        Border borderWithCorrectSquare = new Wall(squareDefault, false);
        Border borderWithWrongSquare = new Wall(square_T100_H50, false);

        assertTrue(squareDefault.isProperBorderAt(Direction.NORTH,
                                                borderWithCorrectSquare));
        assertFalse(squareDefault.isProperBorderAt(Direction.NORTH,
                                                borderWithWrongSquare));
        assertFalse(squareDefault.isProperBorderAt(Direction.NORTH,
                                                terminatedBorder));
        assertFalse(squareDefault.isProperBorderAt(null,
                                                borderWithCorrectSquare));
        assertFalse(squareDefault.isProperBorderAt(Direction.NORTH, null));

        squareDefault.terminate();
        assertTrue(squareDefault.isProperBorderAt(Direction.NORTH, null));
        assertFalse(squareDefault.isProperBorderAt(Direction.NORTH,
                                                borderWithWrongSquare));
        assertFalse(squareDefault.isProperBorderAt(Direction.NORTH,
                                                borderWithCorrectSquare));
        assertFalse(squareDefault.isProperBorderAt(Direction.NORTH,
                                                terminatedBorder));
    }

    @Test
    public void getDirectionOfBorder_Legal() {
        for (Direction direction : Direction.values())
            assertEquals(direction,
                    squareDefault.getDirectionOfBorder(
                        squareDefault.getBorderAt(direction)));

        Border newBorder = new Wall(
                squareDefault.getBorderAt(Direction.NORTH), false);
        assertEquals(Direction.NORTH,
                squareDefault.getDirectionOfBorder(newBorder));
    }

    @Test (expected = IllegalArgumentException.class)
    public void getDirectionOfBorder_null() {
        squareDefault.getDirectionOfBorder(null);
    }
    @Test (expected = IllegalArgumentException.class)
    public void getDirectionOfBorder_notBordering() {
        squareDefault.getDirectionOfBorder(
                square_T100_H50.getBorderAt(Direction.NORTH));
    }
    @Test (expected = IllegalStateException.class)
    public void getDirectionOfBorder_isTerminated() {
        Border border = squareDefault.getBorderAt(Direction.NORTH);
        squareDefault.terminate();
        squareDefault.getDirectionOfBorder(border);
    }

    @Test
    public void hasBorder_Legal() {
        for (Direction direction : Direction.values())
            assertTrue(squareDefault.hasBorder(
                        squareDefault.getBorderAt(direction)));

        Border newBorder = new Wall(
                squareDefault.getBorderAt(Direction.NORTH), false);
        assertTrue(squareDefault.hasBorder(newBorder));
    }

    @Test
    public void mergeWith_TestProperBinding() {
        square_T100_H50.mergeWith(square_T40_H100, Direction.NORTH);
        Border border = square_T100_H50.getBorderAt(Direction.NORTH);

        assertEquals(square_T100_H50.getBorderAt(Direction.NORTH),
                        square_T40_H100.getBorderAt(Direction.SOUTH));

        assertTrue(border.bordersOnSquare(square_T100_H50));
        assertTrue(border.bordersOnSquare(square_T40_H100));
        
        assertEquals(square_T40_H100, border.getNeighbour(square_T100_H50));
        assertEquals(square_T100_H50, border.getNeighbour(square_T40_H100));

        assertClassInvariants(square_T100_H50);
        assertClassInvariants(square_T40_H100);
    }

    @Test
    public void mergeWith_TestHumiditiesAndTemperatures_Legal() {
        square_T100_H50.mergeWith(square_T40_H100, Direction.NORTH);

        assertEquals(7500, square_T100_H50.getHumidity());
        assertEquals(7500, square_T40_H100.getHumidity());

        double weightOffset = SquareImpl.getMergeTemperatureWeight();
        Temperature newTemp = new Temperature(
                ((weightOffset + (1 - weightOffset) * 50/75) * 100
                 + (weightOffset + (1 - weightOffset) * 100/75) * 40) / 2.0);
        assertTrue(newTemp.equals(square_T100_H50.getTemperature()));
        assertTrue(newTemp.equals(square_T40_H100.getTemperature()));

        assertClassInvariants(square_T100_H50);
        assertClassInvariants(square_T40_H100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void mergeWith_IllegalSquare() {
        squareDefault.mergeWith(null, Direction.NORTH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void mergeWith_IllegalDirection() {
        squareDefault.mergeWith(square_T40_H100, null);
    }

    @Test(expected = BorderMergeException.class)
    public void mergeWith_Loop() {
        squareDefault.mergeWith(squareDefault, Direction.NORTH);
    }

    @Test(expected = IllegalStateException.class)
    public void mergeWith_ThisTerminated() {
        square_T100_H50.terminate();
        square_T100_H50.mergeWith(square_T40_H100, Direction.NORTH);
    }

    @Test(expected = IllegalStateException.class)
    public void mergeWith_OtherTerminated() {
        square_T40_H100.terminate();
        square_T100_H50.mergeWith(square_T40_H100, Direction.NORTH);
    }


    @Test
    public void getNeighbours_getAccessibleNeighbours_getNavigatableSquares() {
        assertTrue(square_T100_H50.getNeighbours().isEmpty());
        assertTrue(square_T100_H50.getAccessibleNeighbours().isEmpty());
        assertTrue(square_T100_H50.getNavigatableSquares().isEmpty());

        square_T100_H50.mergeWith(square_T40_H100, Direction.NORTH);
        Door door = new Door(square_T100_H50.getBorderAt(Direction.NORTH), false);


        assertEquals(1, square_T100_H50.getNeighbours().size());
        assertEquals(square_T40_H100,
                     square_T100_H50.getNeighbours().get(Direction.NORTH));
        assertTrue(square_T100_H50.getAccessibleNeighbours().isEmpty());
        assertTrue(square_T100_H50.getNavigatableSquares().isEmpty());

        door.open();

        assertEquals(1, square_T100_H50.getNeighbours().size());
        assertEquals(square_T40_H100,
                     square_T100_H50.getNeighbours().get(Direction.NORTH));

        assertEquals(1, square_T100_H50.getAccessibleNeighbours().size());
        assertEquals(square_T40_H100,
                     square_T100_H50.getAccessibleNeighbours().get(
                                                        Direction.NORTH));

        assertEquals(1, square_T100_H50.getNavigatableSquares().size());
        assertTrue(square_T100_H50.getNavigatableSquares().contains(
                                                        square_T40_H100));

        assertClassInvariants(square_T100_H50);
        assertClassInvariants(square_T40_H100);
    }


    public void setHeatDamageThreshold_LegalCase() {
        Temperature temperature = new Temperature(100);
        SquareImpl.setHeatDamageThreshold(temperature);
        assertEquals(temperature, SquareImpl.getHeatDamageThreshold());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setHeatDamageThreshold_Null() {
        SquareImpl.setHeatDamageThreshold(null);
    }

    public void setHeatDamageStep_LegalCase() {
        SquareImpl.setHeatDamageThreshold(new Temperature(100));
        assertEquals(100, SquareImpl.getHeatDamageStep(), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setHeatDamageStep_Zero() {
        SquareImpl.setHeatDamageStep(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setHeatDamageStep_Negative() {
        SquareImpl.setHeatDamageStep(-1);
    }

    @Test
    public void setMergeTemperatureWeight_LegalCase() {
        SquareImpl.setMergeTemperatureWeight(0.3);
        assertEquals(0.3, SquareImpl.getMergeTemperatureWeight(), 0); 
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMergeTemperatureWeight_IllegalCase() {
        SquareImpl.setMergeTemperatureWeight(-1);
    }

    @Test
    public void terminate_test() {
        squareDefault.terminate();
        assertClassInvariants(squareDefault);
        assertTrue(squareDefault.isTerminated());
        for (Direction direction : Direction.values())
            assertEquals(null, squareDefault.getBorderAt(direction));
    }
}

// vim: ts=4:sw=4:expandtab:smarttab

