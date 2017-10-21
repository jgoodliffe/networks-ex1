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


//    public Boolean recursiveSearch(String query, File dir){
//        File[] files = dir.listFiles();
//        System.out.println("I'm looking for "+query);
//        for(File file : files){
//            System.out.println("Current File - "+file.getName());
//            if(file.getName().equals(query)){
//                selectedFile = file;
//                return true;
//            }
//
//            if (file.isDirectory()){
//                String query2 = query.toString();
//                query2.substring(query2.indexOf('/')+1); //Remove first '/' in query.
//                System.out.println("New folder search: "+query2 + " within folder "+file.getName());
//                recursiveSearch(query2, file);
//            }
//        }
//        System.out.println("Couldn't find file. 404!");
//        return false;
//    }


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
