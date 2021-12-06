package org.aplas.mylibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.widget.Button;
import android.widget.EditText;

import org.aplas.mylibrary.databinding.ActivityInputDataBindingImpl;
import org.aplas.mylibrary.databinding.ActivityShowDataBindingImpl;
import org.aplas.mylibrary.models.BookData;
import org.aplas.mylibrary.models.UserData;
import org.aplas.mylibrary.viewmodels.Book;
import org.aplas.mylibrary.viewmodels.BookList;
import org.aplas.mylibrary.viewmodels.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

//ShowDataActivity
public class TestC1MyData101 extends ViewTest {
    ActivityScenario<MainActivity> scenario;
    String[] types = {"Fiction", "Magazine", "Non Fiction", "Type"};
    User currUser;

    @Before
    public void initTest() {
        scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.STARTED);
    }

    private ShowDataActivity startShowDataActivity(MainActivity activity) {
        currUser = completingMainActivity(activity);
        Intent intent = Shadows.shadowOf(activity).getNextStartedActivity();
        InputDataActivity inputDataActivity = Robolectric.buildActivity(InputDataActivity.class,intent).create().start().get();
        ActivityInputDataBindingImpl bindingInput = DataBindingUtil.getBinding(getRootView(inputDataActivity));
        assert bindingInput != null;
        bindingInput.setLifecycleOwner(inputDataActivity);
        bindingInput.btnShow.performClick();
        intent = Shadows.shadowOf(activity).getNextStartedActivity();
        return Robolectric.buildActivity(ShowDataActivity.class,intent).create().start().get();
    }

    public User completingMainActivity(MainActivity activity) {
        String name = getRandomString(20);
        String country = getRandomString(10);
        String phone = Integer.toString(getRandomInteger(10000,99999));
        int color = Color.RED;
        User user = new User(name, country, phone, color);

        EditText etName = activity.findViewById(R.id.editUserName);
        EditText etCountry = activity.findViewById(R.id.editCountry);
        EditText etPhone = activity.findViewById(R.id.editPhone);
        Button btn = activity.findViewById(R.id.btnSaveUser);

        etName.setText(name);
        etCountry.setText(country);
        etPhone.setText(phone);
        activity.bgColor = color;
        activity.layout.setBackgroundColor(activity.bgColor);
        btn.performClick();

        return user;
    }

    public User saveUserData(MainActivity activity) {
        String name = getRandomString(20);
        String country = getRandomString(10);
        String phone = Integer.toString(getRandomInteger(10000,99999));
        int color = Color.RED;
        User user = new User(name, country, phone, color);
        UserData data = new UserData(activity);
        data.saveUserData(name, country, phone, color);
        return user;
    }

    @Test
    public void check_01_Content_at_ViewData() {
        scenario.onActivity(main -> {
            ShowDataActivity activity = startShowDataActivity(main);
            ActivityShowDataBindingImpl binding = DataBindingUtil.getBinding(getRootView(activity));
            binding.setLifecycleOwner(activity);

            //check profile
            testItem(currUser.getProfile(), binding.txtProfile.getText().toString(),
                    "The TextView 'txtProfile' should show the correct user profile", 1);

            BookData bookData = new BookData(main);
            int count = getRandomInteger(5,8);
            ArrayList<String> titleList = new ArrayList<>();
            ArrayList<String> yearList = new ArrayList<>();
            ArrayList<String> typeList = new ArrayList<>();
            ArrayList<String> headerList = new ArrayList<>();
            for (int i=0; i<count; i++) {
                String header = getRandomString(10);
                String title = header+" "+getRandomString(7)+" "+getRandomString(10);
                String year = ""+getRandomInteger(1900,2050);
                String type = types[getRandomInteger(0,3)];
                bookData.saveBookData(title,type,year);
                titleList.add(title);
                yearList.add(year);
                typeList.add(type);
                headerList.add(header);
            }
            BookList bookList = bookData.getBookList();
            binding.setBooklist(bookList);
            binding.setBook(bookData.getBookData(bookList.getList().get(0)));
            binding.executePendingBindings();

            testItem(null, bookData.getBookList().getSize()==binding.getBooklist().getSize(),
                    "The content of Spinner 'spBook' should show all stored books data", 3);
            testItem(null, bookData.getBookList().getSize()==binding.spBook.getCount(),
                    "The content of Spinner 'spBook' should show all stored books data", 3);

            ElementTest spinner = new ElementTest(binding.spBook);
            spinner.testSpinnerContentUnordered(bookData.getBookList().getList());
            testItem(binding.getBooklist().getList().get(0), spinner.getSelectedSpinnerItem(),
                    "The selected item of Spinner 'spBook' should show the first book", 1);

            int idx = headerList.indexOf(bookData.getBookList().getList().get(0).split("_")[0]);

            String expected = "1st Book Data";
            testItem(null, expected.equals(binding.txtBook.getText().toString()),
                    "The TextView 'txtBook' should show '"+expected+"' ", 3);

            //for (int i=0; i<titleList.size(); i++) System.out.println(titleList.get(i)+" == "+bookList.get(i));
            expected = "Title: "+titleList.get(idx);
            testItem(null, expected.equals(binding.txtTitle.getText().toString()),
                    "The TextView 'txtBook' should show the correct first book title => Title: <BookTitle>", 3);
            expected = "Type: "+typeList.get(idx);
            testItem(null, expected.equals(binding.txtType.getText().toString()),
                    "The TextView 'txtType' should show the correct first book type => Type: <BookType>", 3);
            expected = "Year: "+yearList.get(idx);
            testItem(null, expected.equals(binding.txtYear.getText().toString()),
                    "The TextView 'txtYear' should show the correct first book year => Year: <BookTitle>", 3);
            clearUserData(activity);
        });
    }

    @Test
    public void check_02_Selecting_Spinner_spBook() {
        scenario.onActivity(main -> {
            BookData bookData = new BookData(main);
            int count = getRandomInteger(5,8);
            ArrayList<String> titleList = new ArrayList<>();
            ArrayList<String> yearList = new ArrayList<>();
            ArrayList<String> typeList = new ArrayList<>();
            ArrayList<String> headerList = new ArrayList<>();
            for (int i=0; i<count; i++) {
                String header = getRandomString(10);
                String title = header+" "+getRandomString(7)+" "+getRandomString(10);
                String year = ""+getRandomInteger(1900,2050);
                String type = types[getRandomInteger(0,3)];
                bookData.saveBookData(title,type,year);
                titleList.add(title);
                yearList.add(year);
                typeList.add(type);
                headerList.add(header);
            }

            Intent intent = new Intent(main, ShowDataActivity.class);
            intent.putExtra("filename", "");
            main.startActivity(intent);
            ShowDataActivity activity = Robolectric.buildActivity(ShowDataActivity.class,intent).create().start().get();
            ActivityShowDataBindingImpl binding = DataBindingUtil.getBinding(getRootView(activity));
            binding.setLifecycleOwner(activity);

            ArrayList<String> bookList = bookData.getBookList().getList();

            for (int i=0; i<3; i++) {
                binding.spBook.setSelection(getRandomInteger(1, count - 1));
                String bookFile = binding.spBook.getSelectedItem().toString();
                binding.executePendingBindings();


                int idxData = bookList.indexOf(bookFile);
                int idxList = headerList.indexOf(bookFile.split("_")[0]);
                Book book = new Book(idxData + 1, titleList.get(idxList), typeList.get(idxList), yearList.get(idxList));

                String expected = book.getBookNumber() + " Book Data";

                //System.out.println(expected + " == " + binding.txtBook.getText().toString() + " ~~ " + binding.getBook().getBookNumber() + binding.getBook().getBookTitle());
                testItem(null, expected.equals(binding.txtBook.getText().toString()),
                        "The TextView 'txtBook' should show correct book number of selected data on Spinner 'spBook' ", 3);

                expected = "Title: " + book.getBookTitle();
                testItem(null, expected.equals(binding.txtTitle.getText().toString()),
                        "The TextView 'txtBook' should show the correct book title of selected data on Spinner 'spBook'", 3);
                expected = "Type: " + book.getBookType();
                testItem(null, expected.equals(binding.txtType.getText().toString()),
                        "The TextView 'txtType' should show the correct book type of selected data on Spinner 'spBook'", 3);
                expected = "Year: " + book.getBookYear();
                testItem(null, expected.equals(binding.txtYear.getText().toString()),
                        "The TextView 'txtYear' should show the correct book year of selected data on Spinner 'spBook'", 3);
            }
            clearUserData(activity);
        });
    }

    @Test
    public void check_03_Delete_Data_Button() {
        scenario.onActivity(main -> {
            Context context = main.getApplicationContext();
            Intent intent = new Intent(main, ShowDataActivity.class);
            intent.putExtra("filename", "");
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            main.startActivity(intent);

            ShowDataActivity activity = Robolectric.buildActivity(ShowDataActivity.class,intent).create().start().get();
            ActivityShowDataBindingImpl binding = DataBindingUtil.getBinding(getRootView(activity));
            assert binding != null;
            binding.setLifecycleOwner(activity);

            BookData bookData = new BookData(context);
            int count = 5;
            for (int i=0; i<count; i++) {
                String title = getRandomString(10)+" "+getRandomString(7)+" "+getRandomString(10);
                String year = ""+getRandomInteger(1900,2050);
                String type = types[getRandomInteger(0,3)];
                bookData.saveBookData(title,type,year);
            }

            BookList bookList = bookData.getBookList();
            binding.setBooklist(bookList);
            binding.setBook(bookData.getBookData(bookList.getList().get(0)));
            binding.executePendingBindings();

            binding.spBook.setSelection(getRandomInteger(2, bookData.getBookList().getSize()-1));
            binding.executePendingBindings();
            //System.out.println("File "+fn+" "+isFilelocked(context,h));
            binding.btnDelData.performClick();
            testItem(null, ShadowToast.getTextOfLatestToast().toLowerCase().contains("success"),
                    "A Toast informing 'Deleting is Success!!' should appear if deleting success", 3);
            //System.out.println(bookFile+" ^^ "+binding.getBooklist().getSize()+" --- "+binding.spBook.getCount()+"++++"+ShadowToast.getTextOfLatestToast());

            binding.executePendingBindings();
            binding.spBook.setSelection(0);
            String fn = (String)binding.spBook.getSelectedItem();
            if (bookData.deleteBookData(fn)) {
                binding.btnDelData.performClick();
                testItem(null, ShadowToast.getTextOfLatestToast().toLowerCase().contains("fail"),
                        "A Toast informing 'Deleting is Failed!!' should appear if deleting failed", 3);
            }
            clearUserData(activity);
        });
    }

    @Test
    public void check_04_Add_Data_Button() {
        scenario.onActivity(main -> {
            Context context = main.getApplicationContext();
            Intent intent = new Intent(main, ShowDataActivity.class);
            intent.putExtra("filename", "");
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            main.startActivity(intent);

            ShowDataActivity activity = Robolectric.buildActivity(ShowDataActivity.class,intent).create().start().get();
            ActivityShowDataBindingImpl binding = DataBindingUtil.getBinding(getRootView(activity));
            assert binding != null;
            binding.setLifecycleOwner(activity);

            binding.btnAddData.performClick();
            intent = Shadows.shadowOf(activity).getNextStartedActivity();
            testItem(null, intent, "Activity 'InputDataActivity' should be opened after adding a new book", 6);
            testItem(".InputDataActivity", intent.getComponent().getShortClassName(),
                    "Activity 'InputDataActivity' should be opened after adding a new book", 1);

            clearUserData(activity);
        });
    }

    @Test
    public void check_05_Edit_Data_Button() {
        scenario.onActivity(main -> {
            Context context = main.getApplicationContext();
            Intent intent = new Intent(main, ShowDataActivity.class);
            intent.putExtra("filename", "");
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            main.startActivity(intent);

            ShowDataActivity activity = Robolectric.buildActivity(ShowDataActivity.class,intent).create().start().get();
            ActivityShowDataBindingImpl binding = DataBindingUtil.getBinding(getRootView(activity));
            assert binding != null;
            binding.setLifecycleOwner(activity);

            BookData bookData = new BookData(context);
            int count = 5;
            for (int i=0; i<count; i++) {
                String title = getRandomString(10)+" "+getRandomString(7)+" "+getRandomString(10);
                String year = ""+getRandomInteger(1900,2050);
                String type = types[getRandomInteger(0,3)];
                bookData.saveBookData(title,type,year);
            }

            BookList bookList = bookData.getBookList();
            binding.setBooklist(bookList);
            binding.setBook(bookData.getBookData(bookList.getList().get(0)));
            binding.executePendingBindings();

            binding.spBook.setSelection(getRandomInteger(2, bookData.getBookList().getSize()-1));
            binding.executePendingBindings();
            String filename = (String)binding.spBook.getSelectedItem();
            binding.btnEditData.performClick();

            intent = Shadows.shadowOf(activity).getNextStartedActivity();
            testItem(null, intent, "Activity 'InputDataActivity' should be opened after adding a new book", 6);
            testItem(".InputDataActivity", intent.getComponent().getShortClassName(),
                    "Activity 'InputDataActivity' should be opened after adding a new book", 1);
            testItem(null, intent.getStringExtra("filename"),
                    "Intent should contain extra 'filename' with correct value (selected value of Spinner 'spBook')", 6);
            testItem(null, filename.equals(intent.getStringExtra("filename")),
                    "Intent should contain extra 'filename' with correct value (selected value of Spinner 'spBook')", 3);

            InputDataActivity activity2 = Robolectric.buildActivity(InputDataActivity.class,intent).create().start().get();
            ActivityInputDataBindingImpl binding2 = DataBindingUtil.getBinding(getRootView(activity2));
            assert binding2 != null;
            binding2.setLifecycleOwner(activity2);
            bookData = new BookData(context);
            Book book = bookData.getBookData(filename);

            testItem(book.getBookTitle(), binding2.editTitle.getText().toString(),
                    "The EditText 'editTitle' should be empty for first time", 1);
            testItem(book.getBookYear(), binding2.editYear.getText().toString(),
                    "The EditText 'editYear' should be empty for first time", 1);
            testItem(book.getBookType(), (String)binding2.spType.getSelectedItem(),
                    "The Spinner 'spType' should show the first item at first time", 1);

            clearUserData(activity);
        });
    }

    public void clearUserData(Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        pref.edit().clear().apply();
    }

    public String getFileName(String title) {
        return title.replace(" ","_")+".data";
    }

    public String getTitle(String filename) {
        return filename.replace(".data","").replace("_"," ");
    }

    public void del(Context context, String fn) {
        File filesDir = context.getFilesDir();
        File file = new File(filesDir, fn);
        //file.d
        System.out.println(file.exists()+" - "+file.getAbsolutePath());
        boolean res = context.deleteFile(file.getName());
        Assert.assertTrue("Ga bisa delete "+file.getName()+" - "+file.exists(),res);
    }

    public boolean isFilelocked(Context context, String fn) {
        File filesDir = context.getFilesDir();
        File file = new File(filesDir, fn);
        try {
            try (FileInputStream in = new FileInputStream(file)) {
                in.read();
                return false;
            }
        } catch (FileNotFoundException e) {
            return file.exists();
        } catch (IOException ioe) {
            return true;
        }
    }
}

