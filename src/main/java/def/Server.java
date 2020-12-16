package def;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.Executors;

/**
 * A server for a multi-player Chinese checkers game.
 */
public class Server 
{
    public static void main(String[] args) throws Exception 
    {
        try (var listener = new ServerSocket(58901)) 
        {	
            System.out.println("Chinese checkers server is running...");
            var pool = Executors.newFixedThreadPool(200);
            while (true) 
            {
                Game game = new Game();
                game.players = Integer.parseInt(args[0]);
                pool.execute(game.new Player(listener.accept(), 'X'));
                pool.execute(game.new Player(listener.accept(), 'O'));
            }
        }
    }
}

