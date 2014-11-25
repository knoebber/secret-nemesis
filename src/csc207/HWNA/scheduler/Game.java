package csc207.HWNA.scheduler;


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
  
  
  public boolean isPlaying(School theSchool)
  {
    return theSchool.equals(competing.home) || theSchool.equals(competing.away);
  }

}// class Game
