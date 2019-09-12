package cn.amware.node.red.mbus.data.hex;

import com.alibaba.fastjson.annotation.JSONType;

/**
 * 长度为16个字节的16进制数据
 */
@HexLen(16)
@JSONType(serializer = HexDataSerializer.class, deserializer = HexDataDeserializer.class)
public class HexData16 extends HexData {
}
