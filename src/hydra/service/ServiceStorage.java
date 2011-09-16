package hydra.service;

import com.google.protobuf.InvalidProtocolBufferException;

import bnet.protocol.Entity.EntityId;
import bnet.protocol.Rpc.NoData;
import bnet.protocol.authentication.Authentication.LogonRequest;
import bnet.protocol.authentication.Authentication.LogonResponse;
import bnet.protocol.storage.Storage.ExecuteRequest;
import bnet.protocol.storage.Storage.ExecuteResponse;
import bnet.protocol.storage.Storage.OpenColumnRequest;
import bnet.protocol.storage.Storage.OpenColumnResponse;
import bnet.protocol.storage.Storage.OpenTableRequest;
import bnet.protocol.storage.Storage.OpenTableResponse;
import hydra.HydraServerConnection;

public class ServiceStorage implements Service
{
    private HydraServerConnection conn;
    
    private byte[] tempExecuteResponse = new byte[]{
            (byte)0x12, (byte)0x40, (byte)0x08, (byte)0x04, (byte)0x12, (byte)0x12, 
            (byte)0x0a, (byte)0x10, (byte)0x27, (byte)0xf4, (byte)0xa8, (byte)0xfc, (byte)0xc5, (byte)0xd3, 
            (byte)0x59, (byte)0xdd, (byte)0x0c, (byte)0x50, (byte)0xec, (byte)0x1c, (byte)0x12, (byte)0x9f, 
            (byte)0xeb, (byte)0x9a, (byte)0x1a, (byte)0x28, (byte)0x0a, (byte)0x12, (byte)0x0a, (byte)0x10, 
            (byte)0x68, (byte)0x19, (byte)0x52, (byte)0xdc, (byte)0x11, (byte)0xc9, (byte)0x53, (byte)0x46, 
            (byte)0xbe, (byte)0xb6, (byte)0x99, (byte)0x2e, (byte)0x5f, (byte)0x8e, (byte)0xf0, (byte)0xda, 
            (byte)0x12, (byte)0x12, (byte)0x0a, (byte)0x10, (byte)0x02, (byte)0x00, (byte)0x00, (byte)0x62, 
            (byte)0x00, (byte)0x00, (byte)0x44, (byte)0x33, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, 
            (byte)0x00, (byte)0x4f, (byte)0x5c, (byte)0x21
    };
    
    private byte[] loadAccountDigest = new byte[]{
            (byte)0x12, (byte)0x78, (byte)0x12, 
            (byte)0x12, (byte)0x0a, (byte)0x10, (byte)0x27, (byte)0xf4, (byte)0xa8, (byte)0xfc, (byte)0xc5, 
            (byte)0xd3, (byte)0x59, (byte)0xdd, (byte)0x0c, (byte)0x50, (byte)0xec, (byte)0x1c, (byte)0x12, 
            (byte)0x9f, (byte)0xeb, (byte)0x9a, (byte)0x1a, (byte)0x62, (byte)0x0a, (byte)0x12, (byte)0x0a, 
            (byte)0x10, (byte)0x85, (byte)0x1b, (byte)0x15, (byte)0x11, (byte)0x91, (byte)0x27, (byte)0xfc, 
            (byte)0x3f, (byte)0xf7, (byte)0x43, (byte)0x2c, (byte)0x8d, (byte)0xc2, (byte)0x0d, (byte)0xbb, 
            (byte)0x38, (byte)0x12, (byte)0x12, (byte)0x0a, (byte)0x10, (byte)0x02, (byte)0x00, (byte)0x00, 
            (byte)0x62, (byte)0x00, (byte)0x00, (byte)0x44, (byte)0x33, (byte)0x00, (byte)0x00, (byte)0x00, 
            (byte)0x00, (byte)0x00, (byte)0x4f, (byte)0x5c, (byte)0x21, (byte)0x21, (byte)0x01, (byte)0x00, 
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x2a, (byte)0x2f, 
            (byte)0x08, (byte)0x63, (byte)0x12, (byte)0x15, (byte)0x08, (byte)0xb3, (byte)0x88, (byte)0x81, 
            (byte)0x80, (byte)0xa0, (byte)0xac, (byte)0x80, (byte)0x80, (byte)0x03, (byte)0x10, (byte)0xc8, 
            (byte)0xd9, (byte)0x94, (byte)0x9e, (byte)0xb3, (byte)0x94, (byte)0xd5, (byte)0xb1, (byte)0xcb, 
            (byte)0x01, (byte)0x1a, (byte)0x12, (byte)0x08, (byte)0x08, (byte)0x10, (byte)0x03, (byte)0x18, 
            (byte)0x04, (byte)0x20, (byte)0x0b, (byte)0x28, (byte)0x14, (byte)0x30, (byte)0x07, (byte)0x38, 
            (byte)0x0b, (byte)0x40, (byte)0x04, (byte)0x48, (byte)0x01, (byte)0x20, (byte)0x00
    };
    
    public ServiceStorage(HydraServerConnection conn)
    {
        this.conn = conn;
    }
    
    public void handleMessage(int method, int requestId, int length, byte[] message)
    {
        if (method == 1) {
            System.out.println("Matched ExecuteRequest");
            this.handleExecuteRequest(requestId, message);
        } else if (method == 2) {
            System.out.println("Matched OpenTableRequest");
            this.handleOpenTableRequest(requestId, message);
        } else if (method == 3) {
            System.out.println("Matched OpenColumnRequest");
            this.handleOpenColumnRequest(requestId, message);
        }
    }
    
    private void handleOpenTableRequest(int requestId, byte[] message)
    {
        try
        {
            System.out.println(OpenTableRequest.parseFrom(message));
        }
        catch (InvalidProtocolBufferException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        OpenTableResponse openTableResponse = OpenTableResponse.newBuilder().build();
        this.conn.sendReply(requestId, openTableResponse.toByteArray());
        System.out.println("Sent OpenTableResponse");
    }
    
    private void handleOpenColumnRequest(int requestId, byte[] message)
    {
        try
        {
            System.out.println(OpenColumnRequest.parseFrom(message));
        }
        catch (InvalidProtocolBufferException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        OpenColumnResponse openColumnResponse = OpenColumnResponse.newBuilder().build();
        this.conn.sendReply(requestId, openColumnResponse.toByteArray());
        System.out.println("Sent OpenColumnResponse");
    }
    
    private void handleExecuteRequest(int requestId, byte[] message)
    {
        try
        {
            ExecuteRequest request = ExecuteRequest.parseFrom(message);
            System.out.println(request);
            if (request.getQueryName().equals("LoadAccountDigest")) {
                this.conn.sendReply(requestId, this.loadAccountDigest);
                System.out.println("Sent ExecuteResponse (LAD)");
            } else {
                ExecuteResponse executeResponse = ExecuteResponse.newBuilder().build();
                this.conn.sendReply(requestId, executeResponse.toByteArray());
                System.out.println("Sent ExecuteResponse");
            }
        }
        catch (InvalidProtocolBufferException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private void sendUnknownResponse(int requestId)
    {
        NoData noData = NoData.newBuilder().build();
        this.conn.sendReply(requestId, noData.toByteArray());
        System.out.println("Sent UnknownResponse");
    }
}