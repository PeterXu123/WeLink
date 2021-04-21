package edu.neu.madcourse.welink.posts;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import edu.neu.madcourse.welink.R;

public class OpenImageActivity extends AppCompatActivity {

    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        imageView = findViewById(R.id.view_image_iv);
        String uri;
        if (getIntent().getExtras() != null) {
            uri = getIntent().getExtras().getString("uri");
            Picasso.get().load(uri).placeholder(R.drawable.loading_place_holder).into(imageView);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
