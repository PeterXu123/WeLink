package edu.neu.madcourse.welink.following;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.neu.madcourse.welink.R;

public class SearchResultActivity extends AppCompatActivity {
    private String searchName;
    private RecyclerView mSearchResultListView;
    private DatabaseReference mDatabaseReference;
    private FollowingAdapter mFollowingAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        if (getIntent().getExtras() != null) {
            searchName = getIntent().getExtras().getString("search_name");
        }
        mSearchResultListView = findViewById(R.id.searchResultView);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFollowingAdapter = new FollowingAdapter(mDatabaseReference, this, searchName);


    }

    @Override
    protected void onStart() {
        super.onStart();
        mSearchResultListView.setAdapter(mFollowingAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mSearchResultListView.setLayoutManager(layoutManager);
    }
}