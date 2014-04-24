package vike.flash.cards;

import java.util.ArrayList;

import vike.flash.cards.R;

import vike.voice.voice;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

public class flash_cards extends ListActivity {
	private String[] WORD_NAME = new String[]{};
	private String[] word = new String[]{};
	private String[] name = new String[]{};
	private String[] tran = new String[]{};
	private voice vc;
	private int start;
	private int end;
	private String filename;

	public void setNoTitle() { 
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
	} 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setNoTitle();
		ArrayList<String> list1 = new ArrayList<String>();
		ArrayList<String> list2 = new ArrayList<String>();
		Intent intent= getIntent();
		filename=intent.getStringExtra("filename");
		start =intent.getIntExtra("start", 0);
		end =intent.getIntExtra("end", 0);
		WORD_NAME = intent.getStringArrayExtra("WORD_NAME");
		for(int i=start;i<end;i++){
			word = WORD_NAME[i].split("=");
			list1.add(word[0]);
			list2.add(word[1]);
		}
		name = new String[list1.size()];
		tran = new String[list2.size()];
		list1.toArray(name);
		list2.toArray(tran);
		setListAdapter(new ArrayAdapter<String>(this, R.layout.main, name));
		vc=new voice(this);
		vc.InitVoice();
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(new DisplayItem());
		lv.setOnItemLongClickListener(new ShowWords());
	}

	private class DisplayItem implements OnItemClickListener{
		public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
			String str=tran[position];
			Toast.makeText(getApplicationContext(),str,Toast.LENGTH_SHORT).show();
		}
	}


	private class ShowWords implements OnItemLongClickListener{
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
			vc.speak(name[arg2]);
			return false;
		}
	}



	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, R.string.set).setIcon(R.drawable.ic_menu_preferences);
		menu.add(0, 2, 2, R.string.flash).setIcon(R.drawable.ic_menu_flashcards);
		menu.add(0, 3, 3, R.string.test).setIcon(R.drawable.ic_menu_attachment);
		menu.add(0, 4, 4, R.string.about).setIcon(R.drawable.ic_menu_info_details);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		if(item.getItemId() == 1){
			Intent intent = new Intent();
			intent.setClass(flash_cards.this, Preferences.class);
			flash_cards.this.startActivity(intent);
		}
		else if(item.getItemId() == 2){

			Intent intent = new Intent();
			intent.putExtra("start", start);
			intent.putExtra("filename", filename);
			intent.putExtra("end", end);
			intent.putExtra("WORD_NAME", WORD_NAME);
			intent.setClass(flash_cards.this, words.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			flash_cards.this.startActivity(intent);
		}

		else if(item.getItemId() == 3){
			Intent intent = new Intent();
			intent.putExtra("WORD_NAME", WORD_NAME);
			intent.putExtra("filename", filename);
			intent.putExtra("start", start);
			intent.putExtra("end", end);
			intent.setClass(flash_cards.this, test_words.class);
			flash_cards.this.startActivity(intent);
		}
		else if(item.getItemId() == 4){
			new AlertDialog.Builder(flash_cards.this)
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
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPause() {
		SharedPreferences.Editor mEditor =this.getSharedPreferences("Last", 0).edit();
		mEditor.putInt("present_page",0);
		mEditor.putString("filename", filename);
		mEditor.putInt("start", start+1);
		mEditor.putInt("end", end+1);
		mEditor.commit();
		super.onPause();
	}
	

}