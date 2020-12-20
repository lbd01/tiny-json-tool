package net.lbd01.tiny.json.test;

import net.lbd01.tiny.json.TinyJsonTool;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonSerializeTest {

    @Test
    public void serializeToJsonTest() {
        String json = "{\"xxx\":null,\"b\":1,\"c\":2.3,\"d\":false,\"e\":\"txt\",\"f\":[null,-1,-2.3456,false,\"txt2\",{}],\"g\":{\"h\":1}}";
        String json2 = TinyJsonTool.parseObject(json).toJson();
        assertEquals(json,json2);
    }

}
