package vike.flash.cards;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import vike.flash.cards.R;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;

public class Course extends ListActivity {

	private static final int ABOUT = 1;
	private static final int SETTING = 2;
	private String[] mFile = new String[] {};
	private String FilePath = Environment.getExternalStorageDirectory()
			.getPath() + "/data/flash cards/";

	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, ABOUT, 2, R.string.about).setIcon(
				R.drawable.ic_menu_info_details);
		menu.add(0, SETTING, 3, R.string.set).setIcon(
				R.drawable.ic_menu_preferences);
		return super.onCreateOptionsMenu(menu);
	}

	public void onCreate(Bundle savedInstanceState) {
		// FilePath = Environment.getExternalStorageDirectory().getPath();
		super.onCreate(savedInstanceState);
		getApplicationContext();
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.putExtra("filename", ((TextView) view).getText()
						+ ".vocal");
				intent.putExtra("FilePath", FilePath);
				intent.setClass(Course.this, setting.class);
				Course.this.startActivity(intent);
			}
		});
		String[] mFile = getFileList(FilePath);
		setListAdapter(new ArrayAdapter<String>(this, R.layout.course, mFile));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == ABOUT) {
			new AlertDialog.Builder(Course.this)
					.setIcon(R.drawable.alert_dialog_icon)
					.setTitle(R.string.about)
					.setMessage(R.string.imformation)
					.setPositiveButton(R.string.alert_dialog_ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

								}
							})
					.setNegativeButton(R.string.alert_dialog_cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

								}
							}).create().show();
		} else if (item.getItemId() == SETTING) {
			Intent intent = new Intent();
			intent.setClass(Course.this, Preferences.class);
			Course.this.startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

	private String[] getFileList(String path) {
		FilenameFilter fnf = new FilenameFilter() {
			public boolean accept(File dir, String filename) {
				if (filename.endsWith(".vocal"))
					return true;
				return false;
			}
		};

		File flist = new File(FilePath);
		if (!flist.exists()) {
			boolean result = flist.mkdir();
			if (result) {
				System.out.println("DIR created");
				copyFileOrDir("");
				String fname= flist.list()[0];
				unpackZip(FilePath,fname);
			}
		}

		mFile = flist.list(fnf);
		
		for (int i = 0; i < mFile.length; i++) {
			mFile[i] = mFile[i].substring(0, mFile[i].length() - 6);
		}
		return mFile;
	}
	private void copyFileOrDir(String path) {
	    AssetManager assetManager = this.getAssets();
	    String assets[] = null;
	    try {
	        Log.i("tag", "copyFileOrDir() "+path);
	        assets = assetManager.list(path);
	        if (assets.length == 0) {
	            copyFile(path);
	        } else {
	            String fullPath =  FilePath + path;
	            Log.i("tag", "path="+fullPath);
	            File dir = new File(fullPath);
	            if (!dir.exists() && !path.startsWith("images") && !path.startsWith("sounds") && !path.startsWith("webkit"))
	                if (!dir.mkdirs())
	                    Log.i("tag", "could not create dir "+fullPath);
	            for (int i = 0; i < assets.length; ++i) {
	                String p;
	                if (path.equals(""))
	                    p = "";
	                else 
	                    p = path + "/";

	                if (!path.startsWith("images") && !path.startsWith("sounds") && !path.startsWith("webkit"))
	                    copyFileOrDir( p + assets[i]);
	            }
	        }
	    } catch (IOException ex) {
	        Log.e("tag", "I/O Exception", ex);
	    }
	}
	private void copyFile(String filename) {
	    AssetManager assetManager = this.getAssets();

	    InputStream in = null;
	    OutputStream out = null;
	    String newFileName = null;
	    try {
	        Log.i("tag", "copyFile() "+filename);
	        in = assetManager.open(filename);
	        if (filename.endsWith(".jpg")) // extension was added to avoid compression on APK file
	            newFileName = FilePath + filename.substring(0, filename.length()-4);
	        else
	            newFileName = FilePath + filename;
	        out = new FileOutputStream(newFileName);

	        byte[] buffer = new byte[1024];
	        int read;
	        while ((read = in.read(buffer)) != -1) {
	            out.write(buffer, 0, read);
	        }
	        in.close();
	        in = null;
	        out.flush();
	        out.close();
	        out = null;
	    } catch (Exception e) {
	        Log.e("tag", "Exception in copyFile() of "+newFileName);
	        Log.e("tag", "Exception in copyFile() "+e.toString());
	    }

	}

	private boolean unpackZip(String path, String zipname)
	{       
	     InputStream is;
	     ZipInputStream zis;
	     try 
	     {
	         String filename;
	         is = new FileInputStream(path + zipname);
	         zis = new ZipInputStream(new BufferedInputStream(is));          
	         ZipEntry ze;
	         byte[] buffer = new byte[1024];
	         int count;

	         while ((ze = zis.getNextEntry()) != null) 
	         {
	             filename = ze.getName();

	             // Need to create directories if not exists, or
	             // it will generate an Exception...
	             if (ze.isDirectory()) {
	                File fmd = new File(path + filename);
	                fmd.mkdirs();
	                continue;
	             }

	             FileOutputStream fout = new FileOutputStream(path + filename);

	             while ((count = zis.read(buffer)) != -1) 
	             {
	                 fout.write(buffer, 0, count);             
	             }

	             fout.close();               
	             zis.closeEntry();
	         }

	         zis.close();
	     } 
	     catch(IOException e)
	     {
	         e.printStackTrace();
	         return false;
	     }

	    return true;
	}


}