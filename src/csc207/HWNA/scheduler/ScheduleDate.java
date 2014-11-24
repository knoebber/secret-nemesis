package csc207.HWNA.scheduler;

/**
 * @author Harry Baker
 * @author William Royle
 * @author Nicolas Knoebber
 * @author Amanda Hinchman
 *
 * @date November 18, 2014
 *
 * Holds the calendar information which is relevant to a game date.
 * We only need the month and day of the month
 * 
 */
@SuppressWarnings("rawtypes")
public class ScheduleDate
    implements Comparable
{
  // +--------+------------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The month of the game
   */
  int month;

  /**
   * The day of the game
   */
  int day;
  
  
  /**
   * The time ordering of the date
   */
  int order;

  // +--------------+------------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Creates an empty ScheduleDate object
   */
  ScheduleDate()
  {
  }// ScheduleDate(int month, int day)

  /**
   * Makes a ScheduleDate object with the specified info
   * @param month - the month of the event
   *        day - the day of the event
   * @pre
   *    month must be between 1 - 12
   *    day must be a valid date for the specified month
   *    (e.x.) in range 1 - 31 
   */
  ScheduleDate(int month, int day, int order)
  {
    this.month = month;
    this.day = day;
    this.order = order;
  }// ScheduleDate(int month, int day)

  // +---------+-----------------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Tells if two ScheduleDate objects are identical
   */
  public boolean equals(Object other)
  {
    if (other instanceof ScheduleDate)
      {
        ScheduleDate alternate = (ScheduleDate) other;
        // We make sure month, day equal
        return alternate.month == month && alternate.day == day;
      }// if
    return false;
  }// equals(Object other)

  /**
   * Set the month
   * @pre
   *    month must be between 1 - 12
   */
  void setMonth(int month)
  {
    this.month = month;
  }// setMonth(int month)

  /**
   * Set the month
   * @pre
   *    day must be a valid date for the current month
   *    (e.x.) in range 1 - 31 
   */
  void setDay(int day)
  {
    this.day = day;
  }// setDay(int day)
  
  void setOrder(int order)
  {
    this.order = order;
  }// setDay(int day)

  /**
   * Print the date
   */
  void printDate()
  {
    System.out.println(month + "/" + day+", order "+order);
  }// printDate()
  
  public String toString()
  {
    return (month + "/" + day);
  }

  
  int get365()
  {
    int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    int date365 = 0;
    
    for (int i = 0; i < (month - 1); i++)
      {
        date365 += daysOfMonth[i];
      }
    date365 += day;
    return date365;
  }
  
  
  
  
  
  /**
   * Compares two date objects. Makes a judgment by comparing the 
   * numeric value of month, and if that is not sufficient, 
   * compares the numeric value of day
   * 
   * @post
   *    returns 1 if this > other
   *    returns 0 if this == other
   *    returns -1 if this < other
   */
  @Override
  public int compareTo(Object o)
  {
    ScheduleDate other = (ScheduleDate) o;
    if (order>other.order)
      return 1;
    if (order<other.order)
      return -1;
      

    return 0;
  }// compareTo(Object o)

}
