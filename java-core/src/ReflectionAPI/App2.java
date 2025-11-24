package ReflectionAPI;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class App2 {
    public static void main(String[] args) {
        checkUserId(100);
    }
    private static void checkUserId(Integer id){
        UserService userService = new UserServiceIpl();
        UserInvocationHandler userInvocationHandler = new UserInvocationHandler(userService);
        UserService proxy = (UserService) Proxy.newProxyInstance(userService.getClass().getClassLoader(),
                userService.getClass().getInterfaces(), userInvocationHandler);
       if(id == null){
            id = 0;
            proxy.getUser(id);
       }
       else{
           System.out.println(proxy.getUser(id));
       }

    }
}
