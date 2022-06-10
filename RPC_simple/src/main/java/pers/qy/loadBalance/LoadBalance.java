package pers.qy.loadBalance;

import pers.qy.remoting.dataFormat.RpcRequest;
import pers.qy.utils.extension.SPI;

import java.util.List;

/**
 * load balance interface
 * @author Whisper
 * @Date 2022/06/09
 *
 */
@SPI
public interface LoadBalance {

    /**
     * 从所有待选服务地址中选取一条地址
     * @param rpcRequest
     * @param serviceAddressList 所有服务地址的集合
     * @return
     */
    public String selectServiceAddress(RpcRequest rpcRequest, List<String> serviceAddressList);

}
