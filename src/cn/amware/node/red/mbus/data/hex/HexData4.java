package cn.amware.node.red.mbus.data.hex;

import com.alibaba.fastjson.annotation.JSONType;

@HexLen(4)
@JSONType(serializer = HexDataSerializer.class, deserializer = HexDataDeserializer.class)
public class HexData4 extends HexData {
}
