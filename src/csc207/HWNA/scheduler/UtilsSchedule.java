package csc207.HWNA.scheduler;

import java.util.ArrayList;

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
    int foundDates = 0;
    int validAddition = 1;
    ArrayList<ScheduleDate> allDates = addTo.allDates;
    ArrayList<Schedule.Game> gameList = addTo.gameList;
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
  
  
  
  
}
