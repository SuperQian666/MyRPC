package pers.qy.serviceImpl;

import lombok.extern.slf4j.Slf4j;
import pers.qy.annotation.RpcService;
import pers.qy.service.Hello;
import pers.qy.service.SayHello;

/**
 * @author whisper
 * @date 2022/06/10
 **/
@Slf4j
@RpcService(group = "test-1", version = "version-1.0")
public class SayHelloImpl implements SayHello {
    @Override
    public String hello(Hello hello) {
        log.info("SayHelloServiceImpl received: ", hello.getMessage());
        String res = "the description is :" + hello.getDescription();
        log.info("SayHelloServiceImpl returns :", res);
        return res;
    }
}