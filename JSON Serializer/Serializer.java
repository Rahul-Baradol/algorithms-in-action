import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

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

    BodyType deserialize(String serialized) {
        String filtered = serialized.substring(1, serialized.length() - 1);

        List<String> pairs = getStrings(filtered, ';');

        BodyType bodyType = new BodyType();

        for (String pair: pairs) {
            List<String> splitPair = getStrings(pair, ':');

            if (splitPair.isEmpty()) break;
            if (splitPair.get(0).startsWith("{")) {
                bodyType.add(splitPair.get(0), deserialize(splitPair.get(1)));
            } else {
                bodyType.add(splitPair.get(0), splitPair.get(1));
            }
        }
        return bodyType;
    }

    private static List<String> getStrings(String filtered, char delimiter) {
        List<String> pairs = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int openBrackets = 0;

        for (char c: filtered.toCharArray()) {
            if (c == delimiter && openBrackets == 0) {
                pairs.add(current.toString());
                current.setLength(0);
                continue;
            }

            if (c == '{') {
                openBrackets++;
                current.append(c);
                continue;
            }

            if (c == '}') {
                openBrackets--;
            }

            current.append(c);
        }

        if (!current.isEmpty()) {
            pairs.add(current.toString());
        }
        return pairs;
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

        BodyType deserializedBodyType = serializer.deserialize(result);

        String result2 = serializer.serialize(deserializedBodyType).toString();
        System.out.println(result2);

        FileOutputStream stream = new FileOutputStream("/Users/rahulbaradol/Documents/projects/algorithms-in-action/JSON Serializer/serialized.bin");
        stream.write(result.getBytes());
        stream.flush();
        stream.close();
    }

}