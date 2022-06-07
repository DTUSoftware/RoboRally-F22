package dk.dtu.compute.se.pisd.roborally_server.server.service;

import com.google.common.io.Resources;
import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * JSON Service.
 *
 * @author Marcus Sand, mwasa@dtu.dk (s215827)
 */
@Service
public class JSONService implements IJSONService {
    final private static AppDirs appDirs = AppDirsFactory.getInstance();

    /**
     * Function to get the AppData folder.
     *
     * @return the path to the appdata folder.
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public static String getAppDataFolder() {
        return appDirs.getUserDataDir("RoboRally-Server", "prod", "DTU");
    }

    // From RoboRally-Client (AppController)

    /**
     * Gets filenames of JSON-files in folder.
     * (folder can be in resources, appdata or jar)
     *
     * @param foldername name of folder
     * @return the filenames, if any
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public List<String> getFolderJSON(String foldername) {
        List<String> folderFiles = new ArrayList<>();

        // Resource folder files
        List<String> resourceFolderFiles = new ArrayList<>();
        URL folderURL = null;
        File folder = null;
        try {
            folderURL = Resources.getResource(foldername);
            folder = new File(folderURL.getFile());
        } catch (Exception e) {
            if (!e.toString().contains("folder files not found")) {
                if (e.toString().contains("gamestates not found")) {
                    System.out.println("Gamestate folder not found in resources");
                } else {
                    e.printStackTrace();
                }
            }
        }

//        System.out.println("got folder - " + mapsFolder.getPath());

        if (folder != null && !folder.getPath().contains(".jar") && folder.listFiles() != null) {
            for (File file : Objects.requireNonNull(folder.listFiles())) {
                String filename = file.getName();
//                System.out.println(filename);
                if (filename.contains(".json")) {
                    resourceFolderFiles.add(file.getName().replace(".json", ""));
                }
            }
        } else {
            // when we have a .jar file
            // https://mkyong.com/java/java-read-a-file-from-resources-folder/
            try {
                // get path of the current running JAR
                String jarPath = getClass().getProtectionDomain()
                        .getCodeSource()
                        .getLocation()
                        .toURI()
                        .getPath();
//                System.out.println("JAR Path :" + jarPath);

                if (jarPath != null) {
                    // TODO: on some computers Java cannot read the maps from the resources folder in the compiled .jar file. fix it or smthn idk
                    // file walks JAR
                    URI uri = URI.create("jar:file:" + jarPath.replace(" ", "%20"));
                    try (FileSystem fs = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
                        resourceFolderFiles = Files.walk(fs.getPath(foldername))
                                .filter(Files::isRegularFile)
                                .map(p -> p.toString().replace(foldername + "/", "").replace(foldername + "\\", "").replace(".json", ""))
                                .collect(Collectors.toList());
                    } catch (NoSuchFileException e) {
                        if (!e.toString().contains("gamestates")) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        folderFiles.addAll(resourceFolderFiles);

        // Appdata folder files
        List<String> appdataFolderFiles = new ArrayList<>();
        String appdataFolder = getAppDataFolder();
        folder = new File(appdataFolder + "/" + foldername);
        if (folder.listFiles() != null) {
            for (File file : Objects.requireNonNull(folder.listFiles())) {
                String filename = file.getName();
                System.out.println(filename);
                if (filename.contains(".json")) {
                    appdataFolderFiles.add(file.getName().replace(".json", ""));
                }
            }
        }
        folderFiles.addAll(appdataFolderFiles);

        return folderFiles;
    }
}
