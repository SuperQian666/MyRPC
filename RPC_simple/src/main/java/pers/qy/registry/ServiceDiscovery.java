package pers.qy.registry;

import pers.qy.remoting.dataFormat.RpcRequest;

import java.net.InetSocketAddress;

/**
 * @author whisper
 * @date 2022/06/08
 **/
public interface ServiceDiscovery {
    /**
     * look up service
     * @param rpcRequest
     * @return
     */
    public InetSocketAddress lookupService(RpcRequest rpcRequest);
}