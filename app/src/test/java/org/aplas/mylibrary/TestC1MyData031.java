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

public class TestC1MyData031 extends ViewTest {
    ActivityScenario<InputDataActivity> scenario;

    @Before
    public void initTest() {
        scenario = ActivityScenario.launch(InputDataActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
    }

    @Test
    public void check_01_InputDataActivity_TextView() {
        scenario.onActivity(activity -> {
            TextView titleInput = (TextView)testViewExist("txtTitleInput","TextView",activity);
            testViewContains(titleInput,"Input Book Data");
            TextView title = (TextView)testViewExist("txtTitle","TextView",activity);
            testViewContains(title,"Title");
            TextView type = (TextView)testViewExist("txtType","TextView",activity);
            testViewContains(type,"Type");
            TextView year = (TextView)testViewExist("txtYear","TextView",activity);
            testViewContains(year,"Year");
            testViewExist("txtProfile","TextView",activity);
        });
    }

    @Test
    public void check_02_InputDataActivity_EditText(){
        scenario.onActivity(activity -> {
            EditText title = (EditText) testViewExist("editTitle","EditText",activity);
            EditText year = (EditText) testViewExist("editYear","EditText",activity);
        });
    }

    @Test
    public void check_03_InputDataActivity_ImageView(){
        scenario.onActivity(activity -> {
            String imgId = "imgLogo";
            testViewExist(imgId,"ImageView",activity);
        });
    }

    @Test
    public void check_04_InputDataActivity_Spinner(){
        scenario.onActivity(activity -> {
            String spinId = "spType";
        });
    }

    @Test
    public void check_05_InputDataActivity_Button(){
        scenario.onActivity(activity -> {
            String btnId1 = "btnSaveBook";
            testViewExist(btnId1,"Button",activity);
            String btnId2 = "btnShow";
            testViewExist(btnId2,"Button",activity);
            String btnId3 = "btnLogout";
            testViewExist(btnId3,"Button",activity);
        });
    }

}

