import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MoveChooser {
    // evaluation numbers
    public static int[][] scores = { { 120, -20, 20, 5, 5, 20, -20, 120 }, { -20, -40, -5, -5, -5, -5, -40, -20 },
            { 20, -5, 15, 3, 3, 15, -5, 20 }, { 5, -5, 3, 3, 3, 3, -5, 5 }, { 5, -5, 3, 3, 3, 3, -5, 5 },
            { 20, -5, 15, 3, 3, 15, -5, 20 }, { -20, -40, -5, -5, -5, -5, -40, -20 },
            { 120, -20, 20, 5, 5, 20, -20, 120 } };

    public static Move chooseMove(BoardState boardState) {
        int searchDepth = Othello.searchDepth;
        ArrayList<Integer> FirstMoveScoreList = new ArrayList<>();
        ArrayList<Move> moves = boardState.getLegalMoves();
        if (moves.isEmpty()) {
            return null;
        }
        // This is the top level call, call minimax to every daughter of vertex(current BoardState).
        // (Actually, manually call minimax to current BoardState)
        int alpha = -99999;
        for (Move item : moves) {
            BoardState currentBoardState = boardState.deepCopy();
            currentBoardState.makeLegalMove(item.x, item.y);
            alpha = Math.max(alpha, minimax(currentBoardState, searchDepth-1, alpha, 99999)); //this is already in depth 1, so search depth-1
            FirstMoveScoreList.add(alpha);
        }
        // Get the max value of all the values returned from minimax function. Because
        // from the computer's view, it wants the best position (the biggest).
        Integer maxVal = Collections.max(FirstMoveScoreList);
        Integer maxIdx = FirstMoveScoreList.indexOf(maxVal);
        return moves.get(maxIdx);
    }

    // Evaluate the board state
    private static int evaluate(BoardState boardState) {
        int whiteScore = 0;
        int blackScore = 0;
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (boardState.getContents(i, j) == 1)
                    whiteScore += scores[i][j];
                else if (boardState.getContents(i, j) == -1)
                    blackScore += scores[i][j];
        return whiteScore - blackScore;
    }

    // here the node is an object of the boardState, because we can make moves to
    // boardState, it's like extending it to a search tree.
    private static int minimax(BoardState node, int depth, int alpha, int beta) {
        // base node
        if (depth == 0) {
            return evaluate(node);
        }

        // white turn, maximizing node
        else if (node.colour == 1) {
            alpha = -99999;
            List<Move> daughters = node.getLegalMoves();
            int i = 0;
            // if there is no legal move, then there is a single daughter, which has the
            // same board state as the last one (don't make any move). and then pass to
            // another player.
            if (daughters.size() == 0 && alpha < beta) {
                node.colour = -node.colour;
                alpha = Math.max(alpha, minimax(node, depth - 1, alpha, beta));
            }
            while (alpha < beta && i < daughters.size()) {
                BoardState currentBoardState = node.deepCopy();
                currentBoardState.makeLegalMove(daughters.get(i).x, daughters.get(i).y);
                BoardState node1 = currentBoardState;
                alpha = Math.max(alpha, minimax(node1, depth - 1, alpha, beta));
                i++;
            }
            return alpha;
        }

        // black turn, minimizing node
        else {
            beta = 99999;
            List<Move> daughters = node.getLegalMoves();
            int i = 0;
            if (daughters.size() == 0 && alpha < beta) {
                node.colour = -node.colour;
                beta = Math.min(beta, minimax(node, depth - 1, alpha, beta));
            }
            while (alpha < beta && i < daughters.size()) {
                BoardState currentBoardState = node.deepCopy();
                currentBoardState.makeLegalMove(daughters.get(i).x, daughters.get(i).y);
                BoardState node1 = currentBoardState;
                beta = Math.min(beta, minimax(node1, depth - 1, alpha, beta));
                i++;
            }
            return beta;
        }
    }
}

