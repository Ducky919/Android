package com.hfad.myicon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class HistoryActivity extends Activity {
  PopupWindow popupWindow;
  LinearLayout calculatorHistoryLayout;
  Button closePopupBtn;

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.calculator_history);
    calculatorHistoryLayout = findViewById(R.id.calculatorHistory);

    Intent i = getIntent();
    ArrayList<String> historyArray = i.getStringArrayListExtra("historyArray");
    if (historyArray != null) {
      createTextViewResult(historyArray);
    }

    // Declare backButton and action when clicked
    ImageButton backButton = findViewById(R.id.backButton);
    ImageButton deleteButton = findViewById(R.id.deleteButton);

    backButton.setOnClickListener(getOnBackButtonClickListener());
    deleteButton.setOnClickListener(getOnDeleteButtonClickListener());
  }

  private void createTextViewResult(ArrayList<String> historyArray) {
    for (int i = 0; i < historyArray.size(); i++) {
      TextView textViewResult = new TextView(this);
      textViewResult.setText(historyArray.get(i));
      textViewResult.setTextSize(40);
      calculatorHistoryLayout.addView(textViewResult);
    }
  }

  private View.OnClickListener getOnDeleteButtonClickListener() {
    return new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        LayoutInflater layoutInflater =
            (LayoutInflater) HistoryActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.popup, null);
        closePopupBtn = customView.findViewById(R.id.closePopupBtn);
        popupWindow =
            new PopupWindow(
                customView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.showAtLocation(calculatorHistoryLayout, Gravity.CENTER, 0, 0);
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
