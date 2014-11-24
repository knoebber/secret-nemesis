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
 * when the school can play, and the dates when the school must play
 */
public class School
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

  /**
   * Print a representation of the school to stdout
   */
  public void printInfo()
  {
    System.out.println("School name is " + this.name);
    // We print the mandatory game dates
    System.out.println("Mandatory Game Days:");
    for (int i = 0; i < gameDates.size(); i++)
      {
        gameDates.get(i).printDate();
      }// for
    // We print the optional game dates
    System.out.println("Optional Game Days:");
    for (int i = 0; i < optionalGameDates.size(); i++)
      {
        optionalGameDates.get(i).printDate();
      }// for
  }// printInfo()
  
  public boolean canPlay(ScheduleDate thisDay)
  {
    for (int iter=0;iter<gameDates.size();iter++)
      {
        if (thisDay.equals(gameDates.get(iter)))
          {
            return true;
          }
      }
    for (int iter=0;iter<optionalGameDates.size();iter++)
      {
        if (thisDay.equals(optionalGameDates.get(iter)))
          {
            return true;
          }
      }
    return false;
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
  public boolean equals(String otherName)
  {
    return name.equals(otherName);
  }// getDates()

}//class School