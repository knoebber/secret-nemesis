package csc207.HWNA.scheduler;

import java.util.ArrayList;


/**
 * @author Harry Baker
 * @author William Royle
 * @author Nicolas Knoebber
 * @author Amanda Hinchman
 * 
 * @date December 1, 2014
 *
 * Holds information a date and all the schools which play during that date.
 * While the date itself is final, the schools which play on the date can 
 * be modified
 */
public class Day
{
  
  // +--------+------------------------------------------------------------
  // | Fields |
  // +--------+
  
  /**
   * An arraylist of all the matches taking place during the day, in no
   * particular order
   */
  ArrayList<PairSchools> competing;
  
  /**
   * The date of the matches
   */
  final ScheduleDate theDate;
  
  // +--------------+------------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Creates a new Day object, with the specified schools playing on the 
   * specified date.
   * @param competing - the PairSchools matches taking place
   *        theDate - the Day for this schedule to represent
   * @pre
   *    theDate, competing must not be null
   */
  Day(ArrayList<PairSchools> competing, ScheduleDate theDate)
  {
    this.competing = competing;
    this.theDate = theDate;
  }// Day(ArrayList<PairSchools> competing, ScheduleDate theDate)
  
  /**
   * Creates a new Day object, for the specified date, with no schools 
   * playing
   * @pre
   *    theDate must not be null
   */
  Day(ScheduleDate theDate)
  {
    this.competing = new ArrayList<PairSchools>();
    this.theDate = theDate;
  }// Day(ScheduleDate theDate)
  
  // +---------+-----------------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Returns a string representation of the day
   */
  public String toString()
  {
    // We print the date
    String start = theDate.toString() + ":";
    // We print the schools which are playing
    for (PairSchools thePair : competing)
      {
        start += thePair.toString();
      }// for
    return start;
  }// toString()

  /**
   * Returns whether any matches have been allocated for the day
   * @post
   *    if competing.size()>0, returns true
   *    otherwise, returns false
   */
  public boolean matchesPlaying()
  {
    return competing.size() != 0;
  }// matchesPlaying()

  /**
   * Returns whether the date of the day is the same as the date passed in
   * @param potentialDate - the ScheduleDate to check to see if it represents
   *                        the time of this Day
   * @post
   *    returns potentialDate.equals(theDate)
   */
  public boolean isDate(ScheduleDate potentialDate)
  {
    return potentialDate.equals(theDate);
  }// isDate(ScheduleDate potentialDate)
  

  /**
   * Returns whether a match can be added to this Day
   * @param theMatch - the PairSchools object we see if we can add to the Day
   * @post
   *    if !theMatch.canAdd, return false
   *    if !theMatch.home.canPlay(theDate) or !theMatch.away.canPlay(theDate), return false
   *    if the specified school is already playing on this date, return false
   */
  public boolean canAdd(PairSchools theMatch)
  {
    // Check to see if the match can be allotted in the first place
    if (!theMatch.canAdd)
      {
        return false;
      }// if
    // We find if both the home and the away schools are allowed to play on this Day
    if (!theMatch.home.canPlay(theDate) || !theMatch.away.canPlay(theDate))
      {
        return false;
      }// if
    // We find if either the home or away school already playing on this day
    for (PairSchools competingSchoolPair : competing)
      {

        if (theMatch.home.equals(competingSchoolPair.home)
            || theMatch.home.equals(competingSchoolPair.away))
          {
            return false;
          }// if
        if (theMatch.away.equals(competingSchoolPair.home)
            || theMatch.away.equals(competingSchoolPair.away))
          {
            return false;
          }// if
      }// for (PairSchools competingSchoolPair : competing)
    return true;
  }// canAdd(PairSchools theMatch)


  /**
   * Adds a PairSchools match to the day, regardless of whether the 
   * PairSchools match should logically be added to the day
   * @param theAdd - the PairSchools object we add to the Day
   * @pre
   *    toAdd in not null
   * @post
   *    competing.size() has increased by one
   */
  public void addGame(PairSchools toAdd)
  {
    // We indicate that we cannot add the match to any schedule component again
    toAdd.canAdd=false;
    // We add the match to our Day
    competing.add(toAdd);
  }// addGame(PairSchools toAdd)
  
  /**
   * Returns a match (PairSchools object) from the specified position in the day
   * @param positionToRemove - the position of the ScheduleDate object we want to 
   *                           remove within competing
   * @pre
   *    positionToRemove >= 0 , positionToRemove < competing.size()
   * @post
   *    competing.size() has increased by one
   */
  public PairSchools removeGame(int positionToRemove)
  {
    return competing.remove(positionToRemove);
  }// removeGame(int positionToRemove)
  
  
  
  /**
   * Finds out whether potentialPlayer is playing a match this day
   * @param schoolPlays - the school we want to see if plays on this day
   * @pre
   *    potentialPlayer is not null
   * @post
   *    if Integer i exists such that competing.get(i).home.equals(potentialPlayer)
   *                     or such that competing.get(i).away.equals(potentialPlayer)
   *    returnes true, otherwise, returns false
   */
  public boolean schoolPlays(School potentialPlayer)
  {
    // We loop through every match on the day
    for (PairSchools playing : competing)
      {
        // We return true if the school is already playing in one of the matches
        if (playing.home.equals(potentialPlayer)||playing.away.equals(potentialPlayer))
          {
            return true;
          }// if
      }// for
    return false;
  }// schoolPlays(School potentialPlayer)
  
  
  /**
   * Returns a semi-shallow clone of this Day object (ie, new copies of each PairSchools object
   * in competing are created, but the ScheduleDate is not)
   * @post
   *    for Integer i where i > 0, i < competing.size()
   *            this.clone().competing.get(i) == this.competing.get(i)
   *    this.clone().competing.size() == this.competing.size()
   *    this.clone().equals(this) == false
   */
  public Day clone()
  {
    // We need to clone the pairschools as methods modify them
    ArrayList<PairSchools> theNewPairSchools = new ArrayList<PairSchools>();
    for (PairSchools toCopy : competing)
      {
        theNewPairSchools.add(toCopy.clone());
      }// for
    // We make a new day with our deeply cloned ArrayList of PairSchools
    return new Day(theNewPairSchools,theDate);
  }// clone()

}// class Day
