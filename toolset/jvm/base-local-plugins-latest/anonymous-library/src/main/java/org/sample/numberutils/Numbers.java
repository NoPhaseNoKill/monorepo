package org.sample.numberutils;

/**
 * Utility class for number operations.
 */
public class Numbers {

   /**
    * Default constructor
    */
   public Numbers() {
   }

   /**
    * Adds two integers.
    *
    * @param left the first integer
    * @param right the second integer
    * @return the sum of left and right
    */
   public static int add(int left, int right) {
      System.out.println("My custom number dependency is being called");
      return left + right;
   }
}
