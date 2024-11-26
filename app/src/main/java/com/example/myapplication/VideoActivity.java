package com.example.myapplication;

import android.os.Bundle;
import android.Manifest;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.webrtc.PeerConnectionFactory;
import org.webrtc.VideoCapturerAndroid;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;
import org.webrtc.EglBase;
import org.webrtc.SurfaceViewRenderer;

public class VideoActivity extends AppCompatActivity {

    private PeerConnectionFactory peerConnectionFactory;
    private VideoCapturerAndroid videoCapturer;
    private SurfaceViewRenderer surfaceViewRenderer;
    private EglBase rootEglBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video);

        surfaceViewRenderer = findViewById(R.id.surface_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize WebRTC
        PeerConnectionFactory.initialize(PeerConnectionFactory.InitializationOptions.builder(this).createInitializationOptions());

        // Create PeerConnectionFactory
        PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();
        peerConnectionFactory = PeerConnectionFactory.builder().setOptions(options).createPeerConnectionFactory();

        // Initialize EglBase
        rootEglBase = EglBase.create();

        // Set up video capturing
        videoCapturer = createVideoCapturer();
        if (videoCapturer == null) {
            Log.e("VideoActivity", "Failed to create video capturer");
            return;
        }

        // Create video source and track
        VideoSource videoSource = peerConnectionFactory.createVideoSource(videoCapturer.isScreencast());
        VideoTrack localVideoTrack = peerConnectionFactory.createVideoTrack("100", videoSource);

        // Initialize the surface renderer
        surfaceViewRenderer.init(rootEglBase.getEglBaseContext(), null);
        localVideoTrack.addSink(surfaceViewRenderer);

        // Start video capture
        videoCapturer.startCapture(1024, 768, 30);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoCapturer != null) {
            videoCapturer.stopCapture();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (peerConnectionFactory != null) {
            peerConnectionFactory.dispose();
        }
        if (rootEglBase != null) {
            rootEglBase.release();
        }
    }

    private VideoCapturerAndroid createVideoCapturer() {
        VideoCapturerAndroid capturer = VideoCapturerAndroid.create("front");
        if (capturer == null) {
            capturer = VideoCapturerAndroid.create("back");
        }
        return capturer;
    }
}
