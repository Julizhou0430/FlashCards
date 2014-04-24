package vike.flash.cards;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import vike.flash.cards.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.GestureDetector.OnGestureListener;
import android.view.animation.AnimationUtils;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;


public class test_words extends Activity implements  OnGestureListener{
	private String[] WORD_NAME;
	private int start;
	private int end;
	private int answerNow;
	private int[] answer;
	private int[] key;
	private MutipleChoice MC;
	private GestureDetector detector;
	private ViewFlipper flipper;
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {   
			new AlertDialog.Builder(test_words.this)
			.setIcon(R.drawable.alert_dialog_icon)
			.setTitle(R.string.Quit_test)
			.setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					finish();
				}
			}).create().show();
		        return true;   
		    }else if(keyCode==KeyEvent.KEYCODE_DPAD_RIGHT){//横屏LEFT；
				handleAnswer();
				this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
						R.anim.left_in));
				this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
						R.anim.left_out));
				this.flipper.showNext();
				return true;
			}else if(keyCode==KeyEvent.KEYCODE_DPAD_LEFT){//横屏RIGHT；
				handleAnswer();
				this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
						R.anim.right_in));
				this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
						R.anim.right_out));
				this.flipper.showPrevious();
				return true;
			}
		        return super.onKeyDown(keyCode,event);   
		
	}
	


	//全屏
	public void setFullscreen() { 
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN); 
	}

	//不显示标题
	public void setNoTitle() { 
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setFullscreen();
		setNoTitle();
		detector = new GestureDetector(this);
		flipper = new ViewFlipper(this);
		Intent intent= getIntent();
		start =intent.getIntExtra("start", 0);
		end =intent.getIntExtra("end", 0);
		key=new int[end-start];
		answer=new int[end-start];
		WORD_NAME = intent.getStringArrayExtra("WORD_NAME");
		for (int i=start;i<end;i++){
			flipper.addView(addViewGroup(i));
		}
		setContentView(flipper);
	}

	private View addViewGroup(int WordNum) {
		LinearLayout output= new LinearLayout(this);
		output.setGravity(0x11);
		output.setBackgroundColor(Color.WHITE);
		MC=new MutipleChoice(WORD_NAME,WordNum,5);
		output.setOrientation(LinearLayout.VERTICAL);
		TextView tv=new TextView(this);
		tv.setText(MC.getQuestion());
		tv.setTextSize(40);
		tv.setTextColor(Color.BLACK);
		output.addView(tv);
		RadioGroup rg= new RadioGroup(this);
		rg.setOrientation(LinearLayout.VERTICAL);
		key[WordNum-start]=MC.getAnsewerKey()+1;
		for (int i=0;i<5;i++){
			RadioButton rb = new RadioButton(this);
			rb.setId((WordNum-start)*10+i+1);
			rb.setText(MC.getChoices()[MC.getOrder()[i]]);
			rb.setTextColor(Color.BLACK);
			rg.addView(rb);
		}
		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				answerNow=checkedId;
			}
		});
		output.addView(rg);
		Drawable dabg;
		dabg = this.getResources().getDrawable(R.drawable.bg1);
		output.setBackgroundDrawable(dabg);
		return output;
	}

	public class MutipleChoice{
		private int key;
		private int[] order;
		private String[] Choices=new String[]{};
		private String[] WORD_NAME = new String[]{};
		private String Question = new String();

		public MutipleChoice(String[] words,int key_num, int choiceNumber){
			WORD_NAME = words;
			int total =WORD_NAME.length;
			List <Integer> list = getRandomNumber(choiceNumber-1,total);
			List <String> list1 = new ArrayList<String>();
			for (int d : list) {
				String[] word = WORD_NAME[d].split("=");
				list1.add(word[1]);
			}
			String[] word = WORD_NAME[key_num].split("=");
			Question=word[0];
			list1.add(word[1]);
			Choices = new String[list1.size()];
			list1.toArray(Choices);
			List <Integer> nums = getRandomNumber(choiceNumber,choiceNumber);
			order=toIntArray(nums);
			key= findOrderInArray(order,4);
		}

		private int findOrderInArray(int[] array,int num){
			for (int i=0;i<array.length;i++){
				if (array[i]==num){
					return i;
				}
			}
			return 0;
		}

		private int[] toIntArray(List<Integer> list){
			int[] ret = new int[list.size()];
			for(int i = 0;i < ret.length;i++)
				ret[i] = list.get(i);
			return ret;
		}

		public int getAnsewerKey(){
			return key;
		}

		public String getQuestion(){
			return Question;
		}

		public int[] getOrder(){
			return order;
		}

		public String[] getChoices(){
			return Choices;
		}

		private List <Integer> getRandomNumber(int n , int total){
			List <Integer> nums = new ArrayList <Integer>(); 
			Random rand =new Random();

			while (nums.size() < n) { //生成N个 
				int d =Math.abs(rand.nextInt()); 
				d=d%total;
				if (!nums.contains(d)) { //判断不重复 
					nums.add(d); 
				} 
			}
			return nums;
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, R.string.submit).setIcon(R.drawable.ic_menu_upload);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		if(item.getItemId() == 1){
			handleAnswer();
			new AlertDialog.Builder(test_words.this)
			.setIcon(R.drawable.alert_dialog_icon)
			.setTitle(R.string.submit_dialog_title)
			.setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					String str1= test_words.this.getString(R.string.submit_dialog_score);
					String str2= test_words.this.getString(R.string.Quit_test);
					int Score=0;
					int rightAnswer=0;
					for (int i=0; i<answer.length;i++ ){
						if(answer[i]==key[i]){
							Score+=(100/answer.length);
							rightAnswer+=1;
						}
					}
					new AlertDialog.Builder(test_words.this)
					.setIcon(R.drawable.alert_dialog_icon)
					.setTitle(str1+" "+Score+"("+rightAnswer+"/"+answer.length+")\n"+str2)
					.setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							finish();
						}
					}).create().show();
				}
			})
			.setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {

				}
			})
			.create().show();
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return this.detector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	public void handleAnswer(){
		answer[answerNow/10]=answerNow%10;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() - e2.getX() > 60) {
			handleAnswer();
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.left_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.left_out));
			this.flipper.showNext();
			return true;
		} else if (e1.getX() - e2.getX() < -60) {
			handleAnswer();
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.right_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.right_out));
			this.flipper.showPrevious();
			return true;
		} else if (e1.getY() - e2.getY() > 60) {
			handleAnswer();
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_up_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_up_out));
			this.flipper.showNext();
			return true;
		} else if (e1.getY() - e2.getY() < -60) {
			handleAnswer();
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_down_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_down_out));
			this.flipper.showPrevious();
			return true;
		}
		return false;
	}


	@Override
	public void onLongPress(MotionEvent e) {

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
		return false;
	}



}