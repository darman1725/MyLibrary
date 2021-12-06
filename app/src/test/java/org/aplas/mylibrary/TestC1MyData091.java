package org.aplas.mylibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.aplas.mylibrary.databinding.ActivityInputDataBindingImpl;
import org.aplas.mylibrary.models.BookData;
import org.aplas.mylibrary.models.UserData;
import org.aplas.mylibrary.viewmodels.Book;
import org.aplas.mylibrary.viewmodels.User;
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

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

//InputDataActivity
public class TestC1MyData091 extends ViewTest {
    ActivityScenario<MainActivity> scenario;
    UserData userData;
    User currUser;

    @Before
    public void initTest() {
        scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.STARTED);
    }

    private InputDataActivity startInputDataActivity(MainActivity activity) {
        currUser = completingMainActivity(activity);
        Intent intent = Shadows.shadowOf(activity).getNextStartedActivity();
        return Robolectric.buildActivity(InputDataActivity.class,intent).create().start().get();
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

    @Test
    public void check_01_Content_at_First_Time() {
        scenario.onActivity(main -> {
            InputDataActivity activity = startInputDataActivity(main);
            ActivityInputDataBindingImpl binding = DataBindingUtil.getBinding(getRootView(activity));
            binding.setLifecycleOwner(activity);

            testItem(null, currUser.getProfile().equals(binding.txtProfile.getText().toString()),
                    "The TextView 'txtProfile' should show the correct user profile", 3);
            testItem("", binding.editTitle.getText().toString(),
                    "The EditText 'editTitle' should be empty for first time", 1);
            testItem("", binding.editYear.getText().toString(),
                    "The EditText 'editYear' should be empty for first time", 1);
            testItem(0, binding.spType.getSelectedItemPosition(),
                    "The Spinner 'spType' should show the first item at first time", 1);
            clearUserData(activity);
        });
    }

    @Test
    public void check_02_btnSaveBook_Click_UnComplete_Data() {
        scenario.onActivity(main -> {
            InputDataActivity activity = startInputDataActivity(main);
            ActivityInputDataBindingImpl binding = DataBindingUtil.getBinding(getRootView(activity));
            binding.setLifecycleOwner(activity);

            //Blank title
            binding.editTitle.setText("");
            binding.editYear.setText(""+getRandomInteger(1900,2050));
            binding.spType.setSelection(getRandomInteger(0,3));
            binding.btnSaveBook.performClick();

            testIncompleteData(activity, "title");

            //Blank year
            binding.editTitle.setText(getRandomString(20));
            binding.editYear.setText("");
            binding.spType.setSelection(getRandomInteger(0,3));
            binding.btnSaveBook.performClick();

            testIncompleteData(activity, "year");
            clearUserData(activity);
        });
    }

    private void testIncompleteData(InputDataActivity activity, String data) {
        Toast toast = ShadowToast.getLatestToast();
        testItem(null, toast, "A toast with a message should be shown when "+data+" was blank", 6);
        ShadowToast.reset();

        //initUserProfile(activity,this);
        Intent intent = Shadows.shadowOf(activity).getNextStartedActivity();
        if (intent!=null) {
            failTest("The new screen ('"+intent.getComponent().getShortClassName()
                    +"') should not be appeared because of incompleteness profile ("+data+")");
        }
    }

    @Test
    public void check_03_btnSaveBook_Click_Complete_Data() {
        scenario.onActivity(main -> {
            InputDataActivity activity = startInputDataActivity(main);
            ActivityInputDataBindingImpl binding = DataBindingUtil.getBinding(getRootView(activity));
            binding.setLifecycleOwner(activity);

            String title = getRandomString(10)+" "+getRandomString(7)+" "+getRandomString(10);
            String year = ""+getRandomInteger(1900,2050);
            BookData data = new BookData(activity);
            String filename = data.getFileName(title);
            Book book = data.getBookData(filename);
            testItem(null, book, "Book '"+filename+"' should not be exist in initial", 5);

            //Blank title
            binding.editTitle.setText(title);
            binding.editYear.setText(year);
            binding.spType.setSelection(getRandomInteger(0,3));
            String type = (String) binding.spType.getSelectedItem();
            binding.btnSaveBook.performClick();

            data = new BookData(activity);
            book = data.getBookData(filename);
            testItem(null, book, "A book data file should be exist after saving a book", 6);
            testItem(null, title.equals(book.getBookTitle()), "The stored book title is not correct!!", 3);
            testItem(null, year.equals(book.getBookYear()), "The stored book year is not correct!!", 3);
            testItem(null, type.equals(book.getBookType()), "The stored book type is not correct!!", 3);

            Intent intent = Shadows.shadowOf(activity).getNextStartedActivity();
            testItem(null, intent, "Activity 'ShowDataActivity' should be opened after adding a new book", 6);
            testItem(".ShowDataActivity", intent.getComponent().getShortClassName(),
                    "Activity 'ShowDataActivity' should be opened after adding a new book", 1);
            testItem(null, intent.getStringExtra("filename"),
                    "Intent should contain extra 'filename' with correct value (filename of data)", 6);
            testItem(null, filename.equals(intent.getStringExtra("filename")),
                    "Intent should contain extra 'filename' with correct value (filename of data)", 3);
            clearUserData(activity);
        });
    }

    @Test
    public void check_04_btnShow_Click() {
        scenario.onActivity(main -> {
            InputDataActivity activity = startInputDataActivity(main);
            ActivityInputDataBindingImpl binding = DataBindingUtil.getBinding(getRootView(activity));
            binding.setLifecycleOwner(activity);

            binding.btnShow.performClick();

            Intent intent = Shadows.shadowOf(activity).getNextStartedActivity();
            testItem(null, intent, "Activity 'ShowDataActivity' should be opened after adding a new book", 6);
            testItem(".ShowDataActivity", intent.getComponent().getShortClassName(),
                    "Activity 'ShowDataActivity' should be opened after adding a new book", 1);
            testItem(null, intent.getStringExtra("filename"),
                    "Intent should contain extra 'filename' with blank value", 6);
            testItem(null, "".equals(intent.getStringExtra("filename")),
                    "Intent should contain extra 'filename' with blank value", 3);
            clearUserData(activity);
        });
    }

    @Test
    public void check_05_btnLogout_Click() {
        scenario.onActivity(main -> {
            InputDataActivity activity = startInputDataActivity(main);
            ActivityInputDataBindingImpl binding = DataBindingUtil.getBinding(getRootView(activity));
            binding.setLifecycleOwner(activity);

            binding.btnLogout.performClick();
            UserData data = new UserData(activity);
            testItem(null, data.isUserDataExist(),
                    "User data in SharedPreferences should be deleted after logout", 4);

            Intent intent = Shadows.shadowOf(activity).getNextStartedActivity();
            testItem(null, intent, "Activity 'ShowDataActivity' should be opened after logout", 6);
            testItem(".MainActivity", intent.getComponent().getShortClassName(),
                    "Activity 'MainActivity' should be opened after logout", 1);
        });
    }

    public void clearUserData(Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences("userdata", Context.MODE_PRIVATE);
        pref.edit().clear().apply();
    }
}

