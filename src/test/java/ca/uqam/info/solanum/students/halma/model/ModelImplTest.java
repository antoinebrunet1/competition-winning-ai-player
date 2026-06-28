package ca.uqam.info.solanum.students.halma.model;

import ca.uqam.info.solanum.inf2050.f24halma.model.*;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;


public class ModelImplTest {

    private Model model;

    private void setModel(int baseSize) {
        model = new StarModelTest().getModel(baseSize);
    }

    private boolean verifyPlayerField(int playerIndex, Field field) {

        Set<Field> playerFields = model.getPlayerFields(playerIndex);

        for (Field playerField : playerFields) {
            if (field.x() == playerField.x() && field.y() == playerField.y()) {
                return true;
            }
        }
        
        return false;
    }

    /* tests clearFields */

    @Test(
        expected = FieldException.class
    ) 
    public void clearFieldNonExistingFieldBase1() {
        setModel(1);
        Field field = new Field(3, 5);

        model.clearField(field);
    }

    @Test(
        expected = FieldException.class
    ) 
    public void clearFieldNonExistingFieldBase3() {
        setModel(3);
        Field field = new Field(11, 4);

        model.clearField(field);
    }

    @Test(
        expected = FieldException.class
    ) 
    public void clearFieldOutOfBoundFieldBase1() {
        setModel(1);
        Field field = new Field(5, 3);

        model.clearField(field);
    }

    @Test(
        expected = FieldException.class
    ) 
    public void clearFieldOutOfBoundFieldBase3() {
        setModel(3);
        Field field = new Field(13, 5);

        model.clearField(field);
    }

    @Test(
        expected = ModelAccessConsistencyException.class
    ) 
    public void clearFieldNonOccupiedFieldBase1() {
        setModel(1);
        Field field = new Field(2, 1);

        model.clearField(field);
    }

    @Test(
        expected = ModelAccessConsistencyException.class
    ) 
    public void clearFieldNonOccupiedFieldBase3() {
        setModel(3);
        Field field = new Field(4, 7);

        model.clearField(field);
    }

    @Test
    public void clearFieldValidFieldBase1() {
        setModel(1);
        Field field = new Field(3, 0);

        model.clearField(field);

        Assert.assertTrue("Field is supposed to be cleared", model.isClear(field));
    }

    @Test
    public void clearFieldValidFieldBase3() {
        setModel(3);
        Field field = new Field(2, 9);

        model.clearField(field);

        Assert.assertTrue("Field is supposed to be cleared", model.isClear(field));
    }

    @Test
    public void isClearValidEmptyField() {
        setModel(2);
        Field field = new Field(5, 3);

        Assert.assertTrue("Field is supposed to be clear", model.isClear(field));
    }

    @Test
    public void isClearValidNonEmptyField() {
        setModel(2);
        Field field = new Field(6, 2);

        Assert.assertFalse("Field is not supposed to be clear", model.isClear(field));
    }

    @Test(
        expected = FieldException.class
    )
    public void isClearNonExistantField() {
        setModel(2);
        Field field = new Field(9, 6);

        model.isClear(field);
    }

    /* ***************** */


    /* tests occupyField */

    @Test(
        expected = ModelAccessConsistencyException.class
    )
    public void occupyFieldNegativePlayerIndex() {
        setModel(2);

        int invalidPlayerIndex = -1;   
        Field field = new Field(4, 2);

        model.occupyField(invalidPlayerIndex, field);
    }

    @Test(
        expected = ModelAccessConsistencyException.class
    )
    public void occupyFieldOutOfBoundPlayerIndex() {
        setModel(2);
        
        int invalidPlayerIndex = model.getPlayerNames().length;   
        Field field = new Field(4, 2);

        model.occupyField(invalidPlayerIndex, field);
    }

    @Test(
        expected = ModelAccessConsistencyException.class
    )
    public void occupyFieldAlreadyOccupiedFieldWithBase1() {
        setModel(1);

        model.occupyField(0, new Field(1, 2));
        model.occupyField(1, new Field(2, 1));
        model.occupyField(0, new Field(2, 1));
    }

    @Test(
        expected = ModelAccessConsistencyException.class
    )
    public void occupyFieldAlreadyOccupiedFieldWithBase3() {
        setModel(3);

        model.occupyField(0, new Field(3, 6));
        model.occupyField(1, new Field(6, 3));
        model.occupyField(0, new Field(4, 5));
        model.occupyField(1, new Field(5, 4));
        model.occupyField(0, new Field(5, 4));
    }

    @Test(
        expected = FieldException.class
    )
    public void occupyFieldNonExistingFieldWithBase1() {
        setModel(1);
        int playerIndex = 1;
        Field field = new Field(4, 0);

        model.occupyField(playerIndex, field);
    }

    @Test(
        expected = FieldException.class
    )
    public void occupyFieldNonExistingFieldWithBase3() {
        setModel(3);
        int playerIndex = 1;
        Field field = new Field(10, 0);

        model.occupyField(playerIndex, field);
    }

