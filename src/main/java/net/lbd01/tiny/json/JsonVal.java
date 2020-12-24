package net.lbd01.tiny.json;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class JsonVal {
    private static final byte STRING_VALUE = 1;
    private static final byte BOOLEAN_VALUE = 2;
    private static final byte INTEGER_VALUE = 3;
    private static final byte DECIMAL_VALUE = 4;
    private static final byte OBJECT = 5;
    private static final byte ARRAY = 6;

    private final byte type;
    private Object value;

    private JsonVal(byte type, Object value) {
        this.type = type;
        this.value = value;
    }

    public static JsonVal string(String value) {
        return new JsonVal(STRING_VALUE,value);
    }

    public static JsonVal stringArray(List<String> value) {
        return new JsonVal(ARRAY,value!=null?value.stream().map(JsonVal::string).collect(Collectors.toList()):null);
    }

    public static JsonVal bool(Boolean value) {
        return new JsonVal(BOOLEAN_VALUE,value);
    }

    public static JsonVal boolArray(List<Boolean> value) {
        return new JsonVal(ARRAY,value!=null?value.stream().map(JsonVal::bool).collect(Collectors.toList()):null);
    }

    public static JsonVal integer(Integer value) {
        return new JsonVal(INTEGER_VALUE,value);
    }

    public static JsonVal integerArray(List<Integer> value) {
        return new JsonVal(ARRAY,value!=null?value.stream().map(JsonVal::integer).collect(Collectors.toList()):null);
    }

    public static JsonVal decimal(BigDecimal value) {
        return new JsonVal(DECIMAL_VALUE,value);
    }

    public static JsonVal decimalArray(List<BigDecimal> value) {
        return new JsonVal(ARRAY,value!=null?value.stream().map(JsonVal::decimal).collect(Collectors.toList()):null);
    }

    public static JsonVal object(JsonObj value) {
        return new JsonVal(OBJECT,value);
    }

    public static JsonVal objectArray(List<JsonObj> value) {
        return new JsonVal(ARRAY,value!=null?value.stream().map(JsonVal::object).collect(Collectors.toList()):null);
    }

    public static JsonVal array(List<JsonVal> value) {
        return new JsonVal(ARRAY,value);
    }

    public String getString() {
        return isString()?(String)value:null;
    }

    public Boolean getBool() {
        return isBool()?(Boolean)value:null;
    }

    public Integer getInteger() {
        return isInteger()?(Integer)value:null;
    }

    public BigDecimal getDecimal() {
        return isDecimal()?(BigDecimal) value:null;
    }

    public JsonObj getObject() {
        return isObject()?(JsonObj) value:null;
    }

    public List<JsonVal> getArray() {
        return isArray()?(List<JsonVal>)value:null;
    }

    public boolean isString() {
        return type == STRING_VALUE;
    }

    public boolean isBool() {
        return type == BOOLEAN_VALUE;
    }

    public boolean isInteger() {
        return type == INTEGER_VALUE;
    }

    public boolean isDecimal() {
        return type == DECIMAL_VALUE;
    }

    public boolean isObject() {
        return type == OBJECT;
    }

    public boolean isArray() {
        return type == ARRAY;
    }

    protected void toJson(StringBuilder sb) {
        if (type == STRING_VALUE) {
            sb.append("\"");
            sb.append((String)value);
            sb.append("\"");
        } else if (type == BOOLEAN_VALUE || type == INTEGER_VALUE || type == DECIMAL_VALUE) {
            sb.append(value);
        } else if (type == OBJECT) {
            ((JsonObj)value).toJson(sb);
        } else if (type == ARRAY) {
            sb.append("[");
            boolean first = true;
            for (JsonVal field : ((List<JsonVal>)value) ) {
                if (first) first = false;
                else sb.append(",");
                if (field!=null) field.toJson(sb);
                else sb.append("null");
            }
            sb.append("]");
        }

    }
}
