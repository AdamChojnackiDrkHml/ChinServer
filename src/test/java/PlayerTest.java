

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import def.Game;
import def.NumberOfPlayers;
import def.PlayerId;
import org.junit.Test;
import java.net.ServerSocket;


import static org.junit.Assert.*;


public class PlayerTest
{
    @Test
    public void setupTest()
    {
        try
        {
            ServerSocket listener = new ServerSocket(58901);
            Socket socket = new Socket("192.168.0.164",58901);
            ExecutorService pool = Executors.newFixedThreadPool(200);
            Game game = new Game(NumberOfPlayers.TWO);
            pool.execute(game.new Player(socket, PlayerId.ONE));
            assertEquals(game.getPlayersList().get(0).getPlayerId(), PlayerId.ONE);


        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
