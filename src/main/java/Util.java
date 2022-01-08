import java.io.*;
import java.net.URL;
import java.nio.file.Files;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class Util {
    public static void runCommand(String[] command, File dir) {
        System.out.println("run jar...");
        String result;
        Process process=null;
        Runtime runtime = Runtime.getRuntime();
        try {
            process= runtime.exec(command, null, dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("out put log...");
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
}
