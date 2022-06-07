package dk.dtu.compute.se.pisd.roborally_server.server.service;

import java.util.List;

/**
 * JSON Service interface.
 *
 * @author Marcus Sand, mwasa@dtu.dk (s215827)
 */
public interface IJSONService {
    /**
     * Gets filenames of JSON-files in folder.
     * (folder can be in resources, appdata or jar)
     *
     * @param foldername name of folder
     * @return the filenames, if any
     * @author Marcus Sand, mwasa@dtu.dk (s215827)
     */
    public List<String> getFolderJSON(String foldername);
}
