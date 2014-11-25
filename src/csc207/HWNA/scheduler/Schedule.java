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
  ArrayList<Game> gameList=new ArrayList<Game>();

  /**
   *  Holds all dates which games may potentially be held on
   */
  ArrayList<ScheduleDate> allDates;
  
  /**
   * Holds all of the pairs of schools
   */
  
  ArrayList<PairSchools> pairs;
  
  ArrayList<School> schools;

  /**
   * The number of schools to be scheduled
   */
  int numSchools;
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
  Schedule(ArrayList<PairSchools> pairs,  ArrayList<ScheduleDate> dates, int numSchools, ArrayList<School> schools)
  {
    this.pairs=pairs;
    this.allDates = dates;
    this.numSchools=numSchools;
    this.schools=schools;
    
  }// Schedule(ArrayList<Game> schedule, ArrayList<ScheduleDate> dates)

  // +---------+-----------------------------------------------------------
  // | Methods |
  // +---------+

  /**
   *  Build a schedule 
   */
  public static Schedule generateSchedule(ArrayList<PairSchools> pairs,  ArrayList<ScheduleDate> dates, int numSchools, ArrayList<School> schools)
  {

    Schedule soFar=null;
    ArrayList<ScheduleDate> backToBackDates = ScheduleDate.findBackToBack(dates);
    backToBackDates.get(0).printDate();
    System.out.println("Hi");
    if (backToBackDates.size()!=4)
      {
        System.out.println("Error, we wanted 4 back-to-back dates");
      }
    
    soFar = UtilsSchedule.fillBackToBackWeekends(backToBackDates.get(0), backToBackDates.get(1),
                                   backToBackDates.get(2), backToBackDates.get(3),
                           pairs,
                           numSchools,schools);
    @SuppressWarnings("unchecked")
    ArrayList<ScheduleDate> ordinaryDates = (ArrayList<ScheduleDate>) dates.clone(); 
    ordinaryDates.remove(backToBackDates.get(0));
    ordinaryDates.remove(backToBackDates.get(1));
    ordinaryDates.remove(backToBackDates.get(2));
    ordinaryDates.remove(backToBackDates.get(3));
    
    Schedule temp = UtilsSchedule.fillDates(ordinaryDates, pairs, schools);
    Schedule finalSchedule = UtilsSchedule.mergeSchedule(soFar,temp);
    finalSchedule.allDates=dates;
    
    
    try
      {
        ScheduleWriter.write(finalSchedule , "/home/roylewil16/csc207/secret-nemesis/schedule.txt");
      }
    catch (Exception e)
      {
        // TODO Auto-generated catch block
        System.out.println("fg");
        e.printStackTrace();
      }
    

    return null;
    
  }// generateSchedule(ArrayList<PairSchools> schedule)
  


  
  public void addGame(Game toAdd)
  {
    toAdd.competing.canAdd=false;
    gameList.add(toAdd);
  }
  
  
  
  /**
   * create a a non optimized/correct schedule for the purpose of testing the
   * output before the algorithm is implemented. Only for debugging purposes,
   * not a valid schedule
   */
  public void generateDummySchedule()
  {
    
    int dateIndex = 0;
    for (int i = 0; i < pairs.size()/2; i++)
      {
        gameList.add(new Game(pairs.get(i), allDates.get(dateIndex++)));
        dateIndex = dateIndex % allDates.size();
        //System.out.println(i);
        //pairs.get(i).print();
      }
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

 
}//class Schedule