package csc207.HWNA.scheduler;

import java.util.ArrayList;

/**
 * @author Harry Baker
 * @author William Royle
 * @author Nicolas Knoebber
 * @author Amanda Hinchman
 *
 * @date November 18, 2014
 *
 * Holds information defining a generated basketball game schedule. A
 * schedule is said to be valid if the following conditions are met:
 *      Every school plays every other school twice (once home, once away)
 *      Every school plays two back to back away games and two back to back home games
 *      No school will play any school more than 180 miles away on a back-to-back away
 *      weekend
 *      Every school respects its must play dates, and the dates in which it cannot play
 *      
 *      In addition, the closer together competing schools for midweek games, the more
 *      optimal the schedule
 */
public class Schedule
{

  // +--------+------------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   *  Holds the array of games which the schedule is composed of
   */
  ArrayList<Game> gameList;

  /**
   *  Holds all dates which games may potentially be held on
   */
  ArrayList<ScheduleDate> allDates;

  // +--------------+------------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create an empty schedule object.
   */
  Schedule()
  {
  }// Schedule()

  /**
   * Create a new schedule composed of the given game, with the 
   * dates passed being potential game dates.
   * @pre
   *    some j exists such that for all i,
   *    gameList.get(i).dayOfCalendar.equals(dates.get(j))==true
   *    
   */
  Schedule(ArrayList<Game> gameList, ArrayList<ScheduleDate> dates)
  {
    this.gameList = gameList;
    this.allDates = dates;
  }// Schedule(ArrayList<Game> schedule, ArrayList<ScheduleDate> dates)

  // +---------+-----------------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Create a new schedule composed of the given PairSchools array.
   */
  Schedule generateSchedule(ArrayList<PairSchools> schedule)
  {
    // STUB
    return null;
  }// generateSchedule(ArrayList<PairSchools> schedule)

  /**
   * create a a non optimized/correct schedule for the purpose of testing the
   * output before the algorithm is implemented. Only for debugging purposes,
   * not a valid schedule
   */

  Schedule generateDummySchedule(ArrayList<PairSchools> pairs,
                                 ArrayList<ScheduleDate> dates)
  {
    ArrayList<Game> games = new ArrayList<Game>();
    int dateIndex = 0;
    for (int i = 0; i < pairs.size(); i++)
      {
        games.add(new Game(pairs.get(i), dates.get(dateIndex++)));
        dateIndex = dateIndex % dates.size();
        //System.out.println(i);
        //pairs.get(i).print();
      }
    return new Schedule(games, dates);
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
  }// generateRating()

  public class Game
  {

    // +--------+------------------------------------------------------------
    // | Fields |
    // +--------+

    /**
     * The day of the match
     */
    ScheduleDate dayOfCalendar;

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
    Game(PairSchools competing, ScheduleDate dayOfCalendar)
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
      return dayOfCalendar.equals(other.dayOfCalendar)
             && competing.equals(other.competing);
    }// equals(Game other)

  }// class Game

}