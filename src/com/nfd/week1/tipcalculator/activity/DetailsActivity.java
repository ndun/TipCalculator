package com.nfd.week1.tipcalculator.activity;

import java.math.BigDecimal;
import java.math.RoundingMode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;

import com.nfd.week1.tipcalculator.R;

public class DetailsActivity  extends Activity {
	private BigDecimal totalBillAmount = BigDecimal.ZERO;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		setupBillAmountEditTextListener();
		setupTipPercentageEditTextListener();
		setupNumberPickerListener();
		Intent intent = getIntent();
		String tipPercentage = intent.getStringExtra("tipPercentage");
		if(tipPercentage == null) {
			tipPercentage = "0.00";
		}
		EditText etTipPercent = (EditText) findViewById(R.id.etTipPercentage);
		etTipPercent.setText(tipPercentage);
		String billAmt = intent.getStringExtra("billAmount");
		if(billAmt == null) {
			billAmt = "0.00";
		}
		EditText etBillAmt = (EditText) findViewById(R.id.etBillAmount);
		etBillAmt.setText(billAmt);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.calculator, menu);
		return true;
	}
	
	public void goToMain(View v) {
		setResult(RESULT_OK); // set result code and bundle data for response
		finish();
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

		TextView tvBillAmt = (TextView) findViewById(R.id.tvBillAmount);
		TextView tvTipAmount = (TextView) findViewById(R.id.tvCalculatedTipAmount);
		TextView tvTotalAmount = (TextView) findViewById(R.id.tvTotalAmount);

		tvBillAmt.setText(getString(R.string.currency_symbol) + " " + billAmtInputString);
		tvTipAmount.setText(getString(R.string.currency_symbol) + " " + calculatedTip.toString());
		tvTotalAmount.setText(getString(R.string.currency_symbol) + " " + totalAmount.toString());
		totalBillAmount = totalAmount;
		calculateSplit();
	}
	
	private void setupBillAmountEditTextListener() {
		EditText etBillAmount = (EditText) findViewById(R.id.etBillAmount);
		etBillAmount.addTextChangedListener(new TextWatcher() {			
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3){}
			public void afterTextChanged(Editable arg0) {
				Log.d("TEST - setupBillAmountEditTextListener", "calculating tip for bill amt change");
				TextView tvBillAmt = (TextView) findViewById(R.id.tvBillAmount);

				if(arg0 == null || arg0.length() == 0) {
					TextView tvTotalAmount = (TextView) findViewById(R.id.tvTotalAmount);
					tvTotalAmount.setText(getString(R.string.currency_symbol) + " " + "0.00");
					tvBillAmt.setText("0.00");
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

	private void setupNumberPickerListener() {
		NumberPicker np = (NumberPicker) findViewById(R.id.nbrPickerPerson);
	    np.setMinValue(1);
	    np.setMaxValue(20);
	    np.setWrapSelectorWheel(true);
	    np.setValue(1);		

		np.setOnValueChangedListener(new OnValueChangeListener() {
		    @Override
		    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
		        calculateSplit();
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
	
	private void calculateSplit() {
		NumberPicker nbrPicker = (NumberPicker) findViewById(R.id.nbrPickerPerson);
		int numPerson = nbrPicker.getValue();
		BigDecimal amtPerPerson = totalBillAmount.divide(BigDecimal.valueOf(numPerson), 2, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_EVEN);
		TextView tvPersonAmt = (TextView) findViewById(R.id.tvAmountPerPerson);
		tvPersonAmt.setText(getString(R.string.currency_symbol) + " " + amtPerPerson.toString());
	}

}

