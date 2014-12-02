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

import java.util.ArrayList;

//Assume all fields except theDates are immutable
public class Schedule
{
  ArrayList<Day> theDates = new ArrayList<Day>();

  final ArrayList<ScheduleDate> allDates;

  /**
  * Holds all of the pairs of schools
  */

  ArrayList<PairSchools> pairs;

  final ArrayList<School> schools;

  Schedule(ArrayList<PairSchools> pairs, ArrayList<ScheduleDate> dates,
           ArrayList<School> schools)
  {
    this.pairs = pairs;
    this.allDates = dates;
    this.schools = schools;
    reset();
  }

  Schedule(ArrayList<Day> theDates, ArrayList<PairSchools> pairs,
           ArrayList<ScheduleDate> dates, ArrayList<School> schools)
  {
    this.theDates = theDates;
    this.pairs = pairs;
    this.allDates = dates;
    this.schools = schools;
  }

  public void makeDummy()
  {
    ArrayList<PairSchools> firstGames = new ArrayList<PairSchools>();
    ArrayList<PairSchools> secondGames = new ArrayList<PairSchools>();
    firstGames.add(pairs.get(0));
    secondGames.add(pairs.get(1));
    secondGames.add(pairs.get(2));
    theDates.add(new Day(firstGames, allDates.get(0)));
    theDates.add(new Day(secondGames, allDates.get(1)));
  }

  public String toString()
  {

    String toReturn = "";
    for (Day theDate : theDates)
      {
        if (theDate.matchesPlaying())
          {

            toReturn += theDate.toString() + '\n';
          }
      }
    return toReturn;
  }

  public void reset()
  {
    for (ScheduleDate toPlay : allDates)
      {
        theDates.add(new Day(toPlay));
      }
  }

  public boolean canAdd(PairSchools toAdd, ScheduleDate theDate)
  {

    for (Day mayBeDay : theDates)
      {
        if (mayBeDay.isDate(theDate))
          {
            return mayBeDay.canAdd(toAdd);
          }
      }
    return false;
  }

  public void addGame(PairSchools toAdd, ScheduleDate theDate)
  {
    for (Day mayBeDay : theDates)
      {
        if (mayBeDay.isDate(theDate))
          {
            mayBeDay.addGame(toAdd);
            break;
          }
      }
  }

  public int numSchools()
  {
    return schools.size();
  }

  public Schedule clone()
  {
    ArrayList<Day> theNewDates = new ArrayList<Day>();
    for (Day toCopy : theDates)
      {
        theNewDates.add(toCopy.clone());
      }
    ArrayList<PairSchools> theNewPairSchools = new ArrayList<PairSchools>();
    for (PairSchools toCopy : pairs)
      {
        theNewPairSchools.add(toCopy.clone());
      }
    return new Schedule(theNewDates, theNewPairSchools, allDates, schools);
  }

}
