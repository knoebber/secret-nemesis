package csc207.HWNA.scheduler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * @author Harry Baker
 * @author William Royle
 * @author Nicolas Knoebber
 * @author Amanda Hinchman
 * 
 * @date November 22, 2014
 * (Refactored December 1, 2014)
 *
 * Utilies which allow a blank schedule to be loaded from a file, as long as
 * the file is formatted as specified by the method preconditions
 */
public class Parser
{

  // +---------+-----------------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Saves the information found within the specified File as a Schedule object, with no
   * PairSchools objects allocated to specific Day Objects. We parse for a round robin 
   * schedule, and thus we have two PairSchools objects in our generated schedule for two
   * schools, one for the home match and one for the away match.
   * 
   * @param info - the file to read data from
   * @pre
   *    info must be formatted as follows
   *    Line 1:
   *    [11/12,13/1]
   *    (day/month) all potential game dates. It is assumed that no games can occur on dates
   *    not in this list. It is also assumed that the user enters the dates in chronological
   *    order
   *    Lines 2 - n:
   *    Grinnell;[Cornell:10,IU:5]];[11/12,13/34];[5/2]
   *       Name ;     Distances    ; noPlayDates ; mustPlayDates
   *    
   * @throws Exception
   *    Method will throw an exception if the specified file does not exist
   *    -fileNotFoundException
   *    Method will throw an exception if the specified file does not exist / is incorrectly formatted
   *    -Exception
   */
  public static Schedule parse(File info)
    throws Exception
  {
    // We open the file
    BufferedReader br = new BufferedReader(new FileReader(info));
    // Saves each line read from file
    String line;
    // Components of the info saved from the gile
    ArrayList<ScheduleDate> generalDates = new ArrayList<ScheduleDate>();
    ArrayList<School> theSchools = new ArrayList<School>();
    ArrayList<PairSchools> thePairSchools = new ArrayList<PairSchools>();
    // We read the file line by line, generating school objects as we go
    int lineNum = 0;
    while ((line = br.readLine()) != null && !line.trim().isEmpty())
      {
        // We save the first line as the potential game dates
        if (lineNum == 0)
          {
            generalDates = parseDates(line);
          }// if
        // Each subsequent line has information for a school, which we save
        else
          {
            theSchools.add(processLine(line, generalDates));
          }// else
        lineNum++;
      }// while ((line = br.readLine()) != null && !line.trim().isEmpty())
    br.close();
    /*
     * We loop through again, saving information for each PairSchools as
     * we go. Two reads are needed, as we cannot generate the PairSchools objects
     * until we have the School objects set up
     */
    lineNum = 0;
    BufferedReader cr = new BufferedReader(new FileReader(info));
    while ((line = cr.readLine()) != null && !line.trim().isEmpty())
      {
        /*
         *  For all lines except the first, we have one school per line, so
         *  we save each pair involving that school playing home to the PairSchools
         */
        if (lineNum != 0)
          {
            thePairSchools.addAll(getPairSchools(line, theSchools));
          }// if
        lineNum++;
      }// while
    cr.close();
    return new Schedule(thePairSchools, generalDates, theSchools);
  }// parse(File info)

  /**
   * Saves the information found within the specified string as an ArrayList<PairSchools>
   * @param line - the string to generate PairSchools from
   *        theSchools - the ArrayList of Schools which exist
   * @pre
   *    line must be formatted as follows
   *    Grinnell;[Cornell:10,IU:5];[11/12,13/1];[5/2]
   *       Name ;     Distances   ; noPlayDates ; mustPlayDates
   * @throws Exception
   *    Method will throw an exception if the specified file is incorrectly formatted
   *    -Exception
   */
  private static ArrayList<PairSchools>
    getPairSchools(String line, ArrayList<School> theSchools)
      throws Exception
  {
    // The list that will hold the returned schools
    ArrayList<PairSchools> thePairSchools = new ArrayList<PairSchools>();
    // segment[0] holds the name, segment[1] holds the school - distance pairs
    String[] segments = line.split(";");
    String schoolName = segments[0];
    // We parse the school - distance pairs
    String[] Distances =
        segments[1].substring(1, segments[1].length() - 1).split(",");
    // For each school - distance pair, we save the info to a PairSchools object
    for (String Distance : Distances)
      {
        School home = null;
        School away = null;
        // We actually split the school - distance pair in question
        String[] distNamePair = Distance.split(":");
        String secondName = distNamePair[0];
        // We find the distance, throwing appropriate error
        int distance = 0;
        try
          {
            distance = Integer.parseInt(distNamePair[1]);
          }// try
        catch (Exception E)
          {
            throw new Exception("Incorrectly formatted data");
          }// catch
        // For each PairSchools, we set the home and away school
        for (School theSchool : theSchools)
          {
            if (theSchool.equals(schoolName))
              {
                home = theSchool;
              }// if
            if (theSchool.equals(secondName))
              {
                away = theSchool;
              }// if
          }// for
        // We add the found PairSchools
        thePairSchools.add(new PairSchools(home, away, distance));
      }// for (String Distance : Distances)
    return thePairSchools;
  }// getPairSchools(String line, ArrayList<School> theSchools)

  /**
   * Parses the line passed to return a School, using generalDates as the default dates
   * @param line - the string to generate the school from
   *        generalDates - Default dates for the school to play
   * @pre
   *    line must be formatted as follows
   *    Grinnell;[Cornell:10,IU:5]];[11/12,13/1];[5/2]
   *       Name ;     Distances    ; noPlayDates ; mustPlayDates
   * @throws Exception
   *    Method will throw an expeption if the specified file is incorrectly formatted
   *    -InvalidFormatException
   */
  private static School processLine(String line,
                                    ArrayList<ScheduleDate> generalDates)
    throws Exception
  {
    /*
     *  We split on ; which gives us school name (segments[0]), distances (segments[1])
     *  noPlaydates(segments[2]), mustPlaydates(segments[3])
     */
    String[] segments = line.split(";");
    ArrayList<ScheduleDate> noPlayDates = parseDates(segments[2]);
    ArrayList<ScheduleDate> mustPlayDates = parseDates(segments[3]);
    @SuppressWarnings("unchecked")
    // We have to copy our generalDates so we can remove elements without disturbing original copy
    ArrayList<ScheduleDate> tempDates =
        (ArrayList<ScheduleDate>) generalDates.clone();
    // We remove the dates which are not optional play dates
    tempDates.removeAll(noPlayDates);
    tempDates.removeAll(mustPlayDates);
    @SuppressWarnings("unchecked")
    ArrayList<ScheduleDate> properOrderMustPlayDates =
        (ArrayList<ScheduleDate>) generalDates.clone();
    properOrderMustPlayDates.retainAll(mustPlayDates);
    // Make new school
    return new School(segments[0], properOrderMustPlayDates, tempDates);
  }// processLine(String line,ArrayList<ScheduleDate> generalDates)

  /**
   * Parses the dateString passed to return an ArrayList of ScheduleDate objects
   * @param dateString - the string to generate the ScheduleDate objects from
   * @pre
   *    dateString must be formatted as follows
   *    [11/12,13/1]
   *    (day/month)
   * @throws Exception
   *    Method will throw an expeption if the specified dateString is incorrectly formatted
   *    -InvalidFormatException
   */
  private static ArrayList<ScheduleDate> parseDates(String dateString)
    throws Exception
  {
    // If the string is not long enough to hold ScheduleDates, we return empty ArrayList<ScheduleDate>
    if (dateString.length() <= 2)
      {
        return new ArrayList<ScheduleDate>();
      }// if
    // We split the strings 
    String[] dateStrings =
        dateString.substring(1, dateString.length() - 1).split(",");
    ArrayList<ScheduleDate> theDates = new ArrayList<ScheduleDate>();
    // We loop through each string, parsing and saving to dates
    for (int count = 0; count < dateStrings.length; count++)
      {
        theDates.add(parseDate(dateStrings[count], count));
      }// for
    return theDates;
  }// parseDates(String dateString)

  /**
   * Parses the String date passed to a ScheduleDate object
   * @param date - the string to generate the ScheduleDate object from
   * @pre
   *    date must be formatted as follows
   *    11/12 - day/month
   * @throws Exception
   *    Method will throw an expeption if the specified file is incorrectly formatted
   *    -InvalidFormatException
   */
  private static ScheduleDate parseDate(String date, int count)
    throws Exception
  {
    ScheduleDate theDate = new ScheduleDate();
    int positionSlash = date.indexOf("/");
    // We make new ScheduleDate object, throwing appropriate exceptions
    try
      {
        theDate.setDay(Integer.parseInt(date.substring(0, positionSlash)));
        theDate.setMonth(Integer.parseInt(date.substring(positionSlash + 1)));
        theDate.setOrder(count);
      }// try
    catch (Exception E)
      {
        throw new Exception("Incorrectly formatted data");
      }// catch
    return theDate;
  }// parseDate(String date)
}// class Parser
