package pers.qy.proxy;

import lombok.AllArgsConstructor;
import pers.qy.common.RpcException;
import pers.qy.common.enums.RpcErrorMsgEnum;
import pers.qy.common.enums.RpcResponseEnum;
import pers.qy.config.RpcServiceConfig;
import pers.qy.remoting.dataFormat.RpcRequest;
import pers.qy.remoting.dataFormat.RpcResponse;
import pers.qy.remoting.transport.RpcRequestTransport;

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

    private static final String INTERFACE_NAME = "interfaceName";

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
    }

    public void check(RpcRequest request, RpcResponse<Object> response) {
        if (response == null) {
            throw new RpcException(RpcErrorMsgEnum.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + request.getInterfaceName());
        }
        if(request.getRequestId() != response.getRequestId()) {
            throw new RpcException(RpcErrorMsgEnum.REQUEST_NOT_MATCH_RESPONSE, INTERFACE_NAME + ":" + request.getInterfaceName());
        }
        if(response.getCode() == null || response.getCode() != RpcResponseEnum.SUCCESS.getCode()) {
            throw new RpcException(RpcErrorMsgEnum.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + request.getInterfaceName());
        }
    }
}