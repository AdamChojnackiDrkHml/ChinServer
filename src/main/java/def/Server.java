package def;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A server for a multi-player Chinese checkers game.
 */
public class Server 
{
    public static void main(String[] args) throws Exception 
    {
        try (var listener = new ServerSocket(58900))
        {
            System.out.println("Chinese Checkers Server is Running...");
            ExecutorService pool = Executors.newFixedThreadPool(200);
            while (true) 
            {
                Game game = new Game();
                game.players = Integer.parseInt(args[0]);
                if (game.players == 2)
                {
                	 pool.execute(game.new Player(listener.accept(), 1));
                     pool.execute(game.new Player(listener.accept(), 2));	
                }
                else if (game.players == 3)
                {
               	 	pool.execute(game.new Player(listener.accept(), 1));
                    pool.execute(game.new Player(listener.accept(), 2));	
                    pool.execute(game.new Player(listener.accept(), 3));
                }
                else if (game.players == 4)
                {
               	 	pool.execute(game.new Player(listener.accept(), 1));
                    pool.execute(game.new Player(listener.accept(), 2));	
                    pool.execute(game.new Player(listener.accept(), 3));
                    pool.execute(game.new Player(listener.accept(), 4));
                }
                else if (game.players == 6)
                {
               	 	pool.execute(game.new Player(listener.accept(), 1));
                    pool.execute(game.new Player(listener.accept(), 2));	
                    pool.execute(game.new Player(listener.accept(), 3));
                    pool.execute(game.new Player(listener.accept(), 4));
                    pool.execute(game.new Player(listener.accept(), 5));	
                    pool.execute(game.new Player(listener.accept(), 6));
                }
            }
        }
    }
}