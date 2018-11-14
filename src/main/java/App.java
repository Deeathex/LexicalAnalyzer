import java.io.File;

public class App {
    public static void main(String[] args) {
        File file = new File("src/main/resources/");
        for(String fileNames : file.list()) System.out.println(fileNames);
    }
}
