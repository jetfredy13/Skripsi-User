package com.example.psikologiku;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.psikologiku.Chat.UserMessage;

import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageListViewHolder> {
    private Context mContext;
    private List<UserMessage>  mList;
    private String imageUrl;
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private SharedPreferences sp;

    public MessageListAdapter(Context context , List<UserMessage> message , String imgUrl){
        this.mContext = context;
        this.mList = message;
        this.imageUrl = imgUrl;
    }

    @Override
    public int getItemViewType(int position) {
        sp = mContext.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String kata = sp.getString("username","");
        if(mList.get(position).getSended().equals(kata)){
            return MSG_TYPE_RIGHT;
        }
        else{
            return MSG_TYPE_LEFT;
        }
    }

    public class MessageListViewHolder extends RecyclerView.ViewHolder{
        public TextView message;
        public ImageView profile_image;

        public MessageListViewHolder(View itemView){
            super(itemView);
            message = itemView.findViewById(R.id.message);
            profile_image = itemView.findViewById(R.id.psikolok_profile);
        }
    }
    @NonNull
    @Override
    public MessageListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT)
        {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_message_received,parent,false);
            return new MessageListAdapter.MessageListViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_message_sent,parent,false);
            return new MessageListAdapter.MessageListViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageListViewHolder holder, int position) {
        UserMessage message = mList.get(position);
        holder.message.setText(message.getMessage());
        holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        /*if(imageUrl.equals("default")){

        }*/
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    /*private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;
        SentMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
        }

        void bind(UserMessage message) {
            messageText.setText(message.getMessage());
            // Format the stored timestamp into a readable String using method.
            timeText.setText(Utils.formatDateTime(message.getCreatedAt()));
        }
    }*/



}
