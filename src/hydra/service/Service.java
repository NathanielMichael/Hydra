package hydra.service;

import hydra.HydraServerConnection;

public interface Service
{
    public void handleMessage(int method, int requestId, int length, byte[] message);
}