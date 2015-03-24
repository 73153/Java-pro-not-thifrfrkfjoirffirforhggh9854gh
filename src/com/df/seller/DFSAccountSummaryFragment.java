package com.df.seller;

import java.util.HashMap;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.df.src.R;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerTextView;
import com.smart.framework.SmartFragment;

public class DFSAccountSummaryFragment extends SmartFragment {

	private IjoomerTextView txtDishesSoldToday;
	private IjoomerTextView txtDishesSoldWeek;
	private IjoomerTextView txtDishesSoldMonth;
	private IjoomerTextView txtDishesSoldToDate;
	private IjoomerTextView txtMostPopularDish;

	private IjoomerButton imgSummary;
	private IjoomerButton imgMoney;
	private IjoomerButton imgPayment;

	private HashMap<String, String> accountDetail;

	public DFSAccountSummaryFragment(HashMap<String, String> accountDetail) {
		this.accountDetail = accountDetail;
	}

	@Override
	public int setLayoutId() {
		return R.layout.dfs_account_summary;

	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents(View currentView) {
		txtDishesSoldToday = (IjoomerTextView) currentView.findViewById(R.id.txtDishesSoldToday);
		txtDishesSoldWeek = (IjoomerTextView) currentView.findViewById(R.id.txtDishesSoldWeek);
		txtDishesSoldMonth = (IjoomerTextView) currentView.findViewById(R.id.txtDishesSoldMonth);
		txtDishesSoldToDate = (IjoomerTextView) currentView.findViewById(R.id.txtDishesSoldToDate);
		txtMostPopularDish = (IjoomerTextView) currentView.findViewById(R.id.txtMostPopularDish);

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
		imgMoney.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				((DFSMyAccountActivity) getActivity()).goToAccountMoneySummary();
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
		imgSummary.setBackgroundResource(R.drawable.dfs_account_summary_selected_btn);
		txtDishesSoldToday.setText(accountDetail.get("soldDay"));
		txtDishesSoldWeek.setText(accountDetail.get("soldWeek"));
		txtDishesSoldMonth.setText(accountDetail.get("soldMonth"));
		txtDishesSoldToDate.setText(accountDetail.get("totalSold"));
		txtMostPopularDish.setText(accountDetail.get("popularDish"));
	}
}
