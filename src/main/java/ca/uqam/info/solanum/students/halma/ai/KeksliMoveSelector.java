package ca.uqam.info.solanum.students.halma.ai;

import ca.uqam.info.solanum.inf2050.f24halma.controller.Move;
import ca.uqam.info.solanum.inf2050.f24halma.view.MoveSelector;
import java.util.Collections;
import java.util.List;

/**
 * KeksliMoveSelector.
 */
public class KeksliMoveSelector implements MoveSelector {
  @Override
  public Move selectMove(List<Move> allPossibleMoves) {
    Collections.sort(allPossibleMoves);
    return allPossibleMoves.getFirst();
  }
}
