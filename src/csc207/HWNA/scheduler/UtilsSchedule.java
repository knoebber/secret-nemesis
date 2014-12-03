package csc207.HWNA.scheduler;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Harry Baker
 * @author William Royle
 * @author Nicolas Knoebber
 * @author Amanda Hinchman
 *
 * @date December 2, 2014
 *
 * Tools used for generating optimal schedules. Note that all methods modify
 * the schedules, as opposed to creating new Schedules. This is done for the 
 * sake of simplicity, as it is best to have one paradigm in terms of schedule
 * generation
 */
public class UtilsSchedule
{

  /**
   * Generates an optimal schedule. We first fill the back to back dates in a way
   * which minimizes the back-to-back distance travelled by opposing teams. We then
   * use the rest of the PairSchools objects. We use the unused (canAdd==true) of the PairSchools 
   * objects by first putting all the PairSchools matches on the first non 
   * back-to-back date. Then we randomly permute our schedule and rank it. We save 
   * the schedule if the ranking is an improvement. We continue this process until we
   * have exceeded trials.
   * @param originalSchedule - the unfilled schedule this method fills
   *        trials - The number of times to randomly permute our schedule
   *        permutations - The number of permutations to perform for each permute step
   * @post
   *    For each PairSchools pairSchools in originalSchedule.pairs we have 
   *    pairSchools.canAdd == false
   */
  public static void optimalFillSchedule(Schedule originalSchedule, int trials,
                                         int permutations)
  {
    // We fill back to back dates
    optimalFillBackToBack(originalSchedule, 10, 180);
    // We fill all other dates
    optimalSchedule(originalSchedule, trials, permutations);
  }// optimalFillSchedule(Schedule originalSchedule, int trials, int permutations)

  /**
   * This method distributes the unused (canAdd==true) PairSchools objects in originalSchedule
   * between the Day objects in originalSchedule. We use the rest of the PairSchools 
   * objects by first putting all the unused PairSchools matches on the first non back-to-back date.
   * Then we randomly permute our schedule and rank it. We save the schedule if the ranking 
   * is an improvement. We continue this process until we have exceeded trials.
   * 
   * @param originalSchedule - the schedule this method fills
   *        trials - The number of times to randomly permute our schedule
   *        permutations - The number of permutations to perform for each permute step
   * @post
   *    For each PairSchools pairSchools in originalSchedule.pairs we have 
   *    pairSchools.canAdd == false
   */
  private static void optimalSchedule(Schedule originalSchedule, int trials,
                                      int permutations)
  {
    // We create a copy of the original schedule, fill it as a starting point
    Schedule bestSchedule = originalSchedule.clone();
    fillScheduleBasic(bestSchedule);
    Schedule theSchedule;
    for (int trial = 0; trial < trials; trial++)
      {
        // We save a copy of the bestSchedule
        theSchedule = bestSchedule.clone();
        // We permute the copy
        permute(theSchedule, permutations);
        // We save the copy if it is the best copy
        if (rate(theSchedule) > rate(bestSchedule))
          {
            //System.out.println("Permutation Saved!");
            bestSchedule = theSchedule;
          }// if
      }// for
    // We copy the best schedule to the original schedule
    originalSchedule.theDays = bestSchedule.theDays;
    originalSchedule.pairs = bestSchedule.pairs;
  }// optimalSchedule(Schedule originalSchedule, int trials, int permutations)

  /**
   * This method randomly permutes the Schedule needsOptimizationSchedule. It takes
   * [permutations] randomly selected PairSchools objects from among the days which 
   * needsOptimizationSchedule has, and then places those objects to [permutations]
   * random different days.
   * 
   * @param originalSchedule - the schedule this method permutes
   *        permutations - The number of permutations to perform for each permute step
   */
  private static void permute(Schedule needsOptimizationSchedule,
                              int permutations)
  {
    Random permGen = new Random();
    ArrayList<Day> daysToModify = needsOptimizationSchedule.theDays;
    /*
     *  Save back - to - back days, we don't want to take SchedulePairs from these dates,
     *  or add SchedulePairs to these dates
     */
    ArrayList<ScheduleDate> noPermuteDates =
        findBackToBack(needsOptimizationSchedule.allDates);
    // We perform permutations permutations
    for (int perm = 0; perm < permutations; perm++)
      {
        /*
         * We choose a day to take a PairSchools match from at random
         * We choose a day to add a PairSchools match to at random
         */
        Day toTakeFrom = daysToModify.get(permGen.nextInt(daysToModify.size()));
        Day toAddTo = daysToModify.get(permGen.nextInt(daysToModify.size()));
        /*
         *  If none of the dates are back - to - back, and there is actually a PairSchools match in
         *  toTakeFrom, we move a random PairSchools from toTakeFrom, put it in toAddTo
         */
        if (toTakeFrom.matchesPlaying()
            && !noPermuteDates.contains(toTakeFrom.theDate)
            && !noPermuteDates.contains(toAddTo.theDate))
          {
            PairSchools toReallocate =
                toTakeFrom.removeGame(permGen.nextInt(toTakeFrom.competing.size()));
            toAddTo.addGame(toReallocate);
          }// if
      }// for

  }// permute(Schedule needsOptimizationSchedule, int permutations)

