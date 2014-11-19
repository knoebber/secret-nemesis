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
 * Holds the information regarding a specific school which isrelevant to
 * scheduling purposes. It includes the name of the school and the dates
 * when the school can play
 */
public class School
{
 
  // +--------+------------------------------------------------------------
  // | Fields |
  // +--------+
 
  /**
   * The day of the match
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
 
  public School(String name,ArrayList<ScheduleDate> gameDates,ArrayList<ScheduleDate> optionalGameDates)
  {
    this.name = name;
    this.gameDates = gameDates;
    this.optionalGameDates=optionalGameDates;
  }// School(String name,Date[] gameDates)

  
  public void printInfo()
  {
    System.out.println("School name is "+ this.name);
    System.out.println("Optional Game Days:");
    for (int i=0;i<optionalGameDates.size();i++)
      {
        optionalGameDates.get(i).printDate();
      }
    System.out.println("Mandatory Game Days:");
    for (int i=0;i<gameDates.size();i++)
      {
        gameDates.get(i).printDate();
      }
  }
 
 
  public ArrayList<ScheduleDate> getDates()
  {
    return gameDates;
  }// getDates()
  
  public boolean equals(String otherName)
  {
    return name.equals(otherName);
  }// getDates()
 
 
}//class School