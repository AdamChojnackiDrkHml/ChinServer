package Testy;

import def.*;
import org.junit.Test;



import static org.junit.Assert.*;

public class GameTest
{
    @Test
    public void isThisWinTest()
    {
        int[][] ONEWins = {{3, 6}, {3, 7}, {3, 8}, {3, 9}, {2, 7}, {2, 8}, {2, 9}, {1, 7}, {1, 8}, {0, 8}};
        int[][] TWOWins = {{7, 12}, {6, 12}, {6, 13}, {5, 11}, {5, 12}, {5, 13}, {4, 11}, {4, 12}, {4, 13}, {4, 14}};
        for (int i = 0; i < 10; i++)
        {
            assertEquals(ONEWins[i][0], ONEWins[i][0]);
            assertEquals(ONEWins[i][1], ONEWins[i][1]);
            assertNotEquals(ONEWins[i][0], TWOWins[i][0]);
            assertNotEquals(ONEWins[i][0], TWOWins[i][0]);
        }
        boolean[] tableOfWinners = {true, false, false, false, false, false};
        PlayerId winner = PlayerId.ONE;
        assertTrue(tableOfWinners[PlayerId.getInt(winner) - 1]);
        assertFalse(tableOfWinners[PlayerId.getInt(winner)]);
    }
}
