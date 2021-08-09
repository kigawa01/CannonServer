import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class Setup {
    public static void main(String[] args) {
        String version;
        System.out.println("write minecraft version");
        Scanner scanner = new Scanner(System.in);
        version = scanner.nextLine();

        Path path = Paths.get("");
        File file = new File(path.toAbsolutePath().toString());
        File work = new File(file, "work");
        work.mkdir();
        try {
            download(new URL("https://hub.spigotmc.org/jenkins/job/BuildTools/lastStableBuild/artifact/target/BuildTools.jar"), work, "BuildTools.jar");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] command = {"java", "-jar", "BuildTools.jar", "--rev", version, "--skip-compile"};

        String result;
        Process process = runJar(command, work);
        InputStream inputStream = process.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        try {
            while ((result=bufferedReader.readLine())!=null) {
                System.out.println(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    public static void download(URL url, File file, String name) throws IOException {
        String path = url.getPath();
        File file1 = new File(file, name);

        if (file1.exists()) {
            file1.delete();
        }

        Files.copy(url.openStream(), file1.toPath(), REPLACE_EXISTING);
    }

    public static Process runJar(String[] command, File dir) {
        Runtime runtime = Runtime.getRuntime();
        try {
            return runtime.exec(command, null, dir);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
