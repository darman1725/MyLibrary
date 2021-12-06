package org.aplas.mylibrary;

import org.aplas.mylibrary.viewmodels.Book;
import org.aplas.mylibrary.viewmodels.BookList;
import org.aplas.mylibrary.viewmodels.User;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

//Test Java class: Book, User, Booklist
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestC1MyData050 extends ViewTest {
   //private Distance unit;

    @Test
    public void check_01_Book_Class_Properties() {
        String packname = "org.aplas.mylibrary.viewmodels";
        String cname = "Book";
        try {
            Class<?> c = Class.forName(packname+"."+cname);
            Constructor<?> ctor = c.getConstructor();
            Object obj = ctor.newInstance();
            assertEquals("Class "+cname+" should be in 'models' directory", packname, obj.getClass().getPackage().getName());
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            failTest("Class " + cname + " is not defined ("+ e.toString() +")");
        }
    }

    @Test
    public void check_02_Book_Class_Empty_Constructor() {
        String[] fields = {"bookNumber", "bookTitle", "bookType", "bookYear"};
        String[] values = {"0th", "", "", ""};
        Book book = new Book();
        String msg = "Mistake in constructor Book() => field ";

        for (int i=0; i<fields.length; i++) {
            Object obj = getFieldValue(book,fields[i]);

            assertNotNull("Field '"+fields[i]+"' is not defined", obj);
            assertEquals(msg + "'" + fields[i] + "'", values[i], obj.toString());
        }
    }

    @Test
    public void check_03_Book_Class_Filled_Constructor() {
        int x = getRandomInteger(1,20);
        String[] fields = {"bookNumber", "bookTitle", "bookType", "bookYear"};
        String[] vars = {"number", "title", "type", "year"};
        String[] values = {ordinal(x), getRandomString(20), getRandomString(10), Integer.toString(getRandomInteger(1900,2020))};
        Book book = new Book(x,values[1],values[2],values[3]);
        String msg = "Mistake in constructor Book(int number, String title, String type, String year) => field ";

        for (int i=0; i<fields.length; i++) {
            Object obj = getFieldValue(book,fields[i]);
            assertNotNull("Field '"+fields[i]+"' is not defined", obj);
            assertTrue(msg+"'"+fields[i]+"' should be set from '"+vars[i]+"'",values[i].equals(obj.toString()));
        }
    }

    @Test
    public void check_04_Book_Class_setBook() {
        int x = getRandomInteger(1,20);
        String[] fields = {"bookNumber", "bookTitle", "bookType", "bookYear"};
        String[] vars = {"number", "title", "type", "year"};
        String[] values = {ordinal(x), getRandomString(20), getRandomString(10), Integer.toString(getRandomInteger(1900,2020))};
        Book book = new Book();
        book.setBook(x,values[1],values[2],values[3]);
        String msg = "Mistake in method setBook(int number, String title, String type, String year) => field ";

        for (int i=0; i<fields.length; i++) {
            Object obj = getFieldValue(book,fields[i]);
            assertNotNull("Field '"+fields[i]+"' is not defined", obj);
            assertTrue(msg+"'"+fields[i]+"' should be set from '"+vars[i]+"'",values[i].equals(obj.toString()));
        }
    }

    @Test
    public void check_05_User_Class_Properties() {
        String packname = "org.aplas.mylibrary.viewmodels";
        String cname = "User";
        try {
            Class<?> c = Class.forName(packname+"."+cname);
            Constructor<?> ctor = c.getConstructor(String.class, String.class, String.class, int.class);
            Object obj = ctor.newInstance("","","",0);
            assertEquals("Class "+cname+" should be in 'models' directory", packname, obj.getClass().getPackage().getName());
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            failTest("Class " + cname + " is not defined ("+ e.toString() +")");
        }
    }

    @Test
    public void check_06_User_Class_Filled_Constructor() {
        String name = getRandomString(20);
        String country = getRandomString(10);
        String phone = Integer.toString(getRandomInteger(1000000,9000000));
        int color = getRandomInteger(10000,99000);
        User user = new User(name,country,phone,color);

        String f = "profile";
        Object obj = getFieldValue(user,f);
        assertNotNull("Field '"+f+"' is not defined", obj);
        String msg = "Field 'profile' should contains value of ";
        assertTrue(msg + "'name'",obj.toString().contains(name));
        assertTrue(msg + "'country'",obj.toString().contains(country));
        assertTrue(msg + "'phone'",obj.toString().contains(phone));

        f = "color";
        obj = getFieldValue(user,f);
        assertNotNull("Field '"+f+"' is not defined", obj);
        assertTrue("Field 'color' should be equal to value of 'color'",obj.toString().equals(Integer.toString(color)));
    }

    @Test
    public void check_07_User_Class_setProfile() {
        User user = new User("","","",0);

        String name = getRandomString(20);
        String country = getRandomString(10);
        String phone = Integer.toString(getRandomInteger(1000000,9000000));
        user.setProfile(name,country,phone);

        String f = "profile";
        Object obj = getFieldValue(user,f);
        assertNotNull("Field '"+f+"' is not defined", obj);
        String msg = "Field 'profile' should contains value of ";
        assertTrue(msg + "'name'",obj.toString().contains(name));
        assertTrue(msg + "'country'",obj.toString().contains(country));
        assertTrue(msg + "'phone'",obj.toString().contains(phone));
    }

    @Test
    public void check_08_User_Class_setColor() {
        User user = new User("","","",0);

        int color = getRandomInteger(10000,99000);
        user.setColor(color);

        String f = "color";
        Object obj = getFieldValue(user,f);
        assertNotNull("Field '"+f+"' is not defined", obj);
        assertTrue("Field 'color' should be equal to value of 'color'",obj.toString().equals(Integer.toString(color)));
    }

    @Test
    public void check_09_BookList_Class_Properties() {
        String packname = "org.aplas.mylibrary.viewmodels";
        String cname = "BookList";
        try {
            Class<?> c = Class.forName(packname+"."+cname);
            Constructor<?> ctor = c.getConstructor();
            Object obj = ctor.newInstance();
            assertEquals("Class "+cname+" should be in 'models' directory", packname, obj.getClass().getPackage().getName());
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            failTest("Class " + cname + " is not defined ("+ e.toString() +")");
        }
    }

    @Test
    public void check_10_BookList_Class_Filled_Constructor() {
        BookList bl = new BookList();
        testItem(null,bl.getList(),"Contructor 'BookList()' should not set anything",5);
        ArrayList<String> list = new ArrayList<String>();
        bl = new BookList(list);
        testItem(bl.getList(),list,"Contructor 'BookList(ArrayList<String>)' should set the field 'list' that can be returned by method 'getList'",1);
    }

    @Test
    public void check_11_BookList_Class_setBookList_getList() {
        BookList bl = new BookList();
        testItem(null,checkMethodInBookList("setBookList", ArrayList.class),"Method 'setBookList(ArrayList<String> booklist)' is not exist",3);
        testItem(null,checkMethodInBookList("getList", null),"Method 'getList()' is not exist",3);

        ArrayList<String> list = new ArrayList<String>();
        bl.setBookList(list);
        ArrayList<String> list2 = bl.getList();
        testItem(bl.getList(),list,"Method 'setBookList' should set the field 'list' that can be returned by method 'getList'",1);
    }

    private String ordinal(int i) {
        String[] suffixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + suffixes[i % 10];

        }
    }

    private Object getObject(String className) {
       try {
           Class<?> clazz = Class.forName(className);
           Constructor<?> ctor = clazz.getConstructor(String.class);
           return ctor.newInstance(new Object[]{""});
       } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e1) {
           //failTest("Class " + className + " is not defined");
           return null;
       }
    }

    private boolean checkMethodInBookList(String name, Class<?> param) {
        Method methodToFind = null;
        try {
            if (param == null) {
                methodToFind = BookList.class.getMethod(name, (Class<?>[])null);
            }  else {
                methodToFind = BookList.class.getMethod(name, param);
            }
            return true;
        } catch (NoSuchMethodException | SecurityException e) {
            System.out.println(e.toString());
            return false;
        }
    }
}
