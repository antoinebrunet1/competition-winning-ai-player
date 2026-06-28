package ca.uqam.info.solanum.students.halma.controller;

import ca.uqam.info.solanum.inf2050.f24halma.controller.ModelFactory;
import ca.uqam.info.solanum.inf2050.f24halma.model.Field;
import ca.uqam.info.solanum.inf2050.f24halma.model.FieldException;
import ca.uqam.info.solanum.inf2050.f24halma.model.Model;
import ca.uqam.info.solanum.inf2050.f24halma.model.ModelInitializationException;
import ca.uqam.info.solanum.students.halma.model.ModelImpl;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Allows creation of a StarModelFactory, which allows to create Models.
 *
 * @author Zachary Ethier
 */
public class StarModelFactory implements ModelFactory {
  private int baseSize;
  private String[] players;
  private final Set<Field> allFields = new LinkedHashSet<>();
  private final Map<Integer, Set<Field>> homeZones = new LinkedHashMap<>();
  private final Map<Integer, Set<Field>> targetZones = new LinkedHashMap<>();
  private final Map<Integer, Set<Field>> playerFields = new LinkedHashMap<>();
  private final Map<Field, Set<Field>> neighbours = new LinkedHashMap<>();

  private record Coordinate(int x, int y) {
    @Override
    public String toString() {
      return "Coordinate{"
          + "x=" + x
          + ", y=" + y
          + '}';
    }
  }

  private final Map<Field, Map<Field, Field>> extendedNeighboursForEveryField =
      new LinkedHashMap<>();

  /**
   * Default StarModelFactory Constructor.
   */
  public StarModelFactory() {
  }

  @Override
  public Model createModel(int baseSize, String[] players) {
    if (!(players.length == 3 || players.length == 6)) {
      throw new ModelInitializationException("The number of players needs to be 3 or 6");
    }
    this.baseSize = baseSize;
    this.players = players;
    createFields();
    fillPlayerFields();
    fillNeighbours();
    fillExtendedNeighboursForEveryField();
    return new ModelImpl(baseSize, players, playerFields, allFields, homeZones, targetZones,
        neighbours, extendedNeighboursForEveryField);
  }

  private void createFields() {
    createFieldsForNonZoneFields();
    createFieldsForHomeZones();
    createFieldsForTargetZones();
    if (players.length == 6) {
      createHomeZonesForLast3PlayersFor6PlayersCase();
      createTargetZonesForLast3PlayersFor6PlayersCase();
    }
  }

  private void createFieldsForNonZoneFields() {
    int lengthOfLineBelowAnyZone = baseSize + 1;
    int numberOfSpacesAbove = baseSize * 2;
    int maximumForX = baseSize + lengthOfLineBelowAnyZone + (lengthOfLineBelowAnyZone - 1) - 1;
    int height = lengthOfLineBelowAnyZone;

    /*
    Adds the fields indicated by 'x' (to allFields) (for baseSize = 1, for example):
    y\x |  00  01  02  03  04
    ----+--------------------
    00  |     [ ]     [ ]
    01  |         (x)
    02  |     ( )     (x)
    03  | [ ]     (x)     [ ]
    04  |     ( )     (x)
    05  |         (x)
    06  |     [ ]     [ ]
     */
    for (int x = maximumForX; x >= maximumForX - (lengthOfLineBelowAnyZone - 1); x--) {
      for (int y = numberOfSpacesAbove; y <= numberOfSpacesAbove + ((height - 1) * 2); y += 2) {
        allFields.add(new Field(x, y));
      }
      height++;
      numberOfSpacesAbove--;
    }
    height -= 2;
    numberOfSpacesAbove += 2;
    maximumForX -= (lengthOfLineBelowAnyZone - 1) + 1;

    /*
    Adds the fields indicated by 'x' (to allFields) (for baseSize = 1, for example):
    y\x |  00  01  02  03  04
    ----+--------------------
    00  |     [ ]     [ ]
    01  |         ( )
    02  |     (x)     ( )
    03  | [ ]     ( )     [ ]
    04  |     (x)     ( )
    05  |         ( )
    06  |     [ ]     [ ]
     */
    for (int x = maximumForX; x >= maximumForX - (lengthOfLineBelowAnyZone - 2); x--) {
      for (int y = numberOfSpacesAbove; y <= numberOfSpacesAbove + ((height - 1) * 2); y += 2) {
        allFields.add(new Field(x, y));
      }
      height--;
      numberOfSpacesAbove++;
    }
  }

