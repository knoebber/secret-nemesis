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
    if (!theMatch.canAdd)
      {
        return false;
      }
    
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
                          ArrayList<PairSchools> theMatches, int numSchools, ArrayList<School> schools)
  {
    ArrayList<ScheduleDate> theDates = new ArrayList<ScheduleDate>();
    theDates.add(sat);
    theDates.add(sun);
    Schedule soFar = new Schedule(null, theDates, numSchools, schools);
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
            System.out.println("Adding " + addPairSchool.home.name + " vs "
                               + addPairSchool.away.name + " to saturday");
            soFar.addGame(new Game(addPairSchool, sat));
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
                    System.out.println("Adding " + toInsert.home.name + " vs "
                                       + toInsert.away.name + " to sunday");
                    found = true;
                    soFar.addGame(new Game(toInsert, sun));
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

                    System.out.println("Adding " + toInsert.home.name + " vs "
                                       + toInsert.away.name + " to saturday");
                    found = true;
                    soFar.addGame(new Game(toInsert, sat));
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
                soFar.addGame(new Game(toInsert, sun));
          }
      }

    return soFar;
  }

  public static Schedule fillBackToBackWeekends(ScheduleDate sat, ScheduleDate sun,
                                         ScheduleDate sat2, ScheduleDate sun2,
                                         ArrayList<PairSchools> theMatches,
                                         int numSchools,ArrayList<School> schools)
  {
    ArrayList<School> cantPlayHome = new ArrayList<School>();
    ArrayList<School> cantPlayAway = new ArrayList<School>();
    Schedule schedule1 = fillBackToBackWeekend(sat,sun,cantPlayHome,cantPlayAway,theMatches,numSchools,schools);
    Schedule schedule2= fillBackToBackWeekend(sat2,sun2,cantPlayHome,cantPlayAway,theMatches,numSchools,schools);
    
    
    
    
    return mergeSchedule(schedule1, schedule2);
  }
  
  private class AllocatedSchool
  {
    School theSchool;
    
    ArrayList<ScheduleDate> needToPlayGames;
    
    @SuppressWarnings("unchecked")
    AllocatedSchool(School theSchool)
    {
      this.theSchool=theSchool;
      needToPlayGames=(ArrayList<ScheduleDate>)theSchool.gameDates.clone();
    }
    
    
    public boolean allPlayed()
    {
      return needToPlayGames.size()==0;
    }
    
    public boolean metRequirement(ScheduleDate theDate)
    {
      return needToPlayGames.remove(theDate);
    }
    
    public ScheduleDate getRequirement()
    {
      return needToPlayGames.get(0);
    }
    
    public School getSchool()
    {
      return theSchool;
    }
    
    public void registerGame(Game played)
    {
      if (played.isPlaying(theSchool))
        {
          metRequirement(played.dayOfCalendar);
        }
    }
     
  }
  
  
  public static PairSchools randomPair(ArrayList<PairSchools> theMatches)
  {
    Random dice = new Random();
    int pairNum = dice.nextInt(theMatches.size());
    return theMatches.get(pairNum);
  }
  
  // gets a random pair where the school is playing
  public static PairSchools randomPairWithSchool(ArrayList<PairSchools> theMatches,School theSchool)
  {
    PairSchools found=randomPair(theMatches);
    int count=0;
    while(!found.home.equals(theSchool) && !found.away.equals(theSchool))
      {
        found=randomPair(theMatches);
        count++;
        if (count>1000)
          {
            System.out.println("randomPairWithSchool Broke");
          }
      }
    return found;
  }
  
  //DO NOT PUT BACK TO BACK WEEKENDS AS MUST PLAY DATES
  //Are we responsible for hash table lab report
  public static Schedule fillDates(ArrayList<ScheduleDate> theDates, ArrayList<PairSchools> theMatches, ArrayList<School> theSchools)
  {
    UtilsSchedule Dummy = new UtilsSchedule();
    Schedule generated = new Schedule(theMatches,theDates,theSchools.size(),theSchools);
    ArrayList<AllocatedSchool> schoolRequirements = new ArrayList<AllocatedSchool>();
    for (School school : theSchools)
    {
      schoolRequirements.add(Dummy.new AllocatedSchool(school));
    }
    // We make sure every school plays on must play dates
    for (AllocatedSchool toSatisfy : schoolRequirements)
    {
      // Until the school has played all needed games
      while(!toSatisfy.allPlayed())
        {
          // We get a random pair where the school is playing 
          PairSchools toInsert = randomPairWithSchool(theMatches,toSatisfy.getSchool());
          // We get new pairs with the school until we can actually add it to the date
          while (!canAdd(toInsert,toSatisfy.getRequirement(),generated))
            {
              toInsert = randomPairWithSchool(theMatches,toSatisfy.getSchool());
            }
          // We add the game to our schedule
          Game played = new Game(toInsert,toSatisfy.getRequirement());
          generated.addGame(played);
          // We register that both the home and away schools have played
          for (AllocatedSchool satisfied : schoolRequirements)
            {
              satisfied.registerGame(played);
            }  
        }
    }
    
    // We place remaining PairSchools objects into schedule
    for (PairSchools toInsert : theMatches)
      {
        if (toInsert.canAdd)
          {
            ScheduleDate tryDate = randomScheduleDate(theDates);
            int count=0;
            while(!canAdd(toInsert,tryDate,generated))
              {
                tryDate = randomScheduleDate(theDates);
                count++;
                if (count > 1000)
                  {
                    //System.out.println("Giving up on "+toInsert.);
                    break;
                  }
              }
            generated.addGame(new Game(toInsert,tryDate));
          }
      }
    
    
    
    
    return generated;
  }
  
  
public static ScheduleDate randomScheduleDate(ArrayList<ScheduleDate> theDates)
{
  Random dice = new Random();
  int dateNum = dice.nextInt(theDates.size());
  return theDates.get(dateNum);
}
  
 
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  

}