    @Test(
        expected = FieldException.class
    )
    public void occupyFieldOutOfBoundFieldWithBase1() {
        setModel(1);
        int playerIndex = 1;
        Field field = new Field(5, 0);

        model.occupyField(playerIndex, field);
    }

    @Test(
        expected = FieldException.class
    )
    public void occupyFieldOutOfBoundFieldWithBase3() {
        setModel(3);
        int playerIndex = 1;
        Field field = new Field(10, 20);

        model.occupyField(playerIndex, field);
    }

    @Test
    public void occupyFieldValidFieldWithBase1Player0() {
        setModel(1);
        int playerIndex = 0;
        Field field = new Field(1, 4);

        model.occupyField(playerIndex, field);

        Assert.assertTrue("Error trying to occupy field for current player", verifyPlayerField(playerIndex, field));
    }

    @Test
    public void occupyFieldValidFieldWithBase3Player0() {
        setModel(3);
        int playerIndex = 0;
        Field field = new Field(3, 12);

        model.occupyField(playerIndex, field);

        Assert.assertTrue("Error trying to occupy field for current player", verifyPlayerField(playerIndex, field));
    }

    @Test
    public void occupyFieldValidFieldWithBase1Player1() {
        setModel(1);
        int playerIndex = 1;
        Field field = new Field(2, 1);

        model.occupyField(playerIndex, field);

        Assert.assertTrue("Error trying to occupy field for current player", verifyPlayerField(playerIndex, field));
    }

    @Test
    public void occupyFieldValidFieldWithBase3Player1() {
        setModel(3);
        int playerIndex = 1;
        Field field = new Field(6, 3);

        model.occupyField(playerIndex, field);

        Assert.assertTrue("Error trying to occupy field for current player", verifyPlayerField(playerIndex, field));
    }

    /* ***************** */

    
    /* tests setCurrentPlayer */

    @Test(
        expected = ModelAccessConsistencyException.class
    )
    public void setCurrentPlayerNegativeIndex() {
        setModel(2);
        model.setCurrentPlayer(-1);
    }

    @Test(
        expected = ModelAccessConsistencyException.class
    )
    public void setCurrentPlayerOutOfBoundIndex() {
        setModel(2);
        int outOfBoundIndex = model.getPlayerNames().length;

        model.setCurrentPlayer(outOfBoundIndex);
    }

    @Test
    public void setCurrentPlayerValidIndex() {
        setModel(2);

        model.setCurrentPlayer(1);
        Assert.assertEquals("Current player was not correctly assigned", 1, model.getCurrentPlayer());
    }

    @Test
    public void setCurrentPlayerIndexZero() {
        setModel(2);

        model.setCurrentPlayer(0);
        Assert.assertEquals("Current player was not correctly assigned for index 0", 0, model.getCurrentPlayer());
    }

    @Test
    public void setCurrentPlayerLastIndex() {
        setModel(2);
        int lastIndex = model.getPlayerNames().length - 1;

        model.setCurrentPlayer(lastIndex);
        Assert.assertEquals("Current player was not correctly assigned for last index", lastIndex, model.getCurrentPlayer());
    }

    /* ***************** */

    @Test
    public void getPlayerNamesReturnsCorrectArray() {
        setModel(1);
        String[] expectedPlayerNames = {"Max", "Ryan", "Quentin"};
        String[] actualPlayerNames = model.getPlayerNames();

        Assert.assertArrayEquals("Players names in model are not as expected", expectedPlayerNames, actualPlayerNames);
    }

    @Test
    public void getPlayerFieldsReturnsCorrectSet() {
        setModel(1);
        String serializedSetOfExpectedPlayerFields = "[(0,3)]";
        Set<Field> deserializedSetOfExpectedPlayerFields = Field.deserializeSetOfFields(serializedSetOfExpectedPlayerFields);
        Set<Field> actualPlayerFields = model.getPlayerFields(0);

        Assert.assertEquals("Player field in model are not as expected", deserializedSetOfExpectedPlayerFields, actualPlayerFields);
    }

    @Test
    public void getCurrentPlayerReturnsCorrectString() {
        setModel(1);
        int expectedCurrentPlayer = 0;
        int actualNameOfCurrentPlayer = model.getCurrentPlayer();

        Assert.assertEquals("Current player in model is not as expected", expectedCurrentPlayer, actualNameOfCurrentPlayer);
    }

    @Test
    public void getBoardReturnsNonNullBoard() {
        setModel(1);
        Board board = model.getBoard();

        Assert.assertNotNull("Board in model is not supposed to be null", board);
    }

    @Test
    public void equalsReturnsTrueWhenExpected() {
        setModel(1);
        Model model2 = new StarModelTest().getModel(1);

        Assert.assertEquals("Models should be equal", model, model2);
    }

    @Test
    public void equalsReturnsFalseForModelWithDifferentBaseSize() {
        setModel(1);
        Model model2 = new StarModelTest().getModel(2);

        Assert.assertNotEquals("Models should not be equal", model, model2);
    }
}
