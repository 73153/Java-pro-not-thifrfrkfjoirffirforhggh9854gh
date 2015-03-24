package com.df.customer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.df.customer.data.provider.DFCDataprovider;
import com.df.src.R;
import com.ijoomer.common.classes.IjoomerUtilities;
import com.ijoomer.customviews.IjoomerButton;
import com.ijoomer.customviews.IjoomerTextView;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.CustomAlertNeutral;

import java.util.ArrayList;
import java.util.HashMap;

public class DFCAddTipActivity extends DFCustomerMasterActivity {

	private IjoomerTextView txtTipPercent;
	private IjoomerTextView txtCurrentAmount;
	private IjoomerTextView txtNewAmount;
	private IjoomerButton btnPay;
	private HashMap<String, String> IN_DISH_DETAIL;
	private DFCDataprovider dataprovider;

    private ImageView imgTipCircle;

	private int TIPPERCENT = 0;

	private float curentPrice;
	private float newPrice;


    private static Bitmap imageOriginal, imageScaled;
    private static Matrix matrix;
    private int dialerHeight, dialerWidth;
    private GestureDetector detector;
    private boolean[] quadrantTouched;
    private boolean allowRotating;

    private float TOTALROTATION = 0;

	@Override
	public int setLayoutId() {
		return R.layout.dfc_add_tip;
	}

	@Override
	public View setLayoutView() {
		return null;
	}

	@Override
	public void initComponents() {
        imgTipCircle = (ImageView) findViewById(R.id.imgTipCircle);
		txtTipPercent = (IjoomerTextView) findViewById(R.id.txtTipPercent);
		txtCurrentAmount = (IjoomerTextView) findViewById(R.id.txtCurrentAmount);
		txtNewAmount = (IjoomerTextView) findViewById(R.id.txtNewAmount);
		btnPay = (IjoomerButton) findViewById(R.id.btnPay);
		dataprovider = new DFCDataprovider(this);
	}

