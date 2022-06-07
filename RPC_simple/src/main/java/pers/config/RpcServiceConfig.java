package pers.config;

import jdk.nashorn.internal.objects.annotations.Getter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author whisper
 * @date 2022/06/07
 **/


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RpcServiceConfig {
    /**
     * service version
     */
    private String version = "";

    /**
     * service group
     */
    private String group = "";

    /**
     * service
     */
    private Object service;

    public String getRpcServiceName() {
        return this.getVersion() + this.getGroup() + this.getServiceName();
    }

    public String getServiceName() {
        return  this.getClass().getInterfaces()[0].getCanonicalName();
    }
}