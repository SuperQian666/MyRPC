package pers.qy.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author whisper
 * @date 2022/06/10
 **/

@AllArgsConstructor
@Getter
@ToString
public enum RpcResponseEnum {
    SUCCESS(200, "the remote call is successful"),
    FAIL(500, "the remote call is failed");

    private final int code;
    private final String msg;

}
