package Java8;

public class Order {
    private int id;
    private String productName, productType;
    private String vendor;
    private int quantity;
    private double price;

    public Order() {
    }

    public Order(int id, String productName, String productType, String vendor, int quantity, double price) {
        this.id = id;
        this.productName = productName;
        this.productType = productType;
        this.vendor = vendor;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", productType='" + productType + '\'' +
                ", vendor='" + vendor + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
