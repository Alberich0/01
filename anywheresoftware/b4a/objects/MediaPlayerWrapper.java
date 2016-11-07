package anywheresoftware.b4a.objects;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BA.ShortName;
import anywheresoftware.b4a.objects.streams.File;
import java.io.FileInputStream;
import java.io.IOException;

@ShortName("MediaPlayer")
public class MediaPlayerWrapper {
    protected String eventName;
    protected MediaPlayer mp;

    /* renamed from: anywheresoftware.b4a.objects.MediaPlayerWrapper.1 */
    class C00261 implements OnCompletionListener {
        private final /* synthetic */ BA val$ba;

        C00261(BA ba) {
            this.val$ba = ba;
        }

        public void onCompletion(MediaPlayer mp) {
            this.val$ba.raiseEvent(MediaPlayerWrapper.this, new StringBuilder(String.valueOf(MediaPlayerWrapper.this.eventName)).append("_complete").toString(), new Object[0]);
        }
    }

    public void Initialize() throws IllegalArgumentException, IllegalStateException, IOException {
        this.mp = new MediaPlayer();
    }

    public void Initialize2(BA ba, String EventName) throws IllegalArgumentException, IllegalStateException, IOException {
        Initialize();
        this.eventName = EventName.toLowerCase(BA.cul);
        this.mp.setOnCompletionListener(new C00261(ba));
    }

    public void Load(String Dir, String FileName) throws IllegalArgumentException, IllegalStateException, IOException {
        this.mp.reset();
        AssetFileDescriptor fd;
        if (Dir.equals(File.getDirAssets())) {
            fd = BA.applicationContext.getAssets().openFd(FileName.toLowerCase(BA.cul));
            if (fd.getDeclaredLength() < 0) {
                this.mp.setDataSource(fd.getFileDescriptor());
            } else {
                this.mp.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getDeclaredLength());
            }
        } else if (Dir.equals(File.getDirInternal()) || Dir.equals(File.getDirInternalCache())) {
            this.mp.setDataSource(new FileInputStream(new java.io.File(Dir, FileName)).getFD());
        } else if (Dir.equals(File.ContentDir)) {
            fd = BA.applicationContext.getContentResolver().openAssetFileDescriptor(Uri.parse(FileName), "r");
            if (fd.getDeclaredLength() < 0) {
                this.mp.setDataSource(fd.getFileDescriptor());
            } else {
                this.mp.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getDeclaredLength());
            }
        } else {
            this.mp.setDataSource(new java.io.File(Dir, FileName).toString());
        }
        this.mp.prepare();
    }

    public boolean getLooping() {
        return this.mp.isLooping();
    }

    public void setLooping(boolean value) {
        this.mp.setLooping(value);
    }

    public void Play() {
        this.mp.start();
    }

    public void Stop() {
        this.mp.reset();
    }

    public void Pause() {
        this.mp.pause();
    }

    public int getDuration() {
        return this.mp.getDuration();
    }

    public int getPosition() {
        return this.mp.getCurrentPosition();
    }

    public void setPosition(int value) {
        this.mp.seekTo(value);
    }

    public void SetVolume(float Right, float Left) {
        this.mp.setVolume(Right, Left);
    }

    public boolean IsPlaying() {
        return this.mp.isPlaying();
    }

    public void Release() {
        this.mp.release();
    }
}