  /**
   * This method provides a rating for the schedule toRate. It loops through each Day,
   * assessing violations. A large violation is assessed if a school plays multiple times
   * on one day. A violation is assessed if a school plays on a cant play date. A small
   * violation is assessed if a school fails to play a must play date
   * @param toRate - The Schedule we want a rating for
   * @post
   *    The higher rate returns, the closer the schedule to meeting requirements
   */
  private static int rate(Schedule toRate)
  {
    // We set violations at zero
    int violations = 0;
    // Loop through every day
    for (Day gameTime : toRate.theDays)
      {
        // The schools which have played for the given day
        ArrayList<School> playedSoFar = new ArrayList<School>();
        // Loop through every match in the day
        for (PairSchools match : gameTime.competing)
          {
            // If the home school plays multiple times in a day, we assess violations
            if (playedSoFar.contains(match.home))
              {
                violations += 5;
              }// if
            // Otherwise, we recognize that the home school in the match has played
            else
              {
                playedSoFar.add(match.home);
              }// else
            // If the away school plays multiple times in a day, we assess violations
            if (playedSoFar.contains(match.away))
              {
                violations += 5;
              }// if
            // Otherwise, we recognize that the away school in the match has played
            else
              {
                playedSoFar.add(match.away);
              }// else
            // We access violations if the schools play on cant play dates
            if (!match.home.canPlay(gameTime.theDate))
              {
                violations += 3;
              }
            if (!match.away.canPlay(gameTime.theDate))
              {
                violations += 3;
              }// if
          }// for (PairSchools match : gameTime.competing)
      }// for (Day gameTime : toRate.theDays)
    /*
     * We now assess violations for the schools not playing on must play dates. We start
     * by looping through all the schools
     */
    for (School toTest : toRate.schools)
      {
        // We loop through each school's must play date
        for (ScheduleDate mustPlay : toTest.gameDates)
          {
            // Find corresponding date in the schedule
            for (Day gameTime : toRate.theDays)
              {
                // Make sure is same date, if school doesn't play assess violation
                if (gameTime.isDate(mustPlay) && !gameTime.schoolPlays(toTest))
                  {
                    violations += 1;
                  }// if
              }// for (Day gameTime : toRate.theDays)
          }// for (ScheduleDate mustPlay : toTest.gameDates)

      }// for (School toTest : toRate.schools)
    // Return negative, as we want a higher return value to indicate we have a good schedule
    return -violations;
  }// rate(Schedule toRate)

  /**
   * Distributes all the unused (canAdd==true) PairSchools  objects by putting all the unused 
   * PairSchools matches on the first non back-to-back date.
   * @param toFill - the Schedule to fill with PairSchools matches
   * @post
   *    For each PairSchools pairSchools in originalSchedule.pairs we have 
   *    pairSchools.canAdd == false
   */
  private static void fillScheduleBasic(Schedule toFill)
  {
    // We find back to back dates
    ArrayList<ScheduleDate> omitDates = findBackToBack(toFill.allDates);
    // Loop through each date
    for (ScheduleDate potentialFill : toFill.allDates)
      {
        /*
         *  The second we find a date which is not a back to back date, 
         *  we put all the PairSchools objects on that day in the schedule
         */
        if (!omitDates.contains(potentialFill))
          {
            for (PairSchools pairToInsert : toFill.pairs)
              {
                // We add all the PairSchool objects we can add
                if (pairToInsert.canAdd)
                  {
                    toFill.addGame(pairToInsert, potentialFill);
                  }// if
              }// for
            // Once we've added everything, we break
            break;
          }// if
      }// for(ScheduleDate potentialFill : toFill.allDates)
  }// fillScheduleBasic(Schedule toFill)

  
  /**
   * Fills the back to back weekend in the schedule originalSchedule
   * in such a way so that every school plays two home games in a row, and two
   * away games in a row. Minimizes the distance opposing teams have to 
   * travel
   * @param originalSchedule - the Schedule with the back to back dates we 
   *                           fill
   *        trials - the number of filled back to back combinations we want to 
   *                 try in to find the edistance minimizing combination
   *        allowedTravel - the recommended maximum distance for a team to 
   *                        travel on a back to back weekend
   */
  private static void optimalFillBackToBack(Schedule originalSchedule,
                                            int trials, int allowedTravel)
  {
    // We set up a best schedule as a starting point
    Schedule bestSchedule = originalSchedule.clone();
    Schedule theSchedule;
    // We set bestSchedule as a schedule with randomly filled back to backs
    fillBackToBackWeekends(bestSchedule);

    for (int trial = 0; trial < trials; trial++)
      {

        // We set theSchedule as a schedule with randomly filled back to backs
        theSchedule = originalSchedule.clone();
        fillBackToBackWeekends(theSchedule);
        // If theSchedule is an improvement, save it as bestSchedule
        if (backToBackWeekendRanking(theSchedule, allowedTravel) > backToBackWeekendRanking(bestSchedule,
                                                                                            allowedTravel))
          {
            bestSchedule = theSchedule;
          }// if
      }// for
    // Save bestSchedule to original Schedule
    originalSchedule.theDays = bestSchedule.theDays;
    originalSchedule.pairs = bestSchedule.pairs;
  }// optimalFillBackToBack(Schedule originalSchedule, int trials, int allowedTravel)

