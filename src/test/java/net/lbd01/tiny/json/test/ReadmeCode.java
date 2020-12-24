package net.lbd01.tiny.json.test;

import net.lbd01.tiny.json.JsonObj;
import net.lbd01.tiny.json.JsonVal;
import net.lbd01.tiny.json.TinyJsonTool;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class ReadmeCode {

    public static void main(String[] args) {
        String jsonStr = "{\"a\":null,\"b\":1,\"c\":2.3,\"d\":false,\"e\":\"txt\",\"f\":[1],\"g\":{\"h\":1}}";

        //parse json
        JsonObj json = TinyJsonTool.parseObject(jsonStr);

        //access json object fields
        System.out.println("Field a is null: " + (json.get("a")==null));
        System.out.println("Field b val: " + json.getInteger("b"));
        System.out.println("Field c val: " + json.getDecimal("c"));
        System.out.println("Field d val: " + json.getBool("d"));
        System.out.println("Field e val: " + json.getString("e"));
        System.out.println("Field f val: " + json.getArray("f").get(0).getInteger());
        System.out.println("Field h val: " + json.getObject("g").getInteger("h"));

        //create new json
        JsonObj newJson = new JsonObj();
        newJson.setNull("a");
        newJson.set("b",1);
        newJson.set("c",new BigDecimal("2.3"));
        newJson.set("d",false);
        newJson.set("e","txt");
        newJson.setArrayOfIntegers("f", Arrays.asList(1));
        newJson.set("g",new JsonObj().set("h",1));

        //serialize to json
        System.out.println("json: " + newJson.toJson());
    }

}
