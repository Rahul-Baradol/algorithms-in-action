import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

abstract class Type {}

class StringType extends Type {
    private final String value;

    public StringType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

class BodyType extends Type {
    private final HashMap<Type, Type> pairs = new HashMap<>();

    public void add(String key, String value) {
        pairs.put(new StringType(key), new StringType(value));
    }

    public void add(String key, BodyType value) {
        pairs.put(new StringType(key), value);
    }

    public Set<Map.Entry<Type, Type>> getEntries() {
        return pairs.entrySet();
    }

    public Type get(StringType key) {
        return pairs.get(key);
    }
}

public class Serializer {

    StringBuilder serialize(BodyType bodyType) {
        StringBuilder builder = new StringBuilder();

        builder.append("{");
        Set<Map.Entry<Type, Type>> entries = bodyType.getEntries();

        for (Map.Entry<Type, Type> entry : entries) {
            Type keyType = entry.getKey();
            Type valueType = entry.getValue();

            if (keyType instanceof StringType) {
                String key = ((StringType) keyType).getValue();
                switch (valueType) {
                    case StringType value -> builder.append(key).append(":").append(value.getValue());
                    case BodyType value -> builder.append(key).append(":").append(serialize(value));
                    default -> throw new IllegalStateException("Unexpected value: " + valueType);
                }
            }

            builder.append(";");
        }
        builder.append("}");
        return builder;
    }

    public static void main(String[] args) throws IOException {
        /*
        *   Body body = new Body();
        *   body.add(key, value);
        **/

        BodyType founderInfo = new BodyType();
        founderInfo.add("name", "someone");
        founderInfo.add("email", "someone@example.com");

        BodyType bodyType = new BodyType();
        bodyType.add("industry", "software");
        bodyType.add("name", "example.com");
        bodyType.add("founderInfo", founderInfo);

        Serializer serializer = new Serializer();
        String result = serializer.serialize(bodyType).toString();

        System.out.println(result);

        FileOutputStream stream = new FileOutputStream("/Users/rahulbaradol/Documents/projects/algorithms-in-action/JSON Serializer/serialized.bin");
        stream.write(result.getBytes());
        stream.flush();
        stream.close();
    }

}