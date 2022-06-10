package pers.qy.loadBalance;

import pers.qy.remoting.dataFormat.RpcRequest;

import java.util.List;

/**
 * @author whisper
 * @date 2022/06/09
 **/
public abstract class AbstractLoadBalance implements LoadBalance{
    @Override
    public String selectServiceAddress(RpcRequest rpcRequest, List<String> serviceAddressList) {
        if(serviceAddressList.isEmpty()) {
            return null;
        }
        if(serviceAddressList.size() == 1) {
            return serviceAddressList.get(0);
        }
        return doSelect(rpcRequest, serviceAddressList);
    }

    /**
     * 对待选地址超过一条的进行选取
     * @param rpcRequest
     * @param serviceAddressList
     * @return
     */
    protected abstract String doSelect(RpcRequest rpcRequest, List<String> serviceAddressList);

}