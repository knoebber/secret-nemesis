package csc207.HWNA.scheduler;

import java.util.ArrayList;

/**
 * @author Harry Baker
 * @author William Royle
 * @author Nicolas Knoebber
 * @author Amanda Hinchman
 * 
 * @date November 24, 2014
 * (Refactored December 1, 2014)
 *
 * Represents a schedule, as in a set of teams playing each other on a set of
 * dates. A schedule can be full, in which case each school is playing each other
 * school twice, both home and away, or unfilled, in which case no schools are yet playing. 
 * Each schedule has an ArrayList of Day objects, to hold the matches which take place,
 * (this will be empty if the schedule is unfilled). However, each schedule also has an 
 * ArrayList of PairSchools, which are the matches which must play, and an ArrayList of
 * the Schools. The Day objects in the Schedule are not required to point to the PairSchool
 * objects in the Schedule. However, a PairSchool object where object.equals(pair) must
 * exist for each pair inside each Day object in the Schedule. The object must also have 
 * the same canAdd status.
 * 
 * There is exactly one Day object for each ScheduleDate in allDates, so for each day in 
 * theDays, we have exactly one date in allDates such that day.isDate(date)==true
 */
public class Schedule
{

  // +--------+------------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * Holds all of the matches which must occur sometime in the schedule. May or may not have
   * yet been distributed between the Day object 
   */
  ArrayList<PairSchools> pairs;

  /**
   * An arraylist of all of the ScheduleDates in the schedule. These are the days when a 
   * game could potentially occur
   */
  final ArrayList<ScheduleDate> allDates;

  /**
   * Holds all of the schools playing in the schedule.
   */
  final ArrayList<School> schools;

  /**
   * An arraylist of all of the Days in the schedule. The main contents of the schedule,
   * as holds all the information on when the actual matches occur. One Day object for 
   * each date object (see Schedule class description)
   */
  ArrayList<Day> theDays = new ArrayList<Day>();

  /**
   * Constructs a new unfilled Schedule
   * @param pairs - the ScheduleDate matches which must take place
   *        dates - the potential dates for matches to occur on
   *        schools - the schools competing
   * @pre
   *    pairs must not be null
   *    dates must not be null
   *    schools must not be null
   */
  Schedule(ArrayList<PairSchools> pairs, ArrayList<ScheduleDate> dates,
           ArrayList<School> schools)
  {
    this.pairs = pairs;
    this.allDates = dates;
    this.schools = schools;
    /*
     *  We add Days corresponding to our Dates, days have no ScheduleDate matches
     *  yet
     */
    for (ScheduleDate toPlay : allDates)
      {
        theDays.add(new Day(toPlay));
      }// for
  }// Schedule(ArrayList<PairSchools> pairs, ArrayList<ScheduleDate> dates, ArrayList<School> schools)

  /**
   * Constructs a new filled Schedule
   * @param pairs - the ScheduleDate matches which must take place
   *        dates - the potential dates for matches to occur on
   *        schools - the schools competing
   * @pre
   *    pairs must not be null
   *    dates must not be null
   *    schools must not be null
   *    theDates must not be null
   *    for each PairSchool pairSchool within each Day object in theDays, we must have 
   *    exactly one PairSchool object where pairSchool.equals(pair) must exist for each 
   *    pair inside of pairs.
   *    for each Day object day in theDays, we must have exactly one ScheduleDate 
   *    scheduleDate in dates such that day.isDate(ScheduleDate) is true
   */
  private Schedule(ArrayList<PairSchools> pairs, ArrayList<ScheduleDate> dates,
                   ArrayList<School> schools, ArrayList<Day> theDays)
  {

    this.pairs = pairs;
    this.allDates = dates;
    this.schools = schools;
    this.theDays = theDays;
  }// Schedule(ArrayList<PairSchools> pairs, ArrayList<ScheduleDate> dates, ArrayList<School> schools, ArrayList<Day> theDays)

  // +---------+-----------------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Returns a string representation of this schedule, in particular we print the
   * filled portions (ie the Day objects and contents)
   */
  public String toString()
  {
    String toReturn = "";
    // We consider adding each day to the returned string, with newline separation
    for (Day theDate : theDays)
      {
        // We only add those days during which teams are playing
        if (theDate.matchesPlaying())
          {
            toReturn += theDate.toString() + '\n';
          }// if
      }// for
    return toReturn;
  }// toString()

  /**
   * Returns true if we can add toAdd to the schedule on the specified date (dateGame)
   * @param toAdd - the PairSchools we want to add to the specified date
   *        dateGame - the date representing the day we want to add toAdd to
   * @returns
   *        return true if dateGame is among the Schools optional or mandatory play dates,
   *        and toAdd is not already playing that day, and toAdd has not already been
   *        scheduled. Otherwise returns false
   */
  public boolean canAddToDate(PairSchools toAdd, ScheduleDate dateGame)
  {
    // We find the day corresponding to dateGame
    for (Day mayBeDay : theDays)
      {
        // We see if we can add toAdd to the day
        if (mayBeDay.isDate(dateGame))
          {
            return mayBeDay.canAdd(toAdd);
          }// if
      }// for
    return false;
  }// canAdd(PairSchools toAdd, ScheduleDate dateGame)

  /**
   * Adds toAdd to the schedule on the specified date (dateGame)
   * @param toAdd - the PairSchools we to add to the specified date
   *        dateGame - the date representing the day we add toAdd to
   * @pre 
   *    We have exactly one date in allDates such that date.equals(theDate)==true
   */
  public void addGame(PairSchools toAdd, ScheduleDate theDate)
  {
    // We find the day corresponding to dateGame
    for (Day mayBeDay : theDays)
      {
        // We add toAdd to the day
        if (mayBeDay.isDate(theDate))
          {
            mayBeDay.addGame(toAdd);
            break;
          }// if
      }// for
  }// addGame(PairSchools toAdd, ScheduleDate theDate)

  /**
   * Returns the number of Schools
   * @returns
   *    schools.size();
   */
  public int numSchools()
  {
    return schools.size();
  }// numSchools()

  /**
   * Returns a semi-shallow clone of the Schedule. Our clone shares allDates and 
   * Schools with the original. However, a new ArrayList of PairSchools is created with
   * new PairSchool objects so that we can edit our clone independently of the original
   * @post
   *    for all Integer 0 <= i < theDays.size()
   *            this.theDays.get(i).equals(this.clone().theDays.get(i)
   *            this.theDays.size() == this.clone().theDays.size()
   *     
   */
  public Schedule clone()
  {
    // We make hard copies of our Day objects
    ArrayList<Day> theNewDates = new ArrayList<Day>();
    for (Day toCopy : theDays)
      {
        theNewDates.add(toCopy.clone());
      }// for
    // We make hard copies of our PairSchool objects
    ArrayList<PairSchools> theNewPairSchools = new ArrayList<PairSchools>();
    for (PairSchools toCopy : pairs)
      {
        theNewPairSchools.add(toCopy.clone());
      }// for
    /*
     *  We join our copies with the elements which do not need to be copied
     *  and return
     */
    return new Schedule(theNewPairSchools, allDates, schools, theNewDates);
  }// clone()

}// class Schedule
