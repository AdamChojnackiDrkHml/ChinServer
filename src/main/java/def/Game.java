package def;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

class Game
{
	private PlayerId[][] board = new PlayerId[17][17];
	private StandardGamePools standardGamePools = new StandardGamePools();
    private ArrayList<Player> playersList = new ArrayList<>();
    Player currentPlayer;
	public int players;
	private PlayerId winner = PlayerId.ZERO;
	int[][] ONEWins = {{3, 6}, {3, 7}, {3, 8}, {3, 9}, {2, 7}, {2, 8}, {2, 9}, {1, 7}, {1, 8}, {0, 8}};
	int[][] TWOWins = {{7, 12}, {6, 12}, {6, 13}, {5, 11}, {5, 12}, {5, 13}, {4, 11}, {4, 12}, {4, 13}, {4, 14}};
	int[][] THREEWins = {{12, 11}, {12, 12}, {12, 13}, {12, 14}, {11, 11}, {11, 12}, {11, 13}, {10, 12}, {10, 13}, {9, 12}};
	int[][] FOURWins = {{16, 8}, {15, 7}, {15, 8}, {14, 7}, {14, 8}, {14, 9}, {13, 6}, {13, 7}, {13, 8}, {13, 9}};
	int[][] FIVEWins = {{12, 2}, {12, 3}, {12, 4}, {12, 5}, {11, 2}, {11, 3}, {11, 4}, {10, 3}, {10, 4}, {9, 3}};
	int[][] SIXWins = {{7, 3}, {6, 3}, {6, 4}, {5, 2}, {5, 3}, {5, 4}, {4, 2}, {4, 3}, {4, 4}, {4, 5}};

