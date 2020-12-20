package net.lbd01.tiny.json;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class JsonObj {

    private LinkedHashMap<String, JsonVal> fields = new LinkedHashMap<>();

    public JsonObj setFieldValue(String fieldName, JsonVal value) {
        fields.put(fieldName,value);
        return this;
    }

    public JsonVal getFieldValue(String fieldName) {
        return fields.get(fieldName);
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
            field.getValue().toJson(sb);
        }
        sb.append("}");
    }
}
