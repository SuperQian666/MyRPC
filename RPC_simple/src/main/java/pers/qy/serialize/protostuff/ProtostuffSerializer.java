package pers.qy.serialize.protostuff;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import pers.qy.serialize.Serializer;

/**
 * 使用protostuff实现序列化
 * @author whisper
 * @date 2022/06/10
 **/
public class ProtostuffSerializer implements Serializer {
    /**
     * 使用默认分配大小
     */
    private static final LinkedBuffer LINKEDBUFFER = LinkedBuffer.allocate();

    @Override
    public byte[] serialize(Object obj) {
        //return new byte[0];
        Class<?> clazz = obj.getClass();
        Schema schema = RuntimeSchema.getSchema(clazz);
        byte[] bytes;
        try {
            bytes = ProtostuffIOUtil.toByteArray(obj, schema, LINKEDBUFFER);
        } finally {
            LINKEDBUFFER.clear();
        }
        return bytes;
    }

    @Override
    public <T> T deSerialize(byte[] bytes, Class<T> clazz) {
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        T obj = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(bytes, obj, schema);
        return obj;
    }
}