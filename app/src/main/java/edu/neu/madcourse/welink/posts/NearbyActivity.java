package edu.neu.madcourse.welink.posts;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import edu.neu.madcourse.welink.R;
import edu.neu.madcourse.welink.utility.TimeFormatter;

public class NearbyActivity extends AppCompatActivity {

    private RecyclerView.LayoutManager layoutManger;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;

    private Button tempAdd;

    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posts_list);
        createRecyclerView();
        tempAdd = findViewById(R.id.temp_buttom);
        tempAdd.setOnClickListener(tempListener);
        count = 0;
    }


    private void createRecyclerView() {
        layoutManger = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.post_rv);
        recyclerView.setHasFixedSize(true);
        postAdapter = new PostAdapter();
        recyclerView.setAdapter(postAdapter);
        recyclerView.setLayoutManager(layoutManger);
    }

    //TODO:dummy add post
    private View.OnClickListener tempListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Date now = new Date();
            PostDAO dummyPost = new PostDAO(count + " post in weLink!","37.25, 38.45", now.getTime(), "MxOJGG6VRuZPVaQKVJ9Dzth2omd2");
            count++;
            DatabaseReference res = FirebaseDatabase.getInstance().getReference().child("post_self").child("MxOJGG6VRuZPVaQKVJ9Dzth2omd2").push();
            res.setValue(dummyPost);
            String postId = res.getKey();
        }
    };

}
