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
    PrintWriter writer = new PrintWriter(fileName, "UTF-8"); 
    ArrayList<ScheduleDate> theDates = schedule.allDates;
    ArrayList<Game> theGames = schedule.gameList;
    for (int date = 0; date < theDates.size(); date++)
      {
        ScheduleDate theDate=theDates.get(date);
        boolean found=false;
        writer.print(theDate.day+"/"+theDate.month+":");
        for (int game = 0; game < theGames.size(); game++)
          {
            Game theGame=theGames.get(game);
            if (theGame.dayOfCalendar.equals(theDate))
              {
                found=true;
                writer.print("["+theGame.competing.home.name +" vs "+ theGame.competing.away.name+"]");
              }
          }
        if (found)
          {
            writer.print(";");
          }
        writer.println();
      }

    writer.close();
  }
}