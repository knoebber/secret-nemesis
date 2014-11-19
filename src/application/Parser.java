package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;

public class Parser
{
  static Date[] gameDates;
  static Date[] optionalGameDates;

  static ArrayList<PairSchools> parse(File info)
  {
    try
      {
        //System.out.println("here");
        BufferedReader br = new BufferedReader(new FileReader(info));
        String line;
        String name;
        String homeName;
        String awayNames;
        School home;
        School away;
        int firstBracket = 0;
        while ((line = br.readLine()) != null)
          {
            for (int i = 0; i < line.length(); i++)
              {
                char c = line.charAt(i);
                if (c == ',')
                  {
                    homeName = line.substring(0, i);
                    //home=new School(homeName,gameDates,optionalGameDates);
                    firstBracket = i + i;

                  }//if
                if (c == ']')
                  {
                    awayNames = line.substring(firstBracket, i);
                    for(int j=1;j<awayNames.length();j++)
                      {
                        char d = line.charAt(i);
                      }
                  }
                

              }//for
          }//while
        br.close();
      }//try
    catch (Exception e)
      {
        System.out.println("Failed to open info");
      }//catch

    return null;

  }
}//class Parser
