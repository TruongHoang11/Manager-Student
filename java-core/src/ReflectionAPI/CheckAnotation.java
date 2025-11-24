package ReflectionAPI;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CheckAnotation {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> clazz1 = User.class;
        Object obj = clazz1.getDeclaredConstructor().newInstance();

        Method method = clazz1.getDeclaredMethod("print");
        method.setAccessible(true);
        if(method.isAnnotationPresent(SampleAnotation.class)){
            SampleAnotation sampleAnotation = method.getAnnotation(SampleAnotation.class);
            System.out.println(sampleAnotation.value());

        }

    }
}
