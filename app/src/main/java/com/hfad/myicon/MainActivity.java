package com.hfad.myicon;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.mbms.MbmsErrors;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Stack;

public class MainActivity<i> extends Activity {
  static final HashMap<Integer, String> idButtonMap = new HashMap<>();
  boolean isZero = false;
  double resultNumber;

  // Function to Execute Operator
  public double applyOp(char op, double operand1, double operand2) {
    switch (op) {
      case '+':
        return operand1 + operand2;
      case '-':
        return operand1 - operand2;
      case '*':
        return operand1 * operand2;
      case '/':
        return operand1 / operand2;
    }
    return 0;
  }

  // Check if existing operator and the last one has Precedence or not
  public boolean hasPrecedence(char op1, char op2) {
    return (op1 == '+' || op1 == '-') && (op2 == '*' || op2 == '/');
  }

  // Function to calculate math from input
  public double evaluateMath(String input) {
    Stack<Double> valueStack = new Stack<>();
    Stack<Character> operationStack = new Stack<>();
    double currentNumber = 0;
    double operand1;
    double operand2;
    char operator;
    for (int i = 0; i < input.length(); i++) {
      char currentChar = input.charAt(i);
      if (currentChar >= '0' && currentChar <= '9') {
        currentNumber = currentNumber * 10 + (currentChar - '0');
      } else {
        valueStack.push(currentNumber);
        currentNumber = 0;
        while (!operationStack.empty() && hasPrecedence(currentChar, operationStack.peek())) {
          operator = operationStack.pop();
          operand2 = valueStack.pop();
          operand1 = valueStack.pop();
          valueStack.push(applyOp(operator, operand1, operand2));
        }
        operationStack.push(currentChar);
      }
    }
    valueStack.push(currentNumber);

    // Execute remain operator in stack
    while (!operationStack.empty()) {
      operator = operationStack.pop();
      operand2 = valueStack.pop();
      operand1 = valueStack.pop();
      valueStack.push(applyOp(operator, operand1, operand2));
    }

    // The final number in stack value is result
    return valueStack.pop();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.calculator);

    idButtonMap.put(R.id.number0, "0");
    idButtonMap.put(R.id.number1, "1");
    idButtonMap.put(R.id.number2, "2");
    idButtonMap.put(R.id.number3, "3");
    idButtonMap.put(R.id.number4, "4");
    idButtonMap.put(R.id.number5, "5");
    idButtonMap.put(R.id.number6, "6");
    idButtonMap.put(R.id.number7, "7");
    idButtonMap.put(R.id.number8, "8");
    idButtonMap.put(R.id.number9, "9");
    idButtonMap.put(R.id.add, "+");
    idButtonMap.put(R.id.subtract, "-");
    idButtonMap.put(R.id.multiple, "*");
    idButtonMap.put(R.id.division, "/");

    // Get Calculate Result
    final TextView result = findViewById(R.id.calculatorScreen);

    // Get Number Button
    final Button number0 = findViewById(R.id.number0);
    final Button number1 = findViewById(R.id.number1);
    final Button number2 = findViewById(R.id.number2);
    final Button number3 = findViewById(R.id.number3);
    final Button number4 = findViewById(R.id.number4);
    final Button number5 = findViewById(R.id.number5);
    final Button number6 = findViewById(R.id.number6);
    final Button number7 = findViewById(R.id.number7);
    final Button number8 = findViewById(R.id.number8);
    final Button number9 = findViewById(R.id.number9);

    // Get Function Button
    final Button equal = findViewById(R.id.equal);
    final Button add = findViewById(R.id.add);
    final Button subtract = findViewById(R.id.subtract);
    final Button division = findViewById(R.id.division);
    final Button multiple = findViewById(R.id.multiple);
    final Button AC = findViewById(R.id.AC);
    final Button history = findViewById(R.id.history);

    number0.setOnClickListener(getOnButtonClickListener(result));
    number1.setOnClickListener(getOnButtonClickListener(result));
    number2.setOnClickListener(getOnButtonClickListener(result));
    number3.setOnClickListener(getOnButtonClickListener(result));
    number4.setOnClickListener(getOnButtonClickListener(result));
    number5.setOnClickListener(getOnButtonClickListener(result));
    number6.setOnClickListener(getOnButtonClickListener(result));
    number7.setOnClickListener(getOnButtonClickListener(result));
    number8.setOnClickListener(getOnButtonClickListener(result));
    number9.setOnClickListener(getOnButtonClickListener(result));
    add.setOnClickListener(getOnButtonClickListener(result));
    subtract.setOnClickListener(getOnButtonClickListener(result));
    multiple.setOnClickListener(getOnButtonClickListener(result));
    division.setOnClickListener(getOnButtonClickListener(result));
    history.setOnClickListener(getOnHistoryButtonClickListener());
    // AC button function
    AC.setOnClickListener(getOnACButtonClickListener(result));

    // Equal Button Click
    equal.setOnClickListener(getOnEqualButtonClickListener(result));
  }

  private View.OnClickListener getOnHistoryButtonClickListener() {
    return new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent i = new Intent(MainActivity.this,HistoryActivity.class);
        startActivity(i);
      }
    };
  }

  private View.OnClickListener getOnACButtonClickListener(final TextView result) {
    return new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        result.setText("0");
        isZero = true;
      }
    };
  }

  private View.OnClickListener getOnEqualButtonClickListener(final TextView result) {
    return new View.OnClickListener() {
      @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
      @Override
      public void onClick(View v) {
        resultNumber = evaluateMath(result.getText().toString());
        DecimalFormat df = new DecimalFormat("0.###");
        result.setText(df.format(resultNumber));
      }
    };
  }

  private View.OnClickListener getOnButtonClickListener(final TextView result) {
    return new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        int id = v.getId();

        // Check if the number is delete or not
        if (isZero) {
          result.setText("");
          isZero = false;
        }

        // Display on TextView when click button
        result.append(idButtonMap.get(id));
      }
    };
  }
}
