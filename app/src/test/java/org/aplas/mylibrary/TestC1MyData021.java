package org.aplas.mylibrary;

import android.widget.EditText;
import android.widget.TextView;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestC1MyData021 extends ViewTest {
    ActivityScenario<MainActivity> scenario;

    @Before
    public void initTest() {
        scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
    }

    @Test
    public void check_01_MainActivity_TextView() {
        scenario.onActivity(activity -> {
            TextView title = (TextView)testViewExist("txtLogin","TextView",activity);
            testViewContains(title,"User Profile");
            TextView name = (TextView)testViewExist("txtName","TextView",activity);
            testViewContains(name,"Name");
            TextView hobby = (TextView)testViewExist("txtCountry","TextView",activity);
            testViewContains(hobby,"Country");
            TextView phone = (TextView)testViewExist("txtPhone","TextView",activity);
            testViewContains(phone,"Phone Number");
        });
    }


    @Test
    public void check_02_MainActivity_EditText(){
        scenario.onActivity(activity -> {
            EditText name = (EditText) testViewExist("editUserName","EditText",activity);
            EditText country = (EditText) testViewExist("editCountry","EditText",activity);
            EditText phone = (EditText) testViewExist("editPhone","EditText",activity);
        });
    }

    @Test
    public void check_03_MainActivity_ImageView(){
        scenario.onActivity(activity -> {
            String imgId = "imgLogo";
            testViewExist(imgId,"ImageView",activity);
        });
    }

    @Test
    public void check_04_MainActivity_Button(){
        scenario.onActivity(activity -> {
            String btnId1 = "btnPickBgColor";
            testViewExist(btnId1,"Button",activity);
            String btnId2 = "btnSaveUser";
            testViewExist(btnId2,"Button",activity);
        });
    }

}

