package com.nfd.week1.tipcalculator.activity;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.nfd.week1.tipcalculator.R;
import com.nfd.week1.tipcalculator.R.id;
import com.nfd.week1.tipcalculator.R.layout;
import com.nfd.week1.tipcalculator.R.menu;
import com.nfd.week1.tipcalculator.R.string;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class CalculatorActivity extends Activity {
	private static final int REQUEST_CODE = 20;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calculator);
		setupBillAmountEditTextListener();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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
					TextView tvTotalAmount = (TextView) findViewById(R.id.tvTotalAmount);
					tvTotalAmount.setText(getString(R.string.currency_symbol) + " " + "0.00");
					return;
				}
				TextView tvTipPercentage = (TextView) findViewById(R.id.tvTipPercentage);
				BigDecimal tipPercentage = BigDecimal.ZERO;
				if(tvTipPercentage.getText() != null && tvTipPercentage.getText().length() > 0) {
					tipPercentage = new BigDecimal(tvTipPercentage.getText().toString()).divide(BigDecimal.valueOf(100));
				}
				calculateTip(tipPercentage);
			}
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

		TextView tvTipAmount = (TextView) findViewById(R.id.tvCalculatedTipAmount);
		TextView tvTotalAmount = (TextView) findViewById(R.id.tvTotalAmount);

		tvTipAmount.setText(getString(R.string.currency_symbol) + " " + calculatedTip.toString());
		tvTotalAmount.setText(getString(R.string.currency_symbol) + " " + totalAmount.toString());
	}
	
	public void calculateTipPercent10(View view) {
		TextView tvTipPercentage = (TextView) findViewById(R.id.tvTipPercentage);
		tvTipPercentage.setText("10.0");
		calculateTip(BigDecimal.valueOf(0.10));
	}
	
	public void calculateTipPercent15(View view) {
		TextView tvTipPercentage = (TextView) findViewById(R.id.tvTipPercentage);
		tvTipPercentage.setText("15.0");
		calculateTip(BigDecimal.valueOf(0.15));

	}
	
	public void calculateTipPercent20(View view) {
		TextView tvTipPercentage = (TextView) findViewById(R.id.tvTipPercentage);
		tvTipPercentage.setText("20.0");
		calculateTip(BigDecimal.valueOf(0.20));

	}
	
	public void getDetails(View view) {
        // sending data to new activity
		TextView percentageAmt = (TextView) findViewById(R.id.tvTipPercentage);
        Intent i = new Intent(getApplicationContext(), DetailsActivity.class);
        i.putExtra("tipPercentage", percentageAmt.getText().toString());
        EditText billAmount = (EditText)findViewById(R.id.etBillAmount);
        i.putExtra("billAmount", billAmount.getText().toString());
        startActivityForResult(i, REQUEST_CODE);
	}
	
	/*	
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
*/	

}
