package ca.uqam.info.solanum.students.halma.model;

import ca.uqam.info.solanum.inf2050.f24halma.model.Board;
import ca.uqam.info.solanum.inf2050.f24halma.model.Field;
import ca.uqam.info.solanum.inf2050.f24halma.model.FieldException;
import ca.uqam.info.solanum.inf2050.f24halma.model.Model;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class BoardImplTest {
    private Board board;

    private void setModelAndBoard(int baseSize) {
        Model model = new StarModelTest().getModel(baseSize);
        board = model.getBoard();
    }

    private Field getField(int x, int y) {
        for (Field field : board.getAllFields()) {
            if (field.x() == x && field.y() == y) {
                return field;
            }
        }

        return null;
    }

    private void compareToActualNeighbours(int baseSize, String serializedSetOfExpectedNeighbours, int xOfOrigin, int yOfOrigin) {
        setModelAndBoard(baseSize);
        Set<Field> deserializedSetOfExpectedNeighbours = Field.deserializeSetOfFields(serializedSetOfExpectedNeighbours);
        Field origin = getField(xOfOrigin, yOfOrigin);
        Set<Field> actualNeighbours =  board.getNeighbours(origin);

        Assert.assertEquals("Neighbours in model do not match expected set of fields", deserializedSetOfExpectedNeighbours, actualNeighbours);
    }

    @Test
    public void getCorrectNeighboursForOriginInMiddleWithBaseSize1() {
        compareToActualNeighbours(1, "[(1,2), (2,1), (3,2), (3,4), (2,5), (1,4)]", 2, 3);
    }

    @Test
    public void getCorrectNeighboursForOriginOnFarLeftWithBaseSize1() {
        compareToActualNeighbours(1, "[(1,2), (1,4)]", 0, 3);
    }

    @Test
    public void getCorrectNeighboursForOriginInMiddleWithBaseSize2() {
        compareToActualNeighbours(2, "[(3,5), (4,4), (5,5), (5,7), (4,8), (3,7)]", 4, 6);
    }

    @Test
    public void getCorrectNeighboursForOriginInMiddleWithBaseSize3() {
        compareToActualNeighbours(3, "[(5,8), (6,7), (7,8), (7,10), (6,11), (5,10)]", 6, 9);
    }

    @Test
    public void getCorrectNeighboursForOriginInMiddleWithBaseSize4() {
        compareToActualNeighbours(4, "[(7,11), (8,10), (9,11), (9,13), (8,14), (7,13)]", 8, 12);
    }

    @Test
    public void getNeighboursDoesNotThrowAnyException() {
        setModelAndBoard(1);
        Field origin = getField(2, 3);

        board.getNeighbours(origin);
    }

    @Test(
            expected = UnsupportedOperationException.class
    )
    public void getNeighboursThrowsUnsupportedOperationExceptionWhenAdding() {
        setModelAndBoard(1);
        Field origin = getField(2, 3);
        Set<Field> neighbours = board.getNeighbours(origin);

        neighbours.add(new Field(99, 99));
    }

    @Test
    public void getExtendedNeighbourOnTheTopLeftForFarRightWithBaseSize1() {
        setModelAndBoard(1);
        Field origin = getField(4, 3);
        Field neighbour = getField(3, 2);
        Field actualExtendedNeighbour = board.getExtendedNeighbour(origin, neighbour);

        Assert.assertEquals("Extended neighbour in model has incorrect x position", 2, actualExtendedNeighbour.x());
        Assert.assertEquals("Extended neighbour in model has incorrect y position", 1, actualExtendedNeighbour.y());
    }

    @Test
    public void getExtendedNeighbourOnTheTopForX3Y4WithBaseSize1() {
        setModelAndBoard(1);
        Field origin = getField(3, 4);
        Field neighbour = getField(3, 2);
        Field actualExtendedNeighbour = board.getExtendedNeighbour(origin, neighbour);

        Assert.assertEquals("Extended neighbour in model has incorrect x position", 3, actualExtendedNeighbour.x());
        Assert.assertEquals("Extended neighbour in model has incorrect y position", 0, actualExtendedNeighbour.y());
    }

    @Test
    public void getExtendedNeighbourOnTheTopRightForFarLeftWithBaseSize1() {
        setModelAndBoard(1);
        Field origin = getField(0, 3);
        Field neighbour = getField(1, 2);
        Field actualExtendedNeighbour = board.getExtendedNeighbour(origin, neighbour);

        Assert.assertEquals("Extended neighbour in model has incorrect x position", 2, actualExtendedNeighbour.x());
        Assert.assertEquals("Extended neighbour in model has incorrect y position", 1, actualExtendedNeighbour.y());
    }

    @Test
    public void getExtendedNeighbourOnTheBottomRightForFarLeftWithBaseSize1() {
        setModelAndBoard(1);
        Field origin = getField(0, 3);
        Field neighbour = getField(1, 4);
        Field actualExtendedNeighbour = board.getExtendedNeighbour(origin, neighbour);

        Assert.assertEquals("Extended neighbour in model has incorrect x position", 2, actualExtendedNeighbour.x());
        Assert.assertEquals("Extended neighbour in model has incorrect y position", 5, actualExtendedNeighbour.y());
    }

    @Test
    public void getExtendedNeighbourOnTheBottomForX1Y2WithBaseSize1() {
        setModelAndBoard(1);
        Field origin = getField(1, 2);
        Field neighbour = getField(1, 4);
        Field actualExtendedNeighbour = board.getExtendedNeighbour(origin, neighbour);

        Assert.assertEquals("Extended neighbour in model has incorrect x position", 1, actualExtendedNeighbour.x());
        Assert.assertEquals("Extended neighbour in model has incorrect y position", 6, actualExtendedNeighbour.y());
    }

    @Test
    public void getExtendedNeighbourOnTheBottomLeftForX2Y1WithBaseSize1() {
        setModelAndBoard(1);
        Field origin = getField(2, 1);
        Field neighbour = getField(1, 2);
        Field actualExtendedNeighbour = board.getExtendedNeighbour(origin, neighbour);

        Assert.assertEquals("Extended neighbour in model has incorrect x position", 0, actualExtendedNeighbour.x());
        Assert.assertEquals("Extended neighbour in model has incorrect y position", 3, actualExtendedNeighbour.y());
    }

    @Test(
        expected = FieldException.class
    )
    public void getExtendedNeighbourForNonExistingNeighbour() {
        setModelAndBoard(1);
        Field origin = getField(0, 3);
        Field neighbour = getField(0, 1);
        board.getExtendedNeighbour(origin, neighbour);
    }

    @Test
    public void getExtendedNeighbourForNonExistingExtendedNeighbour() {
        setModelAndBoard(1);
        Field origin = getField(1, 2);
        Field neighbour = getField(1, 0);
        Field actualExtendedNeighbour = board.getExtendedNeighbour(origin, neighbour);

        Assert.assertNull("Extended neighbour in model is supposed to be null", actualExtendedNeighbour);
    }

    @Test
    public void getAllFieldsDoesNotThrowAnyException() {
        setModelAndBoard(1);

        board.getAllFields();
    }

    @Test(
            expected = UnsupportedOperationException.class
    )
    public void getAllFieldsThrowsUnsupportedOperationExceptionWhenAdding() {
        setModelAndBoard(1);
        Set<Field> allFields = board.getAllFields();

        allFields.add(new Field(99, 99));
    }

    @Test
    public void getHomeFieldsForPlayer1WithBase1() {
        compareHomeFieldsForPlayer(1, 0, "[(0,3)]");
    }

    @Test
    public void getHomeFieldsForPlayer1WithBase2() {
        compareHomeFieldsForPlayer(2, 0, "[(1,5), (0,6), (1,7)]");
    }

    private void compareHomeFieldsForPlayer(int baseSize, int playerIndex, String serializedSetOfExpectedHomeFields) {
        setModelAndBoard(baseSize);
        Set<Field> actualHomeFields = board.getHomeFieldsForPlayer(playerIndex);
        Set<Field> deserializedSetOfExpectedHomeFields = Field.deserializeSetOfFields(serializedSetOfExpectedHomeFields);

        Assert.assertEquals("Home fields for player in model do not match expected set of fields", deserializedSetOfExpectedHomeFields, actualHomeFields);
    }

    @Test
    public void getHomeFieldsForPlayer2WithBase1() {
        compareHomeFieldsForPlayer(1, 1, "[(3,0)]");
    }

    @Test
    public void getHomeFieldsForPlayer3WithBase1() {
        compareHomeFieldsForPlayer(1, 2, "[(3,6)]");
    }

    @Test
    public void getHomeFieldsForPlayer2WithBase2() {
        compareHomeFieldsForPlayer(2, 1, "[(6,0), (5,1), (6,2)]");
    }

    @Test
    public void getHomeFieldsForPlayer3WithBase2() {
        compareHomeFieldsForPlayer(2, 2, "[(6,10), (5,11), (6,12)]");
    }

    @Test
    public void getHomeFieldsForPlayerDoesNotThrowAnyException() {
        setModelAndBoard(1);

        board.getHomeFieldsForPlayer(1);
    }

    @Test(
            expected = UnsupportedOperationException.class
    )
    public void getHomeFieldsForPlayerThrowsUnsupportedOperationExceptionWhenAdding() {
        setModelAndBoard(1);
        Set<Field> homeFieldsForPlayer = board.getHomeFieldsForPlayer(1);

        homeFieldsForPlayer.add(new Field(99, 99));
    }

    @Test
    public void getAllHomeFieldsWithBaseSize1() {
        compareHomeFields(1, "[(1,0), (3,0), (0,3), (4,3), (1,6), (3,6)]");
    }

    private void compareHomeFields(int baseSize, String serializedSetOfExpectedHomeFields) {
        setModelAndBoard(baseSize);
        Set<Field> actualHomeFields = board.getAllHomeFields();
        Set<Field> deserializedSetOfExpectedHomeFields = Field.deserializeSetOfFields(serializedSetOfExpectedHomeFields);

        Assert.assertEquals("All home fields in model do not match expected set of fields", deserializedSetOfExpectedHomeFields, actualHomeFields);
    }

    @Test
    public void getAllHomeFieldsWithBaseSize2() {
        compareHomeFields(2, "[(2,0), (6,0), (3,1), (5,1), (2,2), (6,2), (1,5), (7,5), (0,6), (8,6), (1,7), (7,7), (2,10), (6,10), (3,11), (5,11), (2,12), (6,12)]");
    }

    @Test
    public void getAllHomeFieldsDoesNotThrowAnyException() {
        setModelAndBoard(1);

        board.getAllHomeFields();
    }

    @Test(
            expected = UnsupportedOperationException.class
    )
    public void getAllHomeFieldsThrowsUnsupportedOperationExceptionWhenAdding() {
        setModelAndBoard(1);
        Set<Field> homeFields = board.getAllHomeFields();

        homeFields.add(new Field(99, 99));
    }

    @Test
    public void getTargetFieldsForPlayerForPlayer1WithBaseSize1() {
        compareTargetFieldsForPlayer(1, 0, "[(4,3)]");
    }

    private void compareTargetFieldsForPlayer(int baseSize, int playerIndex, String serializedSetOfExpectedTargetFieldsForPlayer) {
        setModelAndBoard(baseSize);
        Set<Field> actualTargetFieldsForPlayer = board.getTargetFieldsForPlayer(playerIndex);
        Set<Field> deserializedSetOfExpectedTargetFieldsForPlayer = Field.deserializeSetOfFields(serializedSetOfExpectedTargetFieldsForPlayer);

        Assert.assertEquals("Target fields for player in model do not match expected set of fields", deserializedSetOfExpectedTargetFieldsForPlayer, actualTargetFieldsForPlayer);
    }

    @Test
    public void getTargetFieldsForPlayerForPlayer1WithBaseSize2() {
        compareTargetFieldsForPlayer(2, 0, "[(7,5), (8,6), (7,7)]");
    }

    @Test
    public void getTargetFieldsForPlayerForPlayer2WithBaseSize1() {
        compareTargetFieldsForPlayer(1, 1, "[(1,6)]");
    }

    @Test
    public void getTargetFieldsForPlayerForPlayer3WithBaseSize1() {
        compareTargetFieldsForPlayer(1, 2, "[(1,0)]");
    }

    @Test
    public void getTargetFieldsForPlayerForPlayer2WithBaseSize2() {
        compareTargetFieldsForPlayer(2, 1, "[(2,10), (3,11), (2,12)]");
    }

    @Test
    public void getTargetFieldsForPlayerForPlayer3WithBaseSize2() {
        compareTargetFieldsForPlayer(2, 2, "[(2,0), (3,1), (2,2)]");
    }

    @Test
    public void getTargetFieldsForPlayerDoesNotThrowAnyException() {
        setModelAndBoard(1);

        board.getTargetFieldsForPlayer(1);
    }

    @Test(
            expected = UnsupportedOperationException.class
    )
    public void getTargetFieldsForPlayerThrowsUnsupportedOperationExceptionWhenAdding() {
        setModelAndBoard(1);
        Set<Field> targetFieldsForPlayer = board.getTargetFieldsForPlayer(1);

        targetFieldsForPlayer.add(new Field(99, 99));
    }
}
