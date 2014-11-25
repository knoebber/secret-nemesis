package csc207.HWNA.scheduler;

import java.util.ArrayList;
import java.util.Random;

public class UtilsSchedule
{

  public static ArrayList<ScheduleDate>
    findBackToBack(ArrayList<ScheduleDate> dates)
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
        if (dateDifference == 1 || dateDifference == -364
            || dateDifference == 0)
          {
            backToBacks.add(trackerDate1);
            backToBacks.add(trackerDate2);
          }
      }
    return backToBacks;
  }

  
  
  
  public static boolean canAdd(PairSchools theMatch, ScheduleDate theDate,
                               Schedule addTo)
  {
    if (!theMatch.home.canPlay(theDate) || !theMatch.away.canPlay(theDate))
      {
        return false;
      }
    int foundDates = 0;
    int validAddition = 1;
    ArrayList<ScheduleDate> allDates = addTo.allDates;
    ArrayList<Game> gameList = addTo.gameList;
    for (int iter = 0; iter < allDates.size(); iter++)
      {
        if (allDates.get(iter).equals(theDate))
          {
            foundDates++;
          }
      }

    for (int iter = 0; iter < gameList.size(); iter++)
      {
        PairSchools existantGamePair = gameList.get(iter).competing;
        if (gameList.get(iter).dayOfCalendar.equals(theDate))
          {
            if (theMatch.home == existantGamePair.home
                || theMatch.home == existantGamePair.away)
              {
                validAddition = 0;
              }
            if (theMatch.away == existantGamePair.home
                || theMatch.away == existantGamePair.away)
              {
                validAddition = 0;
              }
          }
      }
    return (foundDates == 1) && (validAddition == 1);
  }

  
  

  public static Schedule mergeSchedule(Schedule A, Schedule B)
  {
    A.allDates.addAll(B.allDates);
    A.gameList.addAll(B.gameList);
    return A;
  }

  
  

  private static Schedule
    fillBackToBackWeekend(ScheduleDate sat, ScheduleDate sun,
                          ArrayList<School> cantHome,
                          ArrayList<School> cantAway,
                          ArrayList<PairSchools> theMatches, int numSchools)
  {
    ArrayList<ScheduleDate> theDates = new ArrayList<ScheduleDate>();
    theDates.add(sat);
    theDates.add(sun);
    Schedule soFar = new Schedule(null, theDates, numSchools);
    Random dice = new Random();
    boolean added = false;
    int firstPairNum = 0;
    while (!added)
      {
        firstPairNum = dice.nextInt(theMatches.size());
        PairSchools addPairSchool = theMatches.get(firstPairNum);
        if (addPairSchool.canAdd && !cantHome.contains(addPairSchool.home)
            && !cantAway.contains(addPairSchool.away))
          {
            addPairSchool.canAdd = false;
            System.out.println("Adding " + addPairSchool.home.name + " vs "
                               + addPairSchool.away.name + " to saturday");
            soFar.gameList.add(new Game(addPairSchool, sat));
            added = true;
          }
      }
    School firstSchool = theMatches.get(firstPairNum).home;
    cantHome.add(firstSchool);
    School prevSchool = theMatches.get(firstPairNum).away;
    cantAway.add(prevSchool);
    boolean found;
    for (int count = 0; count < numSchools - 2; count++)
      {
        found = false;
        if (count % 2 == 0)//even, sunday
          {
            while (!found)
              {
                int pairNum = dice.nextInt(theMatches.size());
                PairSchools toInsert = theMatches.get(pairNum);
                if (toInsert.away.equals(prevSchool.name) && canAdd(toInsert, sun, soFar)
                    && toInsert.canAdd && !toInsert.home.equals(firstSchool)
                    && !cantHome.contains(toInsert.home))
                  {
                    toInsert.canAdd = false;
                    System.out.println("Adding " + toInsert.home.name + " vs "
                                       + toInsert.away.name + " to sunday");
                    found = true;
                    soFar.gameList.add(new Game(toInsert, sun));
                    prevSchool = toInsert.home;
                    cantHome.add(prevSchool);
                  }
              }

          }
        else
          //saturday
          {
            while (!found)
              {
                int pairNum = dice.nextInt(theMatches.size());
                PairSchools toInsert = theMatches.get(pairNum);
                if (toInsert.home.equals(prevSchool.name) && canAdd(toInsert, sat, soFar)
                    && toInsert.canAdd && !cantAway.contains(toInsert.away))
                  {

                    toInsert.canAdd = false;
                    System.out.println("Adding " + toInsert.home.name + " vs "
                                       + toInsert.away.name + " to saturday");
                    found = true;
                    soFar.gameList.add(new Game(toInsert, sat));
                    prevSchool = toInsert.away;
                    cantAway.add(prevSchool);
                  }
              }
          }

      }
    found = false;
    while (!found)
      {
        int pairNum = dice.nextInt(theMatches.size());
        PairSchools toInsert = theMatches.get(pairNum);

        if (toInsert.away.equals(prevSchool.name)
            && toInsert.home.equals(firstSchool.name) && toInsert.canAdd)
          {
            System.out.println("Adding " + toInsert.home.name + " vs "
                               + toInsert.away.name + " to sunday");
                found = true;
                soFar.gameList.add(new Game(toInsert, sun));
          }
      }

    return soFar;
  }

  public static Schedule fillBackToBackWeekends(ScheduleDate sat, ScheduleDate sun,
                                         ScheduleDate sat2, ScheduleDate sun2,
                                         ArrayList<PairSchools> theMatches,
                                         int numSchools)
  {
    ArrayList<School> cantPlayHome = new ArrayList<School>();
    ArrayList<School> cantPlayAway = new ArrayList<School>();
    Schedule schedule1 = fillBackToBackWeekend(sat,sun,cantPlayHome,cantPlayAway,theMatches,numSchools);
    Schedule schedule2= fillBackToBackWeekend(sat2,sun2,cantPlayHome,cantPlayAway,theMatches,numSchools);
    
    
    
    
    return mergeSchedule(schedule1, schedule2);
  }

}
