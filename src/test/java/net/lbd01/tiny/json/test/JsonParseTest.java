package net.lbd01.tiny.json.test;

import net.lbd01.tiny.json.JsonObj;
import net.lbd01.tiny.json.JsonVal;
import net.lbd01.tiny.json.JsonParseException;
import net.lbd01.tiny.json.TinyJsonTool;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class JsonParseTest {

    @Test
    public void stringParseTest() {
        String[] values = new String[]{"","a","\u0105","1","\\\"","\\\\","\\/","\\b","\\f","\\n","\\r","\\t","\\u0105","\\\"\\\\\\/\\b\\f\\n\\r\\t\\u0105","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."};
        for (String value : values) {
            String json = "{\"txt\":\""+value+"\"}";
            JsonObj node = TinyJsonTool.parseObject(json);
            JsonVal val = node.get("txt");
            assertTrue("Not an string: " + value,val.isString());
            assertEquals("Parse error: " + value,processEscapes(value),val.getString());
        }
    }

    private String processEscapes(String str) {
        return str
                .replace("\\\"","\"")
                .replace("\\\\","\\")
                .replace("\\/","/")
                .replace("\\b","\b")
                .replace("\\f","\f")
                .replace("\\n","\n")
                .replace("\\r","\r")
                .replace("\\t","\t")
                .replace("\\u0105","\u0105");
    }


    @Test
    public void numberParseTest() {
        String[] values = new String[]{"0","1","-1","100","-100","1234567890","-1234567890"};
        for (String value : values) {
            String json = "{\"num\":"+value+"}";
            JsonObj node = TinyJsonTool.parseObject(json);
            JsonVal val = node.get("num");
            assertTrue("Not an integer: " + value,val.isInteger());
            assertTrue("Parse error: " + value,Integer.valueOf(value).equals(val.getInteger()));
        }
        values = new String[]{"0.00","1.01","-1.001","100.1","-100.12","123.456","-123.456","1234567890.1234567890","-1234567890.1234567890"};
        for (String value : values) {
            String json = "{\"num\":"+value+"}";
            JsonObj node = TinyJsonTool.parseObject(json);
            JsonVal val = node.get("num");
            assertTrue("Not a decimal: " + value,val.isDecimal());
            assertTrue("Parse error: " + value,new BigDecimal(value).equals(val.getDecimal()));
        }
    }

    @Test
    public void booleanParseTest() {
        String[] values = new String[]{"true","false"};
        for (String value : values) {
            String json = "{\"bool\":"+value+"}";
            JsonObj node = TinyJsonTool.parseObject(json);
            JsonVal val = node.get("bool");
            assertTrue("Not a boolean: " + value,val.isBool());
            assertTrue("Parse error: " + value,Boolean.valueOf(value).equals(val.getBool()));
        }
    }

    @Test
    public void nullParseTest() {
        String json = "{\"a\":null}";
        JsonObj node = TinyJsonTool.parseObject(json);
        JsonVal val = node.get("a");
        assertNull("Not a null: " + json,val);
    }

    @Test
    public void mixedArrayParseTest() {
        String json = "{\"array\":[null,1,2.3,\"txt\",{\"b\":\"txt\"}]}";
        JsonObj node = TinyJsonTool.parseObject(json);
        JsonVal val = node.get("array");
        assertTrue("Not an array",val.isArray());
        assertNotNull("Array empty",val.getArray());
        assertEquals("Wrong array size",5,val.getArray().size());

        assertNull("Wrong item 1 value",val.getArray().get(0));
        assertTrue("Wrong item 2 value type",val.getArray().get(1).isInteger());
        assertTrue("Wrong item 2 value", Integer.valueOf(1).equals(val.getArray().get(1).getInteger()));
        assertTrue("Wrong item 3 value type",val.getArray().get(2).isDecimal());
        assertTrue("Wrong item 3 value", new BigDecimal("2.3").equals(val.getArray().get(2).getDecimal()));
        assertTrue("Wrong item 4 value type",val.getArray().get(3).isString());
        assertTrue("Wrong item 4 value", "txt".equals(val.getArray().get(3).getString()));
        assertTrue("Wrong item 5 value type",val.getArray().get(4).isObject());
        assertTrue("Wrong item 5 field value type",val.getArray().get(4).getObject().get("b").isString());
        assertTrue("Wrong item 5 value", "txt".equals(val.getArray().get(4).getObject().get("b").getString()));

    }

    @Test
    public void integerArrayParseTest() {
        String json = "{\"array\":[1,2,3]}";
        JsonObj node = TinyJsonTool.parseObject(json);
        JsonVal val = node.get("array");
        assertTrue("Not an array",val.isArray());
        assertNotNull("Array empty",val.getArray());
        assertEquals("Wrong array size",3,val.getArray().size());
        int counter = 1;
        for (JsonVal item : val.getArray()) {
            assertTrue("Wrong item value type",item.isInteger());
            assertEquals("Wrong item value", counter++,item.getInteger().intValue());
        }
    }

    @Test
    public void decimalArrayParseTest() {
        String json = "{\"array\":[1.1,2.00,3.23]}";
        JsonObj node = TinyJsonTool.parseObject(json);
        JsonVal val = node.get("array");
        assertTrue("Not an array",val.isArray());
        assertNotNull("Array empty",val.getArray());
        assertEquals("Wrong array size",3,val.getArray().size());
        assertTrue("Wrong item 1 value type",val.getArray().get(0).isDecimal());
        assertTrue("Wrong item 1 value", new BigDecimal("1.1").equals(val.getArray().get(0).getDecimal()));
        assertTrue("Wrong item 2 value type",val.getArray().get(1).isDecimal());
        assertTrue("Wrong item 2 value", new BigDecimal("2.00").equals(val.getArray().get(1).getDecimal()));
        assertTrue("Wrong item 3 value type",val.getArray().get(2).isDecimal());
        assertTrue("Wrong item 3 value", new BigDecimal("3.23").equals(val.getArray().get(2).getDecimal()));
    }

    @Test
    public void textArrayParseTest() {
        String json = "{\"array\":[\"\",\"One\",\"Two\tThree\n\"]}";
        JsonObj node = TinyJsonTool.parseObject(json);
        JsonVal val = node.get("array");
        assertTrue("Not an array",val.isArray());
        assertNotNull("Array empty",val.getArray());
        assertEquals("Wrong array size",3,val.getArray().size());
        assertTrue("Wrong item 1 value type",val.getArray().get(0).isString());
        assertTrue("Wrong item 1 value", "".equals(val.getArray().get(0).getString()));
        assertTrue("Wrong item 2 value type",val.getArray().get(1).isString());
        assertTrue("Wrong item 2 value", "One".equals(val.getArray().get(1).getString()));
        assertTrue("Wrong item 3 value type",val.getArray().get(2).isString());
        assertTrue("Wrong item 3 value", "Two\tThree\n".equals(val.getArray().get(2).getString()));
    }

    @Test
    public void booleanArrayParseTest() {
        String json = "{\"array\":[true,false,true]}";
        JsonObj node = TinyJsonTool.parseObject(json);
        JsonVal val = node.get("array");
        assertTrue("Not an array",val.isArray());
        assertNotNull("Array empty",val.getArray());
        assertEquals("Wrong array size",3,val.getArray().size());
        assertTrue("Wrong item 1 value type",val.getArray().get(0).isBool());
        assertTrue("Wrong item 1 value", Boolean.TRUE.equals(val.getArray().get(0).getBool()));
        assertTrue("Wrong item 2 value type",val.getArray().get(1).isBool());
        assertTrue("Wrong item 2 value", Boolean.FALSE.equals(val.getArray().get(1).getBool()));
        assertTrue("Wrong item 3 value type",val.getArray().get(2).isBool());
        assertTrue("Wrong item 3 value", Boolean.TRUE.equals(val.getArray().get(2).getBool()));
    }

    @Test
    public void nullArrayParseTest() {
        String json = "{\"array\":[null,null,null]}";
        JsonObj node = TinyJsonTool.parseObject(json);
        JsonVal val = node.get("array");
        assertTrue("Not an array",val.isArray());
        assertNotNull("Array empty",val.getArray());
        assertEquals("Wrong array size",3,val.getArray().size());
        for (JsonVal item : val.getArray()) {
            assertNull("Item not null",item);
        }
    }

    @Test
    public void objectArrayParseTest() {
        String json = "{\"array\":[{\"a\":1},{\"b\":null},{\"c\":\"txt\"}]}";
        JsonObj node = TinyJsonTool.parseObject(json);
        JsonVal val = node.get("array");
        assertTrue("Not an array",val.isArray());
        assertNotNull("Array empty",val.getArray());
        assertEquals("Wrong array size",3,val.getArray().size());
        assertTrue("Wrong item 1 value type",val.getArray().get(0).isObject());
        assertTrue("Wrong item 1 field value type",val.getArray().get(0).getObject().get("a").isInteger());
        assertTrue("Wrong item 1 value", Integer.valueOf(1).equals(val.getArray().get(0).getObject().get("a").getInteger()));
        assertTrue("Wrong item 2 value type",val.getArray().get(1).isObject());
        assertNull("Wrong item 2 value", val.getArray().get(1).getObject().get("b"));
        assertTrue("Wrong item 3 value type",val.getArray().get(2).isObject());
        assertTrue("Wrong item 3 field value type",val.getArray().get(2).getObject().get("c").isString());
        assertTrue("Wrong item 3 value", "txt".equals(val.getArray().get(2).getObject().get("c").getString()));
    }

    @Test
    public void emptyArrayParseTest() {
        String json = "{\"array\":[]}";
        JsonObj node = TinyJsonTool.parseObject(json);
        JsonVal val = node.get("array");
        assertTrue("Not an array",val.isArray());
        assertNotNull("Array empty",val.getArray());
        assertEquals("Wrong array size",0,val.getArray().size());
    }

    @Test
    public void emptyObjectParseTest() {
        List<String> jsonList = Arrays.asList("{ }","{}"," { } ","\n{\n}\n","\t{\t}\t") ;
        for (String json : jsonList) {
            JsonObj node = TinyJsonTool.parseObject(json);
            assertNotNull("Empty object", node);
            assertEquals("Field names not empty",0,node.getFieldNames().size());
        }
    }

    @Test
    public void objectParseTest() {
        List<String> jsonList = Arrays.asList(
                "{\"a\":null,\"b\":1,\"c\":2.3,\"d\":false,\"e\":\"txt\",\"f\":[1],\"g\":{\"h\":1}}",
                "{\n" +
                        "   \"a\" : null,\n" +
                        "   \"b\": 1,\n" +
                        "   \"c\" :2.3,\n" +
                        "   \"d\" :   false,\n" +
                        "   \"e\" :\t\"txt\",\n" +
                        "   \"f\"\t:\t[\n" +
                        "      1\n" +
                        "   ],\n" +
                        "   \"g\":{\n" +
                        "\t\t\"h\" :  1   \n" +
                        "   }\n" +
                        "}"
                );
        for (String json : jsonList) {
            JsonObj node = TinyJsonTool.parseObject(json);

            JsonVal val = node.get("a");
            assertNull("Wrong field a value", val);

            val = node.get("b");
            assertTrue("Wrong field b type", val.isInteger());
            assertTrue("Wrong field b value",Integer.valueOf(1).equals(val.getInteger()));
            assertTrue("Wrong field b value (alt get)",Integer.valueOf(1).equals(node.getInteger("b")));

            val = node.get("c");
            assertTrue("Wrong field c type", val.isDecimal());
            assertTrue("Wrong field c value",new BigDecimal("2.3").equals(val.getDecimal()));
            assertTrue("Wrong field c value (alt get)",new BigDecimal("2.3").equals(node.getDecimal("c")));

            val = node.get("d");
            assertTrue("Wrong field d type", val.isBool());
            assertTrue("Wrong field d value",Boolean.FALSE.equals(val.getBool()));
            assertTrue("Wrong field d value (alt get)",Boolean.FALSE.equals(node.getBool("d")));

            val = node.get("e");
            assertTrue("Wrong field e type", val.isString());
            assertTrue("Wrong field e value","txt".equals(val.getString()));
            assertTrue("Wrong field e value (alt get)","txt".equals(node.getString("e")));

            val = node.get("f");
            assertTrue("Wrong field f type", val.isArray());
            assertTrue("Wrong field f type", val.getArray().get(0).isInteger());
            assertTrue("Wrong field f value",Integer.valueOf(1).equals( val.getArray().get(0).getInteger() ));
            assertTrue("Wrong field f value (alt get)",Integer.valueOf(1).equals( node.getArray("f").get(0).getInteger() ));

            val = node.get("g");
            assertTrue("Wrong field g type", val.isObject());
            assertTrue("Wrong field h type", val.getObject().get("h").isInteger());
            assertTrue("Wrong field h value",Integer.valueOf(1).equals( val.getObject().get("h").getInteger() ));
            assertTrue("Wrong field h value (alt get)",Integer.valueOf(1).equals( node.getObject("g").getInteger("h") ));
        }
    }

    @Test
    public void wrongValuesParseTest() {
        List<String> jsonList = Arrays.asList(
                null,
                "",
                "  ",
                "{",
                "}",
                "{a}",
                "{1}",
                "{\\\"a\\\"}",
                "{\"a:1}",
                "{a\":1}",
                "{\"a\":1,}", //TODO fix
                "{\"a\"}",
                "{\"a\":}",
                "{\"a\":,}",
                "{\"a\":,\"b\":2}",
                "{\"a\":1\"b\":2}",
                "{\"a\":1,\"b\":2,}", //TODO fix
                "{\"a\":[}",
                "{\"a\":]}",
                "{\"a\":[,]}",
                "{\"a\":[1,]}", //TODO fix
                "{\"a\":[,2]}",
                "{\"a\":[1,2,]}", //TODO fix
                "{\"a\":{}",
                "{\"a\":}}",
                "{\"a\":x}",
                "{\"a\":fals}",
                "{\"a\":truee}",
                "{\"a\":nul}",
                "{\"a\":\"\\u0\\\"}",
                "{\"a\":\"\\u00\\\"}",
                "{\"a\":\"\\u001\\\"}",
                "{\"a\":\"\\u00123\\\"}",
                "{\"a\":\"b}",
                "{\"a\":b\"}",
                "{\"a\":\"b\"\"}",
                "{\"a\":\"\"b\"}",
                "{\"a\":\"b\"\"}",
                "{\"a\":\"\\x\"}"
                );

        for (String json : jsonList) {
            assertThrows("Exception not thrown for: " + json,JsonParseException.class,()->TinyJsonTool.parseObject(json));
        }
    }

}
