import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.StringJoiner;

public class CustomObjectMapper {

    public static String dtoToJsonString(Object dto) {
        if (dto == null) {
            return "null";
        }

        StringJoiner jsonObject = new StringJoiner(",", "{", "}");
        PropertyDescriptor[] propertyDescriptors;
        try {
            propertyDescriptors = java.beans.Introspector.getBeanInfo(dto.getClass(), Object.class).getPropertyDescriptors();
        } catch (java.beans.IntrospectionException e) {
            e.printStackTrace();
            return "{}";
        }

        for (PropertyDescriptor pd : propertyDescriptors) {
            String fieldName = pd.getName();
            Method getter = pd.getReadMethod();

            if (getter != null) {
                try {
                    Object fieldValue = getter.invoke(dto);
                    String fieldValueString;

                    if (fieldValue == null) {
                        fieldValueString = "null";
                    } else if (fieldValue instanceof String) {
                        fieldValueString = "\"" + fieldValue + "\"";
                    } else if (fieldValue instanceof Number || fieldValue instanceof Boolean) {
                        fieldValueString = fieldValue.toString();
                    } else {
                        fieldValueString = dtoToJsonString(fieldValue);
                    }

                    jsonObject.add("\"" + fieldName + "\":" + fieldValueString);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        return jsonObject.toString();
    }

}
