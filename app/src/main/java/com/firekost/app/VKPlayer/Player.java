package com.firekost.app.VKPlayer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONException;

import java.io.IOException;
import java.util.Locale;

public class Player extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener{

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private ImageView play, loopOn, shuffleOn, dlOn, addOn;
    private TextView lyrics, current, duration, title, artist;
    private SeekBar seekBar;
    private String LYRICS;
    private final android.os.Handler handler = new android.os.Handler();
    private int position;
    private SQLiteDatabase sqdb;
    private Cursor cursor;
    private boolean isShuffled, isDownloaded, isAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        position = getIntent().getExtras().getInt("POSITION") + 1;

        DatabaseHelper db = new DatabaseHelper(this, "myaudios.db");
        sqdb = db.getWritableDatabase();

        play = (ImageView) findViewById(R.id.play);
        lyrics = (TextView) findViewById(R.id.lyrics);
        current = (TextView) findViewById(R.id.current);
        duration = (TextView) findViewById(R.id.duration);
        title = (TextView) findViewById(R.id.playerTitle);
        artist = (TextView) findViewById(R.id.playerArtist);
        loopOn = (ImageView) findViewById(R.id.loopEnabled);
        shuffleOn = (ImageView) findViewById(R.id.shuffleEnabled);
        dlOn = (ImageView) findViewById(R.id.dlEnabled);
        addOn = (ImageView) findViewById(R.id.addEnabled);

        if (mediaPlayer.isLooping()) loopOn.setVisibility(View.VISIBLE);
        if (isShuffled) shuffleOn.setVisibility(View.VISIBLE);
        if (isDownloaded) dlOn.setVisibility(View.VISIBLE);
        if (isAdded) addOn.setVisibility(View.VISIBLE);

        setData(mediaPlayer);
    }

    public void onPlayClick(View v){
        if (!mediaPlayer.isPlaying()) {
            try {
                mediaPlayer.start();
                startPlayProgressUpdater();
                play.setImageResource(R.drawable.pause);
            } catch (IllegalStateException e) {
                mediaPlayer.pause();
            }
        } else {
            play.setImageResource(R.drawable.play);
            mediaPlayer.pause();
        }
    }

    public void onPrevClick(View v) {
        try {
            position--;
            setData(mediaPlayer);
        } catch (Exception ignored) {}
    }

    public void onNextClick(View v) {
        try {
            position++;
            setData(mediaPlayer);
        } catch (Exception ignored) {}
    }

    public void onLoopClick(View v){
        if (!mediaPlayer.isLooping()){
            mediaPlayer.setLooping(true);
            loopOn.setVisibility(View.VISIBLE);
        } else {
            mediaPlayer.setLooping(false);
            loopOn.setVisibility(View.INVISIBLE);
        }
    }

    public void onShuffleClick(View v){

    }

    public void onDownloadClick(View v){

    }

    public void onAddClick(View v){
        cursor = sqdb.rawQuery("SELECT audio_id FROM audios WHERE _id=" + position, null);
        cursor.moveToFirst();
        int audioId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.AUDIO_ID));
        VKRequest request = VKApi.audio().add(VKParameters.from(audioId, 83758810));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);

            }
        });
    }

    public void setData(MediaPlayer mp){
        cursor = sqdb.rawQuery("SELECT url FROM audios WHERE _id=" + position, null);
        cursor.moveToFirst();
        String url = cursor.getString(cursor.getColumnIndex(DatabaseHelper.URL));
        System.out.println(url);
        cursor.close();
        mp.reset();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mp.setDataSource(url);
            mp.setOnPreparedListener(this);
            mp.setOnCompletionListener(this);
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        cursor = sqdb.rawQuery("SELECT title FROM audios WHERE _id=" + position, null);
        cursor.moveToFirst();
        title.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TITLE)));
        cursor.close();

        cursor = sqdb.rawQuery("SELECT artist FROM audios WHERE _id=" + position, null);
        cursor.moveToFirst();
        artist.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ARTIST)));
        cursor.close();

        cursor = sqdb.rawQuery("SELECT duration FROM audios WHERE _id=" + position, null);
        cursor.moveToFirst();
        int durationInt = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.DURATION));
        duration.setText(String.format(Locale.ENGLISH, "%2d:%02d", durationInt/60, durationInt%60));
        cursor.close();

        cursor = sqdb.rawQuery("SELECT lyrics_id FROM audios WHERE _id=" + position, null);
        cursor.moveToFirst();
        int lyrics_id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.LYRICS_ID));
        VKRequest request = VKApi.audio().getLyrics(VKParameters.from(lyrics_id));
        request.executeWithListener(new VKRequest.VKRequestListener() {                             //не работает
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);

                Log.wtf("RESPONSE", String.valueOf(response));
                try {
                    LYRICS = response.json.getJSONObject("response").getString("text");
                    Log.e("LYRICS", LYRICS);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                lyrics.setText(LYRICS);
            }
        });
        cursor.close();
    }

    public void startPlayProgressUpdater() {
        seekBar.setProgress(mediaPlayer.getCurrentPosition());

        if (mediaPlayer.isPlaying()) {
            play.setImageResource(R.drawable.pause);
            Runnable notification = new Runnable() {
                public void run() {
                    startPlayProgressUpdater();

                    int pos = mediaPlayer.getCurrentPosition();
                    current.setText(String.format(Locale.ENGLISH, "%2d:%02d", pos/1000/60, pos/1000%60));
                }
            };
            handler.postDelayed(notification, 1000);
        } else {
            //mediaPlayer.pause();
            play.setImageResource(R.drawable.play);
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.wtf("Preparation", "finished");
        mp.start();

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mediaPlayer.isPlaying()) {
                    SeekBar sb = (SeekBar) v;
                    mediaPlayer.seekTo(sb.getProgress());

                    int pos = mediaPlayer.getCurrentPosition();
                    current.setText(String.format(Locale.ENGLISH, "%2d:%02d", pos/1000/60, pos/1000%60));
                }
                return false;
            }
        });
        startPlayProgressUpdater();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        try {
            position++;
            setData(mp);
        } catch (Exception ignored) {}
    }
}
