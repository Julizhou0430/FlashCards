package vike.flash.cards;

import vike.flash.cards.R;
import vike.Download.FileUtils;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;


public class setting extends Activity {

	private RadioGroup mRG= null;
	private EditText setcus =null;
	private EditText setpage =null;
	private SeekBar seek =null;
	private int number = 10;
	private String filename;
	private String FilePath;
	private String[] WORD_NAME=new String[]{};



	@Override
	protected void onResume() {
		Preferences Pfs=new Preferences();
		@SuppressWarnings("static-access")
		SharedPreferences sp = Pfs.getPreferenceManager().getDefaultSharedPreferences(this);
		Boolean Custumer=sp.getBoolean("Custumer", false);
		int custumer_text=Integer.parseInt(sp.getString("custumer_text","23"));
		if (Custumer){
			setcus.setEnabled(true);
			setcus.setFocusable(true);
			setcus.setFocusableInTouchMode(true);
			number=custumer_text;
			setcus.setText(number+"");
			mRG.check(R.id.Radio4);
		}else{
			int words_number=Integer.parseInt(sp.getString("words_number","0"));
			setcus.setEnabled(false);
			setcus.setFocusable(false);
			if(words_number==10){
				number=10;
				mRG.check(R.id.Radio1);
			}else if(words_number==20){
				number=20;
				mRG.check(R.id.Radio2);
			}else if(words_number==50){
				number=20;
				mRG.check(R.id.Radio3);
			}
		}

		Boolean last=sp.getBoolean("last", false);
		SharedPreferences mSettings = this.getSharedPreferences("Last", 0);
		int start=mSettings.getInt("start", 0);
		if(last&&(filename.equals(mSettings.getString("filename", null)))){
			int end=mSettings.getInt("end", 0);
			Intent intent = new Intent();
			intent.putExtra("WORD_NAME", WORD_NAME);
			intent.putExtra("filename", filename);
			intent.putExtra("start",start);
			intent.putExtra("end", end);
			intent.setClass(setting.this, words.class);
			setting.this.startActivity(intent);
			android.os.Process.killProcess(android.os.Process.myPid());   
		}
		if(filename.equals(mSettings.getString("filename", null))){
			setpage.setText(start+"");
			seek.setProgress(start);
		}else{
			setpage.setText("1");
			seek.setProgress(1);
		}
		setpage.setEnabled(true);
		setpage.setFocusable(true);
		setpage.setFocusableInTouchMode(true);
		seek.requestFocus();
		//seek.requestFocusFromTouch();
		super.onResume();
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.setting);
		Intent intent= getIntent();
		filename=intent.getStringExtra("filename");
		FilePath=intent.getStringExtra("FilePath");
		FileUtils fu = new FileUtils();
		WORD_NAME = fu.readFile(filename,FilePath);

		mRG = (RadioGroup) findViewById(R.id.RadioGroup);
		setcus = (EditText)findViewById(R.id.customer);
		setpage = (EditText)findViewById(R.id.setpage);
		setcus.setEnabled(false);
		seek = (SeekBar)findViewById(R.id.Progress);
		seek.setMax(WORD_NAME.length);
		seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				setpage.setText(progress+"");
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar){}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
		});
		setpage.setOnEditorActionListener(new TextView.OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				seek.setProgress(Integer.parseInt(v.getText().toString()));
				return false;
			}
		});
		RadioButton rb =(RadioButton) findViewById(R.id.Radio1);
		rb.toggle();
		TextView setname = (TextView)findViewById(R.id.textname);
		setname.setText(filename.substring(0,filename.length()-6));
		TextView tv =(TextView)findViewById(R.id.textset);
		tv.setText(tv.getText().toString()+"\n"+"最小序号"+"1"+"\t\t"+"最大序号"+" "+WORD_NAME.length);
		mRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId==R.id.Radio4){
					setcus.setEnabled(true);
					setcus.setFocusable(true);
					setcus.setFocusableInTouchMode(true);
				}else if(checkedId==R.id.Radio1){
					setcus.setEnabled(false);
					setcus.setFocusable(false);
					number=10;
				}else if (checkedId==R.id.Radio2){
					setcus.setEnabled(false);
					setcus.setFocusable(false);
					number=20;
				}else if(checkedId==R.id.Radio3){
					setcus.setEnabled(false);
					setcus.setFocusable(false);
					number=50;
				}
			}
		});
		setcus.setEnabled(false);
		setpage.setFocusable(false);
	}


	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, R.string.flash).setIcon(R.drawable.ic_menu_flashcards);
		menu.add(0, 2, 2, R.string.list).setIcon(R.drawable.ic_menu_list);
		menu.add(0, 3, 3, R.string.test).setIcon(R.drawable.ic_menu_attachment);
		menu.add(0, 4, 4, R.string.back).setIcon(R.drawable.ic_menu_revert);
		return super.onCreateOptionsMenu(menu);
	}


	public boolean onOptionsItemSelected(MenuItem item) {
		if(mRG.getCheckedRadioButtonId()==R.id.Radio4){
			number=Integer.parseInt(setcus.getText().toString());
		}
		int page = Integer.parseInt(setpage.getText().toString());
		if(page<1)page=1;else if(page>WORD_NAME.length-number)page=WORD_NAME.length-number;

		if(item.getItemId() == 1){
			Intent intent = new Intent();
			intent.putExtra("WORD_NAME", WORD_NAME);
			intent.putExtra("filename", filename);
			intent.putExtra("start", page-1);
			intent.putExtra("end", page-1+number);
			intent.setClass(setting.this, words.class);
			setting.this.startActivity(intent);
		}
		else if(item.getItemId() == 2){
			Intent intent = new Intent();
			intent.putExtra("WORD_NAME", WORD_NAME);
			intent.putExtra("filename", filename);
			intent.putExtra("start", page-1);
			intent.putExtra("end", page-1+number);
			intent.setClass(setting.this, flash_cards.class);
			setting.this.startActivity(intent);
		}
		else if(item.getItemId() == 3){
			Intent intent = new Intent();
			intent.putExtra("WORD_NAME", WORD_NAME);
			intent.putExtra("filename", filename);
			intent.putExtra("start", page-1);
			intent.putExtra("end", page-1+number);
			intent.setClass(setting.this, test_words.class);
			setting.this.startActivity(intent);
		}
		else if(item.getItemId() == 4){
			finish();
		}
		finish();
		return super.onOptionsItemSelected(item);
	}


}