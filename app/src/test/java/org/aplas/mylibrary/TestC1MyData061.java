package org.aplas.mylibrary;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Method;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

//test data binding
public class TestC1MyData061 extends ViewTest {

    @Test
    public void check_01_InputDataActivity_DataBinding() {
        ActivityScenario<InputDataActivity> scenario = ActivityScenario.launch(InputDataActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            String packname = "org.aplas.mylibrary";
            ViewDataBinding binding = DataBindingUtil.getBinding(getRootView(activity));
            assert binding != null;
            testItem(packname+".databinding.ActivityInputDataBindingImpl",
                    binding.getClass().getName(), "Data binding for 'InputDataActivity' is not exist",1);

            String layoutFile = "activity_input_data.xml";
            String[] data = {"User", "Book"};
            for (int i=0; i<data.length; i++) {
                String name = "get"+ getCapitalFirstLetter(data[i].toLowerCase());
                Method m = getMethodInObject(binding, name);
                testItem(null,m,"Data variable '"+data[i].toLowerCase()+"' is not defined in '"+layoutFile+"'",6);
                testItem(packname+".viewmodels."+data[i], m.getReturnType().getName(),
                        "Data type '"+data[i].toLowerCase()+"' should be 'org.aplas.mydata.viewmodels."+data[i]+"'", 1);
            }
        });
    }

    @Test
    public void check_02_ShowDataActivity_DataBinding() {
        ActivityScenario<ShowDataActivity> scenario = ActivityScenario.launch(ShowDataActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);

        scenario.onActivity(activity -> {
            String packname = "org.aplas.mylibrary";
            ViewDataBinding binding = DataBindingUtil.getBinding(getRootView(activity));
            assert binding != null;
            testItem(packname+".databinding.ActivityShowDataBindingImpl",
                    binding.getClass().getName(), "Data binding for 'ShowDataActivity' is not exist",1);

            String layoutFile = "activity_show_data.xml";
            String[] data = {"User", "Book", "BookList"};
            for (int i=0; i<data.length; i++) {
                String name = "get"+ getCapitalFirstLetter(data[i].toLowerCase());
                Method m = getMethodInObject(binding, name);
                testItem(null,m,"Data variable '"+data[i].toLowerCase()+"' is not defined in '"+layoutFile+"'",6);
                testItem(packname+".viewmodels."+data[i], m.getReturnType().getName(),
                        "Data type '"+data[i].toLowerCase()+"' should be 'org.aplas.mydata.viewmodels."+data[i]+"'", 1);
            }
        });
    }

}

