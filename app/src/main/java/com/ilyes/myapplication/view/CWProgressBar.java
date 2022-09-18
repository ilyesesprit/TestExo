package com.ilyes.myapplication.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.ilyes.myapplication.R;
import com.ilyes.myapplication.databinding.CWProgressBarBinding;

import java.text.MessageFormat;
import java.util.Timer;
import java.util.TimerTask;

public class CWProgressBar extends LinearLayout {

    CWProgressBarBinding cwProgressBarBinding;
    private Timer timer;
    Context mContext = null;
    String[] listMessage;
    int index = 0;

    public Timer getTimer() {
        return timer;
    }

    public CWProgressBar(Context context) {

        super(context);
        mContext = context;


    }

    public CWProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        listMessage = getResources().getStringArray(R.array.messages);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CWProgressBar);
        String messageText = a.getString(R.styleable.CWProgressBar_messageText);
        String percentageText = a.getString(R.styleable.CWProgressBar_percentageText);
        int progress = a.getInt(R.styleable.CWProgressBar_progressBar, 0);
        messageText = messageText == null ? "" : messageText;
        progress = Math.max(progress, 0);
        String service = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(service);
        cwProgressBarBinding = DataBindingUtil.inflate(li, R.layout.c_w_progress_bar, this, true);
        cwProgressBarBinding.messageText.setText(messageText);
        cwProgressBarBinding.progressBar.setProgress(progress);
        cwProgressBarBinding.percentage.setText(percentageText);
        a.recycle();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (index >= listMessage.length) {
                    index = 0;
                }
                System.out.println(MessageFormat.format("test timer progress listMessage[{0}] {1}", index, listMessage[index]));
                cwProgressBarBinding.messageText.setText(listMessage[index++]);
            }
        }, 0, 6000);
    }

    public CWProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    @SuppressWarnings("unused")
    public void setMessageText(String text) {
        cwProgressBarBinding.messageText.setText(text);
    }

    @SuppressWarnings("unused")
    public void setProgressBar(int progress) {
        cwProgressBarBinding.progressBar.setProgress(progress);
    }

    @SuppressWarnings("unused")
    public void setPercentageText(String text) {
        cwProgressBarBinding.percentage.setText(text);
    }

    @SuppressWarnings("unused")
    public String getMessageText() {
        return cwProgressBarBinding.messageText.getText().toString();
    }

    @SuppressWarnings("unused")
    public String getPercentageText(String text) {
        return cwProgressBarBinding.percentage.getText().toString();
    }


    @SuppressWarnings("unused")
    public int getProgressBar() {
        return cwProgressBarBinding.progressBar.getProgress();
    }

}