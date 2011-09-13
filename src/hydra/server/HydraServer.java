package hydra.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

public class HydraServer extends Thread
{
    private Integer port = 1337;
    
    private ServerSocket socket = null;
    private Thread listeningThread;
    
    private Object lock = new Object();
    private HashSet<HydraServerConnection> activeConnections = new HashSet<HydraServerConnection>();
    
    public void startServer() throws IOException
    {
        this.socket = new ServerSocket(this.port);
        this.listeningThread = this;
        this.start();
        System.out.println("Hydra server started on port: "+this.port);
    }
    
    public void run()
    {
        try
        {
            ServerSocket s = this.socket;
            while (listeningThread == Thread.currentThread()) {
                try {
                    Socket socket = s.accept();
                    HydraServerConnection requestThread = new HydraServerConnection(this, socket);
                    synchronized(lock) {
                        activeConnections.add(requestThread);
                        requestThread.start();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}