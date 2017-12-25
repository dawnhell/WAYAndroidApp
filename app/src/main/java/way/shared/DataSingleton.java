package way.shared;

public class DataSingleton {
    public static DataSingleton instance = null;
    private static String fileName = null;

    protected DataSingleton(String newFileName) {
        fileName = newFileName;
    }

    public static DataSingleton getInstance(String fileName) {
        if (instance == null) {
            return new DataSingleton(fileName);
        }

        return instance;
    }
}
