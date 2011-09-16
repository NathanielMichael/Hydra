package hydra.service;

import com.google.protobuf.InvalidProtocolBufferException;

import bnet.protocol.Entity.EntityId;
import bnet.protocol.Rpc.NoData;
import bnet.protocol.authentication.Authentication.LogonRequest;
import bnet.protocol.authentication.Authentication.LogonResponse;
import bnet.protocol.channel_invitation.ChannelInvitation.SubscribeRequest;
import bnet.protocol.channel_invitation.ChannelInvitation.SubscribeResponse;
import bnet.protocol.toon.external.ToonExternal.ToonListRequest;
import bnet.protocol.toon.external.ToonExternal.ToonListResponse;
import hydra.HydraServerConnection;

public class ServiceChannelInvitation implements Service
{
    private HydraServerConnection conn;
    
    public ServiceChannelInvitation(HydraServerConnection conn)
    {
        this.conn = conn;
    }
    
    public void handleMessage(int method, int requestId, int length, byte[] message)
    {
        if (method == 1) {
            System.out.println("Matched SubscribeRequest");
            this.handleSubscribeRequest(requestId, message);
        }
    }
    
    private void handleSubscribeRequest(int requestId, byte[] message)
    {
        try
        {
            SubscribeRequest subscribeRequest = SubscribeRequest.parseFrom(message);
            System.out.println(subscribeRequest);
            SubscribeResponse subscribeResponse = SubscribeResponse.newBuilder().build();
            this.conn.sendReply(requestId, subscribeResponse.toByteArray());
            System.out.println("Sent SubscribeResponse");
        }
        catch (InvalidProtocolBufferException e)
        {
            System.out.println("Couldn't parse subscribeRequest!");
        }
    }
}