package vike.flash.cards;

import java.util.ArrayList;

import vike.flash.cards.R;
import vike.Animation.Rotate3dAnimation;
import vike.voice.voice;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;
import android.widget.ViewFlipper;

public class words extends Activity implements  OnGestureListener{
	/** Called when the activity is first created. */
	private ViewFlipper flipper;
	private GestureDetector detector;
	private String[] name=new String[]{};
	private String[] tran=new String[]{};
	private Boolean[] type=new Boolean[]{};
	private String[] WORD_NAME = new String[]{};
	private int present_page=0;
	private String tag="0";
	private voice vc;
	private int start;
	private int end;
	private String filename;

	public int page_next(){
		present_page=present_page+1;
		if (present_page>end-start-1){
			present_page=0;
			return present_page;
		}
		return present_page;
	}
	public int page_previous(){
		if (present_page==0){
			present_page=end-start-1;
			return present_page;
		}
		present_page=present_page-1;
		return present_page;
	}
	public String tag_next(){
		if(tag=="0"){
			tag="1";
			return tag;
		}
		else if(tag=="1"){
			tag="0";
			return tag;
		}
		return tag;
	}
	public String tag_previous(){
		if(tag=="0"){
			tag="1";
			return tag;
		}
		if(tag=="1"){
			tag="0";
			return tag;
		}
		return tag;
	}
	//»´∆¡
	public void setFullscreen() { 
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN); 
	}

	//≤ªœ‘ æ±ÍÃ‚
	public void setNoTitle() { 
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ArrayList<String> list1 = new ArrayList<String>();
		ArrayList<String> list2 = new ArrayList<String>();
		Intent intent= getIntent();
		start =intent.getIntExtra("start", 0);
		filename=intent.getStringExtra("filename");
		end =intent.getIntExtra("end", 0);
		WORD_NAME = intent.getStringArrayExtra("WORD_NAME");
		String[] word;
		for(int i=start;i<end;i++){
			word = WORD_NAME[i].split("=");
			list1.add(word[0]);
			list2.add(word[1]);
		}
		name = new String[list1.size()];
		tran = new String[list2.size()];
		type = new Boolean[list2.size()];
		list1.toArray(name);
		list2.toArray(tran);
		for(int i=0;i<end-start;i++){
			type[i]=false;
		}
		vc=new voice(this);
		vc.InitVoice();
		setFullscreen();
		setNoTitle();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		detector = new GestureDetector(this);
		flipper = new ViewFlipper(this);
		flipper.setBackgroundColor(Color.BLACK);
		for (int i=0; i<2;i++){//end-start
			flipper.addView(addViewGroup(i));
		}
		setContentView(flipper);
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_DPAD_DOWN){//∫·∆¡UP£ª
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_up_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_up_out));
			NextPage();
			this.flipper.showNext();
			return true;
		}else if(keyCode==KeyEvent.KEYCODE_DPAD_UP){//∫·∆¡DOWN£ª
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_down_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_down_out));
			PreviousPage();
			this.flipper.showPrevious();
			return true;
		}else if(keyCode==KeyEvent.KEYCODE_DPAD_RIGHT){//∫·∆¡LEFT£ª
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.left_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.left_out));
			NextPage();
			this.flipper.showNext();
			return true;
		}else if(keyCode==KeyEvent.KEYCODE_DPAD_LEFT){//∫·∆¡RIGHT£ª
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.right_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.right_out));
			PreviousPage();
			this.flipper.showPrevious();
			return true;
		}else if(keyCode==KeyEvent.KEYCODE_DPAD_CENTER){//∫·∆¡ENTER£ª
			applyRotation(-1, 0, -90);
			return true;
		}


		return super.onKeyDown(keyCode, event);
	}


	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, R.string.set).setIcon(R.drawable.ic_menu_preferences);
		menu.add(0, 2, 2, R.string.list).setIcon(R.drawable.ic_menu_list);
		menu.add(0, 3, 3, R.string.test).setIcon(R.drawable.ic_menu_attachment);
		menu.add(0, 4, 4, R.string.about).setIcon(R.drawable.ic_menu_info_details);
		menu.add(1, 5, 5, R.string.Previous).setIcon(R.drawable.ic_menu_left);
		menu.add(1, 6, 6, R.string.Next).setIcon(R.drawable.ic_menu_right);
		return super.onCreateOptionsMenu(menu);
	}

	private int Next_start(){
		if((end==(WORD_NAME.length-2))||(end==(WORD_NAME.length-3))||(end==(WORD_NAME.length-4))){
			return WORD_NAME.length-4;
		}
		return end;
	}
	private int Next_end(){
		if((end+end-start+1)>WORD_NAME.length-1){
			System.out.println(WORD_NAME.length);
			return WORD_NAME.length-1;
		}
		return end+end-start;
	}
	private int Previous_start(){
		if((start+start-end-1)<1){
			return 0;
		}
		return start+start-end;
	}
	private int Previous_end(){
		if((start==1)||(start==2)||(start==3)){
			return 3;
		}
		return start;
	}
	public boolean onOptionsItemSelected(MenuItem item) {

		if(item.getItemId() == 1){
			Intent intent = new Intent();
			intent.setClass(words.this, Preferences.class);
			words.this.startActivity(intent);
		}
		else if(item.getItemId() == 2){

			Intent intent = new Intent();
			intent.putExtra("start", start);
			intent.putExtra("end", end);
			intent.putExtra("WORD_NAME", WORD_NAME);
			intent.setClass(words.this, flash_cards.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			words.this.startActivity(intent);
		}
		else if(item.getItemId() == 3){

			Intent intent = new Intent();
			intent.putExtra("WORD_NAME", WORD_NAME);
			intent.putExtra("filename", filename);
			intent.putExtra("start", start);
			intent.putExtra("end", end);
			intent.setClass(words.this, test_words.class);
			words.this.startActivity(intent);
		}
		else if(item.getItemId() == 4){
			new AlertDialog.Builder(words.this)
			.setIcon(R.drawable.alert_dialog_icon)
			.setTitle(R.string.about)
			.setMessage(R.string.imformation)
			.setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {

				}
			})
			.setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {

				}
			})
			.create().show();
		}
		else if(item.getItemId() == 5){
			if(start<=end-start-1){
				Toast.makeText(getApplicationContext(),R.string.warning_start,Toast.LENGTH_SHORT).show();
			}
			else{
				Intent intent = new Intent();
				intent.putExtra("WORD_NAME", WORD_NAME);
				intent.putExtra("filename", filename);
				intent.putExtra("start",Previous_start());
				intent.putExtra("end", Previous_end());
				intent.setClass(words.this, words.class);
				words.this.startActivity(intent);
				finish();
			}
		}
		else if(item.getItemId() == 6){
			if(end>=(WORD_NAME.length-end+start)){
				Toast.makeText(getApplicationContext(),R.string.warning_end,Toast.LENGTH_SHORT).show();
			}
			else{
				Intent intent = new Intent();
				intent.putExtra("WORD_NAME", WORD_NAME);
				intent.putExtra("filename", filename);
				intent.putExtra("start",Next_start());
				intent.putExtra("end", Next_end());
				intent.setClass(words.this, words.class);
				words.this.startActivity(intent);
				finish();
			}
		}
		return super.onOptionsItemSelected(item);
	}


	private View addViewGroup(int i) {

		ViewAnimator va =  new ViewAnimator(this);
		va.setBackgroundColor(Color.BLACK);
		va.addView(addTextView(name[i],0));
		va.addView(addTextView(tran[i],1));
		switch (i) {
		case 0: 
			va.setTag("0");
			break;
		case 1: 
			va.setTag("1");
			break;
		}

		return va;
	}

	private View addTextView(String text, int i) {
		TextView tv = new TextView(this);
		tv.setText(text);
		tv.setTextColor(Color.BLACK);
		tv.setTextSize(30);
		tv.setGravity(Gravity.CENTER);
		LinearLayout output = new LinearLayout(this);
		output.setGravity(Gravity.CENTER);
		output.setOrientation(LinearLayout.VERTICAL);
		Drawable dabg;
		dabg = this.getResources().getDrawable(R.drawable.bg14);
		output.setBackgroundDrawable(dabg);
		switch (i) {
		case 0:
			tv.setTag("name");
			break;
		case 1:
			tv.setTag("tran");
			break;
		}
		output.addView(tv);
		return output;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return this.detector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	public void NextPage(){
		ViewAnimator va=(ViewAnimator)flipper.findViewWithTag(tag_next());
		switch (va.getDisplayedChild()){
		case 1 : va.showNext();
			break;
		}
		if(type[page_next()]){
			va.showNext();
		}
		TextView tv1 = (TextView) va.findViewWithTag("name");
		tv1.setText(name[present_page]);
		TextView tv2 = (TextView) va.findViewWithTag("tran");
		tv2.setText(tran[present_page]);
	}

	public void PreviousPage(){
		ViewAnimator va=(ViewAnimator)flipper.findViewWithTag(tag_previous());
		switch (va.getDisplayedChild()){
		case 1 : va.showNext();
			break;
		}
		if(type[page_previous()]){
			va.showNext();
		}
		TextView tv1 = (TextView) va.findViewWithTag("name");
		tv1.setText(name[present_page]);
		TextView tv2 = (TextView) va.findViewWithTag("tran");
		tv2.setText(tran[present_page]);
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		ViewAnimator va=(ViewAnimator)flipper.findViewWithTag(tag);
		switch (va.getDisplayedChild()){
		case 1 : type[present_page]=true;
		break;
		case 0 : type[present_page]=false;
		break;
		}
		if (e1.getX() - e2.getX() > 60) {
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.left_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.left_out));
			NextPage();
			this.flipper.showNext();
			return true;
		} else if (e1.getX() - e2.getX() < -60) {
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.right_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.right_out));
			PreviousPage();
			this.flipper.showPrevious();
			return true;
		} else if (e1.getY() - e2.getY() > 100) {
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_up_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_up_out));
			NextPage();
			this.flipper.showNext();
			return true;
		} else if (e1.getY() - e2.getY() < -100) {
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_down_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_down_out));
			PreviousPage();
			this.flipper.showPrevious();
			return true;
		}
		return false;
	}

	private void applyRotation(int position, float start, float end) {
		ViewAnimator va=(ViewAnimator)flipper.getCurrentView();

		final float centerX = va.getWidth() / 2.0f;
		final float centerY = va.getHeight() / 2.0f;

		final Rotate3dAnimation rotation =
			new Rotate3dAnimation(start, end, centerX, centerY, 310.0f, true);
		rotation.setDuration(300);
		rotation.setFillAfter(true);
		rotation.setInterpolator(new AccelerateInterpolator());
		rotation.setAnimationListener(new DisplayNextView(position));

		va.startAnimation(rotation);
	}

	private final class DisplayNextView implements Animation.AnimationListener {
		private final int mPosition;
		ViewAnimator va=(ViewAnimator)flipper.getCurrentView();

		private DisplayNextView(int position) {
			mPosition = position;

		}

		public void onAnimationStart(Animation animation) {
		}

		public void onAnimationEnd(Animation animation) {
			va.post(new SwapViews(mPosition));
		}

		public void onAnimationRepeat(Animation animation) {
		}
	}

	/**
	 * This class is responsible for swapping the views and start the second
	 * half of the animation.
	 */
	private final class SwapViews implements Runnable {
		private final int mPosition;

		public SwapViews(int position) {
			mPosition = position;
		}

		public void run() {
			final float centerX = flipper.getWidth() / 2.0f;
			final float centerY = flipper.getHeight() / 2.0f;
			Rotate3dAnimation rotation;
			ViewAnimator va=(ViewAnimator)flipper.getCurrentView();
			View mPresent=va.getCurrentView();
			View mNext=va.getChildAt(0);
			switch (va.getDisplayedChild()){
			case 1 : mNext=va.getChildAt(0);
			break;
			case 0 : mNext=va.getChildAt(1);
			break;
			}

			flipper.setInAnimation(null);
			flipper.setOutAnimation(null);
			if (mPosition > -1) {
				mPresent.setVisibility(View.GONE);
				mNext.setVisibility(View.VISIBLE);
				mNext.requestFocus();
				rotation = new Rotate3dAnimation(90, 180, centerX, centerY, 310.0f, false);
			} else {
				mPresent.setVisibility(View.GONE);
				mNext.setVisibility(View.VISIBLE);
				mNext.requestFocus();
				rotation = new Rotate3dAnimation(90, 0, centerX, centerY, 310.0f, false);
			}

			rotation.setDuration(300);
			rotation.setFillAfter(true);
			rotation.setInterpolator(new DecelerateInterpolator());

			va.startAnimation(rotation);
			va.showNext();
		}
	}
	public static boolean isWifiConnected(Context context) { 
		return getNetworkState(context, ConnectivityManager.TYPE_WIFI) == State.CONNECTED;
	}
	public static boolean isMobileConnected(Context context) {
		return getNetworkState(context, ConnectivityManager.TYPE_MOBILE) == State.CONNECTED;
	}

	private static State getNetworkState(Context context, int networkType) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo info = cm.getNetworkInfo(networkType);

		return info == null ? null : info.getState();        
	}

	@Override
	public void onLongPress(MotionEvent e) {
		ViewAnimator va = (ViewAnimator)this.flipper.getCurrentView();
		TextView tv =(TextView) va.findViewWithTag("name");
		String word=tv.getText().toString();
		vc.speak(word);
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		applyRotation(-1, 0, -90);
		return true;
	}
	@Override
	protected void onPause() {
		SharedPreferences.Editor mEditor =this.getSharedPreferences("Last", 0).edit();
		mEditor.putInt("present_page",present_page);
		mEditor.putString("filename", filename);
		mEditor.putInt("start", start+1);
		mEditor.putInt("end", end+1);
		mEditor.commit();
		super.onPause();
	}



	@Override
	protected void onStart() {
		Preferences Pfs=new Preferences();
		@SuppressWarnings("static-access")
		SharedPreferences sp = Pfs.getPreferenceManager().getDefaultSharedPreferences(this);
		Boolean last=sp.getBoolean("last", false);
		int mstart=sp.getInt("start", 0);
		if(last&&(mstart==start)){
			SharedPreferences mSettings = this.getSharedPreferences("Last", 0);
			present_page=mSettings.getInt("present_page",0);
			ViewAnimator va=(ViewAnimator)flipper.getCurrentView();
			TextView tv1 = (TextView) va.findViewWithTag("name");
			tv1.setText(name[present_page]);
			TextView tv2 = (TextView) va.findViewWithTag("tran");
			tv2.setText(tran[present_page]);
		}
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}