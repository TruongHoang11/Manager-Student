package ReflectionAPI;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
// add Retention , RUNTIME : moi truong thuc thi
@Retention(RetentionPolicy.RUNTIME)
public @interface SampleAnotation {
    String value();
}
