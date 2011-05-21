package rpg;

import be.kuleuven.cs.som.annotate.*;

public enum Direction {
    NORTH {
        @Basic public String symbol() {return "N";}
        @Basic public Direction complement() {return SOUTH;}
        @Basic public Coordinate moveCoordinate(Coordinate c, long s) {
            return new Coordinate(c.x, c.y + s, c.z);
        }
    }, 
    SOUTH {
        @Basic public String symbol() {return "S";}
        @Basic public Direction complement() {return NORTH;}
        @Basic public Coordinate moveCoordinate(Coordinate c, long s) {
            return new Coordinate(c.x, c.y - s, c.z);
        }
    }, 
    WEST {
        @Basic public String symbol() {return "W";}
        @Basic public Direction complement() {return EAST;}
        @Basic public Coordinate moveCoordinate(Coordinate c, long s) {
            return new Coordinate(c.x - s, c.y, c.z);
        }
    }, 
    EAST {
        @Basic public String symbol() {return "E";}
        @Basic public Direction complement() {return WEST;}
        @Basic public Coordinate moveCoordinate(Coordinate c, long s) {
            return new Coordinate(c.x + s, c.y, c.z);
        }
    }, 
    UP {
        @Basic public String symbol() {return "U";}
        @Basic public Direction complement() {return DOWN;}
        @Basic public Coordinate moveCoordinate(Coordinate c, long s) {
            return new Coordinate(c.x, c.y, c.z + s);
        }
    },
    DOWN {
        @Basic public String symbol() {return "D";}
        @Basic public Direction complement() {return UP;}
        @Basic public Coordinate moveCoordinate(Coordinate c, long s) {
            return new Coordinate(c.x, c.y, c.z - s);
        }
    }; 
    
    /** 
     * Returns a short representation of this direction.
     */
    @Basic
    public abstract String symbol();

    /** 
     * Returns the complementary direction of this direction. 
     */
    @Basic
    public abstract Direction complement();

    /** 
     * Return the result of moving the given coordinate in this direction.
     * 
     * @param coordinate 
     * The coordinate to move in this direction.
     * @return 
     *   | result.equals(moveCoordinate(coordinate, 1))
     */
    public Coordinate moveCoordinate(Coordinate coordinate) {
        return moveCoordinate(coordinate, 1);
    }

    /** 
     * Return the result of moving the given coordinate in this direction 
     * for the given amount of steps.
     * 
     * @param coordinate 
     * The coordinate to move in this direction.
     * @param steps
     * The amount of steps to move the given coordinate in this direction.
     * @return 
     * The result of moving the given coordinate in this direction for the 
     * given number of steps.
     */
    @Basic
    public abstract Coordinate moveCoordinate(Coordinate coordinate,
                                              long steps);
}

// vim: ts=4:sw=4:expandtab:smarttab

