package com.nfd.week1.tipcalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CalculatorActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calculator);
		setupTipPercentageEditTextListener();
		setupBillAmountEditTextListener();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calculator, menu);
		return true;
	}
	
	private void setupBillAmountEditTextListener() {
		EditText etBillAmount = (EditText) findViewById(R.id.etBillAmount);
		etBillAmount.addTextChangedListener(new TextWatcher() {			
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3){}
			public void afterTextChanged(Editable arg0) {
				Log.d("TEST - setupBillAmountEditTextListener", "calculating tip for bill amt change");
				if(arg0 == null || arg0.length() == 0) {
					TextView tvBillAmount = (TextView) findViewById(R.id.tvBillAmount);
					tvBillAmount.setText(getString(R.string.currency_symbol) + " " + "0.00");
					TextView tvTotalAmount = (TextView) findViewById(R.id.tvTotalAmount);
					tvTotalAmount.setText(getString(R.string.currency_symbol) + " " + "0.00");
					return;
				}
				EditText etTipPercentage = (EditText) findViewById(R.id.etTipPercentage);
				BigDecimal tipPercentage = BigDecimal.ZERO;
				if(etTipPercentage.getText() != null && etTipPercentage.getText().length() > 0) {
					tipPercentage = new BigDecimal(etTipPercentage.getText().toString()).divide(BigDecimal.valueOf(100));
				}
				calculateTip(tipPercentage);
			}
		});
	}
	
	private void setupTipPercentageEditTextListener() {
		EditText etTipPercentage = (EditText) findViewById(R.id.etTipPercentage);
	    etTipPercentage.addTextChangedListener(new TextWatcher(){
	        public void afterTextChanged(Editable s) {
	        	Log.d("TEST - setupTipPercentageEditTextListener()", s.toString());
	        	String percentage = s.toString();
	        	if(percentage == null || percentage.length() == 0) {
	        		calculateTip(BigDecimal.ZERO);
	        	} else {
	        		BigDecimal tipAmt = new BigDecimal(percentage);
	        		tipAmt = tipAmt.divide(BigDecimal.valueOf(100));
	        		calculateTip(tipAmt);
	        	}
	        	
	        }
	        public void beforeTextChanged(CharSequence s, int start, int count, int after){}
	        public void onTextChanged(CharSequence s, int start, int before, int count){}
	    }); 
	}

	private void calculateTip(BigDecimal tipAmount) {
		EditText billAmountEditText = (EditText) findViewById(R.id.etBillAmount);
		String billAmtInputString = billAmountEditText.getText().toString();
		if(billAmtInputString == null || billAmtInputString.length() == 0) {
			Log.d("TEST", "BILL AMOUNT IS NULL OR EMPTY");
			Toast.makeText(this, "Please enter the bill amount", Toast.LENGTH_SHORT).show();
			return;
		}

		BigDecimal billAmount = new BigDecimal(billAmtInputString).setScale(2, RoundingMode.UP);
		BigDecimal calculatedTip = billAmount.multiply(tipAmount).setScale(2, RoundingMode.UP);
		BigDecimal totalAmount = billAmount.add(calculatedTip).setScale(2, RoundingMode.UP);
		TextView tvBillAmount = (TextView) findViewById(R.id.tvBillAmount);
		TextView tvTipAmount = (TextView) findViewById(R.id.tvCalculatedTipAmount);
		tvTipAmount.setText(getString(R.string.currency_symbol) + " " + calculatedTip.toString());
		tvBillAmount.setText(getString(R.string.currency_symbol) + " " + billAmtInputString);
		TextView tvTotalAmount = (TextView) findViewById(R.id.tvTotalAmount);
		tvTotalAmount.setText(getString(R.string.currency_symbol) + " " + totalAmount.toString());
	}
	
	public void calculateTipPercent10(View view) {
		Log.d("TEST", "calculateTip Start");
		EditText etTipPercentage = (EditText) findViewById(R.id.etTipPercentage);
		etTipPercentage.setText("10.0");
	}
	
	public void calculateTipPercent15(View view) {
		Log.d("TEST", "calculateTip Start");
		EditText etTipPercentage = (EditText) findViewById(R.id.etTipPercentage);
		etTipPercentage.setText("15.0");
	}
	
	public void calculateTipPercent20(View view) {
		Log.d("TEST", "calculateTip Start");
		EditText etTipPercentage = (EditText) findViewById(R.id.etTipPercentage);
		etTipPercentage.setText("20.0");
	}
}
