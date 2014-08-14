package com.urucas.popcorntimerc.utils;

/**
* @copyright Urucas
* @license   Copyright (C) 2013. All rights reserved
* @version   Release: 1.0.0
* @link       http://urucas.com
* @developers Bruno Alassia, Pamela Prosperi
*/

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.widget.Toast;

import org.apache.http.conn.util.InetAddressUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public abstract class Utils {

    /**
     * Get IP address from first non-localhost interface
     * @param ipv4  true=return ipv4, false=return ipv6
     * @return  address or empty string
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        if (useIPv4) {
                            if (isIPv4) return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                                return delim<0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }

    public static String generateNonce() {
        try {
            // Create a secure random number generator
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");

            // Get 1024 random bits
            byte[] bytes = new byte[1024/8];
            sr.nextBytes(bytes);

            // Create two secure number generators with the same seed
            int seedByteCount = 10;
            byte[] seed = sr.generateSeed(seedByteCount);

            sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed(seed);
            SecureRandom sr2 = SecureRandom.getInstance("SHA1PRNG");
            sr2.setSeed(seed);

            String nonce = Long.toHexString(sr2.nextLong());
                   nonce = nonce.substring(0,7);

            return nonce;

        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }

    public static String md5(final String s) {

        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

	public static int intIDFromResource(Context context, String name, String type) {
		return context.getResources().getIdentifier(name, type, context.getPackageName());
	}

	public static String stringFromResource(Context context, String name) {
		int id = context.getResources().getIdentifier(name, "string", context.getPackageName());
		return context.getResources().getString(id);
	}

	public static boolean isConnected(Context context) {
		ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService (Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) return true;

        return false;
	}

    public static boolean isWIFIConnected(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService (Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mWifi != null && mWifi.isConnected()) return true;

        return false;
    }

	public static String capitalize(String s) {
	    if (s!=null && s.length() > 0) {
	        return s.substring(0, 1).toUpperCase() + s.substring(1);
	    }
	    else return s;
	}

	public static String capitalize(String s, boolean allWords) {
		String[] words = s.split("\\s+");
		for(int i=0; i< words.length;i++) {
			words[i] = Utils.capitalize(words[i]);
		}
		return Utils.join(words, " ");
	}

	public static String join(String[] s, String glue) {
		String res = "";
		for(int j=0; j<s.length;j++) {
			res += s[j];
			res += glue;
		}
		return res;
	}

	public static boolean ValidPhoneNumber(String pn) {
		return pn.matches("\\d{8,}");
	}

	public static void Toast(Context context, String text) {
		Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
    	toast.show();
	}

	public static void Toast(Context context, int resourceID) {
		Toast toast = Toast.makeText(context, resourceID, Toast.LENGTH_LONG);
    	toast.show();
	}

	public static void Toast(Context context, String text, int length) {
		Toast toast = Toast.makeText(context, text, length);
    	toast.show();
	}

	public static void Toast(Context context, int resourceID, int length) {
		Toast toast = Toast.makeText(context, resourceID, length);
    	toast.show();
	}

	public static void showOKAlert(Context context, String message) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setMessage(message)
               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    	dialog.cancel();
               }
         });
		 AlertDialog alert = builder.create();
		 alert.show();
	}

	public static void showOKAlert(Context context, int messageResourceID) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setMessage(messageResourceID)
               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    	dialog.cancel();
               }
         });
		 AlertDialog alert = builder.create();
		 alert.show();
	}


	public static Dialog showPreloader(Context context, String title, String message) {
		Dialog dialog = ProgressDialog.show(context, title, message, true);
    	dialog.setCancelable(true);
    	return dialog;
	}


	public static void Log(String tag, String message) {
		Log.i(tag, message);
	}

	public static void Log(int number) {
		Log.i("URUCAS_DEBUGGING >>>", String.valueOf(number));
	}

	public static void Log(String message) {
		Log.i("URUCAS_DEBUGGING >>>", message);
	}

	public static void Log(float f) {
		Log.i("URUCAS_DEBUGGING >>>", String.valueOf(f));
	}
	
	public static String getRegionCodeFromSIM(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getSimCountryIso();
	}
	
	public static String getUUID(Context context) {
		TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return tManager.getDeviceId();
	}

    public static String getMacAddress(Context context){
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        return info.getMacAddress().toString();
    }

	public static String getEmailAccount(Context context) {
		try {
		Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
		Account[] accounts = AccountManager.get(context).getAccounts();
		for (Account account : accounts) {
		    if (emailPattern.matcher(account.name).matches()) {
		        String possibleEmail = account.name;
				return possibleEmail;			   
		    }
		}
		}catch(Exception e) {}
		return null;
	}
	
	public static String getNickFromEmail(String email) {
		if(email == null || email.length() == 0) return "";
		
		String[] nick = email.split("@");
		return nick.length == 0 ? null : nick[0];
		
	}
	
	public static float dp2pixel(Context context, int dp) {
		Resources r = context.getResources();
    	return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
	}
	
	public static float distanceBetween2Points(float vectorX0, float vectorY0, float vectorXP, float vectorYP) {
		float x = Math.abs(vectorXP - vectorX0);
		float y = Math.abs(vectorYP - vectorY0);
		return (float) Math.sqrt((x * x) + (y * y));
	}
	
	public static Date string2Date(String sd,String formato){
		//formato = "yyyy-MM-dd'T'HH:mm:ss'Z'";
		//"Thu Jul 11 12:40:18 GMT-03:00 2013"
		//"EE MMM dd HH:mm:ss z YYYY"
		SimpleDateFormat format = new SimpleDateFormat(formato);
		try {
		    Date date = (Date) format.parse(sd);
		    return date;
		} catch (ParseException e) {
		    // TODO Auto-generated catch block  
		    e.printStackTrace();
		}
		return null;
	}
	public static String getAge(String fec_nac){
	    
		DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
	    Calendar dob = Calendar.getInstance();
	    
		Date d = null;
		try {
			d = df.parse(fec_nac);
			
		}catch(Exception e) {}
		
	    Calendar today = Calendar.getInstance();
	    dob.setTime(d);

	    int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

	    if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
	        age--; 
	    }
	    return String.valueOf(age);
	}
	
	public static void openLink(Context context, String url){
		if(url.equals("")){ return; };
		
		if (!url.startsWith("http://") && !url.startsWith("https://"))
			   url = "http://" + url;
		
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(browserIntent);
	}

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Log.i("uri", contentUri.toString());
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            Cursor cursor = null;
            try {

                String[] proj = { MediaStore.Images.Media.DATA };
                cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }else{
            Uri uri = contentUri;
            // DocumentProvider
            if (DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri2 = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri2, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri2 = null;
                    if ("image".equals(type)) {
                        contentUri2 = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        //contentUri2 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        //contentUri2 = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[] {
                            split[1]
                    };

                    return getDataColumn(context, contentUri2, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }

            return null;
        }
    }
	/* metodos copiados */
    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
