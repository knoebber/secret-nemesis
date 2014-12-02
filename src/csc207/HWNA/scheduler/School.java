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
 * Holds the information regarding a specific school which is relevant to
 * scheduling purposes. It includes the name of the school and the dates
 * when the school can play, and the dates when the school must play. Schools
 * are immutable
 */
final public class School
{

  // +--------+------------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The name of the school
   */
  String name;

  /**
   * The dates which the school must play an opposing school
   */
  ArrayList<ScheduleDate> gameDates;

  /**
   * The dates which the school can play an opposing school
   */
  ArrayList<ScheduleDate> optionalGameDates;

  // +--------------+------------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new school object with the specified info
   * @pre
   *    gameDates.get(i).equals(optionalGameDates.get(j)) for all i, j
   */
  public School(String name, ArrayList<ScheduleDate> gameDates,
                ArrayList<ScheduleDate> optionalGameDates)
  {
    // Save information
    this.name = name;
    this.gameDates = gameDates;
    this.optionalGameDates = optionalGameDates;
  }// School(String name,Date[] gameDates)

  // +---------+-----------------------------------------------------------
  // | Methods |
  // +---------+



  
  
  public boolean canPlay(ScheduleDate thisDay)
  {
    for (ScheduleDate potentialGameDate : gameDates)
      {
        if (thisDay.equals(potentialGameDate))
          {
            return true;
          }
      }
    for (ScheduleDate potentialGameDate : optionalGameDates)
      {
        if (thisDay.equals(potentialGameDate))
          {
            return true;
          }
      }
    return false;
  }
  
  
  public String toString()
  {
    return name;
  }
  

  /**
   * Gets the ScheduleDates from the school
   */
  public ArrayList<ScheduleDate> getDates()
  {
    return gameDates;
  }// getDates()

  /**
   * Returns true if the string is the same as the school's name
   */
  public boolean equals(Object other)
  {
    if (other instanceof School)
      {
        School otherSchool = (School) other;
        return this.name.equals(otherSchool.name);
      }
    if (other instanceof String)
      {
        return name.equals(other);
      }
    return false;
  }// getDates()

}//class School