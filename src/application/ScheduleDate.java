package application;

public class ScheduleDate
{
  
  int month;
  int day;
  
  ScheduleDate()
  {
    this.month=-1;
    this.day=-1;
  }
  
  
  ScheduleDate(int month, int day)
  {
    this.month=month;
    this.day=day;
  }
  
  public boolean equals(Object other)
  {
    if (other instanceof ScheduleDate)
      {
        ScheduleDate alternate = (ScheduleDate) other;
        return alternate.month==month&&alternate.day==day;
      }
    return false;
  }
  
  void setMonth(int month)
  {
    this.month=month;
  }
  
  void setDay(int day)
  {
    this.day=day;
  }
  
  void printDate()
  {
    System.out.println(month+"/"+day);
  }
  
}
