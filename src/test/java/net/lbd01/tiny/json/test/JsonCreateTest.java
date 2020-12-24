package net.lbd01.tiny.json.test;

import net.lbd01.tiny.json.JsonObj;
import net.lbd01.tiny.json.JsonVal;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class JsonCreateTest {

    @Test
    public void createJsonObjectWithJsonValTest() {
        String json = "{\"1\":null,\"2\":true,\"3\":[true,false],\"4\":\"txt\",\"5\":[\"txt1\",\"txt2\"],\"6\":1,\"7\":[2,3],\"8\":1.1,\"9\":[2.2,3.3],\"10\":{\"a\":\"b\"},\"11\":[{\"a\":\"b\"},{\"a\":\"c\"}],\"12\":[null,true,[true,false],\"txt\",[\"txt1\",\"txt2\"],1,[2,3],1.1,[2.2,3.3],{\"a\":\"b\"},[{\"a\":\"b\"},{\"a\":\"c\"}]]}";

        JsonObj jsonObj = new JsonObj()
                .setNull("1")
                .set("2", JsonVal.bool(true))
                .set("3", JsonVal.boolArray(Arrays.asList(true,false)))
                .set("4", JsonVal.string("txt"))
                .set("5", JsonVal.stringArray(Arrays.asList("txt1","txt2")))
                .set("6", JsonVal.integer(1))
                .set("7", JsonVal.integerArray(Arrays.asList(2,3)))
                .set("8", JsonVal.decimal(new BigDecimal("1.1")))
                .set("9", JsonVal.decimalArray(Arrays.asList(new BigDecimal("2.2"),new BigDecimal("3.3"))))
                .set("10",
                        JsonVal.object(new JsonObj().set("a",JsonVal.string("b")))
                )
                .set("11", JsonVal.objectArray(Arrays.asList(
                        new JsonObj().set("a",JsonVal.string("b")),
                        new JsonObj().set("a",JsonVal.string("c"))
                )))
                .set("12", JsonVal.array(Arrays.asList(
                        null,
                        JsonVal.bool(true),JsonVal.boolArray(Arrays.asList(true,false)),
                        JsonVal.string("txt"),JsonVal.stringArray(Arrays.asList("txt1","txt2")),
                        JsonVal.integer(1),JsonVal.integerArray(Arrays.asList(2,3)),
                        JsonVal.decimal(new BigDecimal("1.1")),JsonVal.decimalArray(Arrays.asList(new BigDecimal("2.2"),new BigDecimal("3.3"))),
                        JsonVal.object(new JsonObj().set("a",JsonVal.string("b"))),
                        JsonVal.objectArray(Arrays.asList(
                                new JsonObj().set("a",JsonVal.string("b")),
                                new JsonObj().set("a",JsonVal.string("c"))
                        ))
                )));
        String json2 = jsonObj.toJson();
        assertEquals(json,json2);
    }

    @Test
    public void createJsonObjectWithHelpMethodsTest() {
        String json = "{\"1\":null,\"2\":true,\"3\":[true,false],\"4\":\"txt\",\"5\":[\"txt1\",\"txt2\"],\"6\":1,\"7\":[2,3],\"8\":1.1,\"9\":[2.2,3.3],\"10\":{\"a\":\"b\"},\"11\":[{\"a\":\"b\"},{\"a\":\"c\"}],\"12\":[null,true,[true,false],\"txt\",[\"txt1\",\"txt2\"],1,[2,3],1.1,[2.2,3.3],{\"a\":\"b\"},[{\"a\":\"b\"},{\"a\":\"c\"}]]}";

        JsonObj jsonObj = new JsonObj()
                .setNull("1")
                .set("2", true)
                .setArrayOfBools("3", Arrays.asList(true,false))
                .set("4", "txt")
                .setArrayOfStrings("5", Arrays.asList("txt1","txt2"))
                .set("6", 1)
                .setArrayOfIntegers("7", Arrays.asList(2,3))
                .set("8", new BigDecimal("1.1"))
                .setArrayOfDecimals("9", Arrays.asList(new BigDecimal("2.2"),new BigDecimal("3.3")))
                .set("10",
                        new JsonObj().set("a","b")
                )
                .setArrayOfJsonObjects("11", Arrays.asList(
                        new JsonObj().set("a","b"),
                        new JsonObj().set("a","c")
                ))
                .setArray("12", Arrays.asList(
                        null,
                        JsonVal.bool(true),JsonVal.boolArray(Arrays.asList(true,false)),
                        JsonVal.string("txt"),JsonVal.stringArray(Arrays.asList("txt1","txt2")),
                        JsonVal.integer(1),JsonVal.integerArray(Arrays.asList(2,3)),
                        JsonVal.decimal(new BigDecimal("1.1")),JsonVal.decimalArray(Arrays.asList(new BigDecimal("2.2"),new BigDecimal("3.3"))),
                        JsonVal.object(new JsonObj().set("a","b")),
                        JsonVal.objectArray(Arrays.asList(
                                new JsonObj().set("a","b"),
                                new JsonObj().set("a","c")
                        ))
                ));
        String json2 = jsonObj.toJson();
        assertEquals(json,json2);
    }

}
