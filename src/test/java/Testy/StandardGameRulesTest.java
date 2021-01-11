package Testy;

import static org.junit.Assert.*;
import def.*;
import org.junit.Test;

public class StandardGameRulesTest
{
    /**
     * This test checks if board is set correctly for given number of players
     */
    @Test
    public void setUpBoardTest()
    {
        GameRulesInterface rules = new StandardGameRules();
        PlayerId[][] gameBoard = new PlayerId[17][17];
        gameBoard = rules.setBoardForSixPlayers(gameBoard);
        assertEquals(gameBoard[0][8], PlayerId.FOUR);

        gameBoard = new PlayerId[17][17];
        gameBoard = rules.setBoardForThreePlayers(gameBoard);
        assertNull(gameBoard[0][8]);
        assertNotEquals(gameBoard[16][8], PlayerId.NULL);

        gameBoard = new PlayerId[17][17];
        gameBoard = rules.setBoardForTwoPlayers(gameBoard);
        assertNotNull(gameBoard[0][8]);
        assertNull(gameBoard[12][2]);

        gameBoard = new PlayerId[17][17];
        gameBoard = rules.setBoardForFourPlayers(gameBoard);
        assertNull(gameBoard[4][2]);
        assertNotEquals(gameBoard[4][13], PlayerId.NULL);
    }

}
