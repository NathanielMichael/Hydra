package hydra.service;

import com.google.protobuf.InvalidProtocolBufferException;

import bnet.protocol.Entity.EntityId;
import bnet.protocol.Rpc.NoData;
import bnet.protocol.authentication.Authentication.LogonRequest;
import bnet.protocol.authentication.Authentication.LogonResponse;
import bnet.protocol.game_master.GameMaster.ListFactoriesRequest;
import bnet.protocol.game_utilities.GameUtilities.ClientRequest;
import bnet.protocol.game_utilities.GameUtilities.ClientResponse;
import hydra.HydraServerConnection;

public class ServiceGameUtilities implements Service
{
    private HydraServerConnection conn;
    
    public ServiceGameUtilities(HydraServerConnection conn)
    {
        this.conn = conn;
    }
    
    public void handleMessage(int method, int requestId, int length, byte[] message)
    {
        if (method == 1) {
            System.out.println("Matched ClientRequest");
            //this.handleClientRequest(requestId, message);
        }
    }
    
    private void handleClientRequest(int requestId, byte[] message)
    {
        try
        {
            System.out.println(ClientRequest.parseFrom(message));
        }
        catch (InvalidProtocolBufferException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ClientResponse response = ClientResponse.newBuilder().build();
        this.conn.sendReply(requestId, response.toByteArray());
        System.out.println("Sent ClientResponse");
    }
}