  /**
   * Provides a ranking for the back to back dates in theSchedule, with 
   * better rankings being given if theSchedule has most schools playing 
   * away games which involve less then maxRecommended miles of travel for
   * the back to back dates
   * 
   * @param theSchedule - the Schedule to rank
   *        maxRecommended -the maximum distance a school should have to 
   *                        travel for an away match
   */
  private static int backToBackWeekendRanking(Schedule theSchedule,
                                              int maxRecommended)
  {
    // We find the back to back dates
    ArrayList<ScheduleDate> backToBackDates =
        findBackToBack(theSchedule.allDates);
    // We want this to have a small value
    int excessiveDistances = 0;
    // Loop through the back to back dates
    for (ScheduleDate theDay : backToBackDates)
      {
        // Loop through theSchedules' days, until we find the back to back date
        for (Day scheduleDay : theSchedule.theDays)
          {
            if (scheduleDay.isDate(theDay))
              {
                // Measure distance for each competing pair of schools
                for (PairSchools match : scheduleDay.competing)
                  {
                    /*
                     * We add the square of all distance travelled in excess of 
                     * 180 miles to our excessiveDistances
                     */
                    excessiveDistances +=
                        Math.max(match.distance - maxRecommended, 0)
                            * Math.max(match.distance - maxRecommended, 0);
                  }// for
                break;
              }// if
          }// for (Day scheduleDay : theSchedule.theDays)
      }// for (ScheduleDate theDay : backToBackDates)
    // We return negative b/c we want to have higher return value be good
    return -excessiveDistances;
  }

