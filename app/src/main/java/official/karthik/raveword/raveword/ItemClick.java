package official.karthik.raveword.raveword;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IInterface;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;

public class ItemClick extends AppCompatActivity {
    FloatingActionButton ftbtn,web;
    TextView title, author, description;
    KenBurnsView image;
    Bundle b;
    CircleImageView authimage;
    int c=0;
    TextView designation;    SharedPreferences mshared;
    SharedPreferences.Editor editor;
    String[] line;
    String swebsite,simage, stitle, sauthor, sdescription,delimiter;
RewardedVideoAd rewardedVideoAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_item_click);
        image = findViewById(R.id.image);
        title = findViewById(R.id.title);
        mshared=getSharedPreferences("login",MODE_PRIVATE);
        editor =mshared.edit();
        author = findViewById(R.id.author);
        designation=findViewById(R.id.designation);
        web=findViewById(R.id.website);
        rewardedVideoAd= MobileAds.getRewardedVideoAdInstance(this);
        authimage=findViewById(R.id.view2);
        description = findViewById(R.id.description);
        ftbtn = findViewById(R.id.like);
        delimiter="\n";
        AssetManager am = ItemClick.this.getApplicationContext().getAssets();
        final Typeface typeface = Typeface.createFromAsset(am,
                "HELR45W.ttf");
        b = getIntent().getExtras();
        simage = b.getString("image");
        stitle = b.getString("title");
        sauthor = b.getString("author");
        sdescription = b.getString("description");
        swebsite=b.getString("website");
        designation.setText(b.getString("designation","Content Writer"));
String km=b.getString("authimage","");
      Glide.with(getApplicationContext()).load(km).into(authimage);
        c=mshared.getInt("logs",0);
        if(c==0)
        {
            c++;
            editor.putInt("logs",c);
            editor.apply();

            new GuideView.Builder(this)
                    .setTitle("Share It")
                    .setContentText("to Others")
                    //optional
                    .setDismissType(GuideView.DismissType.anywhere) //optional - default GuideView.DismissType.targetView
                    .setTargetView(ftbtn)
                    .setContentTextSize(12)//optional
                    .setTitleTextSize(14)//optional
                    .build()
            .show();
        }
        if(Objects.equals(swebsite, "null"))
        {
            web.setVisibility(View.INVISIBLE);

        }
        line=sdescription.split(delimiter);
        Glide.with(getApplicationContext()).load(simage).into(image);
        title.setTypeface(typeface);
        title.setText(stitle);
        author.setText(sauthor);
        author.setTypeface(typeface);
        description.setText(sdescription);
        description.setTypeface(typeface);

        ftbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           onShareItem(v);
            }
        });
        loadRewardedVideoAd();

        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent l = new Intent(ItemClick.this,WebsiteContent.class);
                l.putExtra("web",swebsite);
                startActivity(l);
            }
        });
    }
    private void loadRewardedVideoAd() {
        rewardedVideoAd.loadAd(getString(R.string.rewarded_video),
                  new   AdRequest.Builder()

                        // Check the LogCat to get your test device ID
                        .addTestDevice("2CD52870B6A344F7A1C71F92433697DB")
                        .build());

        // showing the ad to user
      //  showRewardedVideo();
    }
    private void showRewardedVideo() {
        // make sure the ad is loaded completely before showing it
        if (rewardedVideoAd.isLoaded()) {
            rewardedVideoAd.show();
        }
    }
    @Override
    public void onBackPressed() {
        int k= new Random().nextInt(3)+1;
        Log.e("Error", "onBackPressed: "+k );
        if(k==1 )
        {
            //rewardedVideoAd.loadAd(getString(R.string.radio),new AdRequest.Builder().addTestDevice("2CD52870B6A344F7A1C71F92433697DB").build());
          showRewardedVideo();
          rewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
              @Override
              public void onRewardedVideoAdLoaded() {

              }

              @Override
              public void onRewardedVideoAdOpened() {

              }

              @Override
              public void onRewardedVideoStarted() {

              }

              @Override
              public void onRewardedVideoAdClosed() {
                  finish();
                  startActivity(new Intent(ItemClick.this,Bloging.class));
              }

              @Override
              public void onRewarded(RewardItem rewardItem) {

              }

              @Override
              public void onRewardedVideoAdLeftApplication() {

              }

              @Override
              public void onRewardedVideoAdFailedToLoad(int i) {
                  finish();
                  startActivity(new Intent(ItemClick.this,Bloging.class));
              }
          });

        }else
        {
            finish();
            startActivity(new Intent(ItemClick.this,Bloging.class));
        }

    }



    public void onShareItem(View v) {
        // Get access to bitmap image from view
     KenBurnsView ivImage = (KenBurnsView) findViewById(R.id.image);
        // Get access to the URI for the bitmap
        Uri bmpUri = getLocalBitmapUri(ivImage);
        if (bmpUri != null) {
            // Construct a ShareIntent with link to image
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);

            shareIntent.putExtra(Intent.EXTRA_TEXT,stitle+"\n"+line[0]+ "\nFor more Download RaveWord from Google Playstore!"+"\nwww.google.com");
            shareIntent.setType("image/*");
            // Launch sharing dialog for image
            startActivity(Intent.createChooser(shareIntent, "Share Image"));
        } else {
            // ...sharing failed, handle error
        }
    }

    // Returns the URI path to the Bitmap displayed in specified ImageView
    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file =  new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }


}
