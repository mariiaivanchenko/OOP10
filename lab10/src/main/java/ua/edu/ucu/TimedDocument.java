package ua.edu.ucu;

public class TimedDocument extends SmartDocument {
    private static SmartDocument doc;
    
    public TimedDocument(String gcsPath) {
        super(gcsPath);
        doc = new SmartDocument(gcsPath);
    }
    
    public static long timeMeasured() {
        long startTime = System.nanoTime();
        doc.parse();
        long endTime = System.nanoTime();
        return endTime - startTime;
    }
}
