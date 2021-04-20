package edu.neu.madcourse.welink.posts;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import edu.neu.madcourse.welink.R;

public class SelfPostActivity extends AppCompatActivity {

    private RecyclerView.LayoutManager layoutManger;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private boolean isSelfPost = true;
    private String uid;
    private String currentUID;
    FloatingActionButton addPostButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);
        if (getIntent().getExtras() != null) {
            uid = getIntent().getExtras().getString("uid");
            currentUID = getIntent().getExtras().getString("currentUID");
            isSelfPost = uid.equals(currentUID);
        }
        addPostButton = findViewById(R.id.add_post_button_self_post);
        if(!isSelfPost) {
            addPostButton.setVisibility(View.GONE);
        } else {
            addPostButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    luanchAddPostActivity();
                }
            });
        }
        createRecyclerView();
    }

    private void luanchAddPostActivity() {
        Intent intent = new Intent(this, AddPostActivity.class);
        intent.putExtra("currUID", currentUID);
        startActivity(intent);
    }


    private void createRecyclerView() {
        layoutManger = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.post_rv);
        recyclerView.setHasFixedSize(true);
        if(isSelfPost) {
            postAdapter = new PostAdapter(uid,"self",null);
        } else {
            postAdapter = new PostAdapter(uid,"user",null);
        }
        postAdapter.setOpenProfileListener(new OpenProfileListener(this));
        recyclerView.setAdapter(postAdapter);
        recyclerView.setLayoutManager(layoutManger);
    }
}
