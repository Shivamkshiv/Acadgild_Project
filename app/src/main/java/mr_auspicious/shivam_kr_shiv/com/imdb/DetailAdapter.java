package mr_auspicious.shivam_kr_shiv.com.imdb;




import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;


import java.util.List;


public class DetailAdapter extends ArrayAdapter<Detail> {


    public DetailAdapter(Activity context, List<Detail> detail) {
        super(context,0, detail);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View  listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.design_movie_list,null);
        }

        Detail currentDetail = getItem(position);

        TextView t1 = (TextView) listItemView.findViewById(R.id.t1);
        t1.setText(currentDetail.getTextView());

        TextView t2 = (TextView) listItemView.findViewById(R.id.t2);
        t2.setText(currentDetail.getTextView1());

        TextView t3 = (TextView) listItemView.findViewById(R.id.t3);
        t3.setText(currentDetail.getTextView2());

        TextView t4 = (TextView) listItemView.findViewById(R.id.t4);
        t4.setText(currentDetail.getTextView3());

        ImageView iv1 = (ImageView) listItemView.findViewById(R.id.iv);
        Picasso.with(getContext()).load(currentDetail.getimageView()).into(iv1);

        return listItemView;
    }

}
