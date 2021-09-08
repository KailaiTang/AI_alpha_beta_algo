import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GameState {
  private int size; // The number of stones
  private boolean[] stones; // Game state: true for available stones, false for taken ones
  private int lastMove; // The last move

  /**
   * Class constructor specifying the number of stones.
   */
  public GameState(int size) {

    this.size = size;

    // For convenience, we use 1-based index, and set 0 to be unavailable
    this.stones = new boolean[this.size + 1];
    this.stones[0] = false;

    // Set default state of stones to available
    for (int i = 1; i <= this.size; ++i) {
      this.stones[i] = true;
    }

    // Set the last move be -1
    this.lastMove = -1;
  }

  /**
   * Copy constructor
   */
  public GameState(GameState other) {
    this.size = other.size;
    this.stones = Arrays.copyOf(other.stones, other.stones.length);
    this.lastMove = other.lastMove;
  }


  /**
   * This method is used to compute a list of legal moves
   *
   * @return This is the list of state's moves
   */
  public List<Integer> getMoves() {
    List<Integer> result = new ArrayList<Integer>();

    for (int i = 1; i <= size; i++) {
      // if it is the first move, it must be an odd number less than one half of the size
      if (lastMove == -1) {
        if (i % 2 == 1 && i < (size / 2.0)) {
          result.add(i);
        }
      } else {
        // otherwise, it has to be available and is a factor or multiple of the lastMove
        if (stones[i] && (i % lastMove == 0 || lastMove % i == 0)) {
          result.add(i);
        }
      }
    }
    return result;
  }


  /**
   * This method is used to generate a list of successors using the getMoves() method
   *
   * @return This is the list of state's successors
   */
  public List<GameState> getSuccessors() {
    return this.getMoves().stream().map(move -> {
      GameState state = new GameState(this);
      state.removeStone(move);
      return state;
    }).collect(Collectors.toList());
  }


  /**
   * This method is used to evaluate a game state based on the given heuristic function
   *
   * @return int This is the static score of given state
   */
  public double evaluate() {

    // if stone 1 ist not taken, return 0
    if (stones[1]) {
      return 0;
    }

    // Calculate the number of stones already taken
    int taken = 0;
    for (int i = 1; i <= size; i++) {
      if (!stones[i]) {
        taken++;
      }
    }

    // At the end game state, all elements in stones are false
    // If size if odd, MAX has the last move and wins, score of 1.0
    // If size if even, MIN has the last move and wins, score of -1.0
    if (taken == size || this.getSuccessors().isEmpty()) {
      if (taken % 2 == 1) {
        return 1.0;
      } else {
        return -1.0;
      }
    }

    // Not at the end game state
    // If it is player 1's turn
    if (taken % 2 == 0) {

      // if the last move is 1, count the number of possible successors.
      // if odd, return 0.5; if even, return -0.5
      if (lastMove == 1) {
        if ((size - taken) % 2 == 1) {
          return 0.5;
        } else {
          return -0.5;
        }
      }

      // if last move is prime, count the number of multiples
      // if odd, return 0.7; if even, return -0.7
      if (Helper.isPrime(lastMove)) {
        int count = 0;
        for (int i = 1; i * lastMove <= size; i++) {
          if (stones[i * lastMove]) {
            count++;
          }
        }
        if (count % 2 == 1) {
          return 0.7;
        } else {
          return -0.7;
        }
      }

      // if the last move is composite number, find the largest prime divide last move
      // count the multiples of that prime including the prime itself available
      // if odd, return 0.6; else, return -0.6
      if (!Helper.isPrime(lastMove)) {
        int largestPrime = Helper.getLargestPrimeFactor(lastMove);
        int count = 0;
        for (int i = 1; largestPrime * i <= size; i++) {
          if (stones[largestPrime * i]) {
            count++;
          }
        }
        if (count % 2 == 1) {
          return 0.6;
        } else {
          return -0.6;
        }
      }

      // otherwise, it is Player 2's turn, performs the same check but return the opposite values.
    } else {

      // if the last move is 1, count the number of possible successors.
      // if odd, return -0.5; if even, return -0.5
      if (lastMove == 1) {
        if ((size - taken) % 2 == 1) {
          return -0.5;
        } else {
          return 0.5;
        }
      }

      // if last move is prime, count the number of multiples
      // if odd, return -0.7; if even, return 0.7
      if (Helper.isPrime(lastMove)) {
        int count = 0;
        for (int i = 1; i * lastMove <= size; i++) {
          if (stones[i * lastMove]) {
            count++;
          }
        }
        if (count % 2 == 1) {
          return -0.7;
        } else {
          return 0.7;
        }
      }

      // if the last move is composite number, find the largest prime divide last move
      // count the multiples of that prime including the prime itself available
      // if odd, return -0.6; else, return 0.6
      if (!Helper.isPrime(lastMove)) {
        int largestPrime = Helper.getLargestPrimeFactor(lastMove);
        int count = 0;
        for (int i = 1; largestPrime * i <= size; i++) {
          if (stones[largestPrime * i]) {
            count++;
          }
        }
        if (count % 2 == 1) {
          return -0.6;
        } else {
          return 0.6;
        }
      }
    }

    return 0.0;
  }

  /**
   * This method is used to take a stone out
   *
   * @param idx Index of the taken stone
   */
  public void removeStone(int idx) {
    this.stones[idx] = false;
    this.lastMove = idx;
  }

  /**
   * These are get/set methods for a stone
   *
   * @param idx Index of the taken stone
   */
  public void setStone(int idx) {
    this.stones[idx] = true;
  }

  public boolean getStone(int idx) {
    return this.stones[idx];
  }

  /**
   * These are get/set methods for lastMove variable
   *
   * @param move Index of the taken stone
   */
  public void setLastMove(int move) {
    this.lastMove = move;
  }

  public int getLastMove() {
    return this.lastMove;
  }

  /**
   * This is get method for game size
   *
   * @return int the number of stones
   */
  public int getSize() {
    return this.size;
  }

}
