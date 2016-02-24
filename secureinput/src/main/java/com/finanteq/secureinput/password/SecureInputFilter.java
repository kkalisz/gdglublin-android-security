package com.finanteq.secureinput.password;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;

import com.finanteq.secureinput.BuildConfig;
import com.finanteq.secureinput.SecureCharSequence;

/**
 * bezpieczny filtr dla haseł, przy wprowadzaniu liter podczas filttrowania wprowaqdzone wartosci zamieniane są na kropki maski, natomiat
 * oryginalne umieszczane są w {@link SecureCharSequence}
 */
public class SecureInputFilter implements InputFilter
{
	private SecureCharSequence secureCharSequence = new SecureCharSequence(60);

	private boolean isEnabled = true;
	@Override
	public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend)
	{
		if(!isEnabled)
		{
			return null;
		}
		if(BuildConfig.DEBUG)
		{
			Log.d("LabeledPassword", String.format("source: %s, start: %s, end: %s, dest: %s dstart: %s dend: %s", source, start, end,
					dest, dstart, dend));
		}
		if(TextUtils.isEmpty(dest))
		{
			secureCharSequence.clear();
		}
		if (TextUtils.isEmpty(source) && dstart < dend)
		{
			secureCharSequence.pop();
		}
		else
		{
			int sizeOfInput = end - start;
			StringBuilder maskedResult = new StringBuilder();
			for (int i = start; i < start + sizeOfInput; i++)
			{
				secureCharSequence.append(source.charAt(i));
				maskedResult.append('•');
			}
			return maskedResult.toString();
		}
		return null;
	}

	public SecureCharSequence getSecureCharSequence()
	{
		return secureCharSequence;
	}

	public void setSecureCharSequence(SecureCharSequence secureCharSequence)
	{
		this.secureCharSequence = secureCharSequence;
	}

	public boolean isEnabled()
	{
		return isEnabled;
	}

	public void setIsEnabled(boolean isEnabled)
	{
		this.isEnabled = isEnabled;
	}
}
