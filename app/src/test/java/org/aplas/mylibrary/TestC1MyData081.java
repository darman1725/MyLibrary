package org.aplas.mylibrary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowToast;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

//MainActivity
public class TestC1MyData081 extends ViewTest {
    ActivityScenario<MainActivity> scenario;

    private final String SHARED_ID = "userdata";
    private final String tag_name = "name";
    private final String tag_country = "country";
    private final String tag_phone = "phone";
    private final String tag_color = "color";

    @Before
    public void initTest() {
        scenario = launchMainApp();
    }

    private ActivityScenario<MainActivity> launchMainApp() {
        ActivityScenario<MainActivity> sc = ActivityScenario.launch(MainActivity.class);
        sc.moveToState(Lifecycle.State.CREATED);
        return sc;
    }

    @Test
    public void check_01_PickColor_OK() {
        scenario.onActivity(activity -> {
            testField(activity,"bgColor", -1, int.class, false);
            testField(activity,"layout", -1, ViewGroup.class, false);
            testItem(activity.getResources().getColor(R.color.bgMain),
                    (int)getFieldValue(activity,"bgColor"),
                    "Default field 'bgColor' is R.color.bgMain",1);
            testItem(R.id.mainLayout,((ViewGroup)getFieldValue(activity,"layout")).getId(),
                    "Field layout must be set from R.id.mainLayout ",1);

            Button btnPickColor = activity.findViewById(R.id.btnPickBgColor);
            btnPickColor.performClick();
            AlertDialog d = ShadowAlertDialog.getLatestAlertDialog();

            Integer clr = (Integer) getFieldValue(activity,"bgColor");
            int c0 = clr;
            clr = Color.RED;
            int c1 = clr;
            ViewGroup layout = (ViewGroup)getFieldValue(activity,"layout");
            layout.setBackgroundColor(c1);

            d.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
            int c2 = (int) getFieldValue(activity,"bgColor");
            testItem(null, c1!=c2,"New Color must be set after pick a color",3);
            testItem(null, c0==c2,"Field 'bgColor' must be set from picked color",3);

            int lcolor = getColorFromDrawable(layout.getBackground());
            testItem(null, c1!=lcolor,"New background color must be set after pick a color",3);
            testItem(null, c0==lcolor,"Background color must be set from picked color",3);
        });
    }

    @Test
    public void check_02_PickColor_Cancel() {
        scenario.onActivity(activity -> {
            testField(activity,"bgColor", -1, int.class, false);
            testField(activity,"layout", -1, ViewGroup.class, false);
            testItem(activity.getResources().getColor(R.color.bgMain),
                    (int)getFieldValue(activity,"bgColor"),
                    "Default field 'bgColor' is R.color.bgMain",1);
            testItem(R.id.mainLayout,((ViewGroup)getFieldValue(activity,"layout")).getId(),
                    "Field layout must be set from R.id.mainLayout ",1);

            Button btnPickColor = activity.findViewById(R.id.btnPickBgColor);
            btnPickColor.performClick();
            AlertDialog d = ShadowAlertDialog.getLatestAlertDialog();

            Integer clr = (Integer) getFieldValue(activity,"bgColor");
            int c0 = clr;
            activity.bgColor = Color.RED;
            int c1 = activity.bgColor;
            ViewGroup layout = (ViewGroup)getFieldValue(activity,"layout");
            layout.setBackgroundColor(c1);

            d.getButton(DialogInterface.BUTTON_NEGATIVE).performClick();
            int c2 = (int) getFieldValue(activity,"bgColor");
            testItem(null, c1==c2,"New Color must not be set after cancel picking color",3);
            testItem(null, c0!=c2,"Field 'bgColor' must not be set after cancel picking color",3);

            int lcolor = getColorFromDrawable(layout.getBackground());
            testItem(null, c1==lcolor,"New background color must not be set after cancel picking color",3);
            testItem(null, c0!=lcolor,"Background color must not be set after cancel picking color",3);
        });
    }