  /**
   * Given toFill, it fills the back to back weekends in toFill by filling the Day
   * obects within toFill which represent the back to back weekends with matches. 
   * Each college is guaranteed to play two home games on one weekend, and two away
   * games on the other
   * 
   * @param theSchedule - the Schedule to fill the back to back weekends of
   */
  private static void fillBackToBackWeekends(Schedule toFill)
  {
    // We save the back to back weekends
    ArrayList<ScheduleDate> backToBackDates = findBackToBack(toFill.allDates);
    /*
     *  The schools which can't play home - indicates if the school is already
     *  playing home during the previous back to back weekend, or has played 
     *  home during the current weekend. cantAway serves parallel purpose
     */
    ArrayList<School> cantHome = new ArrayList<School>();
    ArrayList<School> cantAway = new ArrayList<School>();
    ArrayList<PairSchools> theMatches = toFill.pairs;
    int numSchools = toFill.numSchools();
    Random dice = new Random();
    // We loop through both back to back weekends, filling each
    for (int weekends = 0; weekends < 2; weekends++)
      {
        /*
         *  "sat" is the first back to back day, "sun" is the second day.
         *  No guarantee sat and sun are actually on Saturday and
         *  Sunday (they do not need to be, they could be friday and saturday,
         *  for instance)
         */
        ScheduleDate sat = backToBackDates.get(2 * weekends);
        ScheduleDate sun = backToBackDates.get(2 * weekends + 1);
        boolean added = false;
        int firstPairNum = 0;
        /*
         *  We add the first PairSchools match to saturday. We get random matches
         *  until we find one we can actually add to saturday
         */
        while (!added)
          {
            // Get a random PairSchools match
            firstPairNum = dice.nextInt(theMatches.size());
            PairSchools addPairSchool = theMatches.get(firstPairNum);
            // If we can add the match, we add it
            if (!cantHome.contains(addPairSchool.home)
                && !cantAway.contains(addPairSchool.away))
              {
                toFill.addGame(addPairSchool, sat);
                added = true;
              }// if
          }// while
        // Register that we have added the first day
        School firstSchool = theMatches.get(firstPairNum).home;
        cantHome.add(firstSchool);
        School prevSchool = theMatches.get(firstPairNum).away;
        cantAway.add(prevSchool);
        boolean found;
        /*
         *  We add all the PairSchools to complete the weekend except for the
         *  last PairSchools object. We add a PairSchools object with the same away school
         *  as the previously added PairSchools object to sunday, and then add a 
         *  PairSchools object with the same home school as the previously added
         *  PairSchools object to saturday. We repeat this process until we reach
         *  the last PairSchools object we must add
         */
        for (int count = 0; count < numSchools - 2; count++)
          {
            found = false;
            // We add a ScheduleDate to Sunday
            if (count % 2 == 0)
              {
                /*
                 * We randomly find PairSchools objects until we find one with the same
                 * away school as the previously added PairSchools. We also need to be 
                 * able to add the PairSchools object to sun. We don't add pairSchools where the
                 * home school has already played home. Once we have found a valid SchoolPairs object,
                 * we add it to sun
                 */
                while (!found)
                  {
                    int pairNum = dice.nextInt(theMatches.size());
                    PairSchools toInsert = theMatches.get(pairNum);
                    if (toInsert.away.equals(prevSchool)
                        && toFill.canAddToDate(toInsert, sun)
                        && !cantHome.contains(toInsert.home))
                      {
                        found = true;
                        toFill.addGame(toInsert, sun);
                        prevSchool = toInsert.home;
                        cantHome.add(prevSchool);
                      }// if
                  }// while
              }// if
            /*
             * We randomly find PairSchools objects until we find one with the same
             * home school as the previously added PairSchools. We also need to be 
             * able to add the PairSchools object to sat. We don't add pairSchools where the
             * away school has already played away. Once we have found a valid SchoolPairs object,
             * we add it to sat
             */
            else
              {
                while (!found)
                  {
                    int pairNum = dice.nextInt(theMatches.size());
                    PairSchools toInsert = theMatches.get(pairNum);
                    if (toInsert.home.equals(prevSchool.name)
                        && toFill.canAddToDate(toInsert, sat)
                        && !cantAway.contains(toInsert.away))
                      {
                        found = true;
                        toFill.addGame(toInsert, sat);
                        prevSchool = toInsert.away;
                        cantAway.add(prevSchool);
                      }// if
                  }// while
              }// else

          }
        found = false;
        /*
         *  We add the last PairSchools to complete the back to back weekend
         *  We must add an existing PairSchools instead of creating a new one
         *  so that we ensure we have the distance correct
         */
        while (!found)
          {
            int pairNum = dice.nextInt(theMatches.size());
            PairSchools toInsert = theMatches.get(pairNum);
            if (toInsert.away.equals(prevSchool.name)
                && toInsert.home.equals(firstSchool.name))
              {
                found = true;
                toFill.addGame(toInsert, sun);
              }// if
          }// while
      }// for (int weekends = 0; weekends < 2; weekends++)
  }// fillBackToBackWeekends(Schedule toFill)

  /**
   * Find the back to back dates. By back-to-back, dates which differ by exactly 
   * one day from other dates in the ArrayList
   * @param dates - the dates we will search for back-to-back dates
   * @pre
   *    the ScheduleDates in dates must be stored by ascending order
   * @return
   *    Will return all the back-to-back dates as an ArrayList<ScheduleDate>
   */
  public static ArrayList<ScheduleDate>
    findBackToBack(ArrayList<ScheduleDate> dates)
  {
    // We initialize backToBacks as empty ArrayList
    ArrayList<ScheduleDate> backToBacks = new ArrayList<ScheduleDate>();
    // We loop through each date-pair
    for (int i = 0; i < (dates.size() - 1); i++)
      {
        // We get the current and next date
        ScheduleDate trackerDate1 = dates.get(i);
        int date1 = trackerDate1.get364();
        ScheduleDate trackerDate2 = dates.get(i + 1);
        int date2 = trackerDate2.get364();
        // We find the difference between the dates in days
        int dateDifference = date2 - date1;
        /*
         *  If the date difference is 1, we have two regular back to back games
         *  If the date difference is 0, it is a leap year and Feb 29 vs March 1
         *  If the date difference is -364, we have a Dec 31 vs Jan 1 new years game
         */
        if (dateDifference == 1 || dateDifference == -364
            || dateDifference == 0)
          {
            backToBacks.add(trackerDate1);
            backToBacks.add(trackerDate2);
          }// if
      }// for (int i = 0; i < (dates.size() - 1); i++)
    return backToBacks;
  }// findBackToBack(ArrayList<ScheduleDate> dates)

}
