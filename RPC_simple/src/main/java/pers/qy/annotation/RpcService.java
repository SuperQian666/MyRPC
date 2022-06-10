package pers.qy.annotation;

import java.awt.*;
import java.lang.annotation.*;

/**
 * Rpc service annotation, marked on the service implementation class
 * @author whisper
 * @date 2022/06/10
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RpcService {
    /**
     * service version
     * @return
     */
    String version() default "";

    /**
     * service group
     * @return
     */
    String group() default "";
}
