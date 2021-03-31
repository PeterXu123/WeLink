package edu.neu.madcourse.welink.following.search;

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
    private SearchResultAdapter mSearchResultAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        if (getIntent().getExtras() != null) {
            searchName = getIntent().getExtras().getString("search_name");
        }
        mSearchResultListView = findViewById(R.id.searchResultView);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mSearchResultAdapter = new SearchResultAdapter(mDatabaseReference, this, searchName);


    }

    @Override
    protected void onStart() {
        super.onStart();
        mSearchResultListView.setAdapter(mSearchResultAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mSearchResultListView.setLayoutManager(layoutManager);
    }
}