package ca.uqam.info.solanum.students.halma.controller;

import ca.uqam.info.solanum.inf2050.f24halma.controller.Controller;
import ca.uqam.info.solanum.inf2050.f24halma.controller.IllegalMoveException;
import ca.uqam.info.solanum.inf2050.f24halma.controller.ModelFactory;
import ca.uqam.info.solanum.inf2050.f24halma.controller.Move;
import ca.uqam.info.solanum.inf2050.f24halma.model.Field;
import ca.uqam.info.solanum.inf2050.f24halma.model.Model;
import ca.uqam.info.solanum.inf2050.f24halma.model.ModelReadOnly;
import ca.uqam.info.solanum.inf2050.f24halma.view.TextualVisualizer;
import java.util.ArrayList;
import java.util.List;

/**
 * Allows to create controller for the Star playing field Model.
 *
 * @author Nicolas Mazzone
 */
public class StarController implements Controller {
  private boolean lastMoveWasJump;
  private Move lastJumpMove;
  private final Model model;

  /**
   * Star Controller Constructor that only initializes all class attributes.
   *
   * @param starModelFactory as the Star playing field model.
   * @param baseSize         as the side-length along all player bases.
   * @param playerNames      as the player names involved on the board.
   */
  public StarController(ModelFactory starModelFactory, int baseSize, String[] playerNames) {
    this.model = starModelFactory.createModel(baseSize, playerNames);
  }

  @Override
  public ModelReadOnly getModel() {
    return model;
  }

  @Override
  public List<Move> getPlayerMoves() {
    if (!lastMoveWasJump) {
      return getPlayerMovesForNonJumpPreviousMove();
    }
    return getPlayerMovesForJumpPreviousMove(lastJumpMove.origin(), lastJumpMove.target());
  }

  private List<Move> getPlayerMovesForNonJumpPreviousMove() {
    List<Move> moves = new ArrayList<>();
    getModel().getPlayerFields(getModel().getCurrentPlayer()).forEach(origin ->
        moves.addAll(getPlayerMovesForFieldForNonJumpPreviousMove(origin)));
    return moves;
  }

  private List<Move> getPlayerMovesForFieldForNonJumpPreviousMove(Field origin) {
    List<Move> moves = new ArrayList<>();
    getModel().getBoard().getNeighbours(origin).stream()
        .filter(neighbour -> getModel().isClear(neighbour))
        .forEach(neighbour -> moves.add(new Move(origin, neighbour, false)));
    for (Field neighbour : getModel().getBoard().getNeighbours(origin)) {
      if (!getModel().isClear(neighbour)
          && model.getBoard().getExtendedNeighbour(origin, neighbour) != null
          && getModel().isClear(model.getBoard().getExtendedNeighbour(origin, neighbour))) {
        moves.add(new Move(origin, model.getBoard().getExtendedNeighbour(origin, neighbour),
            true));
      }
    }
    return moves;
  }

  private List<Move> getPlayerMovesForJumpPreviousMove(Field originOfLastMove,
                                                       Field targetOfLastMove) {
    Field extendedNeighbourOfTargetOfLastMove;
    List<Move> moves = new ArrayList<>();
    for (Field neighbourOfTargetOfLastMove : getModel().getBoard()
        .getNeighbours(targetOfLastMove)) {
      extendedNeighbourOfTargetOfLastMove =
          getModel().getBoard().getExtendedNeighbour(targetOfLastMove, neighbourOfTargetOfLastMove);
      if (jumpCanBeDoneAgain(neighbourOfTargetOfLastMove, extendedNeighbourOfTargetOfLastMove,
          originOfLastMove)) {
        moves.add(new Move(targetOfLastMove, extendedNeighbourOfTargetOfLastMove, true));
      }
    }
    moves.add(new Move(targetOfLastMove, targetOfLastMove, false));
    return moves;
  }

  private boolean jumpCanBeDoneAgain(Field neighbourOfTargetOfLastMove,
                                     Field extendedNeighbourOfTargetOfLastMove,
                                     Field originOfLastMove) {
    return intermediateFieldIsOccupied(neighbourOfTargetOfLastMove)
        && extendedNeighbourOfTargetOfLastMoveExists(extendedNeighbourOfTargetOfLastMove)
        && getModel().isClear(extendedNeighbourOfTargetOfLastMove)
        && !isAntiJump(originOfLastMove, extendedNeighbourOfTargetOfLastMove);
  }

  private boolean intermediateFieldIsOccupied(Field intermediateField) {
    return !getModel().isClear(intermediateField);
  }

  private boolean extendedNeighbourOfTargetOfLastMoveExists(
      Field extendedNeighbourOfTargetOfLastMove) {
    return extendedNeighbourOfTargetOfLastMove != null;
  }

  private boolean isAntiJump(Field originOfLastMove, Field extendedNeighbourOfTargetOfLastMove) {
    return extendedNeighbourOfTargetOfLastMove.equals(originOfLastMove);
  }

  @Override
  public void performMove(Move move) {
    List<Move> playerMoves = getPlayerMoves();

    if (!playerMoves.contains(move)) {
      throw new IllegalMoveException("Invalid move " + move);
    }

    model.clearField(move.origin());
    model.occupyField(getModel().getCurrentPlayer(), move.target());

    if (move.jump()) {
      lastMoveWasJump = true;
      lastJumpMove = move;
    } else {
      model.setCurrentPlayer(getNextPlayer());
    }
  }

  private int getNextPlayer() {
    lastMoveWasJump = false;

    if (getModel().getCurrentPlayer() == getModel().getPlayerNames().length - 1) {
      return 0;
    }

    return getModel().getCurrentPlayer() + 1;
  }

  @Override
  public boolean isGameOver() {
    for (int i = 0; i < model.getPlayerNames().length; i++) {
      if (model.getBoard().getTargetFieldsForPlayer(i).equals(model.getPlayerFields(i))) {
        return true;
      }
    }

    return false;
  }
}
