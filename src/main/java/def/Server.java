package def;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A server for a multi-player Chinese checkers game.
 */
public class Server 
{
	/**
	 * Method main is responsible for adding right number of players connected to server.
	 * @param args is the number of players
	 * @throws Exception
	 */
    public static void main(String[] args) throws Exception 
    {
        try (var listener = new ServerSocket(58901))
        {
            System.out.println("Chinese Checkers Server is Running...");
            ExecutorService pool = Executors.newFixedThreadPool(200);

                Game game = new Game(NumberOfPlayers.getFromIntString(args[0]));
                switch (game.getNumberOfPlayers())
                {
                    case TWO:
                    {
                        pool.execute(game.new Player(listener.accept(), PlayerId.ONE));
                        pool.execute(game.new Player(listener.accept(), PlayerId.FOUR));
                        break;
                    }
                    case THREE:
                    {
                        pool.execute(game.new Player(listener.accept(), PlayerId.ONE));
                        pool.execute(game.new Player(listener.accept(), PlayerId.THREE));
                        pool.execute(game.new Player(listener.accept(), PlayerId.FIVE));
                        break;
                    }
                    case FOUR:
                    {
                        pool.execute(game.new Player(listener.accept(), PlayerId.ONE));
                        pool.execute(game.new Player(listener.accept(), PlayerId.TWO));
                        pool.execute(game.new Player(listener.accept(), PlayerId.FOUR));
                        pool.execute(game.new Player(listener.accept(), PlayerId.FIVE));
                        break;
                    }
                    case SIX:
                    {
                        pool.execute(game.new Player(listener.accept(), PlayerId.ONE));
                        pool.execute(game.new Player(listener.accept(), PlayerId.TWO));
                        pool.execute(game.new Player(listener.accept(), PlayerId.THREE));
                        pool.execute(game.new Player(listener.accept(), PlayerId.FOUR));
                        pool.execute(game.new Player(listener.accept(), PlayerId.FIVE));
                        pool.execute(game.new Player(listener.accept(), PlayerId.SIX));
                        break;
                    }
                }

        }
    }
}