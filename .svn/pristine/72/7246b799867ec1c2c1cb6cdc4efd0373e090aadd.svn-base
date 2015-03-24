package com.df.seller;

import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;

import com.df.seller.data.provider.DFSDataprovider;
import com.df.src.R;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;

import java.util.ArrayList;
import java.util.HashMap;

public class DFSConfirmOrderActivity extends DFSellerMasterActivity {

	private IjoomerEditText edtAmount;
	private IjoomerEditText edtMessage;
	private IjoomerButton imgConfirm;
	private DFSDataprovider dataprovider;
	private IjoomerTextView txtDeliveryRequired;
    private Dialog dialog;
	private HashMap<String, String> IN_ORDER_DETAIL;

	@Override
	public int setLayoutId() {
		return R.layout.dfs_confirm_order;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents() {
		edtAmount = (IjoomerEditText) findViewById(R.id.edtAmount);
		edtMessage = (IjoomerEditText) findViewById(R.id.edtMessage);
		imgConfirm = (IjoomerButton) findViewById(R.id.imgConfirm);
		txtDeliveryRequired = (IjoomerTextView) findViewById(R.id.txtDeliveryRequired);
		dataprovider = new DFSDataprovider(this);
		getIntentData();
	}

	@Override
	public void prepareViews() {
		if (IN_ORDER_DETAIL.get("service").equalsIgnoreCase("delivery")) {
			txtDeliveryRequired.setVisibility(View.VISIBLE);
		} else {
            txtDeliveryRequired.setVisibility(View.GONE);
		}
	}

	@Override
	public void setActionListeners() {
		txtDeliveryRequired.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeliveryOptionDialog(IN_ORDER_DETAIL.get("deliveryAddress"));
            }
        });

		imgConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (edtAmount.getText().toString().trim().length() < 0) {
					edtAmount.setError(getString(R.string.validation_value_required));
				} else {

                    try{
                        if(Float.parseFloat(edtAmount.getText().toString().trim())<=0){
                            edtAmount.setError(getString(R.string.validation_invalid_amount));
                        }else{
                            showProgressDialog("Confirming Request..", DFSConfirmOrderActivity.this);
                            dataprovider.acceptRequest(IN_ORDER_DETAIL.get("service"), IN_ORDER_DETAIL.get("offerID"), edtAmount.getText().toString().trim(), edtMessage.getText()
                                    .toString().trim(), new WebCallListener() {

                                @Override
                                public void onProgressUpdate(int progressCount) {

                                }

                                @Override
                                public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
                                    hideProgressDialog();
                                    if (responseCode == 200) {
                                        IjoomerUtilities.getDFInfoDialog(DFSConfirmOrderActivity.this, "Request Confirmed Successfully.", getString(R.string.ok), new CustomAlertNeutral() {

                                            @Override
                                            public void NeutralMethod() {
                                                finish();
                                            }
                                        });
                                    } else {
                                        responseMessageHandler(responseCode, false);
                                    }
                                }
                            });
                        }
                    }catch (Exception e){
                        edtAmount.setError(getString(R.string.validation_invalid_amount));
                    }

				}
			}
		});
	}

	/**
	 * Class Methods
	 */
	private void getIntentData() {
		IN_ORDER_DETAIL = (HashMap<String, String>) getIntent().getSerializableExtra("IN_ORDER_DETAIL");
	}

	private void responseMessageHandler(final int responseCode, final boolean finishActivityOnConnectionProblem) {

		IjoomerUtilities.getDFInfoDialog(this, getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok),
				new CustomAlertNeutral() {

					@Override
					public void NeutralMethod() {

					}
				});

	}

    public void showDeliveryOptionDialog(final String addresss) {

        dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.dfs_delivery_info);

        final IjoomerButton btnOk = (IjoomerButton) dialog.findViewById(R.id.btnOk);
        final IjoomerTextView txtAddress = (IjoomerTextView) dialog.findViewById(R.id.txtAddress);
        txtAddress.setText(addresss);
        btnOk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


}
