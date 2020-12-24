package net.lbd01.tiny.json.test;

import net.lbd01.tiny.json.JsonObj;
import net.lbd01.tiny.json.JsonVal;
import net.lbd01.tiny.json.TinyJsonTool;

import static org.junit.Assert.assertTrue;

public class ReadmeCode {

    public static void main(String[] args) {
        String json = "{\"a\":null,\"b\":1,\"c\":2.3,\"d\":false,\"e\":\"txt\",\"f\":[1],\"g\":{\"h\":1}}";

        //parse json
        JsonObj node = TinyJsonTool.parseObject(json);

        //access json object fields
        JsonVal val = node.get("a");
        System.out.println("Field a is null: " + (val==null));

        val = node.get("b");
        System.out.println("Field b is int: " + val.isInteger());
        System.out.println("Field b val: " + val.getInteger());

        val = node.get("c");
        System.out.println("Field c is decimal: " + val.isDecimal());
        System.out.println("Field c val: " + val.getDecimal());

        val = node.get("d");
        System.out.println("Field d is bool: " + val.isBool());
        System.out.println("Field d val: " + val.getBool());

        val = node.get("e");
        System.out.println("Field e is str: " + val.isString());
        System.out.println("Field e val: " + val.getString());

        val = node.get("f");
        System.out.println("Field f is array: " + val.isArray());
        System.out.println("Field f item is int: " + val.getArray().get(0).isInteger());
        System.out.println("Field f val: " + val.getArray().get(0).getInteger());

        val = node.get("g");
        assertTrue("Wrong field g type", val.isObject());
        System.out.println("Field h is int: " + val.getObject().get("h").isInteger());
        System.out.println("Field h val: " + val.getObject().get("h").getInteger());

        //serialize to json
        System.out.println("json: " + node.toJson());
    }

}
