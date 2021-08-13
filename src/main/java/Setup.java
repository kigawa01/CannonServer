import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Setup {
    public static void main(String[] args) {
        System.out.println("write minecraft version");
        String version;
        Scanner scanner = new Scanner(System.in);
        version = scanner.nextLine();

        boolean isSkipBuild = getIsSkipBuild(scanner);

        Path path = Paths.get("");

        logger("make directory...");

        File file = new File(path.toAbsolutePath().toString());
        File work = new File(file, "work");
        work.mkdir();

        File projects = new File(file, "project");
        projects.mkdir();
        File project = new File(projects, version);
        project.mkdir();
        File src = new File(project, "src");
        src.mkdir();
        File lib = new File(project, "lib");
        lib.mkdir();

        File spigot = new File(src, "Spigot");
        delete(spigot);
        File spigotLib = new File(lib, "Spigot");
        delete(spigotLib);

        File builtSpigot = new File(work, "Spigot");

        if (!isSkipBuild) {
            logger("download BuildTools.jar...");
            try {
                Util.download(new URL("https://hub.spigotmc.org/jenkins/job/BuildTools/lastStableBuild/artifact/target/BuildTools.jar"), work, "BuildTools.jar");
            } catch (IOException e) {
                e.printStackTrace();
            }

            logger("running BuildTools.jar...");
            String[] command = {"java", "-jar", "BuildTools.jar", "--rev", version, "--skip-compile"};
            Util.runCommand(command, work);
        } else {
            logger("skip Build Spigot");
        }

        logger("get files...");
        DirTree builtSpigots = new DirTree(builtSpigot);
        List<Path> builtPath = builtSpigots.getPaths(work);

        logger("get path...");
        List<Path> spigots=builtSpigots.getPaths(src);
        List<Path> spigotLibs=builtSpigots.getPaths(lib);
        logger("copy spigot...");
        try {
            for (int i = 0; i < builtPath.size(); i++) {
                logger("copy "+builtPath);
                Files.copy(builtPath.get(i), spigots.get(i));
                Files.copy(builtPath.get(i), spigotLibs.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger("finish");
    }

    public static void logger(String s) {
        System.out.println(s);
    }

    public static boolean getIsSkipBuild(Scanner scanner) {
        logger("skip build spigot(Y/n)");
        boolean skipBuild = true;
        String skip;
        skip = scanner.nextLine();
        if (skip.equals("n")) {
            skipBuild = false;
        } else {
            if (!skip.equals("y") && !skip.equals("Y") && !skip.equals("")) {
                return getIsSkipBuild(scanner);
            }
        }
        return skipBuild;
    }

    public static void delete(File file){
        System.out.println("delete "+file);
        File[] files=file.listFiles();
        if (files!=null){
            for (File file1:files){
                delete(file1);
            }
        }
        file.delete();
    }
}

class DirTree {
    public List<DirTree> subDirs = new ArrayList<>();
    private String name;

    public DirTree(File file) {
        this.name = file.getName();
        System.out.println("get "+name);
        File[] files = file.listFiles();
        if (files != null) {
            for (File file1 : files) {
                subDirs.add(new DirTree(file1));
            }
        }
    }

    public List<Path> getPaths(File parentDir) {
        System.out.println("get "+name+" path...");
        List<Path> pathList = new ArrayList<>();
        File file = new File(parentDir, name);
        pathList.add(file.toPath());
        for (DirTree dirTree : subDirs) {
            pathList.addAll(dirTree.getPaths(file));
        }
        return pathList;
    }
}
