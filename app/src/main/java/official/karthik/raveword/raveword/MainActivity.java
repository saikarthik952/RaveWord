package official.karthik.raveword.raveword;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.Arrays;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private static final String TAG = "MyActivity";
    SharedPreferences mshared;
    int i=0;
    FirebaseUser user;
SharedPreferences.Editor editor;
    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mshared=getSharedPreferences("login",MODE_PRIVATE);
        editor=mshared.edit();
        user= FirebaseAuth.getInstance().getCurrentUser();

            i = mshared.getInt("log", 0);
            new Handler().postDelayed(new Runnable() {

			/*
			 * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

                @Override
                public void run() {
                    if(user!=null)
                    {
                        startActivity(new Intent(MainActivity.this,SelectionActivity.class));
                    }else {
                    i++;
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()

                                    .setAvailableProviders(
                                            Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                    .build()
                            ,
                            RC_SIGN_IN);
                }
                }
            }, 2000);

        }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            IdpResponse idpResponse = IdpResponse.fromResultIntent(data);
            if(i==1) {
i++;
editor.putInt("log",i);
editor.apply();
                startActivity(new Intent(this, AppIntroActivity.class));
            }
            else
                startActivity(new Intent(this,SelectionActivity.class));
            }
    }



    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}