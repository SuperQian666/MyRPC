package pers.proxy;

import lombok.AllArgsConstructor;
import pers.config.RpcServiceConfig;
import pers.remoting.dataformat.RpcRequest;
import pers.remoting.dataformat.RpcResponse;
import pers.remoting.transport.RpcRequestTransport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * JDK动态代理
 * @author whisper
 * @date 2022/06/08
 **/
@AllArgsConstructor
public class RpcClientProxy implements InvocationHandler {

    /**
     * used to send request
     */
    private RpcRequestTransport rpcRequestTransport;
    private RpcServiceConfig rpcServiceConfig;

    /**
     * get proxy
     * @param clazz
     * @param <T>
     * @return <T>
     */
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest request =RpcRequest.builder().requestId(UUID.randomUUID().toString())
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .paramTypes(method.getParameterTypes())
                .parameters(method.getParameters())
                .version(rpcServiceConfig.getVersion())
                .group(rpcServiceConfig.getGroup())
                .build();
        //send request base on netty 待修改
        RpcResponse<Object> response = (RpcResponse<Object>) rpcRequestTransport.sendRpcRequest(request);
        this.check(request, response);
        return response.getData();
        //return null;
    }

    public void check(RpcRequest request, RpcResponse<Object> response) {
        if (response == null) {
            //throw exception
        }
    }
}