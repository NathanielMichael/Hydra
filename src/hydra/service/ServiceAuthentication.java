package hydra.service;

import com.google.protobuf.InvalidProtocolBufferException;

import bnet.protocol.Entity.EntityId;
import bnet.protocol.authentication.Authentication.LogonRequest;
import bnet.protocol.authentication.Authentication.LogonResponse;
import hydra.HydraServerConnection;

public class ServiceAuthentication implements Service
{
    private HydraServerConnection conn;
    
    public ServiceAuthentication(HydraServerConnection conn)
    {
        this.conn = conn;
    }
    
    public void handleMessage(int method, int requestId, int length, byte[] message)
    {
        if (method == 1) {
            System.out.println("Matched LogonRequest");
            this.handleLogonRequest(requestId, message);
        }
    }
    
    private void handleLogonRequest(int requestId, byte[] message)
    {
        try
        {
            LogonRequest logonRequest = LogonRequest.parseFrom(message);
            System.out.println(logonRequest);
            // Module Request here
            this.sendLogonResponse(requestId);
        }
        catch (InvalidProtocolBufferException e)
        {
            System.out.println("Could not parse LogonRequest!");
        }
    }
    
    private void sendLogonResponse(int requestId)
    {
        LogonResponse logonResponse = LogonResponse.newBuilder().setAccount(EntityId.newBuilder().setLow(1L).setHigh(0x100000000000000L)).setGameAccount(EntityId.newBuilder().setLow(3L).setHigh(0x200006200004433L)).build();
        this.conn.sendReply(requestId, logonResponse.toByteArray());
        System.out.println("Sent LogonResponse");
    }
}