	private void choosePools(int numOfPlayers)
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
        board = standardGamePools.setUpBoardForPlayers(numOfPlayers, board);
    }
	
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
	
    public boolean hasWinner()
    {
    	int temp1 = 0;
    	int temp2 = 0;
    	int temp3 = 0;
    	int temp4 = 0;
    	int temp5 = 0;
    	int temp6 = 0;
    	
    	if (players == 2)
    	{
    		temp1 = 1;
    		temp4 = 1;
    	}
    	else if (players == 3)
    	{
    		temp1 = 1;
    		temp3 = 1;
    		temp5 = 1;
    	}
    	else if (players == 4)
    	{
    		temp1 = 1;
    		temp2 = 1;
    		temp4 = 1;
    		temp5 = 1;
    	}
    	else if (players == 6)
    	{
    		temp1 = 1;
        	temp2 = 1;
        	temp3 = 1;
        	temp4 = 1;
        	temp5 = 1;
        	temp6 = 1;
    	}
    	
    	for (int i = 0; i < 10; i++)
    	{
    		if (board[ONEWins[i][0]][ONEWins[i][1]] != PlayerId.ONE)
    		{
    			temp1 = 0;
    		}    
    		if (board[TWOWins[i][0]][TWOWins[i][1]] != PlayerId.TWO)
    		{
    			temp2 = 0;
    		}
    		if (board[THREEWins[i][0]][THREEWins[i][1]] != PlayerId.THREE)
    		{
    			temp3 = 0;
    		} 
    		if (board[FOURWins[i][0]][FOURWins[i][1]] != PlayerId.FOUR)
    		{
    			temp4 = 0;
    		} 
    		if (board[FIVEWins[i][0]][FIVEWins[i][1]] != PlayerId.FIVE)
    		{
    			temp5 = 0;
    		} 
    		if (board[SIXWins[i][0]][SIXWins[i][1]] != PlayerId.SIX)
    		{
    			temp6 = 0;
    		} 
       	}
    	
    	if (temp1 == 1)
    	{
    		winner = PlayerId.ONE;
    	}
    	else if (temp2 == 1)
    	{
    		winner = PlayerId.TWO;
    	}
    	else if (temp3 == 1)
    	{
    		winner = PlayerId.THREE;
    	}
    	else if (temp4 == 1)
    	{
    		winner = PlayerId.FOUR;
    	}
    	else if (temp5 == 1)
    	{
    		winner = PlayerId.FIVE;
    	}
    	else if (temp6 == 1)
    	{
    		winner = PlayerId.SIX;
    	}
        
    	if (temp1 == 1 || temp2 == 1 || temp3 == 1 || temp4 == 1 || temp5 == 1 || temp6 == 1)
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }

    public void notifyAllSockets(String message)
    {
        for(Player player : playersList)
        {
            player.output.println(message);
        }
    }

    /**
     * A Player is identified by a number. For
     * communication with the client the player has a socket and associated Scanner
     * and PrintWriter.
     */
    class Player implements Runnable
    {
        int number;
        Player opponent;
        Socket socket;
        Scanner input;
        PrintWriter output;

        public Player(Socket socket, int number)
        {
            this.socket = socket;
            this.number = number;
        }

        @Override
        public void run()
        {
            try
            {
                setup();
                processCommands();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (this.opponent != null && this.opponent.output != null)
                {
                    opponent.output.println("OTHER_PLAYER_LEFT");
                }
                try
                {
                    socket.close();
                }
                catch (IOException e)
                {
                }
            }
        }

        private void setup() throws IOException
        {
            input = new Scanner(socket.getInputStream());
            output = new PrintWriter(socket.getOutputStream(), true);
            choosePools(players);
            playersList.add(this);
            if (players == 2)
            {
            	output.println("TWO");
            	if (number == 1)
                {
                    currentPlayer = this;
                    output.println("ONE");
                }
                else if (number == 2)
                {
                    opponent = currentPlayer;
                    opponent.opponent = this;
                    output.println("FOUR");
                }
            }
            else if (players == 3)
            {
            	output.println("THREE");
            	if (number == 1)
                {
                    currentPlayer = this;
                    output.println("ONE");
                }
                else if (number == 2)
                {
                    currentPlayer.opponent = this;
                    output.println("THREE");
                }
                else if (number == 3)
                {
                	currentPlayer.opponent.opponent = this;
                	this.opponent = currentPlayer;
                	output.println("FIVE");
                }
            }
            else if (players == 4)
            {
            	output.println("FOUR");
            	if (number == 1)
                {
                    currentPlayer = this;
                    output.println("ONE");
                }
                else if (number == 2)
                {
                    currentPlayer.opponent = this;
                    output.println("TWO");
                }
                else if (number == 3)
                {
                	currentPlayer.opponent.opponent = this;
                	output.println("FOUR");
                }
                else if (number == 4)
                {
                	currentPlayer.opponent.opponent.opponent = this;
                	this.opponent = currentPlayer;
                	output.println("FIVE");
                }
            }
            else if (players == 6)
            {
            	output.println("SIX");
            	if (number == 1)
                {
                    currentPlayer = this;
                    output.println("ONE");
                }
                else if (number == 2)
                {
                    currentPlayer.opponent = this;
                    output.println("TWO");
                }
                else if (number == 3)
                {
                	currentPlayer.opponent.opponent = this;
                	output.println("THREE");
                }
                else if (number == 4)
                {
                	currentPlayer.opponent.opponent.opponent = this;
                	output.println("FOUR");
                }
                else if (number == 5)
                {
                	currentPlayer.opponent.opponent.opponent.opponent = this;
                	output.println("FIVE");
                }
                else if (number == 6)
                {
                	currentPlayer.opponent.opponent.opponent.opponent.opponent = this;
                	this.opponent = currentPlayer;
                	output.println("SIX");
                }
            }
        }

        private void processCommands()
        {
        	if (input.nextLine().equals("YOUR_MOVE"))
        	{
        		currentPlayer.output.println("YOUR_MOVE");
        	}
        	
        	while (!hasWinner())
        	{
        		while (input.hasNextLine())
        		{
        			String command = input.nextLine();
        			if (command.startsWith("QUIT"))
        			{
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
        		}
        	}
        	String win = "WINNER IS PLAYER " + winner;
        	notifyAllSockets(win);
        	System.out.println(win);
        }
    }

}