package scm.vss.project;

import hudson.model.TaskListener;

/**
 * Created by BilenO on 21/07/2014.
 */
public interface IProjectFile {
    String[] getListOfLocalFiles();
    String[] getListOfVssPaths();
    void setFile(String aLocalProjectFile);
    void setListener(TaskListener aListener);
}
