package net.lbd01.tiny.json;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonObj {

    private LinkedHashMap<String, JsonVal> fields = new LinkedHashMap<>();

    public JsonObj set(String fieldName, JsonVal value) {
        fields.put(fieldName,value);
        return this;
    }

    public JsonObj set(String fieldName, String value) {
        fields.put(fieldName,JsonVal.string(value));
        return this;
    }

    public JsonObj setArrayOfStrings(String fieldName, List<String> value) {
        fields.put(fieldName,JsonVal.stringArray(value));
        return this;
    }

    public JsonObj set(String fieldName, Boolean value) {
        fields.put(fieldName,JsonVal.bool(value));
        return this;
    }

    public JsonObj setArrayOfBools(String fieldName, List<Boolean> value) {
        fields.put(fieldName,JsonVal.boolArray(value));
        return this;
    }

    public JsonObj set(String fieldName, Integer value) {
        fields.put(fieldName,JsonVal.integer(value));
        return this;
    }

    public JsonObj setArrayOfIntegers(String fieldName, List<Integer> value) {
        fields.put(fieldName,JsonVal.integerArray(value));
        return this;
    }

    public JsonObj set(String fieldName, BigDecimal value) {
        fields.put(fieldName,JsonVal.decimal(value));
        return this;
    }

    public JsonObj setArrayOfDecimals(String fieldName, List<BigDecimal> value) {
        fields.put(fieldName,JsonVal.decimalArray(value));
        return this;
    }

    public JsonObj set(String fieldName, JsonObj value) {
        fields.put(fieldName,JsonVal.object(value));
        return this;
    }

    public JsonObj setArrayOfJsonObjects(String fieldName, List<JsonObj> value) {
        fields.put(fieldName,JsonVal.objectArray(value));
        return this;
    }

    public JsonObj setArray(String fieldName, List<JsonVal> value) {
        fields.put(fieldName,JsonVal.array(value));
        return this;
    }

    public JsonObj setNull(String fieldName) {
        fields.put(fieldName,null);
        return this;
    }

    public JsonVal get(String fieldName) {
        return fields.get(fieldName);
    }

    public String getString(String fieldName) {
        JsonVal val = fields.get(fieldName);
        return val!=null?val.getString():null;
    }

    public Boolean getBool(String fieldName) {
        JsonVal val = fields.get(fieldName);
        return val!=null?val.getBool():null;
    }

    public Integer getInteger(String fieldName) {
        JsonVal val = fields.get(fieldName);
        return val!=null?val.getInteger():null;
    }

    public BigDecimal getDecimal(String fieldName) {
        JsonVal val = fields.get(fieldName);
        return val!=null?val.getDecimal():null;
    }

    public JsonObj getObject(String fieldName) {
        JsonVal val = fields.get(fieldName);
        return val!=null?val.getObject():null;
    }

    public List<JsonVal> getArray(String fieldName) {
        JsonVal val = fields.get(fieldName);
        return val!=null?val.getArray():null;
    }

    public Set<String> getFieldNames() {
        return fields.keySet();
    }

    public String toJson() {
        StringBuilder sb = new StringBuilder();
        toJson(sb);
        return sb.toString();
    }

    protected void toJson(StringBuilder sb) {
        sb.append("{");
        boolean first = true;
        for (Map.Entry<String, JsonVal> field : fields.entrySet()) {
            if (first) first = false;
            else sb.append(",");
            sb.append("\"");
            sb.append(field.getKey());
            sb.append("\":");
            if (field.getValue()!=null) field.getValue().toJson(sb);
            else sb.append("null");
        }
        sb.append("}");
    }
}
