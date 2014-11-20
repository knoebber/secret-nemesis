package csc207.HWNA.scheduler;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;



/**
 * @author Harry Baker
 * @author William Royle
 * @author Nicolas Knoebber
 * @author Amanda Hinchman
 * 
 * @date November 18, 2014
 *
 * Handles the writing of a Schedule object to a new output file
 */
public class ScheduleWriter
{

  /**
   * Writes a Schedule object to a file, specified by fileName
   * @param schedule - the Schedule object
   *        fileName - the file to be written too
   * @pre
   *    fileName must not exist
   * @throws UnsupportedEncodingException
   * @throws FileNotFoundException
   */
  static void write(Schedule schedule, String fileName)
    throws Exception
  {
    ScheduleDate lookingFor=schedule.gameList.get(0).dayOfCalendar;
    ScheduleDate checking;
    ScheduleDate current;

    String currentLine = new String();
    ArrayList<String> output = new ArrayList<String>();
    // We loop through every single game within the schedule
    for (int i = 0; i < schedule.gameList.size(); i++)
      {
        current = schedule.gameList.get(i).dayOfCalendar;
        // If we just checked a date, dont check it again
        if (current.compareTo(lookingFor) != 0 || i==0) 
          {
            lookingFor = schedule.gameList.get(i).dayOfCalendar;
            currentLine = currentLine + lookingFor.toString() + ": ";
            for (int j = 0; j < schedule.gameList.size(); j++)
              {
                if (j != i)
                  {
                    checking = schedule.gameList.get(j).dayOfCalendar;
                    // We print the information for the specified game
                    if (lookingFor.compareTo(checking) == 0) //if we find the date
                      {
                        // Build the string that will be the result
                        currentLine = currentLine + "[";
                        currentLine =
                            currentLine
                                + schedule.gameList.get(j).competing.home.name
                                + " vs ";
                        currentLine =
                            currentLine
                                + schedule.gameList.get(j).competing.away.name;
                        currentLine = currentLine + "];";
                      }// if
                  }// if
              }// for
          }
        if (currentLine.length() > 0)
          {
            //if we have a line, add it to the output
            output.add(currentLine);
            currentLine = ""; //reset the line
          }//if
      }//for
    //http://stackoverflow.com/questions/2885173/java-how-to-create-and-write-to-a-file

    PrintWriter writer = new PrintWriter(fileName, "UTF-8"); 
    //write to the file 
    for (int i = 0; i < output.size(); i++)
      writer.println(output.get(i));

    writer.close();

  }
}