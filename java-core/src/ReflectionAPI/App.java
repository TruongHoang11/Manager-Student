package ReflectionAPI;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class App {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        // lay doi tuong class
        Class<?> clazz = User.class;
        //luu doi tuong vua tao vao bien obj
        //clazz.getDeclaredConstructor() : lay constructor khong tham so cua doi tuong clazz cua lop class
        // .newInstance() : goi constructor do de tao ra mot doi tuong moi
        Object obj = clazz.getDeclaredConstructor().newInstance();
        // lay phuong thuc va goi no
        Method method = clazz.getDeclaredMethod("print");
        method.setAccessible(true); // cho phep truy xuat vao variable, meothod( ke ca private method, variable)
        // goi phuong thuc;
        method.invoke(obj);

        Field fieldId = clazz.getDeclaredField("id");
        fieldId.setAccessible(true);
        fieldId.set(obj,1);

        //truy cap va thay doi cac gia tri cua cac truong du lieu( thuoc tinh)
        Field fieldName = clazz.getDeclaredField("name");
        fieldName.setAccessible(true);
        System.out.println(fieldName.get(obj));
        fieldName.set(obj, "Hoang");
//        System.out.println(fieldName.get(obj));

        // in ra thang doi tuong obj da cai(set)
        System.out.println(obj);


    }
}
