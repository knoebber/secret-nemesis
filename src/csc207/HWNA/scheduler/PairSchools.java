package csc207.HWNA.scheduler;


/**
 * @author Harry Baker
 * @author William Royle
 * @author Nicolas Knoebber
 * @author Amanda Hinchman
 * 
 * @date November 18, 2014
 *
 * Holds information regarding a match between two schools, which may or may not
 * be scheduled. Thus, the PairSchools object has the home school, the away school,
 * and the distance between the schools. The only property of a PairSchools object
 * which can be edited is the canAdd field. This field is true if the match has
 * already been scheduled, and false otherwise
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
  
  /**
   * canAdd. Indicates whether the match between the two schools has been scheduled
   * for a day or not. If the match has been scheduled, we have canAdd being false.
   * We preset canAdd to true, as new PairSchool objects have not yet been Scheduled
   */
  boolean canAdd=true;

  // +--------------+------------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Sets up a new PairSchools object for a match between the home school and the 
   * away school, which differ in distance by the specified amount
   * @param home - the School which is playing home in the match
   *        away - the School which is playing away in the match
   *        distance - the distance in miles between schools
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
   * Tells if two pairs of schools are identical. Two PairSchools objects
   * are taken to be equal if the home and away schools are the same
   * @param other - the object to check for equality to this
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
   * Tells if the specified theSchool is playing in the match this PairSchools
   * object represents
   * @pre
   *    theSchool must be non null
   * @param theSchool - the school we want to see if is playing in the PairSchools match
   */
  public boolean isPlaying(School theSchool)
  {
    return theSchool.equals(home) || theSchool.equals(away);
  }// isPlaying(School theSchool)
  
  
  /**
   * Returns a string representation of the match this PairSchools object
   * represents
   */
  public String toString()
  {
    // Format and return
    return "["+home.toString()+" vs "+away.toString()+"]";
  }// toString()
 
  /**
   * Returns a semi-shallow clone of this PairSchools object. While
   * the home, away and distance fields are simply passed to the clone,
   * a separate canAdd field is created for the clone. 
   * @note
   *    Our creating a separate copy of the canAdd field is important,
   *    as in our code, we often need to create multiple schedules and add the 
   *    PairSchool objects in different fashions and orders for our multiple
   *    schedules
   */
  public PairSchools clone()
  {
    PairSchools theCopy = new PairSchools(home, away, distance);
    // Separate copy of canAdd field
    theCopy.canAdd=canAdd;
    return theCopy;
  }// clone()

}// class PairSchools