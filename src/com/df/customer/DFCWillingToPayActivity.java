package com.df.customer;

import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.view.View;
import android.view.View.OnClickListener;

import com.df.customer.data.provider.DFCDataprovider;
import com.df.src.R;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerCheckBox;
import com.ijoomer.customviews.IjoomerEditText;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;

import java.util.ArrayList;
import java.util.HashMap;

public class DFCWillingToPayActivity extends DFCustomerMasterActivity {

	private HashMap<String, String> IN_DISH_DETAIL;
	private IjoomerEditText edtAmount;
	private IjoomerButton imgPay;
	private DFCDataprovider dataprovider;
	private IjoomerCheckBox chkDeliveryRequired;
    private String deliveryAddress="";
    private Dialog dialog;


    @Override
	public int setLayoutId() {
		return R.layout.dfc_willing_to_pay;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents() {
		imgPay = (IjoomerButton) findViewById(R.id.imgPay);
		chkDeliveryRequired = (IjoomerCheckBox) findViewById(R.id.chkDeliveryRequired);
		edtAmount = (IjoomerEditText) findViewById(R.id.edtAmount);
		dataprovider = new DFCDataprovider(this);
	}

	@Override
	public void prepareViews() {
		getIntentData();
	}

	@Override
	public void setActionListeners() {

        chkDeliveryRequired.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialog!=null && dialog.isShowing()){
                    dialog.dismiss();
                }else{
                    showDeliveryOptionDialog();
                }
            }
        });

		imgPay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (edtAmount.getText().toString().trim().length() > 0) {

                    try{
                        if(Float.parseFloat(edtAmount.getText().toString().trim())<=0){
                            edtAmount.setError(getString(R.string.validation_invalid_amount));
                        }else{
                            showProgressDialog(getString(R.string.df_sending_offer), DFCWillingToPayActivity.this);
                            dataprovider.sendOrderToRestaurant(chkDeliveryRequired.isChecked() ? "Delivery" : "Collection", IN_DISH_DETAIL.get("restID"), IN_DISH_DETAIL.get("dishID"),
                                    IN_DISH_DETAIL.get("catID"), edtAmount.getText().toString().trim(),deliveryAddress ,new WebCallListener() {

                                        @Override
                                        public void onProgressUpdate(int progressCount) {

                                        }

                                        @Override
                                        public void onCallComplete(int responseCode, String errorMessage, ArrayList<HashMap<String, String>> data1, Object data2) {
                                            hideProgressDialog();
                                            if (responseCode == 200) {
                                                IjoomerUtilities.getDFInfoDialog(DFCWillingToPayActivity.this, getString(R.string.df_offer_sent_successfully), getString(R.string.ok),
                                                        new CustomAlertNeutral() {

                                                            @Override
                                                            public void NeutralMethod() {
                                                                Intent intent = new Intent("clearStackActivity");
                                                                intent.setType("text/plain");
                                                                sendBroadcast(intent);
                                                                loadNew(DFCPlaceOrderActivity.class, DFCWillingToPayActivity.this, true);
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

				} else {
					edtAmount.setError(getString(R.string.validation_value_required));
				}
			}
		});
	}

	/**
	 * Class Methods
	 */

	private void getIntentData() {
		try {
			IN_DISH_DETAIL = (HashMap<String, String>) getIntent().getSerializableExtra("IN_DISH_DETAIL");
		} catch (Exception e) {
		}
	}

	private void responseMessageHandler(final int responseCode, final boolean finishActivityOnConnectionProblem) {

		IjoomerUtilities.getDFInfoDialog(this, getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok),
				new CustomAlertNeutral() {

					@Override
					public void NeutralMethod() {

					}
				});

	}
    public void showDeliveryOptionDialog() {

        dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.dfc_select_delivery_option);

        final IjoomerCheckBox chkCurrentLocation = (IjoomerCheckBox) dialog.findViewById(R.id.chkCurrentLocation);
        final IjoomerCheckBox chkHomeAddress = (IjoomerCheckBox) dialog.findViewById(R.id.chkHomeAddress);
        final IjoomerEditText edtAddress = (IjoomerEditText) dialog.findViewById(R.id.edtAddress);
        final IjoomerButton btnOk = (IjoomerButton) dialog.findViewById(R.id.btnOk);
        final IjoomerButton btnCancel = (IjoomerButton) dialog.findViewById(R.id.btnCancel);

        chkHomeAddress.setChecked(true);
        edtAddress.setText(getSmartApplication().readSharedPreferences().getString(SP_DF_ADDRESS,""));
        chkCurrentLocation.setChecked(false);

        chkCurrentLocation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chkCurrentLocation.isChecked()) {
                    chkCurrentLocation.setChecked(true);
                    chkHomeAddress.setChecked(false);
                    try{
                        Address address = IjoomerUtilities.getAddressFromLatLong(0,0);
                        edtAddress.setText(address.getAddressLine(0) + "," + address.getAddressLine(1) + "," + address.getAddressLine(2));
                    }catch (Exception e){
                        edtAddress.setText("");
                    }
                }else{
                    edtAddress.setText(getSmartApplication().readSharedPreferences().getString(SP_DF_ADDRESS,""));
                    chkCurrentLocation.setChecked(false);
                    chkHomeAddress.setChecked(true);
                }
            }
        });

        chkHomeAddress.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chkHomeAddress.isChecked()) {
                    chkCurrentLocation.setChecked(false);
                    chkHomeAddress.setChecked(true);
                    edtAddress.setText(getSmartApplication().readSharedPreferences().getString(SP_DF_ADDRESS,""));
                }else{
                    chkHomeAddress.setChecked(false);
                    chkCurrentLocation.setChecked(true);
                    try{
                        Address address = IjoomerUtilities.getAddressFromLatLong(0,0);
                        edtAddress.setText(address.getAddressLine(0) + "," + address.getAddressLine(1) + "," + address.getAddressLine(2));
                    }catch (Exception e){
                        edtAddress.setText("");
                    }
                }
            }
        });

        btnOk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if(edtAddress.getText().toString().trim().length()>0){
                    deliveryAddress = edtAddress.getText().toString().trim();
                    chkDeliveryRequired.setChecked(true);
                    dialog.dismiss();
                }else{
                    edtAddress.setError(getString(R.string.validation_value_required));
                }
            }
        });

        btnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                chkDeliveryRequired.setChecked(false);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
