/*
 *  BugsControl
 *  Copyright (C) 2013  Jon Ander Peñalba
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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

        @Override
        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped < n) {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0L)
                    if (read() < 0)
                        break;  // EOF
                    bytesSkipped = 1; // Read one byte
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }
}
