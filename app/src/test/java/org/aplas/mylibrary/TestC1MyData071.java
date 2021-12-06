package org.aplas.mylibrary;

import android.content.Context;
import android.content.SharedPreferences;

import org.aplas.mylibrary.models.BookData;
import org.aplas.mylibrary.models.UserData;
import org.aplas.mylibrary.viewmodels.Book;
import org.aplas.mylibrary.viewmodels.BookList;
import org.aplas.mylibrary.viewmodels.User;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

//UserData with SharedPreferences and BookData with FileAccess
public class TestC1MyData071 extends ViewTest {

    private final String SHARED_ID = "userdata";
    private final String tag_name = "name";
    private final String tag_country = "country";
    private final String tag_phone = "phone";
    private final String tag_color = "color";
    private final String FILE_EXT = ".data";

    ActivityScenario<MainActivity> scenario;

    @Before
    public void initTest() {
        scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
    }

    @Test
    public void check_01_UserData_Class_Properties() {
        scenario.onActivity(activity -> {
            String packname = "org.aplas.mylibrary.models";
            String cname = "UserData";
            Object obj = null;
            try {
                Class<?> c = Class.forName(packname+"."+cname);
                Constructor<?> ctor = c.getConstructor(Context.class);
                obj = ctor.newInstance(activity.getApplicationContext());
                testItem(packname, obj.getClass().getPackage().getName(),
                        "Class "+cname+" should package '"+packname+"."+cname.toLowerCase()+"'", 1);
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                failTest("Class " + cname + " is not defined ("+ e.toString() +")");
            }

            //Check method
            String[] methods = {"getUserData", "clearUserData", "isUserDataExist"};
            String[] returns = {"User", "void", "boolean"};
            for (int i=0; i<methods.length; i++) {
                Method m = getMethodInObject(obj,methods[i]);
                testItem(null,m,"Method '"+methods[i]+"' is not defined or not public", 6);
                testItem(returns[i], m.getReturnType().getSimpleName(), "Result '" + methods[i] + "()' should be "+returns[i], 1);
            }

            String mname = "saveUserData";
            Method m = getMethodInObject(obj,"saveUserData",String.class,String.class,String.class,int.class);
            testItem(null,m,"Method '"+mname+"' is not defined or not public", 6);
            testItem("void", m.getReturnType().getSimpleName(),
                    "Result '" + mname + "(String name, String country, String phone, int color)' should be void", 1);
        });
    }

    @Test
    public void check_02_UserData_Class_Filled_Constructor() {
        scenario.onActivity(activity -> {
            UserData data = new UserData(activity);
            Object pref = getFieldValue(data,"pref");
            testItem("SharedPreferencesImpl", pref.getClass().getSimpleName(),"The field 'pref' should be a 'SharedPreferences'",1);
            testItem(activity.getSharedPreferences(SHARED_ID, Context.MODE_PRIVATE),pref,
                    "Field 'pref' is not assigned from SharedPreferences '"+SHARED_ID+"'",1);

            SharedPreferences sp = (SharedPreferences)pref;
            testItem((sp.getAll().size() == 4) && sp.contains("name"),
                    data.isUserDataExist(), "Method 'isUserDataExist()' is not correct (check the content of field 'pref')", 1);

            if (data.isUserDataExist()) {
                sp.edit().clear().apply();
            }
            User user = data.getUserData();
            int color = 255*255*255;
            testItem(color, user.getColor(), "Method getUserData() should return "+color+" when SharedPreferences empty", 1);
        });
    }

    @Test
    public void check_03_UserData_Class_saveUserData() {
        scenario.onActivity(activity -> {
            UserData data = new UserData(activity);
            String name = getRandomString(20);
            String country = getRandomString(10);
            String phone = Integer.toString(getRandomInteger(100000,990000));
            int color = getRandomInteger(1000,9999);
            data.saveUserData(name,country,phone,color);

            testSharedPreferences(data, name, country, phone, color);

            User user = data.getUserData();
            //String msg = "User profile should contains 'name' submitted from 'saveUserData' ";
            testItem(null, user.getProfile().contains(name),
                    "User profile should contains 'name' submitted from 'saveUserData' ", 3);
            testItem(null, user.getProfile().contains(country),
                    "User profile should contains 'country' submitted from 'saveUserData' ", 3);
            testItem(null, user.getProfile().contains(phone),
                    "User profile should contains 'phone' submitted from 'saveUserData' ", 3);

            testItem(color, user.getColor(), "User color should be from 'phone' submitted from 'saveUserData'", 1);
        });
    }

    private void testSharedPreferences(UserData data, String name, String country, String phone, int color) {
        SharedPreferences pref = (SharedPreferences)getFieldValue(data,"pref");
        testItem(null,pref,"SharedPreferences 'pref' should not be null", 6);

        testItem(color, pref.getInt(tag_color, 255*255*255),
                "SharedPreferences should contains 'color' submitted from 'saveUserData' ", 1);
        testItem(name, pref.getString(tag_name, ""),
                "SharedPreferences should contains 'name' submitted from 'saveUserData' ", 1);
        testItem(country, pref.getString(tag_country, ""),
                "SharedPreferences should contains 'country' submitted from 'saveUserData' ", 1);
        testItem(phone, pref.getString(tag_phone, ""),
                "SharedPreferences should contains 'phone' submitted from 'saveUserData' ", 1);
    }

