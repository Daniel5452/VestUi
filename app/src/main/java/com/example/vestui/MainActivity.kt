package com.example.vestui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import quevedo.soares.leandro.blemadeeasy.BLE
import quevedo.soares.leandro.blemadeeasy.BluetoothConnection
import android.widget.SeekBar
import android.widget.TextView


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startBLEScanning()
    }

    @SuppressLint("MissingPermission")
    private fun startBLEScanning() {
        val ble = BLE(activity = this)

        ble.verifyPermissionsAsync(
            rationaleRequestCallback = { next ->
                // Improvement: show an Alert or UI explaining why the permissions are required
                next()
            },
            callback = { granted ->
                if (granted) {
                    // Continue with UI
                } else {
                    // Show an Alert or UI indicating that the permissions are required
                }
            }
        )
        ble.scanForAsync(
            macAddress = "84:C6:92:87:91:E4",
            //name = "HMSoft",
            service = "0000FFE0-0000-1000-8000-00805F9B34FB",

            onFinish = { connection ->
                if (connection != null) {
                    //Test for connection
                    connection.write("0000FFE1-0000-1000-8000-00805F9B34FB", "Testing")

                    //Show UI
                    setContentView(R.layout.lui)

                    val compressionSeekBar: SeekBar = findViewById(R.id.compressionLevelSeekBar)
                    val compressionLevelText: TextView = findViewById(R.id.compressionLevelText)

                    compressionSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                            var levelDescription = ""
                            when (progress) {
                                0 -> levelDescription = "No Compression"
                                1 -> levelDescription = "Low Compression"
                                2 -> levelDescription = "Medium Compression"
                                3 -> levelDescription = "High Compression"
                            }
                            compressionLevelText.text = "Compression Level: $levelDescription"

                            val message = progress.toString()
                            //Transmit compression level
                            connection.write(characteristic = "0000FFE1-0000-1000-8000-00805F9B34FB", message, charset = Charsets.UTF_8)

                        }

                        override fun onStartTrackingTouch(seekBar: SeekBar) {
                            // Implement if needed
                        }

                        override fun onStopTrackingTouch(seekBar: SeekBar) {
                            // Implement if needed
                        }
                    })


                } else {
                    // Improvement: show an Alert or UI with error message about the device not being available
                }
            },

            onError = { errorCode ->
                // Improvement: show an Alert or UI about the error
            }
        )

    }


}
