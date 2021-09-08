import java.util.ArrayList;

public class AlphaBetaPruning {

  public int move;
  public double value;
  public int numOfNodesVisited;
  public int numOfNodesEvaluated;
  public int maxDepthReached;
  public int depthLimit;
  public double avgEffectiveBranchingFactor;
  public ArrayList<Double> record;

  public AlphaBetaPruning() {
    move = 0;
    value = 0.0;
    numOfNodesVisited = 0;
    numOfNodesEvaluated = 0;
    maxDepthReached = 0;
    depthLimit = -1;
    avgEffectiveBranchingFactor = 0;
    record = new ArrayList<Double>();
  }

  /**
   * This function will print out the information to the terminal, as specified in the homework
   * description.
   */
  public void printStats() {

    System.out.println("Move: " + move);
    System.out.println("Value: " + value);
    System.out
        .println("Number of Nodes Visited: " + numOfNodesVisited);
    System.out
        .println("Number of Nodes Evaluated: " + numOfNodesEvaluated);
    System.out.println("Max Depth Reached: " + maxDepthReached);
    avgEffectiveBranchingFactor = (numOfNodesVisited - 1)
        / (double) (numOfNodesVisited - numOfNodesEvaluated);
    System.out.println("Avg Effective Branching Factor: "
        + String.format("%.1f", avgEffectiveBranchingFactor));

  }

  /**
   * This function will start the alpha-beta search
   * 
   * @param state This is the current game state
   * @param depth This is the specified search depth
   */
  public void run(GameState state, int depth) {

    // calculate the number of stones available and taken in order to determine maxState or not.
    int available = 0;
    boolean maxState = false;
    for (int i = 1; i <= state.getSize(); i++) {
      if (state.getStone(i)) {
        available++;
      }
    }

    // if even number of stones taken, it is maxState.
    if ((state.getSize() - available) % 2 == 0) {
      maxState = true;
    }

    // update the depth limit. (if depth == 0, search to the end)
    if (depth == 0) {
      depthLimit = state.getSize() + 1;
    } else {
      depthLimit = depth;
    }

    // call the recursive function to get the result of value
    value = alphabeta(state, depthLimit, Double.NEGATIVE_INFINITY,
        Double.POSITIVE_INFINITY, maxState);

    // get the max and min number in the record ArrayList
    Double maxDouble = Double.NEGATIVE_INFINITY;
    Double minDouble = Double.POSITIVE_INFINITY;

    // if maxState, find the largest number in record and find the corresponding move
    if (maxState) {
      for (int i = 0; i < record.size(); i++) {
        if (record.get(i) > maxDouble) {
          maxDouble = record.get(i);
          move = state.getMoves().get(i);
        }
      }
    } else {
      // if minState, find the smallest number in record and find the corresponding move
      for (int i = 0; i < record.size(); i++) {
        if (record.get(i) < minDouble) {
          minDouble = record.get(i);
          move = state.getMoves().get(i);
        }
      }
    }


  }

  /**
   * This method is used to implement alpha-beta pruning for both 2 players
   * 
   * @param state This is the current game state
   * @param depth Current depth of search
   * @param alpha Current Alpha value
   * @param beta Current Beta value
   * @param maxPlayer True if player is Max Player; Otherwise, false
   * @return int This is the number indicating score of the best next move
   */
  private double alphabeta(GameState state, int depth, double alpha,
      double beta, boolean maxPlayer) {

    // update the number of nodes visited
    numOfNodesVisited++;

    // call the helper function according to maxState or minState
    if (maxPlayer) {
      return Max_Value(state, depth, alpha, beta);
    } else {
      return Min_Value(state, depth, alpha, beta);
    }
  }

  /**
   * helper function for MaxState
   * 
   * @param state This is the current game state
   * @param depth Current depth of search
   * @param alpha Current Alpha value
   * @param beta Current Beta value
   * @return
   */
  private double Max_Value(GameState state, int depth, double alpha,
      double beta) {

    // if depth > maxDepth, update maxDepth
    maxDepthReached = Math.max(maxDepthReached, depthLimit - depth);

    // if s is a terminal state or at depth limit, return SBE value of s
    // update number of nodes evaluated
    if (state.getSuccessors().isEmpty() || depth == 0) {
      numOfNodesEvaluated++;
      return state.evaluate();
    }

    double v = Double.NEGATIVE_INFINITY;
    // for each successor of the current game state
    for (GameState successor : state.getSuccessors()) {
      v = Math.max(v,
          alphabeta(successor, depth - 1, alpha, beta, false));
      if (v >= beta) {
        return v; // prune remaining children
      }
      alpha = Math.max(alpha, v);
      if (depthLimit - depth == 0) {
        record.add(v);
      }
    }


    return v;
  }


  /**
   * helper function for minState
   * 
   * @param state This is the current game state
   * @param depth Current depth of search
   * @param alpha Current Alpha value
   * @param beta Current Beta value
   * @return
   */
  private double Min_Value(GameState state, int depth, double alpha,
      double beta) {

    // if depth > maxDepth, update maxDepth
    maxDepthReached = Math.max(maxDepthReached, depthLimit - depth);

    // if s is a terminal state or at depth limit, return SBE value of s
    // update number of nodes evaluated
    // if it has depth 1, add the number to the arrayList
    if (state.getSuccessors().isEmpty() || depth == 0) {
      numOfNodesEvaluated++;
      return state.evaluate();
    }

    double v = Double.POSITIVE_INFINITY;

    // for each successor of the current game state
    for (GameState successor : state.getSuccessors()) {
      v = Math.min(v,
          alphabeta(successor, depth - 1, alpha, beta, true));
      if (v <= alpha) {
        return v; // prune remaining children
      }
      beta = Math.min(beta, v);
      if (depthLimit - depth == 0) {
        record.add(v);
      }
    }


    return v;
  }
}
