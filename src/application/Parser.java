package application;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;




public class Parser
{
  public static ArrayList<PairSchools> parse(File info) throws Exception
  {
    BufferedReader br = new BufferedReader(new FileReader(info));
    String line;
    ArrayList<ScheduleDate> generalDates = new ArrayList<ScheduleDate>();
    ArrayList<School> theSchools = new ArrayList<School>();
    ArrayList<PairSchools> theSchoolPairs = new ArrayList<PairSchools>();
    int lineNum=0;
    while ((line = br.readLine()) != null)
      {
        if (lineNum==0)
          {
            generalDates = parseDates(line);
          }
        else
          {
            theSchools.add(ProcessLine(line, generalDates));
          }
        lineNum++;
      }
    br.close();
    lineNum=0;
    BufferedReader cr = new BufferedReader(new FileReader(info));
    while ((line = cr.readLine()) != null)
      {
        if (lineNum==0)
          {
          }
        else
          {
            String[] Segments = line.split(";");
            String SchoolName = Segments[0];
            String[] Distances = Segments[1].substring(1,Segments[1].length()-1).split(",");
            for (String Distance : Distances)
              {
                School home=null;
                School away=null;
                String[] distNamePair = Distance.split(":");
                String secondName = distNamePair[0];
                int distance = Integer.parseInt(distNamePair[1]);
                System.out.println("Distance of "+distance);
                  for (School theSchool : theSchools)
                    {
                      if (theSchool.equals(SchoolName))
                        {
                          home=theSchool;
                        }
                      if (theSchool.equals(secondName))
                        {
                          away=theSchool;
                        }
                    }
                  theSchoolPairs.add(new PairSchools(home,away,distance));
              }
          }
        lineNum++;
      }
    cr.close();
    return theSchoolPairs;
  }
  
  
  private static School ProcessLine(String line, ArrayList<ScheduleDate> generalDates)
  throws Exception
  {
    String[] Segments = line.split(";");
    ArrayList<ScheduleDate> noPlayDates = parseDates(Segments[2]);
    ArrayList<ScheduleDate> mustPlayDates = parseDates(Segments[3]);
    @SuppressWarnings("unchecked")
    ArrayList<ScheduleDate> tempDates = (ArrayList<ScheduleDate>) generalDates.clone();
    tempDates.removeAll(noPlayDates);
    tempDates.removeAll(mustPlayDates);
    return new School(Segments[0],mustPlayDates,tempDates);
    
  }
  
  
  
  public static ArrayList<ScheduleDate> parseDates(String dateString) throws Exception
  {
    if (dateString.length()<=2)
      {
        return new ArrayList<ScheduleDate>();
      }
    String[] dateStrings = dateString.substring(1,dateString.length()-1).split(",");
    ArrayList<ScheduleDate> theDates= new ArrayList<ScheduleDate>();
    for (int Count=0;Count<dateStrings.length;Count++)
      {
        theDates.add(parseDate(dateStrings[Count]));
      }
    return theDates;
  }
  
  public static ScheduleDate parseDate(String date) throws Exception
  {
    ScheduleDate theDate=new ScheduleDate();
    int positionSlash=date.indexOf("/");
    theDate.setMonth(Integer.parseInt(date.substring(0, positionSlash)));
    theDate.setDay(Integer.parseInt(date.substring(positionSlash+1)));
    return theDate;
  }
}
