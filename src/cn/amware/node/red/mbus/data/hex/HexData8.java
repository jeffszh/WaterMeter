package cn.amware.node.red.mbus.data.hex;

import com.alibaba.fastjson.annotation.JSONType;

/**
 * 长度为8个字节的16进制数据
 */
@HexLen(8)
@JSONType(serializer = HexDataSerializer.class, deserializer = HexDataDeserializer.class)
public class HexData8 extends HexData {
}
