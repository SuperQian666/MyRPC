package pers.qy.registry;

import org.apache.curator.framework.CuratorFramework;
import pers.qy.common.RpcException;
import pers.qy.common.enums.RpcErrorMsgEnum;
import pers.qy.loadBalance.LoadBalance;
import pers.qy.remoting.dataFormat.RpcRequest;
import pers.qy.utils.extension.ExtensionLoader;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * service discovery
 * @author whisper
 * @date 2022/06/08
 **/
public class ServiceDiscoveryImpl implements ServiceDiscovery{

    private final LoadBalance loadBalance;

    public ServiceDiscoveryImpl() {
        this.loadBalance = ExtensionLoader.getExtensionLoader(LoadBalance.class).getExtension("loadBalance");
    }

    @Override
    public InetSocketAddress lookupService(RpcRequest rpcRequest) {
        String rpcServiceName = rpcRequest.getRpcServiceName();
        CuratorFramework zkClient = Curatorutil.getZkClient();
        List<String> serviceAddressList = Curatorutil.getChildrenNodes(zkClient, rpcServiceName);
        if(serviceAddressList == null || serviceAddressList.isEmpty()) {
            throw new RpcException(RpcErrorMsgEnum.SERVICE_CAN_NOT_BE_FOUND, rpcServiceName);
        }
        String serviceAddress = loadBalance.selectServiceAddress(rpcRequest, serviceAddressList);
        //inetSocketAddress -> host + port
        String[] hostAndPort = serviceAddress.split(":");
        return new InetSocketAddress(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
    }
}