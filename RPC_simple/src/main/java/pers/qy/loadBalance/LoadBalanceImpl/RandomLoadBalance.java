package pers.qy.loadBalance.LoadBalanceImpl;

import pers.qy.loadBalance.AbstractLoadBalance;
import pers.qy.remoting.dataformat.RpcRequest;

import java.util.List;
import java.util.Random;

/**
 * @author whisper
 * @date 2022/06/09
 **/
public class RandomLoadBalance extends AbstractLoadBalance {

    @Override
    protected String doSelect(RpcRequest rpcRequest, List<String> serviceAddressList) {
        Random random = new Random();
        return serviceAddressList.get(random.nextInt(serviceAddressList.size()));
    }
}