import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class CreatePatch {
    public static void main(String[] args) {
        logger("write minecraft version");
        String version;
        Scanner scanner = new Scanner(System.in);
        version = scanner.nextLine();

        logger("get files...");
        Path path= Paths.get("");
        File file=new File(path.toAbsolutePath().toString());
        File src=new File(file,"src");
        File main=new File(src,"main");
        File resources=new File(main,"resources");
        resources.mkdirs();

        File work=new File(file,"work");

    }

    public static void logger(String s) {
        System.out.println(s);
    }
}
