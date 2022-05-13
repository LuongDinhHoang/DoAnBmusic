


package com.example.tung_hoang_bmusic.ui.recycler;

import android.util.Log;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;

import com.example.tung_hoang_bmusic.model.Playlist;
import com.example.tung_hoang_bmusic.model.Song;
import com.example.tung_hoang_bmusic.service.MediaPlaybackService;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.FirebaseException;
import com.firebase.client.Query;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @param <T> The class type to use as a model for the data contained in the children of the given Firebase location
 * @author greg
 * @since 6/21/13
 * <p>
 * This class is a generic way of backing an Android ListView with a Firebase location.
 * It handles all of the child events at the given Firebase location. It marshals received data into the given
 * class type. Extend this class and provide an implementation of <code>populateView</code>, which will be given an
 * instance of your list item mLayout and an instance your class that holds your data. Simply populate the view however
 * you like and this class will handle updating the list as the data changes.
 */
public abstract class FirebaseListAdapter<T extends RecyclerData> extends BaseRecyclerAdapter<T> {

    private final Query mRef;
    private final Class<T> mModelClass;
    private final List<T> mModels;
    private final List<String> mKeys;
    private final ChildEventListener mListener;
    Gson gson = new Gson();


    /**
     * @param mRef        The Firebase location to watch for data changes. Can also be a slice of a location, using some
     *                    combination of <code>limit()</code>, <code>startAt()</code>, and <code>endAt()</code>,
     * @param mModelClass Firebase will marshall the data at a location into an instance of a class that you provide
     */
    public FirebaseListAdapter(RecyclerActionListener actionListener, MediaPlaybackService service, Query mRef, Class<T> mModelClass) {
        super(actionListener, service);
        this.mRef = mRef;
        this.mModelClass = mModelClass;
        mModels = new ArrayList<>();
        mKeys = new ArrayList<>();
        mRef.keepSynced(true);
        // Look for all child events. We will then map them to our own internal ArrayList, which backs ListView
        mListener = this.mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

//                Type listType = new TypeToken<T>() {
//                }.getType();
//                T data = gson.fromJson(json, listType);
                T model = null;
                ;
                try {
                    model = dataSnapshot.getValue(FirebaseListAdapter.this.mModelClass);
                } catch (FirebaseException e) {
                    if (FirebaseListAdapter.this.mModelClass == Playlist.class) {
                        Object object = dataSnapshot.getValue(Object.class);
                        String json = gson.toJson(object);
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            JSONObject songListJson = jsonObject.getJSONObject("songList");
                            Iterator<String> iterator = songListJson.keys();
                            List<Song> songList = new ArrayList<>();
                            while (iterator.hasNext()) {
                                JSONObject songJson = songListJson.getJSONObject(iterator.next());
                                songList.add(gson.fromJson(String.valueOf(songJson), Song.class));
                            }
                            model = (T) new Playlist(jsonObject.getInt("idCategory"),
                                    jsonObject.getString("namePlaylist"), songList);
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                            return;
                        }
                    }
                }
                String key = dataSnapshot.getKey();

                // Insert into the correct location, based on previousChildName
                if (previousChildName == null) {
                    mModels.add(0, model);
                    mKeys.add(0, key);
                    notifyItemInserted(0);
                } else {
                    int previousIndex = mKeys.indexOf(previousChildName);
                    int nextIndex = previousIndex + 1;
                    if (nextIndex == mModels.size()) {
                        mModels.add(model);
                        mKeys.add(key);
                    } else {
                        mModels.add(nextIndex, model);
                        mKeys.add(nextIndex, key);
                    }
                    notifyItemInserted(nextIndex);
                }

                onContentChange();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // One of the mModels changed. Replace it in our list and name mapping
                String key = dataSnapshot.getKey();
                T newModel = dataSnapshot.getValue(FirebaseListAdapter.this.mModelClass);
                int index = mKeys.indexOf(key);

                mModels.set(index, newModel);

                onContentChange();
                notifyItemChanged(index);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                // A model was removed from the list. Remove it from our list and the name mapping
                String key = dataSnapshot.getKey();
                int index = mKeys.indexOf(key);

                mKeys.remove(index);
                mModels.remove(index);

                notifyItemRemoved(index);
                onContentChange();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

                // A model changed position in the list. Update our list accordingly
                String key = dataSnapshot.getKey();
                T newModel = dataSnapshot.getValue(FirebaseListAdapter.this.mModelClass);
                int index = mKeys.indexOf(key);
                mModels.remove(index);
                mKeys.remove(index);
                if (previousChildName == null) {
                    mModels.add(0, newModel);
                    mKeys.add(0, key);
                    notifyItemInserted(0);
                } else {
                    int previousIndex = mKeys.indexOf(previousChildName);
                    int nextIndex = previousIndex + 1;
                    if (nextIndex == mModels.size()) {
                        mModels.add(newModel);
                        mKeys.add(key);
                    } else {
                        mModels.add(nextIndex, newModel);
                        mKeys.add(nextIndex, key);
                    }
                    notifyItemMoved(previousIndex, nextIndex);
                }
                onContentChange();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("FirebaseListAdapter", "Listen was cancelled, no more updates will occur");
            }

        });
    }

    public void cleanup() {
        // We're being destroyed, let go of our mListener and forget about all of the mModels
        mRef.removeEventListener(mListener);
        mModels.clear();
        mKeys.clear();
    }

    @Nullable
    public String getKey(T model) {
        if (mModels.contains(model)) {
            return mKeys.get(mModels.indexOf(model));
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @CallSuper
    public void onContentChange() {
        update(mModels);
    }

    @Override
    public void removeItem(int i) {
        if (0 > i || i > mModels.size() - 1) return;
        mRef.getRef().child(mKeys.get(i)).removeValue();
    }
}
