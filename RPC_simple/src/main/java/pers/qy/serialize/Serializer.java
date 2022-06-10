package pers.qy.serialize;


import pers.qy.utils.extension.SPI;


/**
 * serializer interface
 * @author Whisper
 * @date 2022.06.10
 */
@SPI
public interface Serializer {

    /**
     * serialize interface
     * @param obj
     * @return
     */
    byte[] serialize(Object obj);

    /**
     * deserialize interface
     * @param bytes
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T deSerialize(byte[] bytes, Class<T> clazz);

}
