package com.example.newtips.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.newtips.GlobalData;
import com.example.newtips.R;

import java.util.LinkedList;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder>
{

    private final LinkedList<String> mWordList;
    private LayoutInflater mInflater;

    public static final String RECEIVE_ACTION = "GetRecycleReceive";
    public static final String RECEIVE_STRING = "RecycleString";
    int currentIndex = 0;

    SharedPreferences pref;

    class WordViewHolder extends RecyclerView.ViewHolder
    {
        private TextView title,macaddresstext;
        private ImageView delet_pic,add_pic;

        public WordViewHolder(View itemView, WordListAdapter adapter)
        {
            super(itemView);
            macaddresstext=itemView.findViewById(R.id.firstText);
            title=itemView.findViewById(R.id.title);
            delet_pic=itemView.findViewById(R.id.image_delete);
            add_pic=itemView.findViewById(R.id.image_add);
        }
    }


    public WordListAdapter(Context context,
                           LinkedList<String> wordList) {
        mInflater = LayoutInflater.from(context);
        pref=context.getSharedPreferences("MAC_NAME",Context.MODE_PRIVATE);
        this.mWordList = wordList;
    }


    @Override
    public int getItemCount() {
        return mWordList.size();
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        View mItemView = mInflater.inflate(R.layout.recycle_item,
                parent, false);

/*
        mItemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {


                int currentPosition = getClickedPosition(view);
                Log.d("DEBUG", "" + currentPosition);
                setButtonCustomDialog(currentPosition);
                Log.w("show",Integer.toString(viewType));


            }
        });*/
        return new WordViewHolder(mItemView, this);
    }
    private int getClickedPosition(View clickedView)
    {
        RecyclerView recyclerView = (RecyclerView) clickedView.getParent();
        WordViewHolder currentViewHolder = (WordViewHolder) recyclerView.getChildViewHolder(clickedView);
        return currentViewHolder.getAdapterPosition();
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        final int currentPosition= holder.getLayoutPosition();

        holder.macaddresstext.setText(mWordList.get(currentPosition));

        holder.title.setText (pref.getString(mWordList.get(currentPosition),""));

        holder.delet_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG", "" + currentPosition);
                setButtonCustomDialog(currentPosition);
            }
        });
        holder.add_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEdittextCustomDialog(currentPosition,holder.title);
            }
        });
        holder.title.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int lastIndex = currentIndex;
                currentIndex = currentPosition;
                notifyItemChanged(currentIndex);

                if (!(lastIndex<0)){
                    notifyItemChanged(lastIndex);

                }
                GlobalData.FSM="Inituserdata";
            }
        });
        if (currentIndex == position){
            holder.title.setBackgroundColor(Color.rgb(41,187,68));
            GlobalData.macaddress_select= mWordList.get(currentIndex);


        }else {
            holder.title.setBackgroundColor(Color.rgb(255,102,101));
        }





    }
    private void setButtonCustomDialog(final int final_position){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mInflater.getContext());
        alertDialog.setTitle("確定刪除?");
        alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.e("pos",Integer.toString(final_position));
                /**以Intent的方式建立廣播，將得到的值傳至主要Activity*/
                Intent intent = new Intent();
                intent.setAction(RECEIVE_ACTION);
                intent.putExtra(RECEIVE_STRING,mWordList.get(final_position));
                mInflater.getContext().sendBroadcast(intent);
            }
        });
        alertDialog.setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.setCancelable(true);
        alertDialog.show();


    }
    private void setEdittextCustomDialog(final int final_position,TextView title){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mInflater.getContext());
        View v =  mInflater.inflate(R.layout.alert_edittext,null);
        EditText editText = v.findViewById(R.id.editext_alert);
        alertDialog.setTitle("設定名稱");
        alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.e("pos",Integer.toString(final_position));
                /**以Intent的方式建立廣播，將得到的值傳至主要Activity*/

                pref.edit().putString(mWordList.get(final_position),editText.getText().toString()).commit();
                notifyDataSetChanged();

            }
        });
        alertDialog.setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alertDialog.setView(v);
        alertDialog.setCancelable(false);
        alertDialog.show();

    }



}

