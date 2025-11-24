package ExceptionJava;

public class DefineException {
    public static void main(String[] args) {
    int ketqua = add(0,3);
//        try{
//            int ketqua = add(0,3);
//        }
//        catch(Exception e){
//            return;
////            ketqua = 0;
//        }
        System.out.println(ketqua);
    }
    public static int add (int a, int b){
        if(a == 0){
            throw new InvalidParamException("Khong cho phep gia tri a bang 0");
        }
        return a + b;
    }
}
