package com.example.tung_hoang_bmusic.ui.holder;

import static com.example.tung_hoang_bmusic.model.Constants.FIREBASE_REALTIME_SONG_PATH;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tung_hoang_bmusic.R;
import com.example.tung_hoang_bmusic.model.Song;
import com.example.tung_hoang_bmusic.service.MediaPlaybackService;
import com.example.tung_hoang_bmusic.ui.recycler.BaseRecyclerViewHolder;
import com.example.tung_hoang_bmusic.ui.recycler.RecyclerActionListener;
import com.example.tung_hoang_bmusic.ui.recycler.RecyclerData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class HomeSongItemHolder extends BaseRecyclerViewHolder {
    private final ImageView mImageView;
    private final TextView mTextSongName;
    private final TextView mTextArtistsName;
    private DatabaseReference mDatabaseReference;

    public HomeSongItemHolder(@NonNull View itemView) {
        super(itemView);
        mImageView = itemView.findViewById(R.id.image_song_home);
        mTextSongName = itemView.findViewById(R.id.name_song);
        mTextArtistsName = itemView.findViewById(R.id.artists);
    }

    @Override
    public void bindViewHolder(RecyclerData data) {
        if (data instanceof Song) {
            Song song = (Song) data;
            if (TextUtils.isEmpty(song.getNameSong())) {
                if (mDatabaseReference == null)
                    mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                mDatabaseReference.child(FIREBASE_REALTIME_SONG_PATH).child(String.valueOf(song.getId()))
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Gson gson = new Gson();

                                Object object = dataSnapshot.getValue(Object.class);
                                String json = gson.toJson(object);

//                Type listType = new TypeToken<ArrayList<Song>>() {}.getType();
//                ArrayList<Song> data = gson.fromJson(json, listType);
                                try {
                                    Type listType = new TypeToken<Song>() {
                                    }.getType();
                                    Song value = gson.fromJson(json, listType);
                                    if (value == null) return;
                                    song.updateData(value);
                                    updateData(value);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
            } else {
                updateData(song);
            }
        }
    }

    private void updateData(Song song) {
        //Glide.... de load anh :|
        Glide.with(mImageView)
                .load(song.getImageUrl())
                .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
                .apply(RequestOptions.
                        placeholderOf(R.drawable.placeholder_music))
                .into(mImageView);

        mTextSongName.setText(song.getNameSong());
        mTextArtistsName.setText(song.getSinger());
    }

    @Override
    public void setupClickableViews(RecyclerActionListener actionListener) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onViewClick(getAdapterPosition(), v, HomeSongItemHolder.this);
            }
        });
    }

    @Override
    public void setService(MediaPlaybackService service) {

    }

}
