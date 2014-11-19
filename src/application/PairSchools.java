package application;
/**
 * @author Harry Baker
 * @author William Royle
 * @author Nickolas Knoebber
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
    School Home;
    
    /**
     * The school which is playing away during this match
     */
    School Away;
    
    /**
     * The driving distance between the two schools
     */
    double distance;
    
    // +--------------+------------------------------------------------------
    // | Constructors |
    // +--------------+

    /**
     * Creates a new pair of schools.
     */
    PairSchools(School Home, School Away, double distance)
    {
      this.Home = Home;
      this.Away = Away;
      this.distance=distance;
    }// PairSchools(School Home, School Away, double distance)
    
    // +---------+-----------------------------------------------------------
    // | Methods |
    // +---------+
    
    /**
     * Tells if two pairs of schools are identical
     */
    public boolean equals(PairSchools other)
    {
      return Home.equals(other.Home) && Away.equals(other.Away);
    }// equals(PairSchools other)
    
  }// class PairSchools