    @Test
    public void check_03_SaveButton_Click_UnComplete_Data() {
        scenario.onActivity(activity -> {
            activity.bgColor = Color.RED;
            activity.layout.setBackgroundColor(activity.bgColor);

            EditText name = activity.findViewById(R.id.editUserName);
            EditText country = activity.findViewById(R.id.editCountry);
            EditText phone = activity.findViewById(R.id.editPhone);
            Button btn = activity.findViewById(R.id.btnSaveUser);

            //Blank name
            name.setText("");
            country.setText(getRandomString(10));
            phone.setText(Integer.toString(getRandomInteger(10000,99999)));
            btn.performClick();

            testIncompleteProfile(activity, "name");

            //Blank name
            name.setText(getRandomString(20));
            country.setText("");
            phone.setText(Integer.toString(getRandomInteger(10000,99999)));
            btn.performClick();

            testIncompleteProfile(activity, "country");

            //Blank name
            name.setText(getRandomString(20));
            country.setText(getRandomString(10));
            phone.setText("");
            btn.performClick();

            testIncompleteProfile(activity, "phone");
        });
    }

    private void testIncompleteProfile(MainActivity activity, String data) {
        Toast toast = ShadowToast.getLatestToast();
        testItem(null, toast, "A toast with a message should be shown when "+data+" was blank", 6);
        ShadowToast.reset();

        //initUserProfile(activity,this);
        Intent intent = Shadows.shadowOf(activity).getNextStartedActivity();
        if (intent!=null) {
            failTest("The new screen ('"+intent.getComponent().getShortClassName()
                    +"') should not be appeared because of incompleteness profile ("+data+")");
        }
        //.Shadows.shadowOf(activity).clearNextStartedActivities();
    }

    @Test
    public void check_04_SaveButton_Click_Complete() {
        scenario.onActivity(activity -> {
            String name = getRandomString(20);
            String country = getRandomString(10);
            String phone = Integer.toString(getRandomInteger(10000,99999));
            int color = Color.RED;

            completingMainActivity(activity,name,country,phone,color);
            testSharedPreferences(activity,name,country,phone,color);

            Intent intent = Shadows.shadowOf(activity).getNextStartedActivity();
            testItem(".InputDataActivity",intent.getComponent().getShortClassName(),
                    "Intent 'InputDataActivity' should be activated after SaveBtn clicked",1);

            clearUserData(activity);
        });
    }

    private void testSharedPreferences(Activity activity, String name, String country, String phone, int color) {
        SharedPreferences pref = activity.getSharedPreferences(SHARED_ID, Context.MODE_PRIVATE);
        testItem(null,pref,"SharedPreferences 'pref' should not be null", 6);

        testItem(name, pref.getString(tag_name, ""),
                "SharedPreferences should contains 'name' from EditText 'editUserName' ", 1);
        testItem(country, pref.getString(tag_country, ""),
                "SharedPreferences should contains 'country' from EditText 'editCountry' ", 1);
        testItem(phone, pref.getString(tag_phone, ""),
                "SharedPreferences should contains 'phone' from EditText 'editPhone' ", 1);
        testItem(color, pref.getInt(tag_color, 255*255*255),
                "SharedPreferences should contains 'color' from background color of 'MainActivity' ", 1);
    }

    @Test
    public void check_05_OpenApp_After_UserProfile_Was_Set() {
        scenario.onActivity(activity -> {
            String name = getRandomString(20);
            String country = getRandomString(10);
            String phone = Integer.toString(getRandomInteger(10000,99999));
            int color = Color.RED;

            completingMainActivity(activity,name,country,phone,color);
            Shadows.shadowOf(activity).clearNextStartedActivities();
            //clearUserData(activity);
        });
        scenario.moveToState(Lifecycle.State.DESTROYED);

        ActivityScenario<MainActivity> sc2 = launchMainApp();
        sc2.onActivity(act2 -> {
            Intent intent = Shadows.shadowOf(act2).peekNextStartedActivity();
            testItem(null,intent,"After user profile was already set, 'InputDataActivity' should be directly opened when launching app",6);
            testItem(".InputDataActivity",intent.getComponent().getShortClassName(),
                    "After user profile was already set, 'InputDataActivity' should be directly opened when launching app",1);
            clearUserData(act2);
        });
    }

    public void clearUserData(Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences(SHARED_ID, Context.MODE_PRIVATE);
        pref.edit().clear().apply();
    }

    public void completingMainActivity(MainActivity activity, String name, String country, String phone, int color) {
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
    }

}

