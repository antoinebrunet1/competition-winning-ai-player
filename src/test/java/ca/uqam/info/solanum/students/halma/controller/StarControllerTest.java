package ca.uqam.info.solanum.students.halma.controller;

import ca.uqam.info.solanum.inf2050.f24halma.controller.AbstractStarControllerTest;
import ca.uqam.info.solanum.inf2050.f24halma.controller.Controller;
import ca.uqam.info.solanum.inf2050.f24halma.controller.IllegalMoveException;
import ca.uqam.info.solanum.inf2050.f24halma.controller.Move;
import ca.uqam.info.solanum.inf2050.f24halma.model.Field;
import ca.uqam.info.solanum.inf2050.f24halma.model.Model;
import ca.uqam.info.solanum.inf2050.f24halma.model.ModelReadOnly;
import ca.uqam.info.solanum.inf2050.f24halma.controller.ModelFactory;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class StarControllerTest extends AbstractStarControllerTest {

    private Controller starController;
    private Model model;

    @Override
    public Controller getController(int baseSize, String[] playerNames) {

        ModelFactory modelFactory = new StarModelFactory();
        return new StarController(modelFactory, baseSize, playerNames);
    }

    private void getDefaultControllerBaseX(int baseSize) {

        starController = getController(baseSize, new String[] {"Max", "Ryan", "Quentin"});
    }

    public Controller getControllerForTest() {
      return this.getController(1, new String[]{"Max", "Ryan", "Quentin"});
    }

    @Test
    public void isGameOverReturnsTrueWhenExpected() {
      Controller starController = this.getControllerForTest();

      starController.performMove(Move.deserializeMove("(00,03) -> (01,02)"));
      starController.performMove(Move.deserializeMove("(03,00) -> (02,01)"));
      starController.performMove(Move.deserializeMove("(03,06) -> (02,05)"));

      starController.performMove(Move.deserializeMove("(01,02) -> (02,03)"));
      starController.performMove(Move.deserializeMove("(02,01) -> (01,02)"));
      starController.performMove(Move.deserializeMove("(02,05) -> (01,04)"));

      starController.performMove(Move.deserializeMove("(02,03) -> (03,02)"));
      starController.performMove(Move.deserializeMove("(01,02) -> (02,01)"));
      starController.performMove(Move.deserializeMove("(01,04) -> (02,05)"));

      starController.performMove(Move.deserializeMove("(03,02) -> (04,03)"));

      Assert.assertTrue("The game should be over", starController.isGameOver());
    }

    @Test
    public void isGameOverReturnsFalseWhenExpected() {
      Controller starController = this.getControllerForTest();

      starController.performMove(Move.deserializeMove("(00,03) -> (01,02)"));

      Assert.assertFalse("The game should not be over", starController.isGameOver());
    }

    public List<Move> getPlayer0BaseSize1StartingMoves() {

        // Liste des Moves de départ expectés du joueur 0 BaseSize 1
        List<Move> player0MovesArray = new ArrayList<>();

        // Ajoute chaque Move à la liste du joueur
        player0MovesArray.add(new Move(
                new Field(0,3),
                new Field(1,2),
                false));

        player0MovesArray.add(new Move(
                new Field(0,3),
                new Field(1,4),
                false));

        return player0MovesArray;
    }

    public List<Move> getPlayer1BaseSize1StartingMoves() {

        // Liste des Moves de départ expectés du joueur 1 BaseSize 1
        List<Move> player1MovesArray = new ArrayList<>();

        // Ajoute chaque Move à la liste du joueur
        player1MovesArray.add(new Move(
                new Field(3,0),
                new Field(2,1),
                false));

        player1MovesArray.add(new Move(
                new Field(3,0),
                new Field(3,2),
                false));

        return player1MovesArray;
    }

    public List<Move> getPlayer0BaseSize2StartingMoves() {

        // Liste des Moves de départ expectés du joueur 0 BaseSize 2
        List<Move> player0MovesArray = new ArrayList<>();

        // Ajoute chaque Move à la liste du joueur
        player0MovesArray.add(new Move(
                new Field(1,5),
                new Field(2,4),
                false));

        player0MovesArray.add(new Move(
                new Field(1,5),
                new Field(2,6),
                false));

        player0MovesArray.add(new Move(
                new Field(0,6),
                new Field(2,4),
                true));

        player0MovesArray.add(new Move(
                new Field(0,6),
                new Field(2,8),
                true));

        player0MovesArray.add(new Move(
                new Field(1,7),
                new Field(2,6),
                false));

        player0MovesArray.add(new Move(
                new Field(1,7),
                new Field(2,8),
                false));

        return player0MovesArray;
    }

    public List<Move> getPlayer1BaseSize2StartingMoves() {

        // Liste des Moves de départ expectés du joueur 1 BaseSize 2
        List<Move> player1MovesArray = new ArrayList<>();

        // Ajoute chaque Move à la liste du joueur
        player1MovesArray.add(new Move(
                new Field(6,0),
                new Field(4,2),
                true));

        player1MovesArray.add(new Move(
                new Field(6,0),
                new Field(6,4),
                true));

        player1MovesArray.add(new Move(
                new Field(5,1),
                new Field(4,2),
                false));

        player1MovesArray.add(new Move(
                new Field(5,1),
                new Field(5,3),
                false));

        player1MovesArray.add(new Move(
                new Field(6,2),
                new Field(5,3),
                false));

        player1MovesArray.add(new Move(
                new Field(6,2),
                new Field(6,4),
                false));

        return player1MovesArray;
    }

    @Test
    public void testGetPlayerMovesPlayer0BaseSize1Move1() {

        // Création d'une base de size 1
        getDefaultControllerBaseX(1);

        // Liste des Move expectés du joueur 0 lors du début de partie
        List<Move> player0ExpectedMoves = getPlayer0BaseSize1StartingMoves();

        // Liste des Move du joueur 0 lors du début de partie
        List<Move> player0Moves = starController.getPlayerMoves();

        // Les 2 listes des Move sont de même taille
        Assert.assertEquals(player0ExpectedMoves.size(), player0Moves.size());

        // Chaque Move des 2 listes s'équivalent
        // Pour chaque Move expecté
        for (Move player0ExpectedMove : player0ExpectedMoves) {
            boolean foundMove = false;

            // Lorsqu'un Move équivalent a été trouvé
            Assert.assertTrue(player0Moves.contains(player0ExpectedMove));
        }

    }

    @Test
    public void testGetPlayerMovesPlayer1BaseSize1Move1() {

        // Création d'une base de size 1
        getDefaultControllerBaseX(1);

        // move du joueur 0
        // changer au joueur 1
        Move player0Move = new Move(new Field(0,3), new Field(1,4), false);
        starController.performMove(player0Move);

        // Liste des Move expectés du joueur 1 lors du début de partie
        List<Move> player1ExpectedMoves = getPlayer1BaseSize1StartingMoves();

        // Liste des Move trouvés du joueur 1 lors du début de partie
        List<Move> player1Moves = starController.getPlayerMoves();

        // Les 2 listes des Move sont de même taille
        Assert.assertEquals(player1ExpectedMoves.size(), player1Moves.size());

        // Chaque Move des 2 listes s'équivalent
        // Pour chaque Move expecté
        for (Move player1ExpectedMove : player1ExpectedMoves) {
            boolean foundMove = false;

            // Lorsqu'un Move équivalent a été trouvé
            Assert.assertTrue(player1Moves.contains(player1ExpectedMove));
        }

    }

    @Test
    public void testGetPlayerMovesPlayer0BaseSize2Move1() {

        // Création d'une base de size 2
        getDefaultControllerBaseX(2);

        // Liste des Move expectés du joueur 0 lors du début de partie
        List<Move> player0ExpectedMoves = getPlayer0BaseSize2StartingMoves();

        // Liste des Move trouvés du joueur 0 lors du début de partie
        List<Move> player0Moves = starController.getPlayerMoves();

        // Les 2 listes des Move sont de même taille
        Assert.assertEquals(player0ExpectedMoves.size(), player0Moves.size());

        // Chaque Move des 2 listes s'équivalent
        // Pour chaque Move expecté
        for (Move player0ExpectedMove : player0ExpectedMoves) {
            boolean foundMove = false;

            // Lorsqu'un Move équivalent a été trouvé
            Assert.assertTrue(player0Moves.contains(player0ExpectedMove));
        }

    }

    @Test
    public void testGetPlayerMovesPlayer1BaseSize2Move1() {

        // Création d'une base de size 2
        getDefaultControllerBaseX(2);

        // move du joueur 0
        // changer au joueur 1
        Move player0Move = new Move(new Field(1,7), new Field(2,8), false);
        starController.performMove(player0Move);

        // Liste des Move expectés du joueur 1 lors du début de partie
        List<Move> player1ExpectedMoves = getPlayer1BaseSize2StartingMoves();

        // Liste des Move trouvés du joueur 1 lors du début de partie
        List<Move> player1Moves = getPlayer1BaseSize2StartingMoves();

        // Les 2 listes des Move sont de même taille
        Assert.assertEquals(player1ExpectedMoves.size(), player1Moves.size());

        // Chaque Move des 2 listes s'équivalent
        // Pour chaque Move expecté
        for (Move player1ExpectedMove : player1ExpectedMoves) {
            boolean foundMove = false;

            // Lorsqu'un Move équivalent a été trouvé
            Assert.assertTrue(player1Moves.contains(player1ExpectedMove));
        }

    }

    @Test
    public void testGetPlayerMovesPlayer0BaseSize2AfterJump() {

        // Création d'une base de size 2
        getDefaultControllerBaseX(2);

        // jump du joueur 0
        // changer au joueur 1
        Move player0Jump = new Move(new Field(0,6), new Field(2,8), true);
        starController.performMove(player0Jump);

        // Il devrait pouvoir rejouer
        // Liste expectée de son seul Move possible
        List<Move> player0ExpectedMoves = new ArrayList<>();
        player0ExpectedMoves.add(
                new Move (new Field(2,8), new Field(2,8), false));
//        player0ExpectedMoves.add(
//            new Move (new Field(2,8), new Field(0,6), true));

        // Liste trouvée de ses Move
        List<Move> player0Moves = starController.getPlayerMoves();

        // Les 2 listes des Move sont de même taille
        Assert.assertEquals(player0ExpectedMoves.size(), player0Moves.size());

        // Chaque Move des 2 listes s'équivalent
        // Pour chaque Move expecté
        for (Move player0ExpectedMove : player0ExpectedMoves) {
            boolean foundMove = false;

            // Lorsqu'un Move équivalent a été trouvé
            Assert.assertTrue(player0Moves.contains(player0ExpectedMove));
        }
    }

//    @Test
//    public void testGetPlayerMovesPlayer0BaseSize2MultipleJump() {
//
//        // Création d'une base de size 2
//        getDefaultControllerBaseX(2);
//
//        // Va chercher le model
//        ModelReadOnly model = starController.getModel();
//
//        // Premier move du joueur 0
//        starController.performMove(new Move(new Field(1,5), new Field(2,6), false));
//
//        // Premier move du joueur 1
//        starController.performMove(new Move(new Field(6,0), new Field(4,2), true));
//
//        // Deuxième move du joueur 1
//        starController.performMove(new Move(new Field(4,2), new Field(4,2), false));
//
//        // Premier move du joueur 2
//        starController.performMove(new Move(new Field(6,10), new Field(6,8), false));
//
//        // Premier jump du joueur 0 (Deuxieme Move)
//        starController.performMove(new Move(new Field(6,0), new Field(6,4), true));
//
//        // Creation de la liste des Move expectés du joueur 0
//        List<Move> player0ExpectedMoves = new ArrayList<>();
//
//        // Ajoute chaque Move à la liste du joueur
//        player0ExpectedMoves.add(new Move(
//                new Field(6,4),
//                new Field(6,4),
//                false));
//
////        player0ExpectedMoves.add(new Move(
////                new Field(2,8),
////                new Field(2,4),
////                true));
//
//        // Liste des Move trouvés du joeur 0
//        List<Move> player0Moves = starController.getPlayerMoves();
//
//        // Les 2 listes des Move sont de même taille
//        Assert.assertEquals(player0ExpectedMoves.size(), player0Moves.size());
//
//        // Chaque Move des 2 listes s'équivalent
//        // Pour chaque Move expecté
//        for (Move player0ExpectedMove : player0ExpectedMoves) {
//
//            // Lorsqu'un Move équivalent a été trouvé
//            Assert.assertTrue(player0Moves.contains(player0ExpectedMove));
//        }
//
//
//    }

    @Test
    public void testPerformMovePlayer0BaseSize2Move1() {

        // Création d'une base de size 2
        getDefaultControllerBaseX(2);

        // Va chercher le model
        ModelReadOnly model = starController.getModel();

        // Va chercher les Fields du joueur 0
        Set<Field> playerFields = model.getPlayerFields(0);

        // Creation des Fields expectés du joueur 0 avant un Move
        Set<Field> expectedPlayerFields = new HashSet<>();
        expectedPlayerFields.add(new Field(0,6));
        expectedPlayerFields.add(new Field(1,5));
        expectedPlayerFields.add(new Field(1,7));

        // Fields expectés et trouvés avant le Move sont égaux
        Assert.assertEquals(playerFields, expectedPlayerFields);

        // Fait le Move avec le joueur 0
        starController.performMove(new Move(new Field(1,5), new Field(2,4), false));

        // Va chercher les Fields du joueur 0 après le Move
        playerFields = model.getPlayerFields(0);

        // Creation des Fields expectés du joueur 0 après un Move
        Set<Field> expectedPlayerFieldsAfterMove = new HashSet<>();
        expectedPlayerFieldsAfterMove.add(new Field(0,6));
        expectedPlayerFieldsAfterMove.add(new Field(2,4));
        expectedPlayerFieldsAfterMove.add(new Field(1,7));

        // Fields expectés et trouvés après le Move sont égaux
        Assert.assertEquals(expectedPlayerFieldsAfterMove, playerFields);

    }

    @Test
    public void testPerformMovePlayer0BaseSize2Jump() {

        // Création d'une base de size 2
        getDefaultControllerBaseX(2);

        // Va chercher le model
        ModelReadOnly model = starController.getModel();

        // Va chercher les Fields du joueur 0
        Set<Field> playerFields = model.getPlayerFields(0);

        // Creation des Fields expectés du joueur 0 avant un Move
        Set<Field> expectedPlayerFields = new HashSet<>();
        expectedPlayerFields.add(new Field(0,6));
        expectedPlayerFields.add(new Field(1,5));
        expectedPlayerFields.add(new Field(1,7));

        // Fields expectés et trouvés avant le Move sont égaux
        Assert.assertEquals(playerFields, expectedPlayerFields);

        // Fait le Move avec le joueur 0
        starController.performMove(new Move(new Field(0,6), new Field(2,8), true));

        // Va chercher les Fields du joueur 0 après le Move
        playerFields = model.getPlayerFields(0);

        // Creation des Fields expectés du joueur 0 après un Move
        Set<Field> expectedPlayerFieldsAfterMove = new HashSet<>();
        expectedPlayerFieldsAfterMove.add(new Field(1,5));
        expectedPlayerFieldsAfterMove.add(new Field(1,7));
        expectedPlayerFieldsAfterMove.add(new Field(2,8));

        // Fields expectés et trouvés après le Move sont égaux
        Assert.assertEquals(expectedPlayerFieldsAfterMove, playerFields);

    }

    @Test
    public void testPerformMovePlayer0BaseSize2MultipleJump() {

        // Création d'une base de size 2
        getDefaultControllerBaseX(2);

        // Va chercher le model
        ModelReadOnly model = starController.getModel();

        // Va chercher les Fields du joueur 0
        Set<Field> playerFields = model.getPlayerFields(0);

        // Creation des Fields expectés du joueur 0 avant un Move
        Set<Field> expectedPlayerFields = new HashSet<>();
        expectedPlayerFields.add(new Field(0,6));
        expectedPlayerFields.add(new Field(1,5));
        expectedPlayerFields.add(new Field(1,7));

        // Fields expectés et trouvés avant le Move sont égaux
        Assert.assertEquals(playerFields, expectedPlayerFields);

        // Premier move du joueur 0
        starController.performMove(new Move(new Field(1,5), new Field(2,6), false));

        // Premier move du joueur 1
        starController.performMove(new Move(new Field(6,2), new Field(6,4), false));

        // Premier move du joueur 2
        starController.performMove(new Move(new Field(6,10), new Field(6,8), false));

        // Premier jump du joueur 0
        starController.performMove(new Move(new Field(0,6), new Field(2,8), true));

        // Deuxième jump du joueur 0
        starController.performMove(new Move(new Field(2,8), new Field(2,4), true));

        // Va chercher les Fields du joueur 0 après les Moves
        playerFields = model.getPlayerFields(0);

        // Creation des Fields expectés du joueur 0 après les Moves
        Set<Field> expectedPlayerFieldsAfterMultipleJump = new HashSet<>();
        expectedPlayerFieldsAfterMultipleJump.add(new Field(2,6));
        expectedPlayerFieldsAfterMultipleJump.add(new Field(2,4));
        expectedPlayerFieldsAfterMultipleJump.add(new Field(1,7));

        // Fields expectés et trouvés après les Moves sont égaux
        Assert.assertEquals(playerFields, expectedPlayerFieldsAfterMultipleJump);

    }

    @Test(
            expected = IllegalMoveException.class
    )
    public void testPerformMovePlayer0BaseSize2IncorrectMove() {

        // Création d'une base de size 2
        getDefaultControllerBaseX(2);

        // Va chercher les Moves possibles du joueur 0
        List<Move> player0Moves = starController.getPlayerMoves();

        // Fait un Move qui n'est pas dans la liste
        starController.performMove(new Move(new Field(1,5), new Field(3,3), false));

    }


}
