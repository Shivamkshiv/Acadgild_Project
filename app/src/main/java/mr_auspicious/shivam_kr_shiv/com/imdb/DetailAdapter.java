package mr_auspicious.shivam_kr_shiv.com.imdb;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.List;


public class DetailAdapter extends ArrayAdapter<Detail> {


    public DetailAdapter(MainActivity context, List<Detail> detail) {
        super(context,0, detail);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View  listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.design_movie_list,null);
        }

        Detail currentDetail = getItem(position);


        TextView tx1 = (TextView) listItemView.findViewById(R.id.tv1);
        tx1.setText(currentDetail.getTextView());

        TextView tx2 = (TextView) listItemView.findViewById(R.id.textView);
        tx2.setText(currentDetail.getTextView1());

        TextView  tx3 = (TextView) listItemView.findViewById(R.id.textView2);
        tx3.setText( currentDetail.getTextView2());

        TextView tx4 = (TextView) listItemView.findViewById(R.id.textView3);
        tx4.setText(currentDetail.getTextView3());

        TextView tx5 = (TextView) listItemView.findViewById(R.id.textView4);
        tx5.setText(currentDetail.getmTextView4());

        ImageView iv1 = (ImageView) listItemView.findViewById(R.id.iv);
        Picasso.with(getContext()).load(currentDetail.getimageView()).into(iv1);

        return listItemView;

    }
}
