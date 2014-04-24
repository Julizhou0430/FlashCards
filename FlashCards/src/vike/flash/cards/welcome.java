package vike.flash.cards;

import vike.flash.cards.R;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;

public class welcome extends Activity{
	public void setFullscreen() { 
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN); 
	}
	public void setNoTitle() { 
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
	} 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setFullscreen();
		View tv =new View(this);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setNoTitle();
		Drawable dabg;
		dabg = this.getResources().getDrawable(R.drawable.background);
		System.out.print("ok");
		tv.setBackgroundDrawable(dabg);
		tv.setAnimation(AnimationUtils.loadAnimation(this,R.anim.my_alpha_action));
		tv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(welcome.this, Course.class);
				welcome.this.startActivity(intent);
				finish();
			}
		});
		setContentView(tv);
	}
//	
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		Intent intent = new Intent();
//		intent.setClass(welcome.this, Course.class);
//		welcome.this.startActivity(intent);
//		finish();
//		return super.onKeyDown(keyCode, event);
//	}
//

	@Override
	protected void onPause() {
		super.onDestroy();
		super.onPause();
	}

}