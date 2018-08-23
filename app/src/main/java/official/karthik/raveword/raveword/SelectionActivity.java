package official.karthik.raveword.raveword;

import android.content.Intent;
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
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Locale;
import java.util.Random;

public class SelectionActivity extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;
    InterstitialAd mInterstitialAd;
    TextView id, namee;
    FirebaseAuth firebaseAuth;
    String activity;
    int x=0;
    Random r;
    private FirebaseFirestore db;
    RecyclerView friendList;
android.support.v7.widget.Toolbar toolbar;
    private FirestoreRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        x= new Random().nextInt(4)+1;
        AssetManager am = SelectionActivity.this.getApplicationContext().getAssets();

    final Typeface    typeface = Typeface.createFromAsset(am,
            "HELR45W.ttf");


        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("Error", "FCM token: " + refreshedToken);
        String name=user.getDisplayName();
         final AdRequest adRequest = new AdRequest.Builder()
                 .addTestDevice("2CD52870B6A344F7A1C71F92433697DB")
                // Check the LogCat to get your test device ID

                .build();


        getSupportActionBar().setTitle("Welcome ! "+name);
        setSupportActionBar(toolbar);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        friendList=(RecyclerView)findViewById(R.id.recylerview);
        friendList.setLayoutManager(linearLayoutManager);
        db = FirebaseFirestore.getInstance();
        Query query = db.collection("MainPage").orderBy("title", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<MainPage> response = new FirestoreRecyclerOptions.Builder<MainPage>()
                .setQuery(query, MainPage.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<MainPage, FriendsHolder>(response) {
            @Override
            public void onBindViewHolder(@NonNull FriendsHolder holder, int position, @NonNull final MainPage model) {
                YoYo.with(Techniques.FadeIn).playOn(holder.img);
                holder.title.setText(model.getTitle());
holder.title.setTypeface(typeface);
activity=model.getActivity();
Log.e("Error",""+activity);
                Glide.with(getApplicationContext())
                        .load(model.getImage())
                        .into(holder.img);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(SelectionActivity.this, Bloging.class);

                        // Get the transition name from the string
                        String transitionName = getString(R.string.hello);

                        // Define the view that the animation will start from
                        View viewStart = findViewById(R.id.recylerview);
intent.putExtra("title",model.getActivity());
                        ActivityOptionsCompat options =

                                ActivityOptionsCompat.makeSceneTransitionAnimation(SelectionActivity.this,
                                        viewStart,   // Starting view
                                        transitionName    // The String
                                );
                        //Start the Intent
                        ActivityCompat.startActivity(SelectionActivity.this, intent, options.toBundle());
                        // Toast.makeText(getApplicationContext(),mref,Toast.LENGTH_LONG).show();
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
    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
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
        if (mInterstitialAd != null) {//mInterstitialAd.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
       // if (mInterstitialAd != null)// mInterstitialAd.resume();
    }
    int l=0;
    @Override
    public void onBackPressed() {

l++;
if(l==1)
{
    Toast.makeText(SelectionActivity.this,"Press Again To Exit",Toast.LENGTH_SHORT).show();

}else if(l==2)
{
    System.exit(0);
}


    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }
}
