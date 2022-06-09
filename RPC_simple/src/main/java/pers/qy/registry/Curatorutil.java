package pers.qy.registry;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author whisper
 * @date 2022/06/08
 **/
@Slf4j
public class Curatorutil {

    public static final String ZK_ROOT_PATH = "myRpc";
    private static final Map<String, List<String>> SERVICE_MAP = new ConcurrentHashMap<>();
    private static final Set<String> REGISTERED_SET = ConcurrentHashMap.newKeySet();
    private static final String DEFAULT_ZK_ADDRESS = "127.0.0.1:2181";
    private static CuratorFramework zkClient;

    /**
     *重试策略参数
     */
    private static final int BASE_SLEEP_TIME = 1000;
    private static final int MAX_RETRIES = 4;

    private Curatorutil(){}

    public static CuratorFramework getZkClient(){

        //decide if is existed
        if(zkClient != null && zkClient.getState() == CuratorFrameworkState.STARTED) {
            return zkClient;
        }
        //可以做一个配置文件优化选择zk_address，本次使用default_address
        String zkAddress = DEFAULT_ZK_ADDRESS;

        //配置重试策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(BASE_SLEEP_TIME, MAX_RETRIES);
        zkClient = CuratorFrameworkFactory.builder()
                .connectString(zkAddress)
                .retryPolicy(retryPolicy)
                .build();

        zkClient.start();
        try {
            if(!zkClient.blockUntilConnected(30, TimeUnit.SECONDS)) {
                throw new RuntimeException("Time out to connect to zookeeper!");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return  zkClient;
    }

    public static void createPersistentNode(CuratorFramework zkClient, String path) {
        try {
            if(REGISTERED_SET.contains(path) || zkClient.checkExists().forPath(path) != null) {
                log.info("the node whose path is [{}] already exists", path);
                return;
            }
            zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
            log.info("the node whose path is [{}] created successfully", path);
            REGISTERED_SET.add(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String> getChildrenNodes(CuratorFramework zkClient, String rpcServiceName) {
        if(SERVICE_MAP.containsKey(rpcServiceName)) {
            return SERVICE_MAP.get(rpcServiceName);
        }
        List<String> res = null;
        String path = ZK_ROOT_PATH + "/" + rpcServiceName;
        try {
            res = zkClient.getChildren().forPath(path);
            SERVICE_MAP.put(rpcServiceName, res);
            registerWatcher(rpcServiceName, zkClient);
        } catch (Exception e) {
            log.error("get children for path[{}] failed", path);
        }
        return res;
    }

    public static void clearRegistry(CuratorFramework zkClient, InetSocketAddress inetSocketAddress) {
        REGISTERED_SET.stream().parallel().forEach(p ->{
            if(p.endsWith(inetSocketAddress.toString())) {
                //REGISTERED_SET.remove(p);
                try {
                    zkClient.delete().forPath(p);
                } catch (Exception e) {
                    log.error("clear registry for path [{}] failed", p);
                }
            }
        });
        log.info("All registered services are removed:[{}]", REGISTERED_SET.toString());
    }

    public static void registerWatcher(String rpcServiceName, CuratorFramework zkClient) throws Exception {
        String servicePath = DEFAULT_ZK_ADDRESS + "/" + rpcServiceName;
        PathChildrenCache pathChildrenCache = new PathChildrenCache(zkClient, servicePath, true);
        PathChildrenCacheListener pathChildrenCacheListener = new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                List<String> serviceAddress = curatorFramework.getChildren().forPath(servicePath);
                SERVICE_MAP.put(rpcServiceName, serviceAddress);
            }
        };
        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
        pathChildrenCache.start();
    }
}