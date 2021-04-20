package edu.neu.madcourse.welink.posts;

import android.graphics.Bitmap;
import android.net.Uri;

public class AddImageItem {

    private final Uri uri;
    private final Bitmap bitmap;

    public AddImageItem(Uri uri, Bitmap bitmap) {
        this.uri = uri;
        this.bitmap = bitmap;
    }

    public Uri getUri() {
        return uri;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
