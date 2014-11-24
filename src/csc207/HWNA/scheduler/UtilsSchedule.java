package csc207.HWNA.scheduler;

import java.util.ArrayList;
import java.util.Random;

public class UtilsSchedule
{
  
  
  
  public static ArrayList<ScheduleDate> findBackToBack(ArrayList<ScheduleDate> dates)
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
  
  
  
  
  public static boolean canAdd(PairSchools theMatch, ScheduleDate theDate, Schedule addTo)
  {
    if (!theMatch.home.canPlay(theDate) || !theMatch.away.canPlay(theDate))
      {
        return false;
      }
    int foundDates = 0;
    int validAddition = 1;
    ArrayList<ScheduleDate> allDates = addTo.allDates;
    ArrayList<Game> gameList = addTo.gameList;
    for (int iter=0 ; iter<allDates.size() ; iter++)
      {
        if (allDates.get(iter).equals(theDate))
          {
            foundDates++;
          }
      }
    
    for (int iter=0 ; iter<gameList.size() ; iter++)
      {
        PairSchools existantGamePair=gameList.get(iter).competing;
        if (gameList.get(iter).dayOfCalendar.equals(theDate))
          {
              if (theMatch.home==existantGamePair.home ||theMatch.home==existantGamePair.away )
                {
                  validAddition=0;
                }
              if (theMatch.away==existantGamePair.home ||theMatch.away==existantGamePair.away )
                {
                  validAddition=0;
                }
          }
      }
    return (foundDates==1) && (validAddition==1);
  }
  
  
  public static Schedule fillBackToBackDates(ScheduleDate sat,ScheduleDate sun, ArrayList<PairSchools> theMatches, int numSchools)
  {
    ArrayList<Game> saturdayGames = new ArrayList<Game>();
    ArrayList<Game> sundayGames = new ArrayList<Game>();
    ArrayList<ScheduleDate> theDates = new ArrayList<ScheduleDate>();
    theDates.add(sat);
    theDates.add(sun);
    Schedule Dummy=new Schedule(null,theDates,numSchools);
    Random dice = new Random();
    int firstPairNum = dice.nextInt(theMatches.size());
    saturdayGames.add(new Game(theMatches.get(firstPairNum),sat));
    School firstSchool=theMatches.get(firstPairNum).home;
    School prevSchool=theMatches.get(firstPairNum).away;
    boolean found;
    for (int count=0;count<numSchools-2;count++)
      {
        found=false;
        if (count % 2==0)//even, sunday
          {
            while (!found)
              {
                int pairNum = dice.nextInt(theMatches.size());
                PairSchools toInsert = theMatches.get(pairNum);
                if (toInsert.away.equals(prevSchool.name) && canAdd(toInsert,sun,Dummy))
                  {
                    found=true;
                    sundayGames.add(new Game(toInsert,sun));
                    prevSchool=toInsert.home;
                  }
              }
            
          }
        else //saturday
          {
            while (!found)
              {
                int pairNum = dice.nextInt(theMatches.size());
                PairSchools toInsert = theMatches.get(pairNum);
                if (toInsert.home.equals(prevSchool.name) && canAdd(toInsert,sat,Dummy))
                  {
                    found=true;
                    saturdayGames.add(new Game(toInsert,sat));
                    prevSchool=toInsert.away;
                  }
              }
          }
        
      }
    found=false;
    while (!found)
      {
        int pairNum = dice.nextInt(theMatches.size());
        PairSchools toInsert = theMatches.get(pairNum);
        if (toInsert.away.equals(prevSchool.name) && canAdd(toInsert,sun,Dummy)&&toInsert.home.equals(firstSchool.name))
          {
            found=true;
            sundayGames.add(new Game(toInsert,sat));
          }
      }
    Dummy.gameList.addAll(saturdayGames);
    Dummy.gameList.addAll(sundayGames);
    
    
    
    
    
    return Dummy;
  }
  
 
  
  
  
}
