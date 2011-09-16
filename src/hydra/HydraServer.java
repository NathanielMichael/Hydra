package hydra;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashSet;

public class HydraServer extends Thread
{
    private ServerSocket sock = null;
    private Thread listeningThread;
    
    private InetAddress bindAddress;
    private int port;
    private int maxConnections;
    
    private Object lock = new Object();
    private HashSet<HydraServerConnection> activeConnections = new HashSet<HydraServerConnection>();
    
    public HydraServer(int port, int maxConnections)
    {
        //this.bindAddress = bindAddress;
        this.port = port;
        this.maxConnections = maxConnections;
    }
    
    public InetAddress getAddress()
    {
        return this.bindAddress;
    }
    
    public int getPort()
    {
        return this.port;
    }
    
    public void startServer() throws IOException
    {
        this.sock = new ServerSocket(this.port, 50);
        this.listeningThread = this;
        this.start();
        System.out.println("Hydra server started on: " + this.port);
    }
    
    public static void main(String[] args) throws Exception
    {
        HydraServer server = new HydraServer(1337, 100);
        server.startServer();
    }
    
    public void run()
    {
        try {
            ServerSocket s = this.sock;
            while (this.listeningThread == Thread.currentThread()) {
                try {
                    Socket socket = s.accept();
                    HydraServerConnection requestThread = new HydraServerConnection(socket, this);
                    synchronized(lock) {
                        this.activeConnections.add(requestThread);
                        requestThread.start();
                    }
                } catch (Exception e) {
                    
                }
            }
        } catch (Exception e) {
            
        }
    }
    
    public void connectionEnded(HydraServerConnection c)
    {
        synchronized(this.lock) {
            activeConnections.remove(c);
            lock.notifyAll();
        }
    }
    
    public void shutdown()
    {
        System.out.println("Shutting down server...");
        this.listeningThread = null;
        try {
            if (this.sock != null) {
                this.sock.close();
                this.sock = null;
            }
            
            HashSet<HydraServerConnection> sc;
            synchronized(lock) {
                sc = new HashSet<HydraServerConnection>(this.activeConnections);
            }
            for (HydraServerConnection c : sc) {
                c.shutdownConnection();
            }
        } catch (Exception e) {
            
        }
    }
}