package csc207.HWNA.scheduler;

import java.util.ArrayList;
import java.util.Random;

public class UtilsSchedule
{

  public static void optimalSchedule(Schedule originalSchedule, int trials)
  {

    Schedule bestSchedule = originalSchedule.clone();
    fillScheduleBasic(bestSchedule);
    Schedule theSchedule;
    for (int trial = 0; trial < trials; trial++)
      {
        System.out.println("Permuting");
        theSchedule = bestSchedule.clone();
        permute(theSchedule, 20);
        if (rate(theSchedule) > rate(bestSchedule))
          {
            System.out.println("Permutation Saved!");
            bestSchedule = theSchedule;
          }
      }
    originalSchedule.theDates = bestSchedule.theDates;
    originalSchedule.pairs = bestSchedule.pairs;
  }

  public static void permute(Schedule toPermute, int permutations)
  {
    Random permGen = new Random();
    ArrayList<Day> daysToModify = toPermute.theDates;
    ArrayList<ScheduleDate> noPermuteDates = findBackToBack(toPermute.allDates);
    for (int perm = 0; perm < permutations; perm++)
      {
        Day toTakeFrom = daysToModify.get(permGen.nextInt(daysToModify.size()));
        Day toAddTo = daysToModify.get(permGen.nextInt(daysToModify.size()));
        if (toTakeFrom.matchesPlaying()
            && !noPermuteDates.contains(toTakeFrom.theDate)
            && !noPermuteDates.contains(toAddTo.theDate))
          {
            PairSchools toReallocate =
                toTakeFrom.removeGame(permGen.nextInt(toTakeFrom.competing.size()));
            toAddTo.addGame(toReallocate);
          }
      }

  }

  // Back to Backs will cause no violations
  public static int rate(Schedule toRate)
  {
    int violations = 0;
    for (Day gameTime : toRate.theDates)
      {
        // We do not need to check back to backs
        ArrayList<School> playedSoFar = new ArrayList<School>();
        for (PairSchools match : gameTime.competing)
          {
            // Does a school play multiple times in a day
            if (playedSoFar.contains(match.home))
              {
                violations += 5;
              }
            else
              {
                playedSoFar.add(match.home);
              }
            if (playedSoFar.contains(match.away))
              {
                violations += 5;
              }
            else
              {
                playedSoFar.add(match.away);
              }
            // Can the schools actually play the day in question
            if (!match.home.canPlay(gameTime.theDate))
              {
                violations += 2;
              }
            if (!match.away.canPlay(gameTime.theDate))
              {
                violations += 2;
              }
          }
      }
    // Do the schools play all must play dates? 
    for (School toTest : toRate.schools)
      {
        // Each must play Date
        for (ScheduleDate mustPlay : toTest.gameDates)
          {
            // Find corresponding date in the schedule
            for (Day gameTime : toRate.theDates)
              {
                // Make sure is same date, if school doesn't play assess violation
                if (gameTime.isDate(mustPlay) && !gameTime.schoolPlays(toTest))
                  {
                    // See if school plays on day
                    violations += 2;
                  }
              }
          }

      }
    return -violations;
  }

  public static void fillScheduleBasic(Schedule toFill)
  {
    ArrayList<ScheduleDate> omitDates = findBackToBack(toFill.allDates);
    for (ScheduleDate potentialFill : toFill.allDates)
      {
        if (!omitDates.contains(potentialFill))
          {
            for (PairSchools pairToInsert : toFill.pairs)
              {
                if (pairToInsert.canAdd)
                  {
                    toFill.addGame(pairToInsert, potentialFill);
                  }
              }
            break;
          }
      }
  }

  public static void optimalFillBackToBack(Schedule originalSchedule,
                                           int trials, int allowedTravel)
  {
    Schedule bestSchedule = originalSchedule.clone();
    Schedule theSchedule;
    fillBackToBackWeekendsNew(bestSchedule);
    for (int trial = 0; trial < trials; trial++)
      {
        theSchedule = originalSchedule.clone();
        fillBackToBackWeekendsNew(theSchedule);
        if (backToBackWeekendRanking(theSchedule, allowedTravel) > backToBackWeekendRanking(bestSchedule,
                                                                                            allowedTravel))
          {
            bestSchedule = theSchedule;
          }
      }
    originalSchedule.theDates = bestSchedule.theDates;
    originalSchedule.pairs = bestSchedule.pairs;
  }

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

  private static int backToBackWeekendRanking(Schedule theSchedule,
                                              int maxRecommended)
  {
    ArrayList<ScheduleDate> backToBackDates =
        findBackToBack(theSchedule.allDates);
    int excessiveDistances = 0;
    for (ScheduleDate theDay : backToBackDates)
      {
        for (Day scheduleDay : theSchedule.theDates)
          {
            if (scheduleDay.isDate(theDay))
              {
                for (PairSchools match : scheduleDay.competing)
                  {
                    excessiveDistances +=
                        Math.max(match.distance - maxRecommended, 0)
                            * Math.max(match.distance - maxRecommended, 0);
                  }
                break;
              }
          }
      }
    return -excessiveDistances;
  }

  private static void fillBackToBackWeekendsNew(Schedule toFill)
  {
    ArrayList<ScheduleDate> backToBackDates = findBackToBack(toFill.allDates);
    ArrayList<School> cantHome = new ArrayList<School>();
    ArrayList<School> cantAway = new ArrayList<School>();
    ArrayList<PairSchools> theMatches = toFill.pairs;
    int numSchools = toFill.numSchools();
    Random dice = new Random();
    for (int weekends = 0; weekends < 2; weekends++)
      {
        ScheduleDate sat = backToBackDates.get(2 * weekends);
        ScheduleDate sun = backToBackDates.get(2 * weekends + 1);
        boolean added = false;
        int firstPairNum = 0;
        // We add the first day to saturday
        while (!added)
          {
            firstPairNum = dice.nextInt(theMatches.size());
            PairSchools addPairSchool = theMatches.get(firstPairNum);
            if (!cantHome.contains(addPairSchool.home)
                && !cantAway.contains(addPairSchool.away))
              {
                toFill.addGame(addPairSchool, sat);
                added = true;
              }
          }
        // Register that we have added the first day
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
                    if (toInsert.away.equals(prevSchool)
                        && toFill.canAdd(toInsert, sun)
                        && !cantHome.contains(toInsert.home))
                      {
                        found = true;
                        toFill.addGame(toInsert, sun);
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
                    if (toInsert.home.equals(prevSchool.name)
                        && toFill.canAdd(toInsert, sat)
                        && !cantAway.contains(toInsert.away))
                      {
                        found = true;
                        toFill.addGame(toInsert, sat);
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
            /*
             *  We add the last SchoolPair to complete the back to back weekend
             *  We must add an existing SchoolPair instead of a new one so that 
             *  we ensure we have the distance correct
             */
            if (toInsert.away.equals(prevSchool.name)
                && toInsert.home.equals(firstSchool.name))
              {
                found = true;
                toFill.addGame(toInsert, sun);
              }
          }

      }

  }

}
