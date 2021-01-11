/**
 * @author Elzbieta Wisniewska and Adam Chojnacki
 */
package def;

import def.*;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.jupiter.api.Test;

import def.Game.Player;

/**
 * Tests for class Game.
 */
class GameTest 
{
	@Test
	void isThisWinTest() 
	{
		int[][] ONEWins = {{3, 6}, {3, 7}, {3, 8}, {3, 9}, {2, 7}, {2, 8}, {2, 9}, {1, 7}, {1, 8}, {0, 8}};
		int[][] TWOWins = {{7, 12}, {6, 12}, {6, 13}, {5, 11}, {5, 12}, {5, 13}, {4, 11}, {4, 12}, {4, 13}, {4, 14}};
		int[][] ONEvictoryPools = ONEWins;
		for(int i = 0; i < 10; i++)
		{
			assertEquals(ONEvictoryPools[i][0], ONEWins[i][0]);
			assertEquals(ONEvictoryPools[i][1], ONEWins[i][1]);
			assertNotEquals(ONEvictoryPools[i][0], TWOWins[i][0]);
			assertNotEquals(ONEvictoryPools[i][0], TWOWins[i][0]);
		}
		boolean[] tableOfWinners = {true, false, false, false, false, false};
		PlayerId winner = PlayerId.ONE;
		assertEquals(tableOfWinners[PlayerId.getInt(winner) - 1], true);
		assertNotEquals(tableOfWinners[PlayerId.getInt(winner)], true);
	}
}
