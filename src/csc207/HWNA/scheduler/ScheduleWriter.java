package csc207.HWNA.scheduler;

import java.io.PrintWriter;

/**
 * @author Harry Baker
 * @author William Royle
 * @author Nicolas Knoebber
 * @author Amanda Hinchman
 * 
 * @date November 18, 2014
 *
 * Contains a utility which handles the writing of a Schedule 
 * object to a new output file
 */
public class ScheduleWriter
{

  /**
   * Writes a Schedule object to a file, specified by fileName
   * @param schedule - the Schedule object
   *        fileName - the file to be written too
   * @throws Exception - If file cannot be created
   */
  static void write(Schedule schedule, String fileName)
    throws Exception
  {
    PrintWriter writer = new PrintWriter(fileName, "UTF-8");
    writer.print(schedule.toString());
    writer.close();
  }// write(Schedule schedule, String fileName)
}// class ScheduleWriter