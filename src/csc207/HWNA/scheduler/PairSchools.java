package csc207.HWNA.scheduler;

//import PairSchools;
//import School;

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
  final School home;

  /**
   * The school which is playing away during this match
   */
  final School away;

  /**
   * The driving distance between the two schools
   */
  final int distance;
  
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

  
  
  public boolean isPlaying(School theSchool)
  {
    return theSchool.equals(home) || theSchool.equals(away);
  }
  
  public String toString()
  {
    return "["+home.toString()+" vs "+away.toString()+"]";
  }
 
  public PairSchools clone()
  {
    PairSchools theCopy = new PairSchools(home, away, distance);
    theCopy.canAdd=canAdd;
    return theCopy;
  }

}// class PairSchools