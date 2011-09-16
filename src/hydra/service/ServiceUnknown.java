package hydra.service;

import com.google.protobuf.InvalidProtocolBufferException;

import bnet.protocol.Entity.EntityId;
import bnet.protocol.Rpc.NoData;
import bnet.protocol.authentication.Authentication.LogonRequest;
import bnet.protocol.authentication.Authentication.LogonResponse;
import bnet.protocol.game_master.GameMaster.ListFactoriesRequest;
import hydra.HydraServerConnection;

public class ServiceUnknown implements Service
{
    private HydraServerConnection conn;
    
    public ServiceUnknown(HydraServerConnection conn)
    {
        this.conn = conn;
    }
    
    public void handleMessage(int method, int requestId, int length, byte[] message)
    {
        try
        {
            System.out.println(ListFactoriesRequest.parseFrom(message));
        }
        catch (InvalidProtocolBufferException e)
        {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
        if (method == 1) {
            System.out.println("Matched UnknownRequest");
            //this.sendUnknownResponse(requestId);
        }
    }
    
    private void sendUnknownResponse(int requestId)
    {
        NoData noData = NoData.newBuilder().build();
        this.conn.sendReply(requestId, noData.toByteArray());
        System.out.println("Sent UnknownResponse");
    }
}