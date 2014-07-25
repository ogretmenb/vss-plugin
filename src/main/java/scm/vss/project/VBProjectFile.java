package scm.vss.project;

import hudson.model.TaskListener;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BilenO on 22/07/2014.
 */
public class VBProjectFile implements IProjectFile {
    public static void main(String[] args) {
        //VBProjectFile file = new VBProjectFile("D:\\Projeler\\FinTest\\COBRANDSOURCE\\KK\\Pgmvbsql\\proje5\\Kredkart.vbp",null);
        VBProjectFile file = new VBProjectFile();
        file.setFile("D:\\Program Files\\Jenkins\\jobs\\silinecek\\workspace\\FinTest\\COBRANDSOURCE\\KK\\Pgmvbsql\\proje5\\Kredkart.vbp");


/*
        File f = null;
        String cacnon = null;
        try {
         f = new File("$/FinTest/COBRANDSOURCE/KK/Pgmvbsql/proje5/Kredkart.vbp");
          cacnon = new File( f.getParentFile().getCanonicalPath() + "..\\..\\..\\VBLIB\\REGISDB.bas").getCanonicalPath();
        }
        catch(Exception ex)
        {
            System.out.println(ex.toString());
        }
*/

        String[] listOfFiles = file.getListOfLocalFiles();
        String[] listvssOfFiles = file.getListOfVssPaths();
        System.out.println(listOfFiles.toString());
        System.out.println(listvssOfFiles.toString());

        System.out.println(listvssOfFiles.toString());
    }

    private String projectFile = null;
    transient private TaskListener listener = null;         //todo maybe removed from class since it makes Jenkins Server throw exception when configuration change (serialization), made transient in order to avoid exception
    private final static String MODULE = "Module";
    private final static String CLASS = "Class";
    private final static String FORM = "Form";


    /*public VBProjectFile()
    {

    }*/

    public String[] getListOfLocalFiles(){
        List<String> listOfProjectFiles = new ArrayList<String>(0);
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(new File(projectFile));
        }
        catch(FileNotFoundException ex)
        {
            listener.getLogger().println("[getListOfLocalFiles] file exception:" + ex.toString());
            return (String[])listOfProjectFiles.toArray();
        }

        BufferedReader br = new BufferedReader(fileReader);
        String line = null;
        // if no more lines the readLine() returns null
        try {
            while ((line = br.readLine()) != null) {
                // reading lines until the end of the file
                if(line.startsWith(MODULE) || line.startsWith(CLASS) || line.startsWith(FORM) )
                {
                    File filePath = new File((new File(projectFile)).getParent()+ "\\" +line.substring(line.indexOf("..")));
                    listOfProjectFiles.add(filePath.getCanonicalPath());
                    if(line.startsWith(FORM))
                    {
                        listOfProjectFiles.add(filePath.getCanonicalPath().substring(0,filePath.getCanonicalPath().lastIndexOf(".")) + ".FRX");
                        //listener.getLogger().println("[getListOfLocalFiles] **************fORM INFO:" + filePath.getCanonicalPath().substring(0,filePath.getCanonicalPath().lastIndexOf(".")) + ".FRX");
                    }
                }
            }
        }
        catch(IOException ex)
        {
            listener.getLogger().println("[getListOfLocalFiles] line read exception:" + ex.toString() );
            return (String[])listOfProjectFiles.toArray();
        }
        return (String[])listOfProjectFiles.toArray(new String[0]);
    }

    public String[] getListOfVssPaths() {
        List<String> vssPaths = new ArrayList<String>(0);
        String[] localPaths = getListOfLocalFiles();

        for(String localPath : localPaths ){
            vssPaths.add("$" + localPath.substring(localPath.lastIndexOf("\\workspace\\")+ 10).replace('\\','/'));
        }
        return vssPaths.toArray(new String[0]);
    }

    public void setFile(String aLocalProjectFile){
        projectFile = aLocalProjectFile;
    }

    public void setListener(TaskListener aListener) {
        listener = aListener;
    }


}
