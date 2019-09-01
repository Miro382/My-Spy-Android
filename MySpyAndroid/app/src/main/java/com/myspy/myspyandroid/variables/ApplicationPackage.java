package com.myspy.myspyandroid.variables;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Miroslav Murin on 02.02.2017.
 */

public class ApplicationPackage implements Comparable<ApplicationPackage>{

    public String PackageName = "";
    public String ApplicationName = "";

    public ApplicationPackage(String packageName, String applicationName)
    {
        PackageName = packageName;
        ApplicationName = applicationName;
    }


    /**
     * Return String ArrayList (Application name) from ApplicationPackage ArrayList
     * @param Applications ApplicationPackage ArrayList
     * @return ApplicationPackage ArrayList - Application Name
     */
    public static ArrayList<String> ApplicationName(ArrayList<ApplicationPackage> Applications)
    {
        ArrayList<String> name = new ArrayList<String>();

        for(ApplicationPackage app : Applications){

            name.add(app.ApplicationName);
        }

        return name;
    }


    /**
     * Return String ArrayList (Package name) from ApplicationPackage ArrayList
     * @param Applications ApplicationPackage ArrayList
     * @return ApplicationPackage ArrayList - Package name
     */
    public static ArrayList<String>PackageName(ArrayList<ApplicationPackage> Applications)
    {
        ArrayList<String> name = new ArrayList<String>();

        for(ApplicationPackage app : Applications){

            name.add(app.PackageName);
        }

        return name;
    }



    @Override
    public int compareTo(ApplicationPackage another) {
        if(another.PackageName.equals(PackageName))
            return 1;
        else
            return 0;
    }


    @Override
    public boolean equals(Object object)
    {
        if(object.getClass() == ApplicationPackage.class) {
            if (((ApplicationPackage) object).PackageName.equals(PackageName))
                return true;
            else
                return false;
        }else
            return false;
    }

}
