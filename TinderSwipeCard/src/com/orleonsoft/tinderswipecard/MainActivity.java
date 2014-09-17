package com.orleonsoft.tinderswipecard;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {
	private final int CARDS = 5;
	private final String IMAGE = "image_";
	private RelativeLayout mRelativeParent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mRelativeParent = (RelativeLayout) findViewById(R.id.relative_parent);
		initCardsView();		
	}
	
	public  void initCardsView(){
		for (int i = 0; i < CARDS; i++) {
			CardViewHolder cardView = new CardViewHolder(MainActivity.this,mRelativeParent);
			cardView.setImageContainer(getImagenDrawable(getApplicationContext(), IMAGE+i));
			cardView.setFreeMove(true);
			if (i == 0) {
				cardView.setLastView(true);
			}
			mRelativeParent.addView(cardView.getViewContainer());
		}
	}
	
	public int getImagenDrawable(Context context, String image) {
		return context.getResources().getIdentifier(image, "drawable",context.getPackageName());
	}
}
