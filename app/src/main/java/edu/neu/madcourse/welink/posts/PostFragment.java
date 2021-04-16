package edu.neu.madcourse.welink.posts;

import androidx.annotation.Nullable;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import edu.neu.madcourse.welink.R;

public class PostFragment extends Fragment {
    private RecyclerView.LayoutManager layoutManger;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private String type;

    private String currUID;

    public PostFragment(String type) {
        this.type = type;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if(bundle != null) {
            currUID = bundle.getString("currUID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.posts_list, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createRecyclerView( view);
    }


    private void createRecyclerView(View view) {
        layoutManger = new LinearLayoutManager(getActivity().getApplication());
        recyclerView = view.findViewById(R.id.post_rv);
        recyclerView.setHasFixedSize(true);
        postAdapter = new PostAdapter(currUID, type,null);
        recyclerView.setAdapter(postAdapter);
        recyclerView.setLayoutManager(layoutManger);
    }
}
