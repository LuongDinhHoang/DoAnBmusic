package com.example.tung_hoang_bmusic.ui.holder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;

import com.example.tung_hoang_bmusic.R;
import com.example.tung_hoang_bmusic.activity.AddSongToPlaylist;
import com.example.tung_hoang_bmusic.model.Song;
import com.example.tung_hoang_bmusic.service.MediaPlaybackService;
import com.example.tung_hoang_bmusic.ui.recycler.BaseRecyclerViewHolder;
import com.example.tung_hoang_bmusic.ui.recycler.RecyclerActionListener;
import com.example.tung_hoang_bmusic.ui.recycler.RecyclerData;

public class AddSongInPlayListItemHolder extends BaseRecyclerViewHolder {

    private CheckBox mSelectSong;

    public AddSongInPlayListItemHolder(@NonNull View itemView) {
        super(itemView);
        mSelectSong = itemView.findViewById(R.id.item_song);
        attachListener();
    }

    @Override
    public void bindViewHolder(RecyclerData data) {
        if (data instanceof Song) {
            Song song = (Song) data;
            mSelectSong.setText(song.getNameSong());
            mSelectSong.setOnCheckedChangeListener(null);
        } else if (data instanceof AddSongToPlaylist.CheckboxSong) {
            AddSongToPlaylist.CheckboxSong cbSong = (AddSongToPlaylist.CheckboxSong) data;
            mSelectSong.setText(cbSong.mSong.getNameSong());
            mSelectSong.setChecked(cbSong.mChecked);
            mSelectSong.setOnCheckedChangeListener((buttonView, isChecked) -> cbSong.mChecked = isChecked);
        }

    }

    @Override
    public void setupClickableViews(RecyclerActionListener actionListener) {
        itemView.setOnClickListener(v -> actionListener.onViewClick(getAdapterPosition(), v, AddSongInPlayListItemHolder.this));
    }

    @Override
    public void setService(MediaPlaybackService service) {
    }

    //Listener nhận sự kiện khi các Checkbox thay đổi trạng thái
    CompoundButton.OnCheckedChangeListener m_listener
            = (compoundButton, b) -> System.out.println("HoangLD: compoundButton " + compoundButton + "  text " + compoundButton.getText());

    //Gán Listener vào CheckBox
    void attachListener() {
        mSelectSong.setOnCheckedChangeListener(m_listener);
    }
}
