package pers.qy.common;

import pers.qy.common.enums.RpcErrorMsgEnum;

/**
 * @author whisper
 * @date 2022/06/10
 **/
public class RpcException extends RuntimeException{
    public RpcException(RpcErrorMsgEnum rpcErrorMsgEnum, String detail) {
        super(rpcErrorMsgEnum.getMessage() + ":" + detail);
    }

    public RpcException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public RpcException(RpcErrorMsgEnum rpcErrorMsgEnum) {
        super(rpcErrorMsgEnum.getMessage());
    }
}