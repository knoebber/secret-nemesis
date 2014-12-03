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
 * Holds the calendar information which is relevant to a game date. We only need the
 * the month and day of the month, and the order, which specifies the position of the 
 * date chronologically within the set of all existing dates. This way, we can have a 
 * schedule with two which have the same day and month, but occur on different years.
 * The first date entered by the user has order 0, the second has order 1, ect
 */
@SuppressWarnings("rawtypes")
final public class ScheduleDate
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
   * The time ordering of the date. Non-negative integer
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
   *        order - the chronological ordering of the date
   * @pre
   *    month must be between 1 - 12
   *    day must be a valid date for the specified month
   *    (e.x.) in range 1 - 31 
   *    order must be non-negative
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
   * Set the day
   * @pre
   *    day must be a valid date for the current month
   *    (e.x.) in range 1 - 31 
   */
  void setDay(int day)
  {
    this.day = day;
  }// setDay(int day)
  
  /**
   * Set the order
   * @pre
   *    order must be non-negative
   */
  void setOrder(int order)
  {
    this.order = order;
  }// setDay(int day)


  /**
   * Returns a string representation of the date.
   */
  public String toString()
  {
    return (day + "/" + month);
  }// toString()
  
  
  /**
   * Find the back to back dates. By back-to-back, dates which differ by exactly 
   * one day from other dates in the ArrayList
   * @param dates - the dates we will search for back-to-back dates
   * @pre
   *    the ScheduleDates in dates must be stored by ascending order
   * @return
   *    Will return all the back-to-back dates as an ArrayList<ScheduleDate>
   */
  public static ArrayList<ScheduleDate> findBackToBack(ArrayList<ScheduleDate> dates)
  {
    // We initialize backToBacks as empty ArrayList
    ArrayList<ScheduleDate> backToBacks = new ArrayList<ScheduleDate>();
    // We loop through each date-pair
    for (int i = 0; i < (dates.size() - 1); i++)
      {
        // We get the current and next date
        ScheduleDate trackerDate1 = dates.get(i);
        int date1 = trackerDate1.get364();
        ScheduleDate trackerDate2 = dates.get(i + 1);
        int date2 = trackerDate2.get364();
        // We find the difference between the dates in days
        int dateDifference = date2 - date1;
        /*
         *  If the date difference is 1, we have two regular back to back games
         *  If the date difference is 0, it is a leap year and Feb 29 vs March 1
         *  If the date difference is -364, we have a Dec 31 vs Jan 1 new years game
         */
        if (dateDifference == 1 || dateDifference == -364 || dateDifference == 0)
          {
            backToBacks.add(trackerDate1);
            backToBacks.add(trackerDate2);
          }// if
      }// for (int i = 0; i < (dates.size() - 1); i++)
    return backToBacks;
  }// findBackToBack(ArrayList<ScheduleDate> dates)
  
  

  /**
   * Returns an integer representation of the date out of 364.
   * @param dates - the dates we will search for back-to-back dates
   * @pre
   *    the scheduledates in dates must be stored by ascending order
   * @return
   *    Will return an integer representation of our date
   * @note
   *  Note that leap years are not accounted for in this paradigm, and are thus 
   *  handled by other methods (ie, findBackToBack)
   */
  int get364()
  {
    int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    int date365 = 0;
    /*
     *  We add the days in each month of our ScheduleDate until we have 
     *  accounted for each month
     */
    for (int i = 0; i < (month - 1); i++)
      {
        date365 += daysOfMonth[i];
      }// for
    // We add the remaining days of our ScheduleDate
    date365 += day;
    return date365;
  }// get364()
  
  
  
  
  
  /**
   * Compares two date objects. Makes a judgment by comparing the 
   * order of the Dates
   * @pre
   *    o instanceOf
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

}// class ScheduleDate

