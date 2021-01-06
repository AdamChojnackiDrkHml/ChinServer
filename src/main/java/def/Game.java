package def;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

class Game
{
    // Board cells numbered 0-8, top to bottom, left to right; null if empty
    private Player[] board = new Player[9];
    private ArrayList<Player> playersList = new ArrayList<>();
    Player currentPlayer;
	public int players;
	private int winner;
	int[][] ONEWins = {{16, 8}, {15, 7}, {15, 8}, {14, 7}, {14, 8}, {14, 9}, {13, 6}, {13, 7}, {13, 8}, {13, 9}};
	int[][] TWOWins = {{12, 2}, {12, 3}, {12, 4}, {12, 5}, {11, 2}, {11, 3}, {11, 4}, {10, 3}, {10, 4}, {9, 3}};
	int[][] THREEWins = {{7, 3}, {6, 3}, {6, 4}, {5, 2}, {5, 3}, {5, 4}, {4, 2}, {4, 3}, {4, 4}, {4, 5}};
	int[][] FOURWins = {{3, 6}, {3, 7}, {3, 8}, {3, 9}, {2, 7}, {2, 8}, {2, 9}, {1, 7}, {1, 8}, {0, 8}};
	int[][] FIVEWins = {{7, 12}, {6, 12}, {6, 13}, {5, 11}, {5, 12}, {5, 13}, {4, 11}, {4, 12}, {4, 13}, {4, 14}};
	int[][] SIXWins = {{12, 11}, {12, 12}, {12, 13}, {12, 14}, {11, 11}, {11, 12}, {11, 13}, {10, 12}, {10, 13}, {9, 12}};

    public boolean hasWinner()
    {
    	int temp1 = 1;
    	int temp2 = 1;
    	int temp3 = 1;
    	int temp4 = 1;
    	int temp5 = 1;
    	int temp6 = 1;
    	
    	for (int i = 0; i < 10; i++)
    	{
    		if (game.getGameBoard()[ONEWins[i][0]][ONEWins[i][1]] != "ONE")
    		{
    			temp1 = 0;
    		}    
    		if (game.getGameBoard()[TWOWins[i][0]][TWOWins[i][1]] != "TWO")
    		{
    			temp2 = 0;
    		}
    		if (game.getGameBoard()[THREEWins[i][0]][THREEWins[i][1]] != "THREE")
    		{
    			temp3 = 0;
    		} 
    		if (game.getGameBoard()[FOURWins[i][0]][FOURWins[i][1]] != "FOUR")
    		{
    			temp4 = 0;
    		} 
    		if (game.getGameBoard()[FIVEWins[i][0]][FIVEWins[i][1]] != "FIVE")
    		{
    			temp5 = 0;
    		} 
    		if (game.getGameBoard()[SIXWins[i][0]][SIXWins[i][1]] != "SIX")
    		{
    			temp6 = 0;
    		} 
       	}
        
    }

    public void notifyAllSockets(String message)
    {
        for(Player player : playersList)
        {
            player.output.println(message);
        }
    }
    public boolean boardFilledUp()
    {
        return Arrays.stream(board).allMatch(p -> p != null);
    }

    public synchronized void move(int location, Player player)
    {
        if (player != currentPlayer)
        {
            throw new IllegalStateException("Not your turn");
        } 
        else if (player.opponent == null)
        {
            throw new IllegalStateException("You don't have an opponent yet");
        } 
        else if (board[location] != null)
        {
            throw new IllegalStateException("Cell already occupied");
        }
        board[location] = currentPlayer;
        currentPlayer = currentPlayer.opponent;
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
           // output.println("WELCOME " + number);
            playersList.add(this);
            if (players == 2)
            {
            	output.println("TWO");
            	if (number == 1)
                {
                    currentPlayer = this;
                    output.println("ONE");
         //           output.println("MESSAGE Waiting for opponent to connect");
                }
                else if (number == 2)
                {
                    opponent = currentPlayer;
                    opponent.opponent = this;
                    output.println("FOUR");
          //          opponent.output.println("MESSAGE Your move");
                }
            }
            else if (players == 3)
            {
            	output.println("THREE");
            	if (number == 1)
                {
                    currentPlayer = this;
                    output.println("ONE");
          //          output.println("MESSAGE Waiting for opponents to connect");
                }
                else if (number == 2)
                {
                    currentPlayer.opponent = this;
                    output.println("THREE");
           //         output.println("MESSAGE Waiting for opponents to connect");
                }
                else if (number == 3)
                {
                	currentPlayer.opponent.opponent = this;
                	this.opponent = currentPlayer;
                	output.println("FIVE");
         //       	currentPlayer.output.println("MESSAGE Your move");
                }
            }
            else if (players == 4)
            {
            	output.println("FOUR");
            	if (number == 1)
                {
                    currentPlayer = this;
                    output.println("ONE");
          //          output.println("MESSAGE Waiting for opponents to connect");
                }
                else if (number == 2)
                {
                    currentPlayer.opponent = this;
                    output.println("TWO");
          //          output.println("MESSAGE Waiting for opponents to connect");
                }
                else if (number == 3)
                {
                	currentPlayer.opponent.opponent = this;
                	output.println("FOUR");
             //   	currentPlayer.output.println("MESSAGE Waiting for opponents to connect");
                }
                else if (number == 4)
                {
                	currentPlayer.opponent.opponent.opponent = this;
                	this.opponent = currentPlayer;
                	output.println("FIVE");
           //     	currentPlayer.output.println("MESSAGE Your move");
                }
            }
            else if (players == 6)
            {
            	output.println("SIX");
            	if (number == 1)
                {
                    currentPlayer = this;
                    output.println("ONE");
         //           output.println("MESSAGE Waiting for opponents to connect");
                }
                else if (number == 2)
                {
                    currentPlayer.opponent = this;
                    output.println("TWO");
         //           output.println("MESSAGE Waiting for opponents to connect");
                }
                else if (number == 3)
                {
                	currentPlayer.opponent.opponent = this;
                	output.println("THREE");
           //     	currentPlayer.output.println("MESSAGE Waiting for opponents to connect");
                }
                else if (number == 4)
                {
                	currentPlayer.opponent.opponent.opponent = this;
                	output.println("FOUR");
          //      	currentPlayer.output.println("MESSAGE Waiting for opponents to connect");
                }
                else if (number == 5)
                {
                	currentPlayer.opponent.opponent.opponent.opponent = this;
                	output.println("FIVE");
          //      	currentPlayer.output.println("MESSAGE Waiting for opponents to connect");
                }
                else if (number == 6)
                {
                	currentPlayer.opponent.opponent.opponent.opponent.opponent = this;
                	this.opponent = currentPlayer;
                	output.println("SIX");
           //     	currentPlayer.output.println("MESSAGE Your move");
                }
            }
        }

        private void processCommands()
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
                //	output.println("VALID_MOVE");
                	String x = input.nextLine();
                    System.out.println("dupa" + x);
                	notifyAllSockets(x);
                	System.out.println(x);
                    //processMoveCommand(Integer.parseInt(command.substring(5)));
                }
                else
                {
                    System.out.println(command);
                }
            }
        }

        private void processMoveCommand(int location)
        {
            try
            {
                move(location, this);
                output.println("VALID_MOVE");
                if (hasWinner())
                {
                    output.println("VICTORY");
                    opponent.output.println("DEFEAT");
                }
                else if (boardFilledUp())
                {
                    output.println("TIE");
                    opponent.output.println("TIE");
                }
            }
            catch (IllegalStateException e)
            {
                output.println("MESSAGE " + e.getMessage());
            }
        }
    }

}