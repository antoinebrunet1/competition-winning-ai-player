package ca.uqam.info.solanum.students.halma.aibattle;

import ca.uqam.info.solanum.inf2050.f24halma.controller.Move;
import ca.uqam.info.solanum.inf2050.f24halma.model.Field;
import ca.uqam.info.solanum.inf2050.f24halma.model.ModelReadOnly;
import ca.uqam.info.solanum.inf2050.f24halma.view.MoveSelector;
import ca.uqam.info.solanum.inf2050.f24halma.view.RobotMoveSelector;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * My submission for the AI battle.
 *
 * @author Antoine Brunet
 */
public class RobotMoveSelectorImpl extends RobotMoveSelector {
  private Set<Field> targetFields;

  @Override
  public MoveSelector createMoveSelector(ModelReadOnly modelReadOnly, int playerIndex) {
    this.targetFields = modelReadOnly.getBoard().getTargetFieldsForPlayer(playerIndex);
    return this;
  }

  @Override
  public Move selectMove(List<Move> allPossibleMoves) {
    Move result;
    Map<Move, Double> movesAndDistances =
        getMovesAndDistances(allPossibleMoves);
    double minimalDistance = movesAndDistances.values().stream().mapToDouble(v -> v).min()
        .getAsDouble();
    result = getAnyMoveWithMinimalDistance(movesAndDistances, allPossibleMoves, minimalDistance);
    return result;
  }

  private Move getAnyMoveWithMinimalDistance(Map<Move, Double> movesAndDistances,
                                             List<Move> allPossibleMoves, double minimalDistance) {
    return allPossibleMoves.stream()
        .filter(move -> movesAndDistances.get(move) == minimalDistance).findFirst().get();
  }

  private double getDistance(Move move) {
    Set<Double> distances = new LinkedHashSet<>();
    targetFields.forEach(field -> distances.add(getDistance(move, field)));
    return distances.stream().mapToDouble(v -> v).min().getAsDouble();
  }

  private double getDistance(Move move, Field targetZoneField) {
    if (targetFields.contains(move.origin())) {
      return Double.MAX_VALUE;
    }
    int p1 = move.target().x();
    int p2 = move.target().y();
    int q1 = targetZoneField.x();
    int q2 = targetZoneField.y();
    return Math.sqrt(Math.pow(p1 - q1, 2) + Math.pow(p2 - q2, 2));
  }

  private Map<Move, Double> getMovesAndDistances(List<Move> allPossibleMoves) {
    Map<Move, Double> result = new LinkedHashMap<>();
    allPossibleMoves.forEach(move -> result.put(move, getDistance(move)));
    return result;
  }
}
