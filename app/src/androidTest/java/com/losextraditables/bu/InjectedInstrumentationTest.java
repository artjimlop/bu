package com.losextraditables.bu;

import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingResource;

import org.junit.After;
import org.junit.Before;

import java.util.LinkedList;
import java.util.List;

import dagger.ObjectGraph;

import static android.support.test.espresso.Espresso.getIdlingResources;
import static android.support.test.espresso.Espresso.unregisterIdlingResources;

public abstract class InjectedInstrumentationTest {

    @Before
    public void setUp() {
        BuApplication application = getApplication();
        List<Object> childTestModules = getTestModules();
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        List<Object> testModules = new LinkedList<>(childTestModules);
        testModules.add(new BaseTestModule(context));
        ObjectGraph objectGraph = application.plusGraph(testModules);
        application.replaceGraph(objectGraph);
        objectGraph.inject(this);
    }

    @After
    public void tearDown() throws Exception {
        List<IdlingResource> idlingResources = getIdlingResources();
        for (IdlingResource resource : idlingResources) {
            unregisterIdlingResources(resource);
        }
        BuApplication application = getApplication();
        application.resetFakeGraph();
    }

    private BuApplication getApplication() {
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        BuApplication app =
                (BuApplication) instrumentation.getTargetContext().getApplicationContext();
        return app;
    }

    public abstract List<Object> getTestModules();

}
