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
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class CalculatorActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calculator);
		setupTipPercentageEditTextListener();
		setupBillAmountEditTextListener();
		setupSeekBarListener();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.calculator, menu);
		return true;
	}
	
	private void setupSeekBarListener() {
		SeekBar sbTipScale = (SeekBar) findViewById(R.id.sbTipScale);
		sbTipScale.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				Log.d("TEST - setupSeekBarListener()", "max: " + seekBar.getMax() + " - progress: " + progress);
				EditText etTipPercentage = (EditText) findViewById(R.id.etTipPercentage);
				BigDecimal tipPercentage = BigDecimal.valueOf(progress);
				etTipPercentage.setText(tipPercentage.toString());
				calculateTip(tipPercentage.divide(BigDecimal.valueOf(100)));
				
			}
		});
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
	        	
	        	if(percentage == null || percentage.length() == 0 || percentage.equals(".")) {
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
			billAmtInputString = "0.00";
		}
	
		BigDecimal billAmount = new BigDecimal(billAmtInputString).setScale(2, RoundingMode.HALF_EVEN);
		BigDecimal calculatedTip = billAmount.multiply(tipAmount).setScale(2, RoundingMode.HALF_EVEN);
		BigDecimal totalAmount = billAmount.add(calculatedTip).setScale(2, RoundingMode.HALF_EVEN);

		TextView tvBillAmount = (TextView) findViewById(R.id.tvBillAmount);
		TextView tvTipAmount = (TextView) findViewById(R.id.tvCalculatedTipAmount);
		TextView tvTotalAmount = (TextView) findViewById(R.id.tvTotalAmount);

		tvTipAmount.setText(getString(R.string.currency_symbol) + " " + calculatedTip.toString());
		tvBillAmount.setText(getString(R.string.currency_symbol) + " " + billAmtInputString);
		tvTotalAmount.setText(getString(R.string.currency_symbol) + " " + totalAmount.toString());
	}
	
	public void calculateTipPercent10(View view) {
		EditText etTipPercentage = (EditText) findViewById(R.id.etTipPercentage);
		etTipPercentage.setText("10.0");
	}
	
	public void calculateTipPercent15(View view) {
		EditText etTipPercentage = (EditText) findViewById(R.id.etTipPercentage);
		etTipPercentage.setText("15.0");
	}
	
	public void calculateTipPercent20(View view) {
		EditText etTipPercentage = (EditText) findViewById(R.id.etTipPercentage);
		etTipPercentage.setText("20.0");
	}
}