    @Test
    public void check_04_BookData_Class_Properties() {
        scenario.onActivity(activity -> {
            String packname = "org.aplas.mylibrary.models";
            String cname = "BookData";
            Object obj = null;
            try {
                Class<?> c = Class.forName(packname+"."+cname);
                Constructor<?> ctor = c.getConstructor(Context.class);
                obj = ctor.newInstance(activity.getApplicationContext());
                testItem(packname, obj.getClass().getPackage().getName(),
                        "Class "+cname+" should package '"+packname+"."+cname.toLowerCase()+"'", 1);
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                failTest("Class " + cname + " is not defined ("+ e.toString() +")");
            }

            //Check method
            String mname = "getBookData";
            String rettype = "Book";
            Method m = getMethodInObject(obj,mname,String.class);
            testItem(null,m,"Method '"+mname+"' is not defined or not public", 6);
            testItem(rettype, m.getReturnType().getSimpleName(), "Result '" + mname + "(String fileName)' should be "+rettype, 1);

            mname = "getBookList";
            rettype = "BookList";
            m = getMethodInObject(obj,mname);
            testItem(null,m,"Method '"+mname+"' is not defined or not public", 6);
            testItem(rettype, m.getReturnType().getSimpleName(), "Result '" + mname + "()' should be "+rettype, 1);

            mname = "saveBookData";
            rettype = "boolean";
            m = getMethodInObject(obj,mname,String.class,String.class,String.class);
            testItem(null,m,"Method '"+mname+"' is not defined or not public", 6);
            testItem(rettype, m.getReturnType().getSimpleName(),
                    "Result '" + mname + "(String title, String type, String year)' should be "+rettype, 1);

            mname = "getFileName";
            rettype = "String";
            m = getMethodInObject(obj,mname,String.class);
            testItem(null,m,"Method '"+mname+"' is not defined or not public", 6);
            testItem(rettype, m.getReturnType().getSimpleName(),
                    "Result '" + mname + "(String title)' should be "+rettype, 1);
/*
            mname = "loadFileList";
            rettype = "void";
            m = getMethodInObject(obj,mname);
            testItem(null,m,"Method '"+mname+"' is not defined or not public", 6);
            testItem(rettype, m.getReturnType().getSimpleName(), "Result '" + mname + "()' should be "+rettype, 1);
*/
        });
    }

    @Test
    public void check_05_BookData_Class_Filled_Constructor() {
        scenario.onActivity(activity -> {
            BookData data = new BookData(activity);
            Object flist = getFieldValue(data,"fileList");
            testItem("ArrayList", flist.getClass().getSimpleName(),"The field 'fileList' should be a 'ArrayList<String>'",1);
            testItem(null, flist,"Field 'fileList' should be assigned",6);
            BookList blist = data.getBookList();
            testItem(blist.getList(),flist,
                    "Method 'getBookList()' should refer to field 'fileList'",1);
            String title = getRandomString(6)+" "+getRandomString(7)+" "+getRandomString(4);
            testItem(title.replace(" ","_")+FILE_EXT,data.getFileName(title),
                    "Method 'getFileName(String title)' is not correct, it should return '"+FILE_EXT+"' file extension",1);
/*
            SharedPreferences sp = (SharedPreferences)pref;
            testItem((sp.getAll().size() == 4) && sp.contains("name"),
                    data.isUserDataExist(), "Method 'isUserDataExist()' is not correct (check the content of field 'pref')", 1);

            if (data.isUserDataExist()) {
                sp.edit().clear().apply();
            }
            User user = data.getUserData();
            int color = 255*255*255;
            testItem(color, user.color, "Method getUserData() should return "+color+" when SharedPreferences empty", 1);
*/
        });
    }

    @Test
    public void check_06_BookData_Class_saveBookData() {
        scenario.onActivity(activity -> {
            BookData data = new BookData(activity);
            Object flist = getFieldValue(data,"fileList");
            int size1 = ((ArrayList)flist).size();

            String title = getRandomString(6)+" "+getRandomString(7)+" "+getRandomString(4);
            String type = getRandomString(10);
            String year = Integer.toString(getRandomInteger(1900,2100));
            data.saveBookData(title,type,year);

            final String DATA_LOCATION = activity.getFilesDir().getAbsolutePath()+"\\";
            String fname = DATA_LOCATION+data.getFileName(title);
            File f = new File(fname);
            testItem(null,f.exists(),"File '"+data.getFileName(title)+"' should be created after 'saveBookData()'",3);

            String content = getFileContent(f);
            testItem(null, content.contains(title),"File '"+data.getFileName(title)+"' should contain book title",3);
            testItem(null, content.contains(type),"File '"+data.getFileName(title)+"' should contain book type",3);
            testItem(null, content.contains(year),"File '"+data.getFileName(title)+"' should contain book year",3);

            Book book = data.getBookData(data.getFileName(title));
            testItem(null, title.equals(book.getBookTitle()),"Result 'getBookTitle()' should contain book title",3);
            testItem(null, type.equals(book.getBookType()),"Result 'getBookType()' should contain book type",3);
            testItem(null, year.equals(book.getBookYear()),"Result 'getBookYear()' should contain book year",3);

            flist = getFieldValue(data,"fileList");
            int size2 = ((ArrayList)flist).size();
            testItem(size1+1, size2,"Content of 'fileList' should add 1 book data",6);
            BookList blist = data.getBookList();
            testItem(flist, blist.getList(),
                    "Method 'getBookList()' should refer to field 'fileList'",1);
        });
    }

    public String getFileContent(File f) {
        String res = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            String line;
            while((line = reader.readLine()) != null){
                res+=line;
            }
            return res;
        } catch (Exception e) {
            return res;
        }
    }
}

