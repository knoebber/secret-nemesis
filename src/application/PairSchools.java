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
    int distance;
    
    // +--------------+------------------------------------------------------
    // | Constructors |
    // +--------------+

    /**
     * Creates a new pair of schools.
     */
    PairSchools(School Home, School Away, int distance)
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
    
    
    public void print()
    {
      System.out.println("Home School is "+ Home.name);
      System.out.println("Away School is "+ Away.name);
      System.out.println("Distance is "+ distance);
      System.out.println();
    }
    
  }// class PairSchools