package org.aplas.mylibrary;

import android.os.Build;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(RobolectricTestRunner.class)
@Config(manifest= Config.NONE)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class TestC1MyData011 extends ViewTest {
    ActivityScenario<MainActivity> scenario;
    private String packageName = "org.aplas";
    private String targetDevice = "9";
    private int minSDK = 21;
    private String actName = "MainActivity";
    private String layoutName = "activity_main";
    private String backwardComp = "AppCompatActivity";
    //private String packName;
    //ResourceTest rsc;

    @Before
    public void initTest() {
        scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
    }


    @Test
    public void check_01_AppName() { //Check Project Name (Should be MyData)
        //ActivityScenario<MainActivity> main = launchMainApp();
        scenario.onActivity(activity -> {
            assertEquals("Application Name is Wrong", appName.toLowerCase(), getAppName(activity.getPackageName()));
        });
    }

    @Test
    public void check_02_PackageName() { //Check Company Domain (Should be org.aplas.android)
        scenario.onActivity(activity -> {
            String packName = packageName+"."+appName.toLowerCase();
            assertEquals("Package Name is Wrong", packName, activity.getPackageName());
        });
    }

    @Test
    public void check_03_TargetDevice() { //Check Target Device (Should be 6.0.1)
        scenario.onActivity(activity -> {
            assertEquals("Target Device is Wrong",targetDevice, Build.VERSION.RELEASE);
        });
    }


    @Test
    public void check_04_MinimumSDK() { //Check Minimum SDK (Should be 28)
        scenario.onActivity(activity -> {
            assertEquals("Minimum SDK Version is Wrong",minSDK,activity.getApplicationInfo().minSdkVersion);
        });
    }

    @Test
    public void check_05_ActivityName() { //Check Activity Name (Should be MyActivity)
        scenario.onActivity(activity -> {
            assertEquals("Activity Name is Wrong", actName, activity.getClass().getSimpleName());
        });
    }

    @Test
    public void check_06_LayoutName() { //Check Layout Name (Should be activity_layout)
        scenario.onActivity(activity -> {
            int resId = activity.getResources().getIdentifier(layoutName, "layout", activity.getPackageName());
            assertNotEquals("Layout Name is Wrong", 0, resId);
        });
    }

    @Test
    public void check_07_ActivityParent() { //Check Backward Compatibility (Should be AppCompatActivity)
        scenario.onActivity(activity -> {
            assertEquals("Activity Parent is Wrong", backwardComp, activity.getClass().getSuperclass().getSimpleName());
        });
    }

    @Test
    public void check_08_Color_Resources() {
        scenario.onActivity(activity -> {
            ResourceTest rsc = new ResourceTest(activity.getResources());
            rsc.testColorResource(activity, "colorPrimary", "#6200EE");
            rsc.testColorResource(activity, "colorPrimaryDark", "#3700B3");
            rsc.testColorResource(activity, "colorAccent", "#03DAC5");
            rsc.testColorResource(activity, "bgMain", "#00D6EA");
            rsc.testColorResource(activity, "bgSave", "#192841");
            rsc.testColorResource(activity, "btnSave", "#FFFC65");
            rsc.testColorResource(activity, "btnDownload", "#FBBAFF");
            rsc.testColorResource(activity, "btnOpen", "#5AFFB1");
            rsc.testColorResource(activity, "lightBlue", "#5AFFFF");
        });

    }

    @Test
    public void check_09_Drawable_Resource() {
        //drawable check
        scenario.onActivity(activity -> {
            ResourceTest rsc = new ResourceTest(activity.getResources());
            rsc.testDrawableResource("btn_download","");
            rsc.testDrawableResource("btn_edit","");
            rsc.testDrawableResource("btn_open","");
            rsc.testDrawableResource("btn_save","");
        });
    }

    @Test
    public void check_10_Image_Resource(){
        //image check
        scenario.onActivity(activity -> {
            ResourceTest rsc = new ResourceTest(activity.getResources());
            rsc.testImgResource("mydata");
            rsc.testImgResource("key");
        });

    }

    private String getAppName(String packName) {
        String[] list = packName.split("\\.");
        String res = list[list.length-1];
        return res;
    }

}
