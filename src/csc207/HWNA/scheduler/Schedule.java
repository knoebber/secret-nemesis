package csc207.HWNA.scheduler;

import java.util.ArrayList;
import java.util.Collections;

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
  Schedule(ArrayList<PairSchools> pairs,  ArrayList<ScheduleDate> dates, int numSchools)
  {
    this.pairs=pairs;
    this.allDates = dates;
    this.numSchools=numSchools;
  }// Schedule(ArrayList<Game> schedule, ArrayList<ScheduleDate> dates)

  // +---------+-----------------------------------------------------------
  // | Methods |
  // +---------+

  /**
   *  Build a schedule 
   */
  public void generateSchedule()
  {
    // STUB
    
  }// generateSchedule(ArrayList<PairSchools> schedule)

  /**
   * create a a non optimized/correct schedule for the purpose of testing the
   * output before the algorithm is implemented. Only for debugging purposes,
   * not a valid schedule
   */
  public void generateDummySchedule()
  {
    
    int dateIndex = 0;
    for (int i = 0; i < pairs.size(); i++)
      {
        gameList.add(new Game(pairs.get(i), allDates.get(dateIndex++)));
        dateIndex = dateIndex % allDates.size();
        //System.out.println(i);
        //pairs.get(i).print();
      }
  }
  /**
   * find the 4 back to back dates
   * @param dates
   * @return
   */
  public ArrayList<ScheduleDate> findBackToBack(ArrayList<ScheduleDate> dates)
  {
    ArrayList<ScheduleDate> backToBacks = new ArrayList<ScheduleDate>();

    for (int i = 0; i < (dates.size() - 1); i++)
      {
        ScheduleDate trackerDate1 = dates.get(i);
        int date1 = trackerDate1.get365();
        ScheduleDate trackerDate2 = dates.get(i + 1);
        int date2 = trackerDate2.get365();
        int dateDifference = date2 - date1;
        // explain leap year, loop logic for dec 31 jan 1
        if (dateDifference == 1 || dateDifference == -364 || dateDifference == 0)
          {
            backToBacks.add(trackerDate1);
            backToBacks.add(trackerDate2);
          }
      }
    return backToBacks;
  }
  
  
  public Schedule generateBackToBack(ScheduleDate saturday,
                                     ScheduleDate sunday,
                                     ArrayList<PairSchools> pairs)
  {
    //randomize the pairs so we get different results
    Collections.shuffle(pairs);
    ArrayList<Game> backToBacks = new ArrayList<Game>();
    boolean isSaturday = true;
    Game currentGame=new Game(pairs.get(0),saturday);
    Game original=currentGame;
    backToBacks.add(currentGame);
    int j=0;
    
    for (int i = 0; i < numSchools-1; i++)
      {        
        while(true)
          {
            //look through all the games to find a pair that has the same away
            if (isSaturday)
              {
                if (pairs.get(j).away.equals(currentGame.competing.away) && 
                        !(pairs.get(j).home.equals(currentGame.competing.home)))
                  {
                    //add to the back to back game list
                    currentGame=new Game(pairs.get(j), saturday);
                    backToBacks.add(currentGame);
                    isSaturday = false;
                    break;
                  }//if
              }
            else
              {
                if (pairs.get(j).home.equals(currentGame.competing.home) && 
                    !(pairs.get(j).away.equals(currentGame.competing.away)) &&
                    !(pairs.get(j).home.equals(original.competing.home)))
                  {
                    //add to the back to back game list
                    currentGame=new Game(pairs.get(j), sunday);
                    backToBacks.add(currentGame);
                    isSaturday = true;
                    break;
                  }//if
              }//else
            j++;
          }//while true
        j=0;
      }//for
    for(int k=0; k < pairs.size(); k++)
      {
        if (pairs.get(k).home.equals(original.competing.home))
          {
            currentGame=new Game(pairs.get(k), sunday);
            backToBacks.add(currentGame);
          }
      }
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
  }// generateRating()

 
}//class Schedule