package def;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class game is responsible for setting up the board for right number of players
 * and checking if someone won the game.
 */
class Game
{
	private PlayerId[][] board = new PlayerId[17][17];
	private StandardGamePools standardGamePools = new StandardGamePools();
    private ArrayList<Player> playersList = new ArrayList<>();
    Player currentPlayer;
	private PlayerId winner = PlayerId.ZERO;
	private int place = 1;
	private NumberOfPlayers numberOfPlayers;
	/**
	 * Array tableOfWinners shows which players have already won the game.
	 */
	private boolean[] tableOfWinners = {false, false, false, false, false, false};
	
	/**
	 * Arrays with winning conditions.
	 */
	int[][] ONEWins = {{3, 6}, {3, 7}, {3, 8}, {3, 9}, {2, 7}, {2, 8}, {2, 9}, {1, 7}, {1, 8}, {0, 8}};
	int[][] TWOWins = {{7, 12}, {6, 12}, {6, 13}, {5, 11}, {5, 12}, {5, 13}, {4, 11}, {4, 12}, {4, 13}, {4, 14}};
	int[][] THREEWins = {{12, 11}, {12, 12}, {12, 13}, {12, 14}, {11, 11}, {11, 12}, {11, 13}, {10, 12}, {10, 13}, {9, 12}};
	int[][] FOURWins = {{16, 8}, {15, 7}, {15, 8}, {14, 7}, {14, 8}, {14, 9}, {13, 6}, {13, 7}, {13, 8}, {13, 9}};
	int[][] FIVEWins = {{12, 2}, {12, 3}, {12, 4}, {12, 5}, {11, 2}, {11, 3}, {11, 4}, {10, 3}, {10, 4}, {9, 3}};
	int[][] SIXWins = {{7, 3}, {6, 3}, {6, 4}, {5, 2}, {5, 3}, {5, 4}, {4, 2}, {4, 3}, {4, 4}, {4, 5}};


	public Game(NumberOfPlayers numberOfPlayers)
	{
		this.numberOfPlayers = numberOfPlayers;
	}
	/**
	 * Method choosePools sets up the board for @param numOfPlayers players.
	 */
	private void choosePools()
    {
        for(int i = 0; i < 17; i++)
        {
            for(int j = 0; j < 17; j++)
            {
                if(isThisValidPool(j,i))
                {
                    board[i][j] = PlayerId.ZERO;
                }
                else
                {
                    board[i][j] = PlayerId.NULL;
                }
            }
        }
        board = standardGamePools.setUpBoardForPlayers(numberOfPlayers, board);
    }
	
	/**
	 * @param xCord
	 * @param yCord	 
	 * Method isThisValidPool checks if a field can be used in a game.
	 */
	private boolean isThisValidPool(int xCord, int yCord)
    {
        return !(((yCord == 13 || yCord == 3) && (xCord > 9 || xCord < 6)) ||
                ((yCord == 14 || yCord == 2) && (xCord > 9 || xCord < 7)) ||
                ((yCord == 15 || yCord == 1) && (xCord > 8 || xCord < 7)) ||
                ((yCord == 16 || yCord == 0) && (xCord != 8)) ||
                (xCord < 2) ||
                (xCord > 14) ||
                (xCord == 2 && (yCord > 5 && yCord < 11 )) ||
                (xCord == 3 && yCord == 8) ||
                (xCord == 14 && yCord > 4 && yCord < 12) ||
                (xCord == 13 && yCord > 6 && yCord < 10));
    }
	
	/**
	 * Method isThisWin checks if someone won the game.
	 * If yes, @return true, unless n-1 players have already won.
	 * Array victoryPools assigns array with winning conditions to the right player.
	 * When someone wins, isThisWin() adds them to array tableOfWinners.
	 */
	public boolean isThisWin()
	{
		int[][] victoryPools = new int[10][2];
		switch(currentPlayer.playerId)
		{
			case ONE:
			{
				victoryPools = ONEWins;
				break;
			}
			case TWO:
			{
				victoryPools = TWOWins;
				break;
			}
			case THREE:
			{
				victoryPools = THREEWins;
				break;
			}
			case FOUR:
			{
				victoryPools = FOURWins;
				break;
			}
			case FIVE:
			{
				victoryPools = FIVEWins;
				break;
			}
			case SIX:
			{
				victoryPools = SIXWins;
				break;
			}
		}

		for(int i = 0; i < 10; i++)
		{
			if(board[victoryPools[i][0]][victoryPools[i][1]] != currentPlayer.playerId)
			{
				return false;
			}
		}
		winner = currentPlayer.playerId;
		if(tableOfWinners[PlayerId.getInt(currentPlayer.playerId) - 1])
		{
			return false;
		}
		tableOfWinners[PlayerId.getInt(currentPlayer.playerId) - 1] = true;
		return true;
	}

    /**
     * Method notifyAllsockets is responsible for passing the message from server to all clients.
     * @param message is string passed to clients
     */
    public void notifyAllSockets(String message)
    {
        for(Player player : playersList)
        {
            player.output.println(message);
        }
    }

	public NumberOfPlayers getNumberOfPlayers()
	{
		return numberOfPlayers;
	}

