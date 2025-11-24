package JavaNetworking;
import java.net .*;
import java.io .*;
public class SampleURL {
    public static void main(String[] args) throws IOException {


        URL url = new URL("http://tayjava.vn");
        BufferedReader in = new BufferedReader(new
                InputStreamReader(url.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null)
            System.out.println(inputLine);


        in.close();


    }
}