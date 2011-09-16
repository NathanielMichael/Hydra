package hydra;

import hydra.service.Service;
import hydra.service.ServiceAuthentication;
import hydra.service.ServiceChannelInvitation;
import hydra.service.ServiceGameUtilities;
import hydra.service.ServiceList;
import hydra.service.ServiceStorage;
import hydra.service.ServiceToon;
import hydra.service.ServiceUnknown;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import bnet.protocol.Rpc.ProcessId;
import bnet.protocol.connection.Connection.BindRequest;
import bnet.protocol.connection.Connection.BindResponse;
import bnet.protocol.connection.Connection.BoundService;
import bnet.protocol.connection.Connection.ConnectResponse;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.InvalidProtocolBufferException;

public class HydraServerConnection extends Thread
{
    private Socket socket;
    private HydraServer server;
    private boolean doShutdown;
    private UUID uuid = UUID.randomUUID();
    
    private InputStream in;
    private CodedInputStream cin;
    private OutputStream out;
    private InetSocketAddress remoteAddress;
    
    private Integer bindId = 10;
    
    private HashMap<Integer, Integer> serverBinds = new HashMap<Integer, Integer>();
    private HashMap<Integer, Integer> clientBinds = new HashMap<Integer, Integer>();
    
    private HashMap<Integer, String> knownServices = new HashMap<Integer, String>();
    
    private HashMap<Integer, Service> serviceClasses = new HashMap<Integer, Service>();
    
    public HydraServerConnection(Socket socket, HydraServer server)
    {
        this.socket = socket;
        this.server = server;
        
        this.knownServices.put(0x0DECFC01, "Authentication");
        this.knownServices.put(0x4124C31B, "Toon");
        this.knownServices.put(0xDA6E4BB9, "Storage");
        this.knownServices.put(0x83040608, "ChannelInvitation");
        this.knownServices.put(0x3FC1274D, "GameUtilities");
        
        this.serviceClasses.put(0x0DECFC01, new ServiceAuthentication(this));
        this.serviceClasses.put(0xE5A11099, new ServiceUnknown(this));
        this.serviceClasses.put(0x4124C31B, new ServiceToon(this));
        this.serviceClasses.put(0xDA6E4BB9, new ServiceStorage(this));
        this.serviceClasses.put(0x83040608, new ServiceChannelInvitation(this));
        this.serviceClasses.put(0x3FC1274D, new ServiceGameUtilities(this));
    }
    
