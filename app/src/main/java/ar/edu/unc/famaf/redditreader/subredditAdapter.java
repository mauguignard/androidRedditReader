package ar.edu.unc.famaf.redditreader;

import android.graphics.Bitmap;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by mauguignard on 9/20/16.
 */
public class subredditAdapter extends ArrayAdapter<subredditModel> {
    private List<subredditModel> subredditList = null;
    private Context context;

    public subredditAdapter(Context context, int textViewResourceId, List<subredditModel> subredditList) {
        super(context, textViewResourceId);
        this.subredditList = subredditList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return subredditList.size();
    }

    @Override
    public int getPosition(subredditModel item) {
        return subredditList.indexOf(item);
    }

    @Override
    public subredditModel getItem(int position) {
        return subredditList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.subreddit_list_item, parent, false);
        }

        subredditModel sm = subredditList.get(position);

        TextView subredditName = (TextView) convertView.findViewById(R.id.subredditName);
        TextView subredditDescription = (TextView) convertView.findViewById(R.id.subredditDescription);
        TextView subredditNoComments = (TextView) convertView.findViewById(R.id.subreditNoComments);
        TextView subredditDate = (TextView) convertView.findViewById(R.id.subredditLastUpdated);
        ImageView subredditIcon = (ImageView) convertView.findViewById(R.id.subredditIcon);

        subredditName.setText(sm.getName());
        subredditDescription.setText(sm.getDescription());
        subredditNoComments.setText(String.format(context.getResources().getString(R.string.no_comments), sm.getNoComments()));
        DownloadImageTask imageDownloader = new DownloadImageTask(subredditIcon);
        imageDownloader.execute(sm.getIconSrc());

        long time = sm.getDate();
        long now = System.currentTimeMillis();
        CharSequence relativeTimeStr = DateUtils.getRelativeTimeSpanString(time,
                now, DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE);
        subredditDate.setText(relativeTimeStr);

        return convertView;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String ... urls) {
            String url = urls[0];
            Bitmap bmp = null;
            try{
                URL urln = new URL(url);
                URLConnection con = urln.openConnection();
                InputStream is = con.getInputStream();
                bmp = BitmapFactory.decodeStream(is);
                if (bmp != null)
                    return bmp;

            }catch(Exception e){
                String msg = (e.getMessage() == null) ? "Unexpected error!" : e.getMessage();
                Log.e("Error", msg);
                e.printStackTrace();
            }
            return bmp;
        }

        protected void onPostExecute(Bitmap result) {
            if (result == null) {
                bmImage.setImageResource(R.mipmap.ic_launcher);
            } else {
                bmImage.setImageBitmap(result);
            }
        }
    }
}
