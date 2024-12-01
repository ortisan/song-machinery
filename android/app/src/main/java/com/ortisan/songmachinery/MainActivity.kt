package com.ortisan.songmachinery

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.ortisan.songmachinery.audio.Frequency
import com.ortisan.songmachinery.audio.bufferSize
import com.ortisan.songmachinery.audio.sampleRate
import com.ortisan.songmachinery.tuner.components.AdmobBanner
import com.ortisan.songmachinery.tuner.components.Tuner
import com.ortisan.songmachinery.ui.theme.SongMachineryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Declare mutable state to store frequency
        val frequencyState = mutableDoubleStateOf(0.0)

        enableEdgeToEdge()

        setContent {
            SongMachineryTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AdmobBanner(modifier = Modifier.fillMaxWidth())

                    TunerCompose(frequencyState.doubleValue,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        // Check and request microphone permission
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) ==
                    PackageManager.PERMISSION_GRANTED -> {
                startAudioProcessing(frequencyState)
            }
            else -> {
                registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                    if (isGranted) {
                        startAudioProcessing(frequencyState)
                    } else {
                        throw IllegalStateException("Microphone permission denied")
                    }
                }.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startAudioProcessing(frequencyState: MutableState<Double>) {
        val audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize
        )

        val audioData = ShortArray(bufferSize)
        audioRecord.startRecording()

        Thread {
            while (true) {
                val readSize = audioRecord.read(audioData, 0, audioData.size)
                if (readSize > 0) {
                    val frequency = Frequency.detectPitch(audioData, sampleRate)
                    if (frequency > 0) {
                        runOnUiThread {
                            frequencyState.value = frequency
                        }
                    }
                }
            }
        }.start()
    }
}

@Composable
fun TunerCompose(frequency: Double, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Tuner(frequency = frequency)
    }
}




