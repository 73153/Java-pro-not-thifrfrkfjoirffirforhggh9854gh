package com.df.seller;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.df.seller.data.provider.DFSDataprovider;
import com.df.src.R;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.AlertMagnatic;
import com.smart.framework.CustomAlertNeutral;

import java.util.ArrayList;
import java.util.HashMap;

public class DFSMyAccountActivity extends DFSellerMasterActivity {

	private ImageView imgBanner;

	private DFSDataprovider dataprovider;
	private HashMap<String, String> accountDetail = new HashMap<String, String>();

	@Override
	public int setLayoutId() {
		return R.layout.dfs_my_account;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents() {
		imgBanner = (ImageView) findViewById(R.id.imgBanner);
		dataprovider = new DFSDataprovider(this);
	}

	@Override
	public void prepareViews() {
        getAccountDetails();
	}

	@Override
	public void setActionListeners() {
	}

    /**
	 * Class Methods
	 */

	private void getAccountDetails() {
		showProgressDialog(getString(R.string.df_loading_account), this);
		dataprovider.getAccountDetails(new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {

			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				hideProgressDialog();
				if (responseCode == 200) {
					accountDetail = data1.get(0);
                    if(!IjoomerUtilities.isSellerPaymentDetailComplete()){
                        goToAccountPaymentDetail();
                    }else{
                        goToAccountSummary();
                    }
				} else {
					responseMessageHandler(responseCode, false);
				}
			}
		});
	}

	public void updatePaymentDetails(final String newPaypalId) {

		showProgressDialog("Updating your Payment Details...", this);
		dataprovider.saveAccountDetails(newPaypalId, new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {

			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
				hideProgressDialog();
				if (responseCode == 200) {

                    getSmartApplication().writeSharedPreferences(SP_DFS_PAYMENT_COMPLETE,"1");
					IjoomerUtilities.getDFInfoDialog(DFSMyAccountActivity.this, "Payment Detail Updated.", getString(R.string.ok), new CustomAlertNeutral() {

						@Override
						public void NeutralMethod() {

						}
					});
					accountDetail = data1.get(0);
					goToAccountPaymentDetail();
				}
			}
		});

	}

	/**
	 * This method used to shown response message.
	 * 
	 * @param responseCode
	 *            represented response code
	 * @param finishActivityOnConnectionProblem
	 *            represented finish activity on connection problem
	 */
	private void responseMessageHandler(final int responseCode, final boolean finishActivityOnConnectionProblem) {

		IjoomerUtilities.getDFInfoDialog(this, getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok),
				new CustomAlertNeutral() {

					@Override
					public void NeutralMethod() {

					}
				});

	}

	public void goToAccountSummary() {
		imgBanner.setImageResource(R.drawable.dfs_account_summary_banner_img);
		addFragment(R.id.lnrFragment, new DFSAccountSummaryFragment(accountDetail));
	}

	public void goToAccountMoneySummary() {
		imgBanner.setImageResource(R.drawable.dfs_account_money_banner_img);
		addFragment(R.id.lnrFragment, new DFSAccountMoneySummaryFragment(accountDetail));
	}

	public void goToAccountPaymentDetail() {
		imgBanner.setImageResource(R.drawable.dfs_account_payment_banner_img);
		addFragment(R.id.lnrFragment, new DFSAccountPaymentDetailFragment(accountDetail));
	}


    @Override
    public void onBackPressed() {

        getConfirmDialog(getString(R.string.app_name),getString(R.string.df_exit_app),getString(R.string.yes),getString(R.string.no),false,new AlertMagnatic() {
            @Override
            public void PositiveMethod(DialogInterface dialog, int id) {
                Intent intent = new Intent("clearStackActivity");
                intent.setType("text/plain");
                sendBroadcast(intent);
                finish();
            }

            @Override
            public void NegativeMethod(DialogInterface dialog, int id) {

            }
        });

    }
}
