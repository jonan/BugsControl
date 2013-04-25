package jp.bugscontrol;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class ImageLoader extends AsyncTask<Void, Void, Bitmap> {
    String src;
    ImageView view;

    public static Map<String,Bitmap> cache = new HashMap<String,Bitmap>();

    public static void loadImage(String src, ImageView view) {
        if (cache.containsKey(src))
            view.setImageBitmap(cache.get(src));
        else
            (new ImageLoader(src, view)).execute();
    }

    private ImageLoader(String src, ImageView view) {
        this.src = src;
        this.view = view;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        Bitmap image = null;
        try {
            URL url = new URL(src);
            image = BitmapFactory.decodeStream(new FlushedInputStream(url.openStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        if (result != null) {
            cache.put(src, result);
            view.setImageBitmap(result);
        }
    }

    // This class addresses an Android bug
    static class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped < n) {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0L) {
                    int b = read();
                    if (b < 0) {
                        break;  // EOF
                    } else {
                        bytesSkipped = 1; // Read one byte
                    }
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }
}
