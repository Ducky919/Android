package com.hfad.myicon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;

public class HistoryActivity extends Activity {
  PopupWindow popupWindow;
  RelativeLayout relativeLayout;
  ImageButton showPopupBtn;
  Button closePopupBtn;

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.calculator_history);

    relativeLayout = findViewById(R.id.relativeLayout);
    // Declare backButton and action when clicked
    ImageButton backButton = findViewById(R.id.backButton);
    ImageButton deleteButton = findViewById(R.id.deleteButton);


    backButton.setOnClickListener(getOnBackButtonClickListener());
    deleteButton.setOnClickListener(getOnDeleteButtonClickListener());
  }

  protected View.OnClickListener getOnDeleteButtonClickListener() {
    return new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        LayoutInflater layoutInflater = (LayoutInflater) HistoryActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.popup,null);
        closePopupBtn = customView.findViewById(R.id.closePopupBtn);
        popupWindow =
            new PopupWindow(
                customView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        popupWindow.showAtLocation(relativeLayout, Gravity.CENTER, 0, 0);

        closePopupBtn.setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                popupWindow.dismiss();
              }
            });
      }
    };
  }

  private View.OnClickListener getOnBackButtonClickListener() {
    return new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent i = new Intent(HistoryActivity.this, MainActivity.class);
        startActivity(i);
      }
    };
  }
}