  private void createFieldsForHomeZones() {
    int lengthOfLineBelowAnyZone = baseSize + 1;
    createFieldsForParticularHomeZone(baseSize - 1, lengthOfLineBelowAnyZone + baseSize, 0);
    createFieldsForParticularHomeZone(baseSize + lengthOfLineBelowAnyZone + baseSize - 1, 0, 1);
    createFieldsForParticularHomeZone(baseSize + lengthOfLineBelowAnyZone + baseSize - 1,
        (lengthOfLineBelowAnyZone + baseSize) * 2, 2);
  }

  private void createFieldsForParticularHomeZone(int startX, int numberOfSpacesAbove, int i) {
    int height = baseSize;
    Field field;
    Set<Field> homeZone = new LinkedHashSet<>();
    homeZones.put(i, homeZone);
    for (int x = startX; x >= startX - (baseSize - 1); x--) {
      for (int y = numberOfSpacesAbove; y <= numberOfSpacesAbove + ((height - 1) * 2); y += 2) {
        field = new Field(x, y);
        allFields.add(field);
        homeZone.add(field);
      }
      height--;
      numberOfSpacesAbove++;
    }
  }

  private void createFieldsForTargetZones() {
    int lengthOfLineBelowAnyZone = baseSize + 1;
    createFieldsForParticularTargetZone((baseSize * 2) - 1, baseSize - 1, 2);
    createFieldsForParticularTargetZone(
        baseSize + lengthOfLineBelowAnyZone + (lengthOfLineBelowAnyZone - 1) + baseSize - 1,
        (baseSize - 1) + lengthOfLineBelowAnyZone + baseSize, 0);
    createFieldsForParticularTargetZone((baseSize * 2) - 1,
        (baseSize - 1) + (lengthOfLineBelowAnyZone + baseSize) * 2, 1);
  }

  private void createHomeZonesForLast3PlayersFor6PlayersCase() {
    createHomeZoneForLast3PlayersFor6PlayersCase(0, 3);
    createHomeZoneForLast3PlayersFor6PlayersCase(1, 4);
    createHomeZoneForLast3PlayersFor6PlayersCase(2, 5);
  }

  private void createHomeZoneForLast3PlayersFor6PlayersCase(int targetZonePlayer,
                                                            int homeZonePlayer) {
    Set<Field> homeZone = new LinkedHashSet<>();
    homeZones.put(homeZonePlayer, homeZone);
    homeZone.addAll(targetZones.get(targetZonePlayer));
  }

  private void createTargetZonesForLast3PlayersFor6PlayersCase() {
    createTargetZoneForLast3PlayersFor6PlayersCase(3, 0);
    createTargetZoneForLast3PlayersFor6PlayersCase(4, 1);
    createTargetZoneForLast3PlayersFor6PlayersCase(5, 2);
  }

  private void createTargetZoneForLast3PlayersFor6PlayersCase(int targetZonePlayer,
                                                            int homeZonePlayer) {
    Set<Field> targetZone = new LinkedHashSet<>();
    targetZones.put(targetZonePlayer, targetZone);
    targetZone.addAll(homeZones.get(homeZonePlayer));
  }

  private void createFieldsForParticularTargetZone(int startX, int numberOfSpacesAbove, int i) {
    int height = 1;
    Field field;
    Set<Field> targetZone = new LinkedHashSet<>();
    targetZones.put(i, targetZone);
    for (int x = startX; x >= startX - (baseSize - 1); x--) {
      for (int y = numberOfSpacesAbove; y <= numberOfSpacesAbove + ((height - 1) * 2); y += 2) {
        field = new Field(x, y);
        allFields.add(field);
        targetZone.add(field);
      }
      height++;
      numberOfSpacesAbove--;
    }
  }

  private void fillPlayerFields() {
    for (int i = 0; i < players.length; i++) {
      playerFields.put(i, new LinkedHashSet<>(homeZones.get(i)));
    }
  }

  private void fillNeighbours() {
    Set<Field> neighboursForField;
    for (Field field : allFields) {
      neighboursForField = getNeighboursForField(field);
      neighbours.put(field, neighboursForField);
    }
  }

  private Set<Field> getNeighboursForField(Field field) {
    Set<Field> result = new LinkedHashSet<>();
    Set<Coordinate> positionsOfPotentialNeighbours =
        getPositionsOfPotentialNeighbours(field.x(), field.y());
    Field fieldToAdd;
    for (Coordinate coordinate : positionsOfPotentialNeighbours) {
      if (isPositionValid(coordinate)) {
        fieldToAdd = getFieldAtPosition(coordinate);
        result.add(fieldToAdd);
      }
    }
    return result;
  }

