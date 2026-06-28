package ca.uqam.info.solanum.students.halma.model;

import ca.uqam.info.solanum.inf2050.f24halma.model.Board;
import ca.uqam.info.solanum.inf2050.f24halma.model.Field;
import ca.uqam.info.solanum.inf2050.f24halma.model.FieldException;
import ca.uqam.info.solanum.inf2050.f24halma.model.Model;
import ca.uqam.info.solanum.inf2050.f24halma.model.ModelAccessConsistencyException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Allows creation of a Halma model.
 *
 * @author Nicolas Mazzone
 */
// ✅
public class ModelImpl implements Model {
  private final String[] players;
  private final int baseSize;
  private final Board board;
  private final Map<Integer, Set<Field>> playerFields;
  private final int numPlayers;
  private int currentPlayer;

  /**
   * ModelImpl constructor.
   *
   * @param baseSize    side-length along all player bases
   * @param players     player names involved on the board
   * @param playerFields as all the fields occupied by each player.
   * @param allFields   as all the fields of the board.
   * @param homeZones   as the fields of all the home zones.
   * @param targetZones as the fields of all the target zones.
   * @param neighbours as the neighbours for every field of the board.
   * @param extendedNeighboursForEveryField as the extended neighbour for each neighbour of each
   *                                        field of the board.
   */
  public ModelImpl(int baseSize, String[] players, Map<Integer, Set<Field>> playerFields,
                   Set<Field> allFields, Map<Integer, Set<Field>> homeZones,
                   Map<Integer, Set<Field>> targetZones, Map<Field, Set<Field>> neighbours,
                   Map<Field, Map<Field, Field>> extendedNeighboursForEveryField) {
    this.baseSize = baseSize;
    this.players = players;
    this.playerFields = playerFields;
    this.board = new BoardImpl(baseSize, allFields, homeZones, targetZones, neighbours,
        extendedNeighboursForEveryField);
    this.numPlayers = players.length;
    this.currentPlayer = 0;
  }

  @Override
  public String[] getPlayerNames() {
    return Arrays.copyOf(players, numPlayers);
  }

  @Override
  public Set<Field> getPlayerFields(int playerIndex) {
    verifyPlayerIndex(playerIndex);

    return Collections.unmodifiableSet(playerFields.get(playerIndex));
  }

  @Override
  public int getCurrentPlayer() {
    return currentPlayer;
  }

  @Override
  public Board getBoard() {
    return board;
  }

  @Override
  public boolean isClear(Field field) throws FieldException {
    verifyIfFieldExists(field);
    for (Set<Field> values : playerFields.values()) {
      if (values.contains(field)) {
        return false;
      }
    }
    return true;
  }

  private void verifyIfFieldExists(Field field) throws FieldException {
    if (!board.getAllFields().contains(field)) {
      throw new FieldException("The provided field is not a valid board position");
    }
  }

  @Override
  public void clearField(Field field) throws FieldException, ModelAccessConsistencyException {
    verifyIfFieldExists(field);

    for (Set<Field> fields : playerFields.values()) {
      if (fields.contains(field)) {
        fields.remove(field);
        return;
      }
    }

    throw new ModelAccessConsistencyException("The provided field is not occupied.");
  }

  @Override
  public void occupyField(int playerIndex, Field field)
      throws FieldException, ModelAccessConsistencyException {
    verifyPlayerIndex(playerIndex);
    if (isClear(field)) {
      Set<Field> fields = new HashSet<>(playerFields.getOrDefault(playerIndex, new HashSet<>()));
      fields.add(field);
      playerFields.put(playerIndex, fields);
    } else {
      throw new ModelAccessConsistencyException("Field is already occupied");
    }
  }

  @Override
  public void setCurrentPlayer(int playerIndex) {
    verifyPlayerIndex(playerIndex);
    this.currentPlayer = playerIndex;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof ModelImpl model)) {
      return false;
    }
    return baseSize == model.baseSize && currentPlayer == model.currentPlayer
        && numPlayers == model.numPlayers && Arrays.equals(players, model.players)
        && Objects.equals(board, model.board) && Objects.equals(playerFields, model.playerFields);
  }

  private void verifyPlayerIndex(int playerIndex) throws ModelAccessConsistencyException {
    // Verify player is in valid range
    if (playerIndex < 0 || playerIndex >= numPlayers) {
      throw new ModelAccessConsistencyException("The provided player index is not valid");
    }
  }
}