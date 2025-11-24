package Java8;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class AppTestOrder {
    public static void main(String[] args) {
        // in ra cac phan tu torng list.
//        Mockup.orderList().stream().forEach(order ->{
//            System.out.println(order);
//        });


        // get all productType is Mobile
//        List<Order> moblie = Mockup.orderList().stream().filter(order -> Objects.equals(order.getProductType(),"Mobile")).toList();
//        System.out.println(moblie);


        // get cac hang co gia lon hon 500
//        List<Order> list = Mockup.orderList().stream().filter(order -> order.getPrice() > 500).toList();
//        System.out.println(list);

        // check Mobile su dung allMatch
//        boolean isMobile = Mockup.orderList().stream().allMatch(order -> Objects.equals(order.getProductType(), "Mobile"));
//        System.out.println(isMobile);
        // in ra false

        // su dung anyMatch
//        boolean isMobile = Mockup.orderList().stream().anyMatch(order -> Objects.equals(order.getProductType(),"Watch"));
//        System.out.println(isMobile);
        // in ra true;
//        boolean check = Mockup.orderList().stream().anyMatch(order -> Objects.equals(order.getProductType(), "type khong co trong danh sach"));
//        System.out.println(check);
        // in ra false



        // su dung noneMatch()
//        boolean check1 = Mockup.orderList().stream().noneMatch(order -> order.getPrice() < 500);
//        System.out.println(check1);
        // khong co hang nao gia tren 1 trieu nen no tra ve true





        // su dung lanbda kiem tra xem co don hang nao lon hon 1000 k
//        boolean check2 = Mockup.orderList().stream().anyMatch(order -> order.getPrice() > 500);
//        if(check2) System.out.println("Có don hang > 500");
//        else System.out.println("Không có don hang nao > 500");
//        if(check2){
//            System.out.println(Mockup.orderList().stream().filter(order -> order.getPrice() > 500).toList());
//        }



        // lay ra toan bo ten nha cung cap ventor
//        List<String> ventor = Mockup.orderList().stream().map(Order::getVendor).toList();
//       System.out.println(ventor);


       // lay ra ten cac nha cung cap khong lap lai
//        List<String> ventor1 = Mockup.orderList().stream().map(Order::getVendor).distinct().toList();
//        System.out.println(ventor1);


        //Stream.concat()
//        Stream<String> stream1 = Stream.of("Alpha");
//        Stream<String> stream2 = Stream.of("Beta");
//        Stream<String> stream3 = Stream.of("Xeta");
//        Stream.concat(Stream.concat(stream1,stream2) , stream3).forEach(s -> System.out.print(s + " "));

//        Stream<Integer> stream1 = Stream.of(1,2,3);
//        stream1.map(n -> n * 2).forEach(n -> System.out.print(n + " "));


        // su dung findAny
//        List<String> type = Mockup.orderList().stream().map(Order::getProductType).distinct().toList();
//        Optional<String> check = type.stream().findAny();
//        if(check.isPresent()){
//            type.forEach(System.out::println);
//        }
//        else{
//            System.out.println("type khong tom tai");
//        }



        // Sư dụng flatMap
//        List<Order> orders = Mockup.orderList();
//        List<String> productName = orders.stream().map(Order :: getProductName).collect(Collectors.toList());
//        List<String> productType = orders.stream().map(Order :: getProductType).collect(Collectors.toList());
//        List<List<String>> product = new ArrayList<>();
//        product.add(productName);
//        product.add(productType);
//        System.out.println("Distinct all product type");
//        product.stream(). flatMap(list -> list.stream().distinct()).forEach(System.out::println);
//        System. out.println("\nTotal price");
//        double total = Mockup.orderList().stream().map(Order :: getPrice).flatMapToDouble(DoubleStream::of). sum();
//        System.out.println(total);
//        System.out.println("Print first character");
//                productName.stream().flatMap(s ->
//                        Stream.of(s.toUpperCase().charAt(0))).forEach(System.out :: println);


//
//        // common
//        // tinh tong gia
//        List<Order> list = Mockup.orderList();
//        //c1
//        double total = list.stream().mapToDouble(Order::getPrice).sum();
//        System.out.println("Sum by Lambda: " + total);
//        //c2
//        double total2 = list.stream().map(Order::getPrice).reduce(0.0d, Double::sum);
//        System.out.println("Sum by reduce,(method reference) Double::sum " + total2);
//        // c3
//        double total3 = list.stream().map(Order::getPrice).reduce(0.0d, (sum, price) -> sum + price);
//        System.out.println("Sum by reduce, Lambda: " + total3);
//        // c4
//        double total4 = list.stream().collect(Collectors.summingDouble(Order::getPrice));
//        System.out.println("Sum by Collectors. summingDouble: " + total4);
//        // Tim max, min, average price
//        System.out.println(list.stream().mapToDouble(Order::getPrice).max().getAsDouble());
//        System.out.println(list.stream().mapToDouble(Order::getPrice).min().getAsDouble());
//        System.out.println(list.stream().mapToDouble(Order::getPrice).average());



//            // dung sorted()
//        // sap xep tang dan theo price c1
//        List<Order> list = Mockup.orderList();
//        list.stream().sorted((o1,o2) -> Double.compare(o1.getPrice(), o2.getPrice())).
//                forEach(p -> System.out.println(p.getProductName() + " " + p.getPrice()));
//        // sap xep giam dan theo price c1
//        System.out.println("-----------------------");
//        list.stream().sorted((o1,o2) -> Double.compare(o2.getPrice(), o1.getPrice())).
//                forEach(p -> System.out.println(p.getProductName() +" " + p.getPrice()));
//        // sap xep tang dan theo price c2
//        System.out.println("----------------------");
//        list.stream().sorted(Comparator.comparing(Order::getPrice)).forEach(p -> System.out.println(p.getProductName() + " " + p.getPrice()));
//        // sap xep giam dan theo price c2
//        System.out.println("--------------------------------");
//        list.stream().sorted(Comparator.comparing(Order::getPrice).reversed()).forEach(p -> System.out.println(p.getProductName() + " " + p.getPrice()));
//        // sap xep tang dan theo price neu price bang nhau sap giam tang dan theo productName
//        System.out.println("------------------------");
//        list.stream().sorted(Comparator.comparing(Order::getPrice).thenComparing(Order::getProductName)).
//                forEach(p -> System.out.println(p.getProductName() + " " + p.getPrice()));
//        System.out.println("-----------------------");
//        // sap xep giam dan theo price neu price bang nhau thi sap xep giam dan theo productName();
//        list.stream()
//                .sorted(
//                        Comparator.comparing(Order::getPrice).reversed()
//                                .thenComparing(Comparator.comparing(Order::getProductName).reversed())
//                )
//                .forEach(p -> System.out.println(p.getProductName() + " " + p.getPrice()));


        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        numbers.stream()
                .filter(n -> n % 2 == 0)                  // Lọc số chẵn
                .peek(n -> System.out.println("Found: " + n)) // In ra số chẵn
                .map(n -> n * n)                          // Bình phương
                .forEach(System.out::println);            // In kết quả



    }
}