	/**
     * A Player is identified by a number. 
     * For communication with the client the player has a socket,
     * associated Scanner and PrintWriter.
     */
    class Player implements Runnable
    {
        PlayerId playerId;
        Player opponent;
        Socket socket;
        Scanner input;
        PrintWriter output;

        public Player(Socket socket, PlayerId playerId)
        {
            this.socket = socket;
            this.playerId = playerId;
        }

        /**
         * Method run is responsible for processing the game.
         * At first it sets up the game.
         * When game is finished run() closes it.
         */
        @Override
        public void run()
        {
            try
            {
                setup();
                while (input.hasNextLine())
				{
					processCommands();
				}
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
            	notifyAllSockets("QUIT");

                try
                {
                    socket.close();
                }
                catch (IOException e)
                {
                }
            }
        }

        /**
         * Method setup is responsible for setting up the board for the right number of players.
         * It sets every next player as the opponent of previous one.
         * When last player joins the game, it sends a message that starts the game.
         * @throws IOException
         */
        private void setup() throws IOException
        {
            input = new Scanner(socket.getInputStream());
            output = new PrintWriter(socket.getOutputStream(), true);
            choosePools();
            playersList.add(this);
            switch (numberOfPlayers)
			{
				case TWO:
				{
					output.println("TWO");
					switch (playerId)
					{
						case ONE:
						{
							currentPlayer = this;
							output.println("ONE");
						}
						case TWO:
						{
							opponent = currentPlayer;
							opponent.opponent = this;
							output.println("FOUR");
							currentPlayer.output.println("YOUR_MOVE");
							break;
						}
					}
					break;
				}
				case THREE:
				{
					output.println("THREE");
					switch (playerId)
					{
						case ONE:
						{
							currentPlayer = this;
							output.println("ONE");
							break;
						}
						case THREE:
						{
							currentPlayer.opponent = this;
							output.println("THREE");
							break;
						}
						case FIVE:
						{
							currentPlayer.opponent.opponent = this;
							this.opponent = currentPlayer;
							output.println("FIVE");
							currentPlayer.output.println("YOUR_MOVE");
							break;
						}
					}
					break;
				}
				case FOUR:
				{
					output.println("FOUR");
					switch (playerId)
					{
						case ONE:
						{
							currentPlayer = this;
							output.println("ONE");
							break;
						}
						case TWO:
						{
							currentPlayer.opponent = this;
							output.println("TWO");
							break;
						}
						case FOUR:
						{
							currentPlayer.opponent.opponent = this;
							output.println("FOUR");
							break;
						}
						case FIVE:
						{
							currentPlayer.opponent.opponent.opponent = this;
							this.opponent = currentPlayer;
							output.println("FIVE");
							currentPlayer.output.println("YOUR_MOVE");
							break;
						}
					}
					break;
				}
				case SIX:
				{
					output.println("SIX");
					switch (playerId)
					{
						case ONE:
						{
							currentPlayer = this;
							output.println("ONE");
							break;
						}
						case TWO:
						{
							currentPlayer.opponent = this;
							output.println("TWO");
							break;
						}
						case THREE:
						{
							currentPlayer.opponent.opponent = this;
							output.println("THREE");
							break;
						}
						case FOUR:
						{
							currentPlayer.opponent.opponent.opponent = this;
							output.println("FOUR");
							break;
						}
						case FIVE:
						{
							currentPlayer.opponent.opponent.opponent.opponent = this;
							output.println("FIVE");
							break;
						}
						case SIX:
						{
							currentPlayer.opponent.opponent.opponent.opponent.opponent = this;
							this.opponent = currentPlayer;
							output.println("SIX");
							currentPlayer.output.println("YOUR_MOVE");
							break;
						}
					}
					break;
				}
			}
        }

        /**
         * Method processComands is responsible for processing the game.
         * If received command starts with "QUIT", it ends the game.
         * Else if a command starts with "MOVE", it moves the counter.
         * Else if a command starts with "END", it changes current player to the opponent of previous one.
         * Else it prints the command.
         * If there is a winner, processCommands() sends to all players a message saying who won and prints it.
         */
        private void processCommands()
        {

       			String command = input.nextLine();
       			if (command.startsWith("QUIT"))
       			{
       				notifyAllSockets("QUIT" + input.next());
       				return;
       			}
       			else if (command.startsWith("MOVE"))
       			{
       				PlayerId playerMovedId = PlayerId.valueOf(input.next());
       	            int xBeg = Integer.parseInt(input.next());
       	            int yBeg = Integer.parseInt(input.next());
       	            int xDest = Integer.parseInt(input.next());
       	            int yDest = Integer.parseInt(input.next());

       	            board[yBeg][xBeg] = PlayerId.ZERO;
       	            board[yDest][xDest] = playerMovedId;

       				String x = "MOVE " + playerMovedId.toString() + " " + xBeg + " " + yBeg + " " + xDest + " " + yDest;
       				System.out.println(x);
       				notifyAllSockets(x);
       				System.out.println(x);
       			}
       			else if (command.startsWith("END"))
       			{
       				currentPlayer = currentPlayer.opponent;
       				currentPlayer.output.println("YOUR_MOVE");
       			}
       			else
       			{
       				System.out.println(command);
       			}
       			if(isThisWin())
				{
					String win = "WINNER " + winner.name() + " " + place;
					place++;
					notifyAllSockets(win);
					System.out.println(win);
				}

        }
    }
}