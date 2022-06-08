package pers.qy.remoting.dataformat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author whisper
 * @date 2022/06/08
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RpcResponse<T> implements Serializable {

    private static final long serialVersionUID = 715745410605631233L;
    private String requestId;
    private Integer code;
    private String message;
    private T data;

}