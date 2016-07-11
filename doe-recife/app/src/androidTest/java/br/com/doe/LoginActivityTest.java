package br.com.doe;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.EditText;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;
import org.junit.runner.RunWith;

import br.com.doe.view.LoginActivity;
import kotlin.jvm.internal.Intrinsics;

/**
 * Created by heitornascimento on 6/24/16.
 */

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static com.google.common.base.Preconditions.checkNotNull;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public final ActivityRule<LoginActivity> loginActivity =
            new ActivityRule<>(LoginActivity.class);

    @Test
    public void shouldShowErrorForm() {


        onView(ViewMatchers.withId(R.id.user_email)).
                check(ViewAssertions.matches(isDisplayed()));
        //onView(ViewMatchers.withId(R.id.user_email)).
               // perform(typeText("aaa"), closeSoftKeyboard());

        onView(withId(R.id.user_email)).perform(closeSoftKeyboard());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(ViewMatchers.withId(R.id.user_email)).
                perform(click()).check(matches
                (hasErrorText(loginActivity.get().getString(R.string.mandatory_fields))));


        //onView(ViewMatchers.withId(R.id.user_email)).
                //perform(ViewActions.clearText(), typeText("aaa"));


    }

    private static Matcher<? super View> hasErrorText(String expectedError) {
        return new ErrorTextMatcher(expectedError);
    }

    private static class ErrorTextMatcher extends TypeSafeMatcher<View> {
        private final String expectedError;

        private ErrorTextMatcher(String expectedError) {
            this.expectedError = expectedError;
        }

        @Override
        public boolean matchesSafely(View view) {
            if (!(view instanceof EditText)) {
                return false;
            }
            EditText editText = (EditText) view;
            return expectedError.equals(editText.getError());
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("with error: " + expectedError);
        }
    }

}
