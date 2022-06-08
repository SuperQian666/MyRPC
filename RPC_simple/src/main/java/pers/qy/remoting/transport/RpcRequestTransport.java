package pers.qy.remoting.transport;

import pers.qy.remoting.dataformat.RpcRequest;

/**
 * @author whisper
 * @date 2022/06/08
 **/
public interface RpcRequestTransport {

    /**
     * send request to server and get response
     * @param rpcRequest
     * @return response
     */
    Object sendRpcRequest(RpcRequest rpcRequest);
}