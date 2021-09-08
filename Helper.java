public class Helper {

  /**
   * Class constructor.
   */
  private Helper() {}

  /**
   * This method is used to check if a number is prime or not
   * 
   * @param x A positive integer number
   * @return boolean True if x is prime; Otherwise, false
   */
  public static boolean isPrime(int x) {
    if (x == 1) {
      return false;
    }
    for (int i = 2; i < x; i++) {
      if (x % i == 0) {
        return false;
      }
    }
    return true;
  }

  /**
   * This method is used to get the largest prime factor
   * 
   * @param x A positive integer number
   * @return int The largest prime factor of x
   */
  public static int getLargestPrimeFactor(int x) {

    if (isPrime(x)) {
      return x;
    }

    for (int i = x - 1; i >= 2; i--) {
      if (x % i == 0) {
        if (isPrime(i)) {
          return i;
        } else {
          return getLargestPrimeFactor(i);
        }
      }
    }

    return -1;
  }
}