  /*
  Here is an example of a field with 8 neighbours:
y\x |  00  01  02  03  04
----+--------------------
00  |     ( )     ( )
01  |         (N)
02  |     (N)     (N)
03  | (N)     (F)     (N)
04  |     (N)     (N)
05  |         (N)
06  |     ( )     ( )
   */
  private Set<Coordinate> getPositionsOfPotentialNeighbours(int x, int y) {
    Set<Coordinate> result = new LinkedHashSet<>();
    Coordinate topLeft = new Coordinate(x - 1, y - 1);
    Coordinate top = new Coordinate(x, y - 2);
    Coordinate topRight = new Coordinate(x + 1, y - 1);
    Coordinate bottomRight = new Coordinate(x + 1, y + 1);
    Coordinate bottom = new Coordinate(x, y + 2);
    Coordinate bottomLeft = new Coordinate(x - 1, y + 1);
    result.add(topLeft);
    result.add(top);
    result.add(topRight);
    result.add(bottomRight);
    result.add(bottom);
    result.add(bottomLeft);
    return result;
  }

  private boolean isPositionValid(Coordinate coordinate) {
    for (Field field : allFields) {
      if (field.x() == coordinate.x() && field.y() == coordinate.y()) {
        return true;
      }
    }
    return false;
  }

  private Field getFieldAtPosition(Coordinate coordinate) {
    for (Field field : allFields) {
      if (field.x() == coordinate.x() && field.y() == coordinate.y()) {
        return field;
      }
    }
    throw new FieldException("No field at coordinate: " + coordinate);
  }

  private void fillExtendedNeighboursForEveryField() {
    for (Field origin : allFields) {
      extendedNeighboursForEveryField.put(origin, getExtendedNeighbours(origin));
    }
  }

  /*
Here is an example of a field with 8 neighbours:
y\x |  00  01  02  03  04
----+--------------------
00  |     ( )     ( )
01  |         (N)
02  |     (N)     (N)
03  | (N)     (F)     (N)
04  |     (N)     (N)
05  |         (N)
06  |     ( )     ( )
*/
  private Map<Field, Field> getExtendedNeighbours(Field origin) {
    Map<Field, Field> extendedNeighbours = new LinkedHashMap<>();
    for (Field neighbour : neighbours.get(origin)) {
      if (isTopLeft(origin, neighbour)) {
        addIfHasNeighbour(neighbour, new Coordinate(neighbour.x() - 1, neighbour.y() - 1),
            extendedNeighbours);
      } else if (isTop(origin, neighbour)) {
        addIfHasNeighbour(neighbour, new Coordinate(neighbour.x(), neighbour.y() - 2),
            extendedNeighbours);
      } else if (isTopRight(origin, neighbour)) {
        addIfHasNeighbour(neighbour, new Coordinate(neighbour.x() + 1, neighbour.y() - 1),
            extendedNeighbours);
      } else if (isBottomRight(origin, neighbour)) {
        addIfHasNeighbour(neighbour, new Coordinate(neighbour.x() + 1, neighbour.y() + 1),
            extendedNeighbours);
      } else if (isBottom(origin, neighbour)) {
        addIfHasNeighbour(neighbour, new Coordinate(neighbour.x(), neighbour.y() + 2),
            extendedNeighbours);
      } else {
        addIfHasNeighbour(neighbour, new Coordinate(neighbour.x() - 1, neighbour.y() + 1),
            extendedNeighbours);
      }
    }
    return extendedNeighbours;
  }

  private void addIfHasNeighbour(Field origin, Coordinate coordinate, Map<Field, Field> map) {
    if (hasNeighbour(origin, coordinate)) {
      map.put(origin, getNeighbour(origin, coordinate));
    }
  }

  private boolean hasNeighbour(Field origin, Coordinate coordinate) {
    for (Field neighbour : neighbours.get(origin)) {
      if (neighbour.x() == coordinate.x() && neighbour.y() == coordinate.y()) {
        return true;
      }
    }
    return false;
  }

  private Field getNeighbour(Field origin, Coordinate coordinate) {
    for (Field neighbour : neighbours.get(origin)) {
      if (neighbour.x() == coordinate.x() && neighbour.y() == coordinate.y()) {
        return neighbour;
      }
    }
    throw new FieldException("No field at coordinate: " + coordinate);
  }

  private boolean isTopLeft(Field origin, Field neighbour) {
    return neighbour.x() == origin.x() - 1 && neighbour.y() == origin.y() - 1;
  }

  private boolean isTop(Field origin, Field neighbour) {
    return neighbour.x() == origin.x() && neighbour.y() == origin.y() - 2;
  }

  private boolean isTopRight(Field origin, Field neighbour) {
    return neighbour.x() == origin.x() + 1 && neighbour.y() == origin.y() - 1;
  }

  private boolean isBottomRight(Field origin, Field neighbour) {
    return neighbour.x() == origin.x() + 1 && neighbour.y() == origin.y() + 1;
  }

  private boolean isBottom(Field origin, Field neighbour) {
    return neighbour.x() == origin.x() && neighbour.y() == origin.y() + 2;
  }
}
