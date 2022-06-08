package pers.qy.registry;

import java.net.InetSocketAddress;

/**
 * @author Whisper
 * @date 2022/06/08
 *
 */
public interface ServiceRegistry {
    /**
     * register service
     * @param serviceName
     * @param inetSocketAddress
     */
    public void registerService(String serviceName, InetSocketAddress inetSocketAddress);
}
