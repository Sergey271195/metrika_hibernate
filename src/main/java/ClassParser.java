import models.views.ViewsByTrafficSource;

import javax.persistence.Table;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;

public class ClassParser {

    public static String parse(Class cls) {
        String tableName;
        Annotation tableAnnotation = cls.getDeclaredAnnotation(Table.class);
        tableName = tableAnnotation instanceof Table
                ? ((Table) tableAnnotation).name()
                : cls.getSimpleName().toLowerCase();
        return tableName;
    }

    public static void getClassFields(Class cls) {
        Field[] fields = cls.getDeclaredFields();
        Arrays.stream(fields).forEach(f -> System.out.println(f.getName() + " - " +f.getDeclaringClass()));
    }

}
