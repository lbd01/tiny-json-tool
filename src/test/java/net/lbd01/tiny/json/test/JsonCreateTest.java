package net.lbd01.tiny.json.test;

import net.lbd01.tiny.json.JsonObj;
import net.lbd01.tiny.json.JsonVal;
import net.lbd01.tiny.json.TinyJsonTool;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class JsonCreateTest {

    @Test
    public void createJsonObjectTest() {
        String json = "{\"1\":null,\"2\":true,\"3\":[true,false],\"4\":\"txt\",\"5\":[\"txt1\",\"txt2\"],\"6\":1,\"7\":[2,3],\"8\":1.1,\"9\":[2.2,3.3],\"10\":{\"a\":\"b\"},\"11\":[{\"a\":\"b\"},{\"a\":\"b\"}],\"12\":[null,true,[true,false],\"txt\",[\"txt1\",\"txt2\"],1,[2,3],1.1,[2.2,3.3],{\"a\":\"b\"},[{\"a\":\"b\"},{\"a\":\"b\"}]]}";

        JsonObj jsonObj = new JsonObj()
                .setFieldValue("1", JsonVal.nullValue())
                .setFieldValue("2", JsonVal.bool(true))
                .setFieldValue("3", JsonVal.boolArray(Arrays.asList(true,false)))
                .setFieldValue("4", JsonVal.string("txt"))
                .setFieldValue("5", JsonVal.stringArray(Arrays.asList("txt1","txt2")))
                .setFieldValue("6", JsonVal.integer(1))
                .setFieldValue("7", JsonVal.integerArray(Arrays.asList(2,3)))
                .setFieldValue("8", JsonVal.decimal(new BigDecimal("1.1")))
                .setFieldValue("9", JsonVal.decimalArray(Arrays.asList(new BigDecimal("2.2"),new BigDecimal("3.3"))))
                .setFieldValue("10",
                        JsonVal.object(new JsonObj().setFieldValue("a",JsonVal.string("b")))
                )
                .setFieldValue("11", JsonVal.objectArray(Arrays.asList(
                        new JsonObj().setFieldValue("a",JsonVal.string("b")),
                        new JsonObj().setFieldValue("a",JsonVal.string("b"))
                )))
                .setFieldValue("12", JsonVal.array(Arrays.asList(
                        JsonVal.nullValue(),
                        JsonVal.bool(true),JsonVal.boolArray(Arrays.asList(true,false)),
                        JsonVal.string("txt"),JsonVal.stringArray(Arrays.asList("txt1","txt2")),
                        JsonVal.integer(1),JsonVal.integerArray(Arrays.asList(2,3)),
                        JsonVal.decimal(new BigDecimal("1.1")),JsonVal.decimalArray(Arrays.asList(new BigDecimal("2.2"),new BigDecimal("3.3"))),
                        JsonVal.object(new JsonObj().setFieldValue("a",JsonVal.string("b"))),
                        JsonVal.objectArray(Arrays.asList(
                                new JsonObj().setFieldValue("a",JsonVal.string("b")),
                                new JsonObj().setFieldValue("a",JsonVal.string("b"))
                        ))
                )));
        String json2 = jsonObj.toJson();
        assertEquals(json,json2);
    }

}
