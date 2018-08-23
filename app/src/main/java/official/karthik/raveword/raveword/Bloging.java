package official.karthik.raveword.raveword;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.Locale;
import java.util.Random;

public class Bloging extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;
    TextView id, namee;
    FirebaseAuth firebaseAuth;
    String k;
    String title,author,image,description;
    private FirebaseFirestore db;
    RecyclerView friendList;
    AdRequest adRequest;
    SharedPreferences mshared;
    InterstitialAd mInterstitialAd;
    android.support.v7.widget.Toolbar toolbar;
    private FirestoreRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;
     AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bloging);
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

mshared=getSharedPreferences("tt",MODE_PRIVATE);
        SharedPreferences.Editor e = mshared.edit();

Bundle i =getIntent().getExtras();
if(i!=null)
{
     k=i.getString("title");
    e.putString("title",k);
    e.apply();
}else
{
    k=mshared.getString("title","");
}

        mAdView=findViewById(R.id.adview);

        mAdView = new AdView(this);
        mAdView.setAdUnitId(getString(R.string.banner_home_footer));
     adRequest = new AdRequest.Builder()

                // Check the LogCat to get your test device ID
             .addTestDevice("2CD52870B6A344F7A1C71F92433697DB")
                .build();
        AssetManager am = Bloging.this.getApplicationContext().getAssets();

        final Typeface    typeface = Typeface.createFromAsset(am,
                "HELR45W.ttf");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        mInterstitialAd.loadAd(adRequest);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.loadAd(adRequest);

        String name=user.getDisplayName();
        getSupportActionBar().setTitle("Welcome !"+name);
        toolbar.setElevation(4);
        setSupportActionBar(toolbar);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        friendList=(RecyclerView)findViewById(R.id.recylerview);
        friendList.setLayoutManager(linearLayoutManager);
        db = FirebaseFirestore.getInstance();
        Query query = null;

            query = db.collection(k).orderBy("id", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<BlodData> response = new FirestoreRecyclerOptions.Builder<BlodData>()
                .setQuery(query, BlodData.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<BlodData, Bloging.FriendsHolder>(response) {
            @Override
            public void onBindViewHolder(@NonNull final Bloging.FriendsHolder holder, int position, @NonNull final BlodData model) {
                YoYo.with(Techniques.FadeIn).playOn(holder.img);
                holder.title.setText(model.getTitle());
holder.title.setTypeface(typeface);

//                Log.e("Error",activity);
                Glide.with(getApplicationContext())
                        .load(model.getImage())
                        .into(holder.img);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Bloging.this, ItemClick.class);

                        // Get the transition name from the string
                        String transitionName = getString(R.string.collapanim);

                        // Define the view that the animation will start from
                        View viewStart =findViewById(R.id.recylerview);

                        ActivityOptionsCompat options =

                                ActivityOptionsCompat.makeSceneTransitionAnimation(Bloging.this,
                                        viewStart,   // Starting view
                                        transitionName    // The String
                                );
                        //Start the Intent
                        i.putExtra("title",model.getTitle());
                        i.putExtra("image",model.getImage());
                        i.putExtra("author",model.getAuthor());
                        i.putExtra("description",model.getDescription());
                        i.putExtra("website",model.getWebsite());
                        i.putExtra("authimage",model.getAuthimage());
                        i.putExtra("designation",model.getDesignation());
                        ActivityCompat.startActivity(Bloging.this, i, options.toBundle());


                    }
                });
            }

            @Override
            public FriendsHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.item_view, group, false);

                return new FriendsHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }


        };


        friendList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    public class FriendsHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView img;
        CardView cardView;

        FriendsHolder(View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.title);
            img=(ImageView)itemView.findViewById(R.id.image);
            cardView=itemView.findViewById(R.id.cardview);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        int x= new Random().nextInt(4);
        Log.e("error", "onBackPressed: "+x);
        if(x<3) {
            mInterstitialAd.show();
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    mInterstitialAd.show();
                }

                @Override
                public void onAdClosed() {
                    Toast.makeText(getApplicationContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Bloging.this, SelectionActivity.class));
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    Toast.makeText(getApplicationContext(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdLeftApplication() {
                    Toast.makeText(getApplicationContext(), "Ad left application!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdOpened() {
                    super.onAdOpened();
                }
            });

        }else
        {
            startActivity(new Intent(Bloging.this, SelectionActivity.class));
        }
    }
    }

