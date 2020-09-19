package com.bespalov.mymessenger.adpters;

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


    public MessageAdapter() {
        messages = new ArrayList<>();
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        String url = message.getImageUrl();
        String textOfMessage = message.getTextMessage();
        holder.textViewAuthor.setText(message.getAuthor());
        if (textOfMessage != null) {
            holder.imageViewImage.setVisibility(View.INVISIBLE);
            holder.textViewTextMessage.setVisibility(View.VISIBLE);
            holder.textViewTextMessage.setText(message.getTextMessage());
        } else if (url != null) {
            holder.imageViewImage.setVisibility(View.VISIBLE);
            holder.textViewTextMessage.setVisibility(View.INVISIBLE);
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
