package com.df.seller;

import android.view.View;
import android.view.View.OnClickListener;

import com.df.src.R;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerTextView;
import com.smart.framework.SmartFragment;

import java.util.HashMap;

public class DFSAccountPaymentDetailFragment extends SmartFragment {

	private IjoomerTextView txtPaypalId;
	private IjoomerEditText edtNewPaypalId;
	private IjoomerButton imgSave;

	private IjoomerButton imgSummary;
	private IjoomerButton imgMoney;
	private IjoomerButton imgPayment;

	private HashMap<String, String> accountDetail;

	public DFSAccountPaymentDetailFragment(HashMap<String, String> accountDetail) {
		this.accountDetail = accountDetail;
	}

	@Override
	public int setLayoutId() {
		return R.layout.dfs_account_payment_detail;

	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents(View currentView) {
		txtPaypalId = (IjoomerTextView) currentView.findViewById(R.id.txtPaypalId);
		edtNewPaypalId = (IjoomerEditText) currentView.findViewById(R.id.edtNewPaypalId);
		imgSave = (IjoomerButton) currentView.findViewById(R.id.imgSave);

		imgSummary = (IjoomerButton) currentView.findViewById(R.id.imgSummary);
		imgMoney = (IjoomerButton) currentView.findViewById(R.id.imgMoney);
		imgPayment = (IjoomerButton) currentView.findViewById(R.id.imgPayment);

	}

	@Override
	public void prepareViews(View currentView) {
		setDetails();
	}

	@Override
	public void setActionListeners(View currentView) {
		imgSummary.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				((DFSMyAccountActivity) getActivity()).goToAccountSummary();
			}
		});
		imgMoney.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				((DFSMyAccountActivity) getActivity()).goToAccountMoneySummary();
			}
		});
		imgSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				boolean validationFlag = true;

				if (edtNewPaypalId.getText().toString().trim().length() <= 0) {
					validationFlag = false;
					edtNewPaypalId.setError(getString(R.string.validation_value_required));
				} else {
					if (!IjoomerUtilities.emailValidator(edtNewPaypalId.getText().toString().trim())) {
						validationFlag = false;
						edtNewPaypalId.setError(getString(R.string.validation_invalid_email));
					}
				}

				if (validationFlag) {
					((DFSMyAccountActivity) getActivity()).updatePaymentDetails(edtNewPaypalId.getText().toString().trim());
				}

			}
		});
	}

	private void setDetails() {
		imgPayment.setBackgroundResource(R.drawable.dfs_account_payment_selected_btn);
		txtPaypalId.setText(accountDetail.get("paypalID"));

	}
}
