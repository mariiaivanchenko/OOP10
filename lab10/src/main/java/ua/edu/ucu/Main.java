package ua.edu.ucu;

public class Main {
    public static void main(String[] args) {
        SmartDocument sd = new SmartDocument("/Users/mariia/Downloads/url.html");
        System.out.println(sd.parse());
    }
}