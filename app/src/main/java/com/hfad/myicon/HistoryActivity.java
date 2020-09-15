package com.hfad.myicon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class HistoryActivity extends Activity {
  PopupWindow popupWindow;
  LinearLayout calculatorMainLayOut;
  LinearLayout calculatorTextViewLayOut;
  Button closePopupBtn;
  Button okButton;
  ImageButton backButton;
  ImageButton deleteButton;
  ArrayList<String> historyArray = new ArrayList<String>();
  String historyKey = "historyKey";

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.calculator_history);

    // Declare Variable
    calculatorMainLayOut = findViewById(R.id.calculatorHistory);
    calculatorTextViewLayOut = findViewById(R.id.calculatorScreenHistory);
    backButton = (ImageButton) findViewById(R.id.backButton);
    deleteButton = (ImageButton) findViewById(R.id.deleteButton);
    backButton.setOnClickListener(getOnBackButtonClickListener());
    deleteButton.setOnClickListener(getOnDeleteButtonClickListener());
    setDataToHistoryScreen();

  }

//  @Override
//  protected void onPause() {
//    super.onPause();
//    saveArrayList(historyArray,historyKey);
//  }

//  @Override
//  protected void onStart() {
//    super.onStart();
//
//  }
  @Override
  protected  void onStop() {
    super.onStop();
    saveArrayList(historyArray,historyKey);
  }

  private void setDataToHistoryScreen() {
    Intent i = getIntent();
    historyArray = (ArrayList<String>) i.getStringArrayListExtra("historyArray");
    if (historyArray != null) {
      createTextViewResult(historyArray);
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
              MainActivity.historyArray.clear();
              deleteArrayList();
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

  private void createTextViewResult(ArrayList<String> historyArray) {
    for (int i = 0; i < historyArray.size(); i++) {
      TextView textViewResult = new TextView(this);
      textViewResult.setText(historyArray.get(i));
      textViewResult.setTextSize(40);
      calculatorTextViewLayOut.addView(textViewResult);
    }
  }

  public void saveArrayList(ArrayList<String> historyArray, String key) {
    SharedPreferences prefs = getSharedPreferences("historyArray",Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = prefs.edit();
    Gson gson = new Gson();
    String json = gson.toJson(historyArray);
    editor.putString(key, json);
    editor.apply();
  }

  public void deleteArrayList() {
    SharedPreferences prefs = getSharedPreferences("historyArray",Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = prefs.edit();
    editor.clear();
    editor.apply();
  }
}
