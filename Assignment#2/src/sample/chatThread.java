package sample;

import java.net.Socket;

/**
 * Created by christien on 18/03/17.
 */
public class chatThread implements Runnable {

    private Socket userSocket = null;


    public chatThread(Socket socket){

        this.userSocket = socket;

    }


    public void run(){



    }


}
