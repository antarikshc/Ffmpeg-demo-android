package com.antarikshc.demoffmpeg

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.antarikshc.demoffmpeg.util.AppExecutors
import com.arthenica.mobileffmpeg.FFmpeg
import com.arthenica.mobileffmpeg.FFmpeg.RETURN_CODE_CANCEL
import com.arthenica.mobileffmpeg.FFmpeg.RETURN_CODE_SUCCESS


class MainActivity : AppCompatActivity() {

    private var editCommand: EditText? = null
    private var textResult: TextView? = null
    private var executors: AppExecutors = AppExecutors.instance!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editCommand = findViewById(R.id.edit_ffmpeg_command)
        textResult = findViewById(R.id.text_result)

    }

    fun runCommand(view: View) {

        try {

            // Use AppExecutors to run on Worker Thread
            executors.diskIO().execute {

                // Execute the FFMPEG command
                val rc: Int = FFmpeg.execute(editCommand?.text.toString())

                // Get the results
                val output: String = FFmpeg.getLastCommandOutput()

                when (rc) {
                    RETURN_CODE_SUCCESS -> {
                        Log.i("FFMPEG", "Command execution completed successfully.")
                        textResult?.text = output
                    }
                    RETURN_CODE_CANCEL -> Log.i("FFMPEG", "Command execution cancelled by user.")
                    else -> Log.i(
                        "FFMPEG",
                        String.format("Command execution failed with rc=%d and output=%s.", rc, output)
                    )
                }

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

}
