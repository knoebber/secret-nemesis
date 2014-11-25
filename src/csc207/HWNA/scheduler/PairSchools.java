package csc207.HWNA.scheduler;

/**
 * @author Harry Baker
 * @author William Royle
 * @author Nicolas Knoebber
 * @author Amanda Hinchman
 * 
 * @date November 18, 2014
 *
 * Holds information regarding an unscheduled match between two schools.
 */
class PairSchools
{

  // +--------+------------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The school which is playing home during this match
   */
  School home;

  /**
   * The school which is playing away during this match
   */
  School away;

  /**
   * The driving distance between the two schools
   */
  int distance;
  
  boolean canAdd=true;

  // +--------------+------------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Creates a new pair of schools.
   */
  PairSchools(School home, School away, int distance)
  {
    this.home = home;
    this.away = away;
    this.distance = distance;
  }// PairSchools(School Home, School Away, double distance)

  // +---------+-----------------------------------------------------------
  // | Methods |
  // +---------+
  
  
  
  /**
   * Tells if two pairs of schools are identical.
   */
  public boolean equals(Object other)
  {
    if (other instanceof PairSchools)
      {
        PairSchools otherPair = (PairSchools) other;
        // They are the same if home, away schools are the same
        return home.equals(otherPair.home) && away.equals(otherPair.away);
      }// if
    return false;
  }// equals(PairSchools other)

  /**
   * Prints a brief summary of the PairSchools object to stdout
   */
  public void print()
  {
    System.out.println("Home School is " + home.name);
    System.out.println("Away School is " + away.name);
    System.out.println("Distance is " + distance);
    System.out.println();
  }// print()

}// class PairSchools