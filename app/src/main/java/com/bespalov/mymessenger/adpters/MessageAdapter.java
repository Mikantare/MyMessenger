package com.bespalov.mymessenger.adpters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Shader;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bespalov.mymessenger.R;
import com.bespalov.mymessenger.pojo.Message;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> messages;
    private int TYPE_MY_MESSAGE = 50;
    private int TYPE_OTHER_MESSAGE = 51;

    private Context context;


    public MessageAdapter(Context context) {
        messages = new ArrayList<>();
        this.context = context;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        String author = message.getAuthor();
        if (author.equals(PreferenceManager.getDefaultSharedPreferences(context).getString("author", "anonimus"))) {
            return TYPE_MY_MESSAGE;
        } else {
            return TYPE_OTHER_MESSAGE;
        }
            }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_MY_MESSAGE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_message, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_other_message, parent, false);
        }
        return new MessageViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        String url = message.getImageUrl();
        String textOfMessage = message.getTextMessage();
        holder.textViewAuthor.setText(message.getAuthor());
        if (url == null || url.isEmpty()) {
            holder.imageViewImage.setVisibility(View.GONE);
        } else {
            holder.imageViewImage.setVisibility(View.VISIBLE);
        }
        if (textOfMessage != null && !textOfMessage.isEmpty()) {
            holder.textViewTextMessage.setVisibility(View.VISIBLE);
            holder.textViewTextMessage.setText(textOfMessage);
        } else {
            holder.textViewTextMessage.setVisibility(View.GONE);
        }
        if (url != null && !url.isEmpty()) {
            Picasso.get().load(url).into(holder.imageViewImage);
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewAuthor, textViewTextMessage;
        private ImageView imageViewImage;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAuthor = itemView.findViewById(R.id.textViewAuthor);
            textViewTextMessage = itemView.findViewById(R.id.textViewTextMessage);
            imageViewImage = itemView.findViewById(R.id.imageViewImage);
        }
    }
}
