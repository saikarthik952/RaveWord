package official.karthik.raveword.raveword;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class AppIntroActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_app_intro);
        setFadeAnimation();
        addSlide(AppIntroFragment.newInstance("Welcome RaveWorders !", "You are in Right Place where you get the latest updates and articles about" +
                " Trending genres like Music ,Technology, Politics ", R.drawable.news, Color.argb(255,255,42,155)));

        addSlide(AppIntroFragment.newInstance("Daily Trending News !", "With Notifications and trending stuff that makes you update with the current world"
              , R.drawable.update, Color.rgb(42,155,255)));
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        startActivity(new Intent(AppIntroActivity.this,SelectionActivity.class));
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(new Intent(AppIntroActivity.this,SelectionActivity.class));
        // Do something when users tap on Done button.
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
