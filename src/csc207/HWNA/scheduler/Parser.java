package csc207.HWNA.scheduler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import com.sun.media.sound.InvalidFormatException;

/**
 * @author Harry Baker
 * @author William Royle
 * @author Nicolas Knoebber
 * @author Amanda Hinchman
 *
 * @date November 18, 2014
 *
 * Methods used to parse the information read from the text file entered by the user
 * and save it into the data structures needed to generate a schedule
 */
public class Parser
{

  // +---------+-----------------------------------------------------------
  // | Methods |
  // +---------+

  
  
  public static int parseSchoolCount(File info) throws Exception
  {
    int schools = -1;
    BufferedReader br = new BufferedReader(new FileReader(info));
    String line;
    while ((line = br.readLine()) != null && !line.trim().isEmpty())
      {
        schools++;
      }

    br.close();
    return schools;
  }
  
  
  public static ArrayList<School> parseSchools (File info) throws Exception
  {

    BufferedReader br = new BufferedReader(new FileReader(info));
    String line;
    ArrayList<School> theSchools = new ArrayList<School>();
    ArrayList<ScheduleDate> generalDates = new ArrayList<ScheduleDate>();
    int lineNum=0;
    while ((line = br.readLine()) != null && !line.trim().isEmpty())
      {
        if (lineNum == 0)
          {
            generalDates = parseDates(line);
          }// if
        // Each subsequent line has information for a school
        else
          {
            theSchools.add(processLine(line, generalDates));
          }// else
        lineNum++;
      }

    br.close();
    return theSchools;
  }
  
  
  
  
  /**
   * Saves the information found within the specified File as an ArrayList<PairSchools>
   * @pre
   *    info must be formatted as follows
   *    Line 1:
   *    [11/12,13/34]
   *    dates in which most schools play
   *    Line 2 - n:
   *    Grinnell;[Cornell:10,IU:5]];[11/12,13/34];[5/2]
   *    ------Name ; Distances ; noPlayDates ; mustPlayDates
   *    
   * @param info - the file to read data from
   * @throws Exception
   *    Method will throw an exception if the specified file does not exist
   *    -fileNotFoundException
   *    Method will throw an expeption if the specified file is incorrectly formatted
   *    -InvalidFormatException
   */
  public static ArrayList<PairSchools> parse(File info)
    throws Exception
  {

    BufferedReader br = new BufferedReader(new FileReader(info));
    String line;
    ArrayList<ScheduleDate> generalDates = new ArrayList<ScheduleDate>();
    ArrayList<School> theSchools = new ArrayList<School>();
    ArrayList<PairSchools> theSchoolPairs = new ArrayList<PairSchools>();
    /*
     * We read the file line by line, generating school objects as we go
     */
    int lineNum = 0;
    while ((line = br.readLine()) != null && !line.trim().isEmpty())
      {
        // We save the first line as the potential game dates
        if (lineNum == 0)
          {
            generalDates = parseDates(line);
          }// if
        // Each subsequent line has information for a school
        else
          {
            theSchools.add(processLine(line, generalDates));
          }// else
        lineNum++;
      }// while
    br.close();
    /*
     * We loop through again, saving information for each SchoolPair
     * we go. 
     */
    lineNum = 0;
    BufferedReader cr = new BufferedReader(new FileReader(info));
    while ((line = cr.readLine()) != null)
      {
        /*
         *  For all lines except the first, we have one school per line, so
         *  we save it to the array of schools
         */
        if (lineNum != 0)
          {
            theSchoolPairs.addAll(getSchoolPairs(line, theSchools));
          }// if
        lineNum++;
      }// while
    cr.close();
    return theSchoolPairs;
  }// parse(File info)

  /**
   * Saves the information found within the specified string as an ArrayList<PairSchools>
   * @pre
   *    line must be formatted as follows
   *    Grinnell;[Cornell:10,IU:5];[11/12,13/34];[5/2]
   *    Name ; Distances ; noPlayDates ; mustPlayDates
   * @param line - the string to generate SchoolPairs from
   *        theSchools - the ArrayList of Schools which exist
   * @throws Exception
   *    Method will throw an expeption if the specified file is incorrectly formatted
   *    -InvalidFormatException
   */
  private static ArrayList<PairSchools>
    getSchoolPairs(String line, ArrayList<School> theSchools)
      throws Exception
  {
    // The list that will hold the returned schools
    ArrayList<PairSchools> theSchoolPairs = new ArrayList<PairSchools>();
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
            throw new InvalidFormatException("Incorrectly formatted data");
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
        theSchoolPairs.add(new PairSchools(home, away, distance));
      }// for
    return theSchoolPairs;
  }// getSchoolPairs(String line, ArrayList<School> theSchools)

  /**
   * Parses the line passed to return a School, using generalDates as the default dates
   * @pre
   *    line must be formatted as follows
   *    Grinnell;[Cornell:10,IU:5]];[11/12,13/34];[5/2]
   *    Name ; Distances ; noPlayDates ; mustPlayDates
   * @param line - the string to generate the school from
   *        generalDates - Default dates for the school to play
   * @throws Exception
   *    Method will throw an expeption if the specified file is incorrectly formatted
   *    -InvalidFormatException
   */
  private static School processLine(String line,
                                    ArrayList<ScheduleDate> generalDates)
    throws Exception
  {
    // We split on ;
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
    ArrayList<ScheduleDate> properOrderMustPlayDates = (ArrayList<ScheduleDate>) generalDates.clone();
    properOrderMustPlayDates.retainAll(mustPlayDates);
    // Make new school
    return new School(segments[0], properOrderMustPlayDates, tempDates);
  }// processLine(String line,ArrayList<ScheduleDate> generalDates)

  /**
   * Parses the dateString passed to return an ArrayList of ScheduleDate objects
   * @pre
   *    dateString must be formatted as follows
   *    [11/12,13/34]
   * @param dateString - the string to generate the ScheduleDate objects from
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
        theDates.add(parseDate(dateStrings[count],count));
      }// for
    return theDates;
  }// parseDates(String dateString)

  /**
   * Parses the String date passed to a ScheduleDate object
   * @pre
   *    date must be formatted as follows
   *    11/12
   * @param date - the string to generate the ScheduleDate object from
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
        throw new InvalidFormatException("Incorrectly formatted data");
      }// catch
    return theDate;
  }// parseDate(String date)

  /**
   * Saves the information found within the specified File as an ArrayList<ScheduleDate>
   * @pre
   *    info must be formatted as follows
   *    Line 1:
   *    [11/12,13/34]
   *    dates in which most schools play
   *    Line 2 - n:
   *    Grinnell;[Cornell:10,IU:5]];[11/12,13/34];[5/2]
   *    ------Name ; Distances ; noPlayDates ; mustPlayDates
   *    
   * @param info - the file to read data from
   * @throws Exception
   *    Method will throw an exception if the specified file does not exist
   *    -fileNotFoundException
   *    Method will throw an expeption if the specified file is incorrectly formatted
   *    -InvalidFormatException
   */
  public static ArrayList<ScheduleDate> parseDates(File info)
    throws Exception
  {
    BufferedReader br = new BufferedReader(new FileReader(info));
    String line;
    ArrayList<ScheduleDate> generalDates = new ArrayList<ScheduleDate>();
    /*
     * We read the first line of the file, generating ArrayList of ScheduleDate objects
     */
    if ((line = br.readLine()) != null)
      {
        generalDates = parseDates(line);
      }// if
    br.close();
    return generalDates;
  }// parseDates(File info)

}// class Parser 
