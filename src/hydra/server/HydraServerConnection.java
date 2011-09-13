package hydra.server;

import hydra.Hydra;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

import bnet.protocol.Entity.EntityId;
import bnet.protocol.Rpc.ProcessId;
import bnet.protocol.authentication.Authentication.LogonRequest;
import bnet.protocol.authentication.Authentication.LogonResponse;
import bnet.protocol.connection.Connection.BindRequest;
import bnet.protocol.connection.Connection.BindResponse;
import bnet.protocol.connection.Connection.BoundService;
import bnet.protocol.connection.Connection.ConnectResponse;

public class HydraServerConnection extends Thread
{
    private Socket socket;
    private HydraServer server;
    private OutputStream out;
    
    public HydraServerConnection(HydraServer server, Socket socket)
    {
        this.socket = socket;
        this.server = server;
    }
    
    public void run()
    {
        InetSocketAddress remoteAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
        System.out.println("Accepted client connection from: "+remoteAddress.getHostName());
        
        try {
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            this.out = socket.getOutputStream();
            
            while (socket.isConnected()) {
                if (inputStream.available() > 0) {
                    byte[] header = new byte[6];
                    inputStream.read(header, 0, 6);
                    byte[] data = new byte[header[5]];
                    inputStream.read(data, 0, header[5]);
                    System.out.println("");
                    System.out.println("==========");
                    System.out.println("Recieved Request: " + data.length + " bytes.");
                    System.out.println("Header: " + Hydra.toHex(header));
                    System.out.println("Data: " + Hydra.toHex(data));
                    matchMessage(header, data);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void matchMessage(byte[] header, byte[] data) throws IOException
    {
        if ((byte)0x00 == header[0] && (byte)0x01 == header[1]) {
            System.out.println("Appears to be ConnectRequest");
            sendConnectResponse(data);
        } else if ((byte)0x00 == header[0] && (byte)0x02 == header[1]) {
            System.out.println("Appears to be BindRequest");
            handleBindRequest(header, data);
        } else if ((byte)0x01 == header[1] && (byte)0x03 == header[2]) {
            System.out.println("Appears to be a LogonRequest");
            handleLogonRequest(header, data);
        }
    }
    
    private void sendConnectResponse(byte[] bytes) throws IOException
    {
        Integer time = Float.floatToIntBits(System.currentTimeMillis()/1000);
        ConnectResponse connectResponse = ConnectResponse.newBuilder().setClientId(ProcessId.newBuilder().setLabel(1).setEpoch(time).build()).setServerId(ProcessId.newBuilder().setLabel(2).setEpoch(time)).build();
        ByteArrayOutputStream byteResponse = new ByteArrayOutputStream();
        byteResponse.write((byte)0xFE);
        byteResponse.write(ByteBuffer.allocate(4).putInt(connectResponse.toByteArray().length).array());
        byteResponse.write(connectResponse.toByteArray());
        this.out.write(byteResponse.toByteArray());
        this.out.flush();
        System.out.println("Sent ConnectResponse");
    }
    
    private void handleBindRequest(byte[] header, byte[] data) throws IOException
    {
        BindRequest bRequest = BindRequest.parseFrom(data);
        System.out.println(bRequest);
        BindResponse.Builder bResponseBuilder = BindResponse.newBuilder();
        for (BoundService s : bRequest.getExportedServiceList()) {
            bResponseBuilder.addImportedServiceId(s.getId());
        }
        BindResponse bResponse = bResponseBuilder.build();
        ByteArrayOutputStream byteResponse = new ByteArrayOutputStream();
        byteResponse.write((byte)0xFE);
        byteResponse.write((byte)0x00);
        byteResponse.write(header[2]); //sequence byte
        byteResponse.write((byte)0x00);
        byteResponse.write((byte)bResponse.toByteArray().length);
        byteResponse.write(bResponse.toByteArray());
        this.out.write(byteResponse.toByteArray());
        this.out.flush();
        System.out.println("Sent BindResponse");
    }
    
    private void handleLogonRequest(byte[] header, byte[] data) throws IOException
    {
        LogonRequest logonRequest = LogonRequest.parseFrom(data);
        System.out.println(logonRequest);
        LogonResponse logonResponse = LogonResponse.newBuilder().setAccount(EntityId.newBuilder().setLow(1L).setHigh(2L)).setGameAccount(EntityId.newBuilder().setLow(3L).setHigh(4L)).build();
        ByteArrayOutputStream byteResponse = new ByteArrayOutputStream();
        byteResponse.write((byte)0xFE);
        byteResponse.write((byte)0x00);
        byteResponse.write(header[2]); //sequence byte
        byteResponse.write((byte)0x00);
        byteResponse.write((byte)logonResponse.toByteArray().length);
        byteResponse.write(logonResponse.toByteArray());
        this.out.write(byteResponse.toByteArray());
        this.out.flush();
        System.out.println("Sent LogonResponse");
    }
}