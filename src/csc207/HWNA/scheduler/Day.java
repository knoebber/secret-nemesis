package csc207.HWNA.scheduler;

import java.util.ArrayList;

public class Day
{
  ArrayList<PairSchools> competing;
  final ScheduleDate theDate;

  Day(ArrayList<PairSchools> competing, ScheduleDate theDate)
  {
    this.competing = competing;
    this.theDate = theDate;
  }

  Day(ScheduleDate theDate)
  {
    this.competing = new ArrayList<PairSchools>();
    this.theDate = theDate;
  }

  public String toString()
  {
    String start = theDate.toString() + ":";
    for (PairSchools thePair : competing)
      {
        start += thePair.toString();
      }
    return start;
  }

  public boolean matchesPlaying()
  {
    return competing.size() != 0;
  }

  public boolean isDate(ScheduleDate potentialDate)
  {
    return potentialDate.equals(theDate);
  }

  public boolean canAdd(PairSchools theMatch)
  {
    if (!theMatch.canAdd)
      {
        return false;
      }
    if (!theMatch.home.canPlay(theDate) || !theMatch.away.canPlay(theDate))
      {
        return false;
      }
    for (PairSchools competingSchoolPair : competing)
      {

        if (theMatch.home.equals(competingSchoolPair.home)
            || theMatch.home.equals(competingSchoolPair.away))
          {
            return false;
          }
        if (theMatch.away.equals(competingSchoolPair.home)
            || theMatch.away.equals(competingSchoolPair.away))
          {
            return false;
          }
      }
    return true;
  }

  public void addGame(PairSchools toAdd)
  {
    toAdd.canAdd = false;
    competing.add(toAdd);
  }

  public PairSchools removeGame(int positionToRemove)
  {
    return competing.remove(positionToRemove);
  }

  public boolean schoolPlays(School potentialPlayer)
  {
    for (PairSchools playing : competing)
      {
        if (playing.home.equals(potentialPlayer)
            || playing.away.equals(potentialPlayer))
          {
            return true;
          }
      }
    return false;
  }

  public Day clone()
  {
    // We need to clone the pairschools as methods modify them
    ArrayList<PairSchools> theNewPairSchools = new ArrayList<PairSchools>();
    for (PairSchools toCopy : competing)
      {
        theNewPairSchools.add(toCopy.clone());
      }
    return new Day(theNewPairSchools, theDate);
  }

}
