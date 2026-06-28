package ca.uqam.info.solanum.students.halma.model;

import ca.uqam.info.solanum.inf2050.f24halma.model.Board;
import ca.uqam.info.solanum.inf2050.f24halma.model.Field;
import ca.uqam.info.solanum.inf2050.f24halma.model.FieldException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Allows to get fields from the Halma board. This class is completely immutable.
 *
 * @author Antoine Brunet
 */
// ✅
public class BoardImpl implements Board {
  private final int baseSize;
  private final Set<Field> allFields;
  private final Map<Integer, Set<Field>> homeZones;
  private final Map<Integer, Set<Field>> targetZones;
  private final Map<Field, Set<Field>> neighbours;
  private final Map<Field, Map<Field, Field>> extendedNeighboursForEveryField;

  /**
   * All args constructor that only initializes all class attributes.
   *
   * @param baseSize                        as the length of every side of every home and target
   *                                        zone.
   * @param allFields                       as all the fields of the board.
   * @param homeZones                       as the fields of all the home zones.
   * @param targetZones                     as the fields of all the target zones.
   * @param neighbours                      as the neighbours for every field of the board.
   * @param extendedNeighboursForEveryField as the extended neighbour for each neighbour of each
   *                                        field of the board.
   */
  public BoardImpl(int baseSize, Set<Field> allFields, Map<Integer, Set<Field>> homeZones,
                   Map<Integer, Set<Field>> targetZones, Map<Field, Set<Field>> neighbours,
                   Map<Field, Map<Field, Field>> extendedNeighboursForEveryField) {
    this.baseSize = baseSize;
    this.allFields = allFields;
    this.homeZones = homeZones;
    this.targetZones = targetZones;
    this.neighbours = neighbours;
    this.extendedNeighboursForEveryField = extendedNeighboursForEveryField;
  }

  @Override
  public Set<Field> getAllFields() {
    return Collections.unmodifiableSet(allFields);
  }

  @Override
  public Set<Field> getHomeFieldsForPlayer(int i) {
    return Collections.unmodifiableSet(homeZones.get(i));
  }

  @Override
  public Set<Field> getAllHomeFields() {
    Set<Field> homeFields = new LinkedHashSet<>();
    for (Set<Field> homeZone : homeZones.values()) {
      homeFields.addAll(homeZone);
    }
    for (Set<Field> targetZone : targetZones.values()) {
      homeFields.addAll(targetZone);
    }
    return Collections.unmodifiableSet(homeFields);
  }

  @Override
  public Set<Field> getTargetFieldsForPlayer(int i) {
    return Collections.unmodifiableSet(targetZones.get(i));
  }

  @Override
  public Set<Field> getNeighbours(Field field) {
    if (!allFields.contains(field)) {
      throw new FieldException("Field is not part of the board");
    }
    return Collections.unmodifiableSet(neighbours.get(field));
  }

  @Override
  public Field getExtendedNeighbour(Field origin, Field neighbour) {
    if (!allFields.contains(origin)) {
      throw new FieldException("Origin field is not part of the board");
    }
    if (!allFields.contains(neighbour)) {
      throw new FieldException("Neighbour field is not part of the board");
    }
    if (!neighbours.get(origin).contains(neighbour)) {
      throw new FieldException("Neighbour field is not a neighbour of origin field");
    }
    return extendedNeighboursForEveryField.get(origin).get(neighbour);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof BoardImpl board)) {
      return false;
    }
    return baseSize == board.baseSize && Objects.equals(allFields, board.allFields)
        && Objects.equals(homeZones, board.homeZones)
        && Objects.equals(targetZones, board.targetZones);
  }
}
