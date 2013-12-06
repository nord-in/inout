package com.exnihilo.inout.services;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.os.RemoteException;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.exnihilo.inout.R;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

public class PhoneBlocker extends PhoneCallReceiver {

	private List<String> blockedNumber = new ArrayList<String>();

	private ITelephony telephonyService;

	private TelephonyManager telephonyManager;

	private Map<String, String> countryCodeMap = new HashMap<String, String>();

	private boolean initialized = false;

	public PhoneBlocker() {
		super();
		telephonyManager = (TelephonyManager) savedContext
				.getSystemService(Context.TELEPHONY_SERVICE);

	}

	private void init() {
		if (!initialized) {
			initialized = true;
			telephonyManager = (TelephonyManager) savedContext
					.getSystemService(Context.TELEPHONY_SERVICE);
			try {
				Class clazz = Class.forName(telephonyManager.getClass()
						.getName());
				Method method = clazz.getDeclaredMethod("getITelephony");
				method.setAccessible(true);
				telephonyService = (ITelephony) method.invoke(telephonyManager);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}

			String[] rl = savedContext.getResources().getStringArray(
					R.array.CountryCodes);
			for (int i = 0; i < rl.length; i++) {
				String[] g = rl[i].split(",");
				countryCodeMap.put(g[0].trim(), g[1].trim());
			}
			initBlockedNumbers();
		}
	}

	@Override
	protected void onIncomingCallStarted(String number, Date start) {

		init();
		try {
			String inNumber = getFormattedNumber(number);
			if (blockedNumber.contains(inNumber)) {
				System.out.println("Blocked number " + inNumber);
				blockCall();
			} else {
				System.out.println("Non blocked number = " + inNumber);
			}
		} catch (NumberParseException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onOutgoingCallStarted(String number, Date start) {
		init();
		System.out.println("Outcoming call started");

	}

	@Override
	protected void onIncomingCallEnded(String number, Date start, Date end) {
		init();
		System.out.println("Incoming call ended");
	}

	@Override
	protected void onOutgoingCallEnded(String number, Date start, Date end) {
		init();
		System.out.println("Outgoing call ended");

	}

	@Override
	protected void onMissedCall(String number, Date start) {
		init();
		System.out.println("Missing call");
	}

	private void blockCall() {

		try {
			telephonyService.endCall();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected void addBlockedNumber(String number) {
		blockedNumber.add(number);
	}

	private void initBlockedNumbers() {
		addBlockedNumber("0617305178");
	}

	private String getCountryZipCode() {
		if (!initialized)
			init();
		String countryID = telephonyManager.getSimCountryIso().toUpperCase(
				Locale.getDefault());
		// return countryCodeMap.get(countryID.trim());
		return countryID;
	}

	private String getFormattedNumber(String original)
			throws NumberParseException {
		String countryCode = getCountryZipCode();
		System.out.println("Utilisation du country code " + countryCode);
		PhoneNumber number = PhoneNumberUtil.getInstance().parse(original,
				countryCode);
		return PhoneNumberUtil.getInstance().format(number,
				PhoneNumberFormat.E164);

	}
}
