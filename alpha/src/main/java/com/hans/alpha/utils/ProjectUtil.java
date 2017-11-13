package com.hans.alpha.utils;

/**
 * Created by Changel on 2016/2/17.
 */
public class ProjectUtil {

    public static String getProjectName(){
        String projectname = System.getProperty("user.dir");
        String pn = projectname.substring(projectname.lastIndexOf("\\")+1,projectname.length());
        return pn;
    }

}
