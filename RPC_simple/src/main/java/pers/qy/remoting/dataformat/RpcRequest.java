package pers.qy.remoting.dataformat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author whisper
 * @date 2022/06/08
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RpcRequest {
    private static final long serialVersionUID = 1905122041950251207L;
    private String requestId;
    private String interfaceName;
    private String methodName;
    private Object[] parameters;
    private Class<?>[] paramTypes;
    private String version;
    private String group;

    public String getRpcServiceName() {
        return this.getVersion() + this.getGroup() + this.getInterfaceName();
    }
}