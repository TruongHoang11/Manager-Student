package ReflectionAPI;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
//  InvocationHandler : nam bat, can thiep theo chieu ngang khong can thiep vo logic
public class UserInvocationHandler implements InvocationHandler {
    private Object target;
    public UserInvocationHandler(Object target){
        this.target = target;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // thuc hien 1 so can thiep ma minh muon
        System.out.println("Chay truoc.........");
        Object result = method.invoke(target,args);
        System.out.println("Chau sai...........");



        return null;
    }
}