    public void run()
    {
        try {
            if (this.socket == null) {
                return;
            }
            //this.socket.setSoTimeout(5000);
            //this.socket.setTcpNoDelay(true);
            this.socket.setReceiveBufferSize(this.socket.getReceiveBufferSize() * 2);
            this.remoteAddress = (InetSocketAddress)this.socket.getRemoteSocketAddress();
            System.out.println("Accepted connection from: " + remoteAddress.getHostName());
            System.out.println("Connection assigned UUID: " + this.uuid);
            this.in = this.socket.getInputStream();
            this.cin = CodedInputStream.newInstance(this.in);
            this.out = this.socket.getOutputStream();
            while (true) {
                //if (in.available() > 0) {
                    byte service = cin.readRawByte();
                    int method = cin.readRawVarint32();
                    int requestId = cin.readRawByte() | (cin.readRawByte() << 8);
                    if (service != (byte)0xFE) { cin.readRawVarint64(); }
                    int length = cin.readRawVarint32();
                    byte[] message = cin.readRawBytes(length);
                    System.out.println("");
                    System.out.print("Client Request:");
                    System.out.print(" [S] 0x" + HydraServerConnection.toHex(service));
                    System.out.print(" [M] " + method);
                    System.out.print(" [R] " + requestId);
                    System.out.print(" [L] " + length +"\n");
                    if (length > 0) {
                        System.out.println("[D] " + HydraServerConnection.toHex(message));
                    }
                    this.handleRequest(service, method, requestId, length, message);
                //}
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (this.socket != null) {
                try {
                    this.socket.close();
                    System.out.println("Closed client connection from: " + this.remoteAddress.getHostName());
                } catch (IOException e) {
                    
                }
            }
            server.connectionEnded(this);
        }
    }
    
    public void handleRequest(byte service, int method, int requestId, int length, byte[] message)
    {
        if (service == (byte)0x00) {
            System.out.println("Matched Basic Service");
            this.handleBasicRequest(method, requestId, length, message);
        } else {
            if (this.serverBinds.containsValue((int)service)) {
                for (Integer i : this.serverBinds.keySet()) {
                    if (this.serverBinds.get(i) == (int)service) {
                        if (this.knownServices.containsKey(i)) {
                            System.out.println("Matched " + this.knownServices.get(i));
                        } else {
                            System.out.println("Matched UNKNOWN service");
                        }
                        if (this.serviceClasses.containsKey(i)) {
                            Service matchedService = this.serviceClasses.get(i);
                            matchedService.handleMessage(method, requestId, length, message);
                        }
                    }
                }
            } else {
                System.out.println("Service isn't registered!");
            }
        }
    }
    
    public void handleBasicRequest(int method, int requestId, int length, byte[] message)
    {
        if (method == 1) {
            System.out.println("Matched ConnectRequest");
            this.sendConnectResponse(requestId);
        } else if (method == 2) {
            System.out.println("Matched BindRequest");
            this.handleBindRequest(requestId, message);
        }
    }
    
    public void sendConnectResponse(int requestId)
    {
        Integer time = Float.floatToIntBits(System.currentTimeMillis()/1000);
        ConnectResponse connectResponse = ConnectResponse.newBuilder()
                .setClientId(ProcessId.newBuilder()
                        .setLabel(1)
                        .setEpoch(time)
                        .build())
                .setServerId(ProcessId.newBuilder()
                        .setLabel(2)
                        .setEpoch(time))
                .build();
        this.sendReply(requestId, connectResponse.toByteArray());
        System.out.println("Sent ConnectResponse");
    }
    
    public void sendReply(int requestId, byte[] reply)
    {
        ByteArrayOutputStream byteResponse = new ByteArrayOutputStream();
        byteResponse.write((byte)0xFE);
        byteResponse.write((byte)0x00);
        byteResponse.write((byte)requestId); //sequence byte
        byteResponse.write((byte)0x00);
        byteResponse.write((byte)reply.length);
        try {
            byteResponse.write(reply);
            this.out.write(byteResponse.toByteArray());
            //this.out.flush();
        } catch (Exception e) {
            System.out.println("Error sending reply.");
            this.shutdownConnection();
        }
    }
    
    public void handleBindRequest(int requestId, byte[] message)
    {
        try {
            BindRequest bindRequest = BindRequest.parseFrom(message);
            BindResponse.Builder bindResponseBuilder = BindResponse.newBuilder();
            for (Integer i : bindRequest.getImportedServiceHashList()) {
                this.bindId++;
                this.bindServerService(i, this.bindId);
                bindResponseBuilder.addImportedServiceId(this.bindId);
                System.out.println("Bound server service " + toHexString(i) + " to " + this.bindId);
            }
            for (BoundService i : bindRequest.getExportedServiceList()) {
                this.bindClientService(i.getHash(), i.getId());
                System.out.println("Client bound service " + toHexString(i.getHash()) + " to " + i.getId());
            }
            BindResponse bindResponse = bindResponseBuilder.build();
            this.sendReply(requestId, bindResponse.toByteArray());
            System.out.println("Send BindResponse");
        } catch (InvalidProtocolBufferException e) {
            System.out.println("Could not parse message!");
        }
    }
    
    public void shutdownConnection() {
        try {
            this.doShutdown = true;
            if (this.socket != null) {
                this.socket.close();
            }
            join();
        } catch (Exception e) {
            
        }
    }
    
    public static String toHex(byte b)
    {
        byte[] by = new byte[1];
        by[0] = b;
        
        return HydraServerConnection.toHex(by);
    }
    
    public static String toHex(byte[] bytes) 
    {
        String[] hexArray = {
                "00","01","02","03","04","05","06","07","08","09","0A","0B","0C","0D","0E","0F",
                "10","11","12","13","14","15","16","17","18","19","1A","1B","1C","1D","1E","1F",
                "20","21","22","23","24","25","26","27","28","29","2A","2B","2C","2D","2E","2F",
                "30","31","32","33","34","35","36","37","38","39","3A","3B","3C","3D","3E","3F",
                "40","41","42","43","44","45","46","47","48","49","4A","4B","4C","4D","4E","4F",
                "50","51","52","53","54","55","56","57","58","59","5A","5B","5C","5D","5E","5F",
                "60","61","62","63","64","65","66","67","68","69","6A","6B","6C","6D","6E","6F",
                "70","71","72","73","74","75","76","77","78","79","7A","7B","7C","7D","7E","7F",
                "80","81","82","83","84","85","86","87","88","89","8A","8B","8C","8D","8E","8F",
                "90","91","92","93","94","95","96","97","98","99","9A","9B","9C","9D","9E","9F",
                "A0","A1","A2","A3","A4","A5","A6","A7","A8","A9","AA","AB","AC","AD","AE","AF",
                "B0","B1","B2","B3","B4","B5","B6","B7","B8","B9","BA","BB","BC","BD","BE","BF",
                "C0","C1","C2","C3","C4","C5","C6","C7","C8","C9","CA","CB","CC","CD","CE","CF",
                "D0","D1","D2","D3","D4","D5","D6","D7","D8","D9","DA","DB","DC","DD","DE","DF",
                "E0","E1","E2","E3","E4","E5","E6","E7","E8","E9","EA","EB","EC","ED","EE","EF",
                "F0","F1","F2","F3","F4","F5","F6","F7","F8","F9","FA","FB","FC","FD","FE","FF"};

        StringBuffer hexString = new StringBuffer();
        
        for (int i = 0; i < bytes.length; i++) {
            hexString.append(hexArray[0xFF & bytes[i]]+" ");
        }
        
        return hexString.toString();
    }
    
    public static String toHexString(byte[] bytes) 
    {
        String[] hexArray = {
                "00","01","02","03","04","05","06","07","08","09","0A","0B","0C","0D","0E","0F",
                "10","11","12","13","14","15","16","17","18","19","1A","1B","1C","1D","1E","1F",
                "20","21","22","23","24","25","26","27","28","29","2A","2B","2C","2D","2E","2F",
                "30","31","32","33","34","35","36","37","38","39","3A","3B","3C","3D","3E","3F",
                "40","41","42","43","44","45","46","47","48","49","4A","4B","4C","4D","4E","4F",
                "50","51","52","53","54","55","56","57","58","59","5A","5B","5C","5D","5E","5F",
                "60","61","62","63","64","65","66","67","68","69","6A","6B","6C","6D","6E","6F",
                "70","71","72","73","74","75","76","77","78","79","7A","7B","7C","7D","7E","7F",
                "80","81","82","83","84","85","86","87","88","89","8A","8B","8C","8D","8E","8F",
                "90","91","92","93","94","95","96","97","98","99","9A","9B","9C","9D","9E","9F",
                "A0","A1","A2","A3","A4","A5","A6","A7","A8","A9","AA","AB","AC","AD","AE","AF",
                "B0","B1","B2","B3","B4","B5","B6","B7","B8","B9","BA","BB","BC","BD","BE","BF",
                "C0","C1","C2","C3","C4","C5","C6","C7","C8","C9","CA","CB","CC","CD","CE","CF",
                "D0","D1","D2","D3","D4","D5","D6","D7","D8","D9","DA","DB","DC","DD","DE","DF",
                "E0","E1","E2","E3","E4","E5","E6","E7","E8","E9","EA","EB","EC","ED","EE","EF",
                "F0","F1","F2","F3","F4","F5","F6","F7","F8","F9","FA","FB","FC","FD","FE","FF"};

        StringBuffer hexString = new StringBuffer();
        
        for (int i = 0; i < bytes.length; i++) {
            hexString.append(hexArray[0xFF & bytes[i]]);
        }
        
        return hexString.toString();
    }
    
    public static String toHexString(int value) 
    {
        return toHexString(intToByteArray(value));
    }
    
    public static final byte[] intToByteArray(int value)
    {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
    }
    
    public void bindServerService(int hash, int value)
    {
        this.serverBinds.put(hash, value);
    }
    
    public void bindClientService(int hash, int value)
    {
        this.clientBinds.put(hash, value);
    }
}