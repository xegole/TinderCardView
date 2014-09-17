package com.orleonsoft.tinderswipecard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class CardViewHolder implements OnTouchListener{
	private View mViewContainer;
	private LayoutInflater mInflate;
	private RelativeLayout mRelativeContenido;
	private RelativeLayout mRelativeParent;
	
	private boolean isFreeMove = false;
	private boolean isLeft = false;
	private boolean isRight = false;
	private boolean isTouchable = true;
	private boolean isLastView = false;
	private float mLastTouchX;
	private float mLastTouchY;
	private float rotation;
	private float alpha;
	
	private int sizeScreenWidth;
	private int sizeScreenHeight;
	
	private Activity mActivity;
	
	
	public CardViewHolder(Activity activity, RelativeLayout mRelativeParent){
		this.mRelativeParent = mRelativeParent;
		mActivity = activity;
		mInflate = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		sizeScreenWidth = activity.getWindowManager().getDefaultDisplay().getWidth() / 2;
		sizeScreenHeight = activity.getWindowManager().getDefaultDisplay().getHeight() / 2;
		if (mInflate != null) {
			initView();
		}
	}
	
	private void initView(){
		mViewContainer = mInflate.inflate(	R.layout.view_tinder_card, null);
		mRelativeContenido = (RelativeLayout) mViewContainer.findViewById(R.id.relative_contenido);
		mRelativeContenido.setOnTouchListener(this);
	}
	
	public View getViewContainer(){
		return mRelativeContenido;
	}
	
	public void setImageContainer(int resourceImage){
		if (resourceImage != 0) {
			mRelativeContenido.setBackgroundResource(resourceImage);
		}
	}
	
	public void setLastView(boolean status){
		isLastView = status;
	}
	
	//default is true movement
	public void setFreeMove(boolean action){
		isFreeMove = action;
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(final View view, MotionEvent event) {
		final int X = (int) event.getRawX();
		final int Y = (int) event.getRawY();
		final float dx;
		final float dy;
		
		ImageView mImgLike = (ImageView) view.findViewById(R.id.img_like);
		ImageView mImgDislike = (ImageView) view.findViewById(R.id.img_dislike);
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastTouchX = X;
			mLastTouchY = Y;
			break;
		case MotionEvent.ACTION_MOVE:
			dx = X - mLastTouchX;
			dy = Y - mLastTouchY;
			alpha = view.getTranslationX() + dx;

			float temp = ((alpha * 100) / sizeScreenWidth) / 100;
			if (temp > 0) {
				mImgLike.setAlpha(temp);
				if (temp > 1) {
					isRight = true;
					isLeft = false;
				} else {
					isRight = false;
					isLeft = false;
				}
			} else if (temp < 0) {
				float conver = temp * -1.f;
				mImgDislike.setAlpha(conver);
				if (conver > 1) {
					isLeft = true;
					isRight = false;

				} else {
					isRight = false;
					isLeft = false;
				}
			}
			rotation = -5 * view.getTranslationX() / (view.getWidth() / 2.f);
			view.setTranslationX(alpha);
			if (isFreeMove) {
				view.setTranslationY(view.getTranslationY() + dy);
			}
			view.setRotation(rotation);
			mLastTouchX = X;
			mLastTouchY = Y;
			break;
		case MotionEvent.ACTION_UP:
			if (isLeft && !isRight) {
				view.animate().setDuration(200).alpha(.75f)
						.setInterpolator(new LinearInterpolator()).x(-900).y(0)
						.rotation(rotation)
						.setListener(new AnimatorListenerAdapter() {
							@Override
							public void onAnimationEnd(Animator animation) {
								mRelativeParent.removeView(view);
								if (isLastView) {
									new Handler().postDelayed(new Runnable() {
										
										@Override
										public void run() {
											((MainActivity)mActivity).initCardsView();
										}
									}, 1500);
								}
							}

							@Override
							public void onAnimationCancel(Animator animation) {
								onAnimationEnd(animation);
							}
						});
			} else if (!isLeft && isRight) {
				view.animate().setDuration(200).alpha(.75f)
						.setInterpolator(new LinearInterpolator()).x(900).y(0)
						.rotation(rotation)
						.setListener(new AnimatorListenerAdapter() {
							@Override
							public void onAnimationEnd(Animator animation) {
								mRelativeParent.removeView(view);
								if (isLastView) {
									new Handler().postDelayed(new Runnable() {
										
										@Override
										public void run() {
											((MainActivity)mActivity).initCardsView();
										}
									}, 1500);
								}
							}

							@Override
							public void onAnimationCancel(Animator animation) {
								onAnimationEnd(animation);
							}
						});
			} else if (!isLeft && !isRight) {
				ValueAnimator animator = ObjectAnimator.ofPropertyValuesHolder(
						view,
						PropertyValuesHolder.ofFloat("translationX", 0),
						PropertyValuesHolder.ofFloat("translationY", 0),
						PropertyValuesHolder.ofFloat("rotation", 0),
						PropertyValuesHolder.ofFloat("pivotX", sizeScreenWidth
								- (view.getWidth() / 2)),
						PropertyValuesHolder.ofFloat("pivotY", sizeScreenHeight
								- (view.getHeight() / 2))).setDuration(200);
				animator.setInterpolator(new AccelerateInterpolator());
				animator.start();
				mImgLike.setAlpha(0.f);
				mImgDislike.setAlpha(0.f);
			}
			break;
		}
		return isTouchable;
	}

}
