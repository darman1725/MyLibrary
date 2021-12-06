package org.aplas.mylibrary;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;

import java.util.Arrays;

import androidx.core.content.ContextCompat;

public class ResourceTest extends ViewTest {
    private Resources resource;

    public ResourceTest(Resources rsc) {
        resource =rsc;
    }

    public int getResourceId(String name, String type) {
        return resource.getIdentifier(name, type, getClass().getPackage().getName());
    }

    public void testStringResource(String name) {
        int resId = getResourceId(name,"string");
        testItem(0,resId,"String \'"+name+"\' is not defined in strings.xml of values resource",2);
    }

    public void testStringResource(String name, String expected) {
        int resId = resource.getIdentifier(name, "string", getClass().getPackage().getName());
        testItem(0,resId,"String \'"+name+"\' is not defined in strings.xml of values resource",2);
        testItem(expected,resource.getString(resId),"Value of string \'"+name+"\' is not valid",1);
    }

    public void testIntegerResource(String name, int expected) {
        int resId = resource.getIdentifier(name, "integer", getClass().getPackage().getName());
        testItem(0,resId,"Integer "+name+" id is not exist",2);
        testItem(expected,resource.getString(resId),"Integer "+name+" value is not valid",1);
    }

    public void testStringArrayResource(String name, String[] expected) {
        int resId = resource.getIdentifier(name, "array", getClass().getPackage().getName());
        testItem(0,resId,"String array \'"+name+"\' is not defined in strings.xml of values resource",2);
        String[] str = resource.getStringArray(resId);
        testItem(arrayToString(expected),arrayToString(str),"Value of String array \'"+name+"\' is not valid",1);
    }

    public void testColorResource(Activity activity, String name, String value) {
        int resId = resource.getIdentifier(name, "color", getClass().getPackage().getName());
        testItem(0,resId,"Color '"+name+"' is not defined in colors.xml of value resource",2);
        int clrVal = ContextCompat.getColor(activity, resId);
        //testItem(Color.parseColor(value),clrVal,"Color '"+name+"' value should be '"+value+"'",1);
        testItem(value.toUpperCase(),getHexColor(clrVal),"Color '"+name+"' value should be '"+value+"'",1);
    }

    public void testColorArrayResource(String name, String[] expected) {
        int resId = resource.getIdentifier(name, "array", getClass().getPackage().getName());
        testItem(0,resId,"Color array \'"+name+"\' is not defined in colors.xml of values resource",2);
        int[] str = resource.getIntArray(resId);
        testItem(arrayToString(expected),colorArrayToString(str),
                "Value of Color array \'"+name+"\' is not valid in colors.xml resource",1);
    }

    public void testFontResource(String name, String expected) {
        //int resId = R.font.cambria;
        //Typeface font = Typeface.createFromAsset(resource.getAssets(),name);
        int resId = resource.getIdentifier(name, "font", getClass().getPackage().getName());
        testItem(0,resId,"Font "+name+" id is not exist",2);
        testItem(expected,resource.getResourceEntryName(resId),"Font "+name+" value is not valid",1);
    }

    public void testImgResource(String name) {
        int resId = resource.getIdentifier(name, "drawable", getClass().getPackage().getName());
        testItem(0,resId,"Image "+name+".(png/jpg) is not exist in drawable resource ",2);
    }

    public void testImgAnimationResource(String name) {
        int resId = resource.getIdentifier(name, "drawable", getClass().getPackage().getName());
        testItem(0,resId,"Image animation "+name+".xml is not exist in drawable resource ",2);
    }

    public void testViewAnimationResource(String name) {
        int resId = resource.getIdentifier(name, "anim", getClass().getPackage().getName());
        testItem(0,resId,"View animation "+name+".xml is not exist in anim resource ",2);
    }

    public void testDrawableResource(String name, String expected) {
        int resId = resource.getIdentifier(name, "drawable", getClass().getPackage().getName());
        testItem(0,resId,"Drawable "+name+" id is not exist",2);
        //testItem(expected,resource.getDrawable(resId).,"Drawable "+expected+" value is not valid",1);
    }

    public void testStyleResource(String name) {
        int resId = resource.getIdentifier(name, "style", getClass().getPackage().getName());
        testItem(0,resId,"Style '"+name+"' is not exist in styles.xml",2);
    }

    public void testVideoResource(String name) {
        int resId = resource.getIdentifier(name, "raw", getClass().getPackage().getName());
        testItem(0,resId,"Video '"+name+".(mp4/mpg)' is not exist in raw folder of resource",2);
    }

    public void testAppTheme(Activity activity, String name) {
        int resId = resource.getIdentifier(name, "style", getClass().getPackage().getName());
        testItem(resId,activity.getApplicationInfo().theme,
                "Applied theme in AndroidManifest.xml should be \'@style/"+name+"\'",1);
    }

    public void testAppPermission(Activity activity, String name) {
        try {
            PackageInfo info = activity.getPackageManager().getPackageInfo(activity.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] permissions = info.requestedPermissions;

            testItem(0,permissions.length>0,"Can't find 'uses-permission' in AndroidManifest.xml file",3);
            int x = Arrays.asList(permissions).indexOf(name);
            testItem(0, x>=0, "AndroidManifest.xml must contain permission value '"+name+"'\n"+
                    "put <uses-permission android:name=\""+name+"\" /> ",3);
        } catch (Exception e) {
            testItem(0,0,"Permission value in AndroidManifest.xml error",2);
        }
    }

    public void testAppActivities(Activity activity, String name, String packName) {
        //testItem(0,activity.getPackageManager().getLaunchIntentForPackage(packName+"."+name),
        //        "Activity "+name+" must be LAUNCHER",6);

        try {
            PackageInfo info = activity.getPackageManager().getPackageInfo(activity.getPackageName(), PackageManager.GET_INTENT_FILTERS);
            ActivityInfo[] activities = info.activities;

            String res="";
            for (int i=0; i<activities.length; i++) {
                res+=activities[i].name+"-"+activities[i].applicationInfo.category+"-"+activities[i].processName+"@";
            }
            testItem(0,0,res,2);
            //testItem(0,permissions.length>0,"Can't find 'uses-permission' in AndroidManifest.xml file",3);
            //int x = Arrays.asList(permissions).indexOf(name);
            //testItem(0, x>=0, "AndroidManifest.xml must contain permission value '"+name+"'\n"+
            //        "put <uses-permission android:name=\""+name+"\" /> ",3);
        } catch (Exception e) {
            testItem(0,0,"Permission value in AndroidManifest.xml error"+e.toString(),2);
        }
    }


}
