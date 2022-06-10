package pers.qy.loadBalance.LoadBalanceImpl;

import pers.qy.loadBalance.AbstractLoadBalance;
import pers.qy.remoting.dataFormat.RpcRequest;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author whisper
 * @date 2022/06/09
 **/
public class ConsistentHashLoadBalance extends AbstractLoadBalance {
    private static final Map<String, ConsistentHashSelector> selectors = new ConcurrentHashMap<>();

    @Override
    protected String doSelect(RpcRequest rpcRequest, List<String> serviceAddressList) {
        int identityHashCode =System.identityHashCode(serviceAddressList);
        String serviceName = rpcRequest.getRpcServiceName();
        ConsistentHashSelector selector = selectors.get(serviceName);

        //判空，如果selector不存在或者不匹配则create
        if(selector == null || selector.identifyHashCode != identityHashCode) {
            selectors.put(serviceName, new ConsistentHashSelector(serviceAddressList,200, identityHashCode));
            selector = selectors.get(serviceName);
        }
        return selector.select(serviceName + Arrays.stream(rpcRequest.getParameters()));
    }

    static class ConsistentHashSelector {
        private  TreeMap<Long, String> virtualInvokers;
        private final int identifyHashCode;

        ConsistentHashSelector(List<String> invokers, int replicaNumber, int identifyHashCode){

            //虚拟节点，使用TreeMap实现Hash一致性hash环状结构
            this.virtualInvokers = new TreeMap<>();
            this.identifyHashCode = identifyHashCode;

            //ketama算法，对实际节点生成虚拟节点并加入TreeMap
            for (String invoker : invokers) {
                for (int i = 0; i < replicaNumber / 4; i++) {
                    byte[] digest = md5(invoker + i);
                    for (int h = 0; h < 4; h++) {
                        long m = hash(digest, h);
                        virtualInvokers.put(m, invoker);
                    }
                }
            }

        }
        /**
         * md5加密
         * @param key
         * @return
         */
        static byte[] md5(String key) {
            MessageDigest md;
            try {
                md = MessageDigest.getInstance("MD5");
                byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
                md.update(bytes);
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }

            return md.digest();
        }

        static long hash(byte[] digest, int idx) {
            return ((long) (digest[3 + idx * 4] & 255) << 24
                    | (long) (digest[2 + idx * 4] & 255) << 16
                    | (long) (digest[1 + idx * 4] & 255) << 8
                    | (long) (digest[idx * 4] & 255)) & 4294967295L;
        }

        /**
         * 选取服务节点
         * @param rpcServiceKey
         * @return
         */
        public String select(String rpcServiceKey) {
            byte[] digest = md5(rpcServiceKey);
            long hashcode = hash(digest, 0);
            Map.Entry<Long, String> entry = virtualInvokers.tailMap(hashcode, true).firstEntry();
            if (entry == null) {
                entry = virtualInvokers.firstEntry();
            }
            return entry.getValue();
        }
    }







}