package ReflectionAPI;

public class UserServiceIpl implements UserService {

    @Override
    public User getUser(int id) {
        System.out.println("get User == id =" + id);
        return new User(id, "someone_" + id);
    }
}
