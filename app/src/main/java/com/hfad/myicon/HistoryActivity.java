package com.hfad.myicon;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HistoryActivity extends Activity {
  PopupWindow popupWindow;
  LinearLayout calculatorMainLayOut;
  LinearLayout calculatorTextViewLayOut;
  Button closePopupBtn;
  Button okButton;
  ImageButton backButton;
  ImageButton deleteButton;
  CalculatorDatabaseHelper db;
  Map<Integer, ArrayList<String>> result = new HashMap<>();

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.calculator_history);

    db = new CalculatorDatabaseHelper(this);

    // Declare Variable
    calculatorMainLayOut = findViewById(R.id.calculatorHistory);
    calculatorTextViewLayOut = findViewById(R.id.calculatorScreenHistory);
    backButton = findViewById(R.id.backButton);
    deleteButton = findViewById(R.id.deleteButton);
    backButton.setOnClickListener(getOnBackButtonClickListener());
    deleteButton.setOnClickListener(getOnDeleteButtonClickListener());

    result = db.getTextData();
    createTextViewResult(result);
  }

  private View.OnClickListener getOnDeleteButtonClickListener() {
    return new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        LayoutInflater layoutInflater =
            (LayoutInflater) HistoryActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.popup, null);
        closePopupBtn = customView.findViewById(R.id.closePopupBtn);
        okButton = customView.findViewById(R.id.ok);
        popupWindow =
            new PopupWindow(
                customView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.showAtLocation(calculatorMainLayOut, Gravity.CENTER, 0, 0);
        getOnCloseButton(closePopupBtn, popupWindow);
        getOnOkButton(okButton, calculatorTextViewLayOut, popupWindow);
      }
    };
  }

  private void getOnOkButton(
      Button okButton, final LinearLayout calculatorScreenLayout, final PopupWindow popupWindow) {
    okButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if (calculatorScreenLayout.getChildCount() > 0) {
              calculatorScreenLayout.removeAllViews();
              popupWindow.dismiss();
              db.deleteTable();
            }
          }
        });
  }

  private static void getOnCloseButton(Button closePopupBtn, final PopupWindow popupWindow) {
    closePopupBtn.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            popupWindow.dismiss();
          }
        });
  }

  private View.OnClickListener getOnBackButtonClickListener() {
    return new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    };
  }

  private void createTextViewResult(Map<Integer, ArrayList<String>> input) {
    for (Map.Entry<Integer, ArrayList<String>> entry : input.entrySet()) {
      // Add Layout to store Result and DateTime Data Within a line
      LinearLayout layout = new LinearLayout(this);
      layout.setOrientation(LinearLayout.HORIZONTAL);
      layout.setLayoutParams(
          new LinearLayout.LayoutParams(
              LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

      // Result TextView
      TextView textViewResult = new TextView(this);
      textViewResult.setLayoutParams(
          new TableLayout.LayoutParams(
              TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1f));
      textViewResult.setText(entry.getValue().get(0));
      textViewResult.setTextSize(22);
      layout.addView(textViewResult);

      // DateTime TextView
      TextView dateTimeResult = new TextView(this);
      dateTimeResult.setLayoutParams(
          new TableLayout.LayoutParams(
              TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1f));
      dateTimeResult.setText(entry.getValue().get(1));
      dateTimeResult.setGravity(Gravity.RIGHT);
      dateTimeResult.setTextSize(22);
      layout.addView(dateTimeResult);

      calculatorTextViewLayOut.addView(layout);
    }
  }
}
