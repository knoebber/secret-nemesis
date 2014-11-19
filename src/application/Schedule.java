package application;

import java.util.ArrayList;
import java.util.Date;


/**
 * @author Harry Baker
 * @author William Royle
 * @author Nicolas Knoebber
 *
 * @date November 18, 2014
 *
 * Holds information defining a generated basketball game schedule. A
 * schedule is said to be valid if the following conditions are met.
 * Every school plays every other school twice (once on each )
 *
 */
public class Schedule
{
 
  // +--------+------------------------------------------------------------
  // | Fields |
  // +--------+
 
  /**
   *  Holds the array of games which the schedule is composed of
   */
  ArrayList<Game> schedule;

 
  /**
   * Create an empty schedule object.
   */
  Schedule()
  {
  }

 
  /**
   * Create a new schedule composed of the given games.
   */
  Schedule(ArrayList<Game> schedule)
  {
    this.schedule = schedule;
  }
 

 
 
  // +---------+-----------------------------------------------------------
  // | Methods |
  // +---------+
 
  /**
   * Create a new schedule composed of the given PairSchools array.
   */
  Schedule generateSchedule(ArrayList<PairSchools> schedule)
  {
    return null;
  }

  /**
   * Generates a rating for the schedule. The higher the
   * rating, the better the schedule
   *
   * @post
   *    if generateRating() returns 0, the schedule is invalid
   */
  public double generateRating()
  {
    // STUB
    return 0;
  }
 
 
  private class Game
  {
   
    // +--------+------------------------------------------------------------
    // | Fields |
    // +--------+

    /**
     * The day of the match
     */
    Date dayOfCalendar;
    
    /**
     * The competition which occurs on the specified date
     */
    PairSchools competing;
    
    
    // +--------------+------------------------------------------------------
    // | Constructors |
    // +--------------+
    
    /**
     * We create a new game, given the pair of schools which are playing and
     * the date of the competition
     */
    Game(PairSchools competing, Date dayOfCalendar)
    {
      this.dayOfCalendar = dayOfCalendar;
      this.competing = competing;
    }// Game(PairSchools competing, Date dayOfCalendar)

   
    // +---------+-----------------------------------------------------------
    // | Methods |
    // +---------+
    
    /**
     * Tells if two game objects are identical
     */
    public boolean equals(Game other)
    {
      return dayOfCalendar.equals(other.dayOfCalendar)&&competing.equals(other.competing);
    }// equals(Game other)
    

  }// class Game

}


