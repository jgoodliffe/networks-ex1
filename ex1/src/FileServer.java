import java.io.File;

/**
 * HTTP Web Server - File Server
 * James Goodliffe, 2017
 *
 * Searches and returns files.
 */
public class FileServer {

    public File fileDirectory;
    public File[] webFiles;
    public File selectedFile;



    public FileServer() {
        fileDirectory = new File("ex1/web/");
        webFiles = fileDirectory.listFiles();
        selectedFile = null;
    }


    /**
     * getFile - Looks for a file in the filesystem and returns it.
     * @param query the name of the file to be found.
     * @return the file if found, or otherwise a 404.
     */
    public File getFile(String query){
        try{
            query = "ex1/web" +query;
            //System.out.println(query);
            File file = new File(query);
            if(!file.isFile()){
                return get404();
            }
            return file;

        } catch(Exception e){ //Should never reach this but just in case...
            System.err.println("File does not exist..");
            e.printStackTrace();
            return get500(); //Return a 500 error.
        }
    }

    /**
     * getIndex
     * @return The index page.
     */
    public File getIndex(){
        return getFile("/index.html");
    }

    /**
     * get404
     * @return A 404 Error page.
     */
    public File get404(){
        return getFile("/404.html");
    }

    /**
     * get500
     * @return A 500 Error page.
     */
    public File get500(){
        return getFile("/500.html");
    }

    /**
     * get403
     * @return A 403 Error page.
     */
    public File get403(){
        return getFile("/403.html");
    }
}
