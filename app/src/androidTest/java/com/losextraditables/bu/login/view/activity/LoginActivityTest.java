package com.losextraditables.bu.login.view.activity;

import android.content.Intent;

import com.losextraditables.bu.InjectedInstrumentationTest;
import com.losextraditables.bu.R;

import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import dagger.Module;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class LoginActivityTest extends InjectedInstrumentationTest {

  @Rule
  public IntentsTestRule<LoginActivity> activityRule =
      new IntentsTestRule<>(LoginActivity.class, true, false);

  @Test
  public void shouldShowCharacterDetailWhenCharacterIsLoaded() throws Exception {
    startActivity();

    onView(withId(R.id.btnConnect)).check(matches(withText("Connect")));
  }

  private LoginActivity startActivity() {
    Intent intent = new Intent();
    return activityRule.launchActivity(intent);
  }

  @Override
  public List<Object> getTestModules() {
    return Arrays.asList((Object) new TestModule());
  }

  @Module(overrides = true, library = true, complete = false,
      injects = {
          LoginActivity.class, LoginActivityTest.class
      }) class TestModule {
  }
}
