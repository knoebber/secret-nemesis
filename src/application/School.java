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
  ArrayList<Date> gameDates;
 
  /**
   * The dates which the school can play an opposing school
   */
  ArrayList<Date> optionalGameDates;
 
  public School(String name,ArrayList<Date> gameDates,ArrayList<Date> optionalGameDates)
  {
    name = this.name;
    this.gameDates = gameDates;
    this.optionalGameDates=optionalGameDates;
  }// School(String name,Date[] gameDates)
 
  public void removeDate(Date dateToRemove)
  {
    //STUB
  }// removeDate(Date dateToRemove)
 
  public void addDate(Date dateToAdd)
  {
    
  }// addDate(Date dateToAdd)
 
 
  public ArrayList<Date> getDates()
  {
    return gameDates;
  }// getDates()
 
 
}//class School