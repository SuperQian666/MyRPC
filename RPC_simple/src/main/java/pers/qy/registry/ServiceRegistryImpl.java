package pers.qy.registry;

import java.net.InetSocketAddress;

/**
 * @author whisper
 * @date 2022/06/08
 **/
public class ServiceRegistryImpl implements ServiceRegistry{
    @Override
    public void registerService(String serviceName, InetSocketAddress inetSocketAddress) {
        String servicePath = Curatorutil.ZK_ROOT_PATH + "/" + serviceName + inetSocketAddress.toString();
        Curatorutil.createPersistentNode(Curatorutil.getZkClient(), servicePath);

    }
}