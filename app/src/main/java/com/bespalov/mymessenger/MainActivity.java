package com.bespalov.mymessenger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bespalov.mymessenger.adpters.MessageAdapter;
import com.bespalov.mymessenger.pojo.Message;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SEND_IMAGE = 100;
    private RecyclerView recyclerViewListMessage;
    private MessageAdapter adapter;
    private EditText editTextMessage;
    private ImageView imageViewSendImage, imageViewSendMessage;
    private String author = "Андрей";

    private   FirebaseFirestore db;
    private StorageReference mStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        imageViewSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textMessage = editTextMessage.getText().toString().trim();
                if (textMessage != null) {
                    sendMessage(textMessage, null);
                }
            }
        });
        imageViewSendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(intent, RC_SEND_IMAGE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        db.collection("MyMessage").orderBy("date").addSnapshotListener(new EventListener <QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null) {
                    recyclerViewListMessage.scrollToPosition(adapter.getItemCount());
                    List<Message> messages = value.toObjects(Message.class);
                    adapter.setMessages(messages);

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SEND_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    final StorageReference imagesRef = mStorageRef.child("images/" + uri.getLastPathSegment());
                    imagesRef.putFile(uri).continueWithTask(new Continuation <UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            return imagesRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                if (downloadUri != null) {
                                    sendMessage("", downloadUri.toString() );
                                }
                            } else {
                                // Handle failures
                                // ...
                            }
                        }
                    });
            }
        }

        }
    }

    private void init() {
        db = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        editTextMessage = findViewById(R.id.editTextMessage);
        imageViewSendMessage = findViewById(R.id.imageViewSendMessage);
        imageViewSendImage = findViewById(R.id.imageViewSendImage);
        recyclerViewListMessage = findViewById(R.id.recyclerViewListMessage);
        adapter = new MessageAdapter();
        recyclerViewListMessage.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewListMessage.setAdapter(adapter);

    }

    private void sendMessage(String textMessage, String imageUrl) {
        editTextMessage.setText("");
            if (!textMessage.isEmpty() || !imageUrl.isEmpty()) {
                db.collection("MyMessage").add(new Message(author, textMessage,System.currentTimeMillis(), imageUrl)).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(MainActivity.this, "Load to DB", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error to load", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            }
    }
