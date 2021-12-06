package org.aplas.mylibrary;

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
public class TestC1MyData041 extends ViewTest {
    ActivityScenario<ShowDataActivity> scenario;

    @Before
    public void initTest() {
        scenario = ActivityScenario.launch(ShowDataActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
    }

    @Test
    public void check_01_ShowDataActivity_TextView() {
        scenario.onActivity(activity -> {
            TextView book = (TextView)testViewExist("txtBook","TextView",activity);
            TextView title = (TextView)testViewExist("txtTitle","TextView",activity);
            TextView type = (TextView)testViewExist("txtType","TextView",activity);
            TextView year = (TextView)testViewExist("txtYear","TextView",activity);
            TextView profile = (TextView)testViewExist("txtProfile","TextView",activity);
        });
    }


    @Test
    public void check_02_ShowDataActivity_Spinner(){
        scenario.onActivity(activity -> {
            String spinner = "spBook";
        });
    }

    @Test
    public void check_03_ShowDataActivity_Button(){
        scenario.onActivity(activity -> {
            String btnId1 = "btnEditData";
            testViewExist(btnId1,"Button",activity);
            String btnId2 = "btnDelData";
            testViewExist(btnId1,"Button",activity);
            String btnId3 = "btnAddData";
            testViewExist(btnId1,"Button",activity);
            String btnId4 = "btnLogout";
            testViewExist(btnId1,"Button",activity);
        });
    }

}

