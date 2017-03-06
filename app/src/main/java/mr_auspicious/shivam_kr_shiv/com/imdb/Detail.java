package mr_auspicious.shivam_kr_shiv.com.imdb;



public class Detail {


    String mTextView;
    String mTextView1;
    String mTextView2;
    String mTextView3;
    String mImageView;
    String mID;
    String mFavorite;
    String mWatchList;






    public Detail(String textView, String textView1, String textView2, String textView3, String imageView,String ID){
        mTextView = textView;
        mTextView1 = textView1;
        mTextView2 = textView2;
        mTextView3 = textView3;
        mImageView = imageView;
        mID = ID;
    }

    public Detail(String mTextView, String mTextView1, String mTextView2, String mTextView3, String mImageView, String mID, String mFavorite, String mWatchList) {
        this.mTextView = mTextView;
        this.mTextView1 = mTextView1;
        this.mTextView2 = mTextView2;
        this.mTextView3 = mTextView3;
        this.mImageView = mImageView;
        this.mID = mID;
        this.mFavorite = mFavorite;
        this.mWatchList = mWatchList;
    }

    public Detail() {

    }

    public String getTextView() {
        return mTextView;
    }

    public String getTextView1() {
        return mTextView1;
    }

    public String  getTextView2() {
        return mTextView2;
    }

    public String getTextView3() {
        return mTextView3;
    }



    public String getimageView() {
        return mImageView;
    }

    public String getID(){
        return mID;
    }

    public String getmWatchList() {
        return mWatchList;
    }

    public void setmTextView(String mTextView) {
        this.mTextView = mTextView;
    }

    public void setmTextView1(String mTextView1) {
        this.mTextView1 = mTextView1;
    }

    public void setmTextView2(String mTextView2) {
        this.mTextView2 = mTextView2;
    }

    public void setmTextView3(String mTextView3) {
        this.mTextView3 = mTextView3;
    }

    public void setmImageView(String mImageView) {
        this.mImageView = mImageView;
    }

    public void setmID(String mID) {
        this.mID = mID;
    }

    public void setmFavorite(String mFavorite) {
        this.mFavorite = mFavorite;
    }

    public void setmWatchList(String mWatchList) {
        this.mWatchList = mWatchList;
    }

    public String getmFavorite() {
        return mFavorite;
    }
}
