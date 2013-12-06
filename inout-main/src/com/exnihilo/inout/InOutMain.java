package com.exnihilo.inout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.exnihilo.inout.R;

//import com.google.analytics.tracking.android.EasyTracker;

public class InOutMain extends Activity {

	private EditText txtAmount;
	private EditText txtPeople;
	private EditText txtTipOther;
	private RadioGroup rdoGroupTips;
	private Button btnCalculate;
	private Button btnReset;

	private TextView txtTipAmount;
	private TextView txtTotalToPay;
	private TextView txtTipPerPerson;

	//private EasyTracker tracker ;
	
	private int radioCheckedId = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tipster);
		txtAmount = (EditText) findViewById(R.id.txtAmount);
		txtAmount.requestFocus();
		txtPeople = (EditText) findViewById(R.id.txtPeople);
		txtTipOther = (EditText) findViewById(R.id.txtTipOther);
		rdoGroupTips = (RadioGroup) findViewById(R.id.RadioGroupTips);
		btnCalculate = (Button) findViewById(R.id.btnCalculate);
		btnCalculate.setEnabled(false);
		btnReset = (Button) findViewById(R.id.btnReset);
		txtTipAmount = (TextView) findViewById(R.id.txtTipAmount);
		txtTotalToPay = (TextView) findViewById(R.id.txtTotalToPay);
		txtTipPerPerson = (TextView) findViewById(R.id.txtTipPerPerson);
		txtTipOther.setEnabled(false);

		rdoGroupTips.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				switch (arg1) {
				case R.id.radioFifteen:
				case R.id.radioTwenty:
					txtTipOther.setEnabled(false);
					btnCalculate.setEnabled(txtAmount.getText().length() > 0
							&& txtPeople.getText().length() > 0);
					break;
				case R.id.radioOther:
					txtTipOther.setEnabled(true);
					txtTipOther.requestFocus();
					btnCalculate.setEnabled(txtAmount.getText().length() > 0
							&& txtPeople.getText().length() > 0
							&& txtTipOther.getText().length() > 0);
					break;
				}
				radioCheckedId = arg1;
			}
		});
		OnKeyListener mKeyListener = new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				switch (v.getId()) {
				case R.id.txtAmount:
				case R.id.txtPeople:
					btnCalculate.setEnabled(txtAmount.getText().length() > 0
							&& txtPeople.getText().length() > 0);
					break;
				case R.id.radioOther:
					txtTipOther.setEnabled(true);
					txtTipOther.requestFocus();
					btnCalculate.setEnabled(txtAmount.getText().length() > 0
							&& txtPeople.getText().length() > 0
							&& txtTipOther.getText().length() > 0);
					break;
				}

				return false;

			}
		};

		txtAmount.setOnKeyListener(mKeyListener);
		txtPeople.setOnKeyListener(mKeyListener);
		txtTipOther.setOnKeyListener(mKeyListener);

		OnClickListener mClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v.getId() == R.id.btnCalculate) {
					calculate();
				} else {
					reset();
				}
			}
		};
		btnCalculate.setOnClickListener(mClickListener);
		btnReset.setOnClickListener(mClickListener);

	//	tracker = EasyTracker.getInstance() ;
//		tracker.activityStart(this);
	}

	@Override
	public void onStop (){
		super.onStop();
	//	tracker.activityStop(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tipster, menu);
		return true;
	}

	private void reset() {
		txtAmount.setText("");
		txtTotalToPay.setText("");
		txtTipPerPerson.setText("");
		txtTipAmount.setText("");
		txtPeople.setText("");
		txtTipOther.setText("");
		rdoGroupTips.clearCheck();
		rdoGroupTips.check(R.id.radioFifteen);
		txtAmount.requestFocus();

	}

	/**
	 * Calculate the tip as per data entered by the user.
	 */
	private void calculate() {
		
		Double billAmount = Double.parseDouble(txtAmount.getText().toString());
		Double totalPeople = Double.parseDouble(txtPeople.getText().toString());
		Double percentage = null;
		boolean isError = false;
		if (billAmount < 1.0) {
			showErrorAlert("Enter a valid Total Amount.", txtAmount.getId());
			isError = true;
		}
		if (totalPeople < 1.0) {
			showErrorAlert("Enter a valid value for No. of people.",
					txtPeople.getId());
			isError = true;
		}
		/*
		 * If user never changes radio selection, then it means the default
		 * selection of 15% is in effect. But its safer to verify
		 */
		if (radioCheckedId == -1) {
			radioCheckedId = rdoGroupTips.getCheckedRadioButtonId();
		}
		if (radioCheckedId == R.id.radioFifteen) {
			percentage = 15.00;
		} else if (radioCheckedId == R.id.radioTwenty) {
			percentage = 20.00;
		} else if (radioCheckedId == R.id.radioOther) {
			percentage = Double.parseDouble(txtTipOther.getText().toString());
			if (percentage < 1.0) {
				showErrorAlert("Enter a valid Tip percentage",
						txtTipOther.getId());
				isError = true;
			}
		}
		/*
		 * If all fields are populated with valid values, then proceed to
		 * calculate the tips
		 */
		if (!isError) {
			Double tipAmount = ((billAmount * percentage) / 100);
			Double totalToPay = billAmount + tipAmount;
			Double perPersonPays = totalToPay / totalPeople;
			txtTipAmount.setText(tipAmount.toString());
			txtTotalToPay.setText(totalToPay.toString());
			txtTipPerPerson.setText(perPersonPays.toString());
		}
	}

	/**
	 * Shows the error message in an alert dialog
	 * 
	 * @param errorMessage
	 *            String the error message to show
	 * @param fieldId
	 *            the Id of the field which caused the error. This is required
	 *            so that the focus can be set on that field once the dialog is
	 *            dismissed.
	 */
	private void showErrorAlert(String errorMessage, final int fieldId) {
		new AlertDialog.Builder(this)
				.setTitle("Error")
				.setMessage(errorMessage)
				.setNeutralButton("Close",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								findViewById(fieldId).requestFocus();
							}
						}).show();
	}

}
