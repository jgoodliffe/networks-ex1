import java.io.File;

/**
 *
 */
public class FileServer {

    public File fileDirectory;
    public File[] webFiles;
    public File selectedFile;


    /**
     *
     */
    public FileServer() {
        fileDirectory = new File("ex1/web/");
        webFiles = fileDirectory.listFiles();
        selectedFile = null;
    }



    public File getFile(String query){
        try{
            query = "ex1/web"+query;
            //System.out.println(query);
            File file = new File(query);

            if(!file.isFile()){
                return get404();
            }
            return file;

        } catch(Exception e){
            System.err.println("File does not exist..");
            e.printStackTrace();
            return null;
        }
    }


    public File getIndex(){
        return getFile("/index.html");
    }

    public File get404(){
        return getFile("/404.html");
    }

    public File get500(){
        return getFile("/500.html");
    }

    public File get403(){
        return getFile("/403.html");
    }
}
