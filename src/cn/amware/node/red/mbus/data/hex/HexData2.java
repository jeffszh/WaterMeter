package cn.amware.node.red.mbus.data.hex;

import com.alibaba.fastjson.annotation.JSONType;

/**
 * 长度为2个字节的16进制数据
 * {@inheritDoc}
 */
@HexLen(2)
@JSONType(serializer = HexDataSerializer.class, deserializer = HexDataDeserializer.class)
public class HexData2 extends HexData {
}
