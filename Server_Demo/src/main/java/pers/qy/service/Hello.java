package pers.qy.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * @author whisper
 * @date 2022/06/10
 **/
@AllArgsConstructor
@Data
@ToString
@Builder
public class Hello {
    private String message;
    private String description;
}