	@Override
	public void prepareViews() {
		getIntentData();
		setAmmountDetails();
        if (imageOriginal == null) {
            imageOriginal = BitmapFactory.decodeResource(getResources(), R.drawable.df_circle);
        }

        // initialize the matrix only once
        if (matrix == null) {
            matrix = new Matrix();
        } else {
            // not needed, you can also post the matrix immediately to restore the old state
            matrix.reset();
        }

        detector = new GestureDetector(this, new MyGestureDetector());

        // there is no 0th quadrant, to keep it simple the first value gets ignored
        quadrantTouched = new boolean[] { false, false, false, false, false };

        allowRotating = true;

        imgTipCircle.setOnTouchListener(new MyOnTouchListener());
        imgTipCircle.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                // method called more than once, but the values only need to be initialized one time
                if (dialerHeight == 0 || dialerWidth == 0) {
                    dialerHeight = imgTipCircle.getHeight();
                    dialerWidth = imgTipCircle.getWidth();

                    // resize
                    Matrix resize = new Matrix();
                    resize.postScale((float)Math.min(dialerWidth, dialerHeight) / (float)imageOriginal.getWidth(), (float)Math.min(dialerWidth, dialerHeight) / (float)imageOriginal.getHeight());
                    imageScaled = Bitmap.createBitmap(imageOriginal, 0, 0, imageOriginal.getWidth(), imageOriginal.getHeight(), resize, false);

                    // translate to the image view's center
                    float translateX = dialerWidth / 2 - imageScaled.getWidth() / 2;
                    float translateY = dialerHeight / 2 - imageScaled.getHeight() / 2;
                    matrix.postTranslate(translateX, translateY);

                    imgTipCircle.setImageBitmap(imageScaled);
                    imgTipCircle.setImageMatrix(matrix);

                    rotateDialer(30);
                }
            }
        });

	}

	@Override
	public void setActionListeners() {

		btnPay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {


				float tip = Math.abs(curentPrice - newPrice);

                tip = Float.parseFloat(String.format("%.2f",tip));

				String paymentUrl = IN_DISH_DETAIL.get("paymentURL") + "&tip=" + tip + "";
				try {
					loadNew(DFCPaymentGatewayActivity.class, DFCAddTipActivity.this, false, "IN_DISH_DETAIL", IN_DISH_DETAIL, "IN_PAYMENT_URL", paymentUrl);
				} catch (Exception e) {
				}

			}
		});
	}

	/**
	 * Class Methods
	 */



    /**
     * Rotate the dialer.
     *
     * @param degrees The degrees, the dialer should get rotated.
     */
    private void rotateDialer(float degrees) {

      matrix.postRotate(degrees, dialerWidth / 2, dialerHeight / 2);

        imgTipCircle.setImageMatrix(matrix);

        System.out.println("Degree : " + degrees);

        TOTALROTATION = TOTALROTATION+degrees;



        try{
            TIPPERCENT=(int)(TOTALROTATION/3.60);
        }catch (Exception e){
            TIPPERCENT=0;
        }

        txtTipPercent.setText(""+(TIPPERCENT) + "%");
        try {
            newPrice = (Float.valueOf(IN_DISH_DETAIL.get("price")) + ((Float.valueOf(IN_DISH_DETAIL.get("price")) * TIPPERCENT) / 100));
            txtNewAmount.setText(getString(R.string.df_curency_sign) + String.format("%.2f",newPrice));
        } catch (Exception e) {
            txtNewAmount.setText(getString(R.string.df_curency_sign) + (IN_DISH_DETAIL.get("price")));
        }
    }

    /**
     * @return The angle of the unit circle with the image view's center
     */
    private double getAngle(double xTouch, double yTouch) {
        double x = xTouch - (dialerWidth / 2d);
        double y = dialerHeight - yTouch - (dialerHeight / 2d);

        switch (getQuadrant(x, y)) {
            case 1:
                return Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;

            case 2:
            case 3:
                return 180 - (Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);

            case 4:
                return 360 + Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;

            default:
                // ignore, does not happen
                return 0;
        }
    }

    /**
     * @return The selected quadrant.
     */
    private static int getQuadrant(double x, double y) {
        if (x >= 0) {
            return y >= 0 ? 1 : 4;
        } else {
            return y >= 0 ? 2 : 3;
        }
    }

    /**
     * Simple implementation of an {@link android.view.View.OnTouchListener} for registering the dialer's touch events.
     */
    private class MyOnTouchListener implements View.OnTouchListener {

        private double startAngle;

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:

                    // reset the touched quadrants
                    for (int i = 0; i < quadrantTouched.length; i++) {
                        quadrantTouched[i] = false;
                    }

                    allowRotating = false;

                    startAngle = getAngle(event.getX(), event.getY());
                    break;

                case MotionEvent.ACTION_MOVE:
                    double currentAngle = getAngle(event.getX(), event.getY());
                    float rotaioAngle = (float) (startAngle - currentAngle);

                    if (TOTALROTATION+rotaioAngle>360){
                        rotaioAngle = rotaioAngle-(TOTALROTATION+rotaioAngle-360);
                    }else if (TOTALROTATION+rotaioAngle>360){
                        rotaioAngle = rotaioAngle+(TOTALROTATION+rotaioAngle);
                    }
                    if (TOTALROTATION+rotaioAngle>=0.0 && TOTALROTATION+rotaioAngle<=360) {
                        rotateDialer(rotaioAngle);
                    }
                    startAngle = currentAngle;
                    break;

                case MotionEvent.ACTION_UP:
                    allowRotating = true;
                    break;
            }

            // set the touched quadrant to true
            quadrantTouched[getQuadrant(event.getX() - (dialerWidth / 2), dialerHeight - event.getY() - (dialerHeight / 2))] = true;

            detector.onTouchEvent(event);

            return true;
        }
    }

    /**
     * Simple implementation of a {@link android.view.GestureDetector.SimpleOnGestureListener} for detecting a fling event.
     */
    private class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            // get the quadrant of the start and the end of the fling
            int q1 = getQuadrant(e1.getX() - (dialerWidth / 2), dialerHeight - e1.getY() - (dialerHeight / 2));
            int q2 = getQuadrant(e2.getX() - (dialerWidth / 2), dialerHeight - e2.getY() - (dialerHeight / 2));

            // the inversed rotations
            if ((q1 == 2 && q2 == 2 && Math.abs(velocityX) < Math.abs(velocityY))
                    || (q1 == 3 && q2 == 3)
                    || (q1 == 1 && q2 == 3)
                    || (q1 == 4 && q2 == 4 && Math.abs(velocityX) > Math.abs(velocityY))
                    || ((q1 == 2 && q2 == 3) || (q1 == 3 && q2 == 2))
                    || ((q1 == 3 && q2 == 4) || (q1 == 4 && q2 == 3))
                    || (q1 == 2 && q2 == 4 && quadrantTouched[3])
                    || (q1 == 4 && q2 == 2 && quadrantTouched[3])) {

                imgTipCircle.post(new FlingRunnable(-1 * (velocityX + velocityY)));
            } else {
                // the normal rotation
                imgTipCircle.post(new FlingRunnable(velocityX + velocityY));
            }

            return true;
        }
    }

    /**
     * A {@link Runnable} for animating the the dialer's fling.
     */
    private class FlingRunnable implements Runnable {

        private float velocity;

        public FlingRunnable(float velocity) {
            this.velocity = velocity;
        }

        @Override
        public void run() {
//            if (Math.abs(velocity) > 5 && allowRotating) {
//                rotateDialer(velocity / 75);
//                velocity /= 1.0666F;
//
//                // post this instance again
//                imgTipCircle.post(this);
//            }
        }
    }

	private void getIntentData() {
		try {
			IN_DISH_DETAIL = (HashMap<String, String>) getIntent().getSerializableExtra("IN_DISH_DETAIL");
		} catch (Exception e) {
		}
	}

	private void setAmmountDetails() {

        try{
            curentPrice = Float.parseFloat(IN_DISH_DETAIL.get("price"));
            txtCurrentAmount.setText(getString(R.string.df_curency_sign) + (IN_DISH_DETAIL.get("price")));
            newPrice = (Float.valueOf(IN_DISH_DETAIL.get("price")) + ((Float.valueOf(IN_DISH_DETAIL.get("price")) * TIPPERCENT) / 100));
            txtNewAmount.setText(getString(R.string.df_curency_sign) + String.format("%.2f",newPrice));
            txtTipPercent.setText(""+(TIPPERCENT)+ "%");
        }catch (Exception e){

        }
	}

	private void startPayment() {
		showProgressDialog("Payment In progress...", DFCAddTipActivity.this);
		dataprovider.payOrder(IN_DISH_DETAIL.get("restID"),IN_DISH_DETAIL.get("offerID"), "card", IN_DISH_DETAIL.get("dishID").equals("0") ? true : false, new WebCallListener() {

			@Override
			public void onProgressUpdate(int progressCount) {

			}

			@Override
			public void onCallComplete(int responseCode, String errorMessage, final ArrayList<HashMap<String, String>> data1, Object data2) {
				hideProgressDialog();
				if (responseCode == 200) {
					IjoomerUtilities.getDFInfoDialog(DFCAddTipActivity.this, "Order Paymnet Done!", getString(R.string.ok), new CustomAlertNeutral() {

						@Override
						public void NeutralMethod() {
							try {
								loadNew(DFCOrderConfirmationAndDetailActivity.class, DFCAddTipActivity.this, false, "IN_DISH_DETAIL", data1.get(0));
							} catch (Exception e) {
							}
						}
					});
				} else {
					responseMessageHandler(responseCode, false);
				}
			}
		});

	}

	private void responseMessageHandler(final int responseCode, final boolean finishActivityOnConnectionProblem) {

		IjoomerUtilities.getDFInfoDialog(this, getString(getResources().getIdentifier("code" + responseCode, "string", getPackageName())), getString(R.string.ok),
				new CustomAlertNeutral() {

					@Override
					public void NeutralMethod() {

					}
				});

	}

}
