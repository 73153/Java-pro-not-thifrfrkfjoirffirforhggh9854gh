package com.df.seller;

import android.view.View;
import android.view.View.OnClickListener;

import com.df.src.R;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerTextView;
import com.smart.framework.SmartFragment;

import java.util.HashMap;

public class DFSAccountMoneySummaryFragment extends SmartFragment {

	private IjoomerTextView txtIncomeToday;
	private IjoomerTextView txtIncomeWeek;
	private IjoomerTextView txtIncomeMonth;
	private IjoomerTextView txtCommisionThisMonth;
	private IjoomerTextView txtTotalEarning;

	private IjoomerButton imgSummary;
	private IjoomerButton imgMoney;
	private IjoomerButton imgPayment;

	private HashMap<String, String> accountDetail;

	public DFSAccountMoneySummaryFragment(HashMap<String, String> accountDetail) {
		this.accountDetail = accountDetail;
	}

	@Override
	public int setLayoutId() {
		return R.layout.dfs_account_money_summary;

	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents(View currentView) {
		txtIncomeToday = (IjoomerTextView) currentView.findViewById(R.id.txtIncomeToday);
		txtIncomeWeek = (IjoomerTextView) currentView.findViewById(R.id.txtIncomeWeek);
		txtIncomeMonth = (IjoomerTextView) currentView.findViewById(R.id.txtIncomeMonth);
		txtCommisionThisMonth = (IjoomerTextView) currentView.findViewById(R.id.txtCommisionThisMonth);
		txtTotalEarning = (IjoomerTextView) currentView.findViewById(R.id.txtTotalEarning);

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
		imgPayment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				((DFSMyAccountActivity) getActivity()).goToAccountPaymentDetail();
			}
		});
	}

	private void setDetails() {
		imgMoney.setBackgroundResource(R.drawable.dfs_account_money_selected_btn);
		txtIncomeToday.setText(accountDetail.get("incomeDay"));
		txtIncomeWeek.setText(accountDetail.get("incomeWeek"));
		txtIncomeMonth.setText(accountDetail.get("incomeMonth"));
		txtCommisionThisMonth.setText(accountDetail.get("commissionMonth"));
		txtTotalEarning.setText(accountDetail.get("totalIncome"));
	}
}
