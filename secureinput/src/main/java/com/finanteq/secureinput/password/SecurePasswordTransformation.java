package com.finanteq.secureinput.password;

import android.graphics.Rect;
import android.text.*;
import android.text.method.TransformationMethod;
import android.view.View;

import com.finanteq.secureinput.SecureCharSequence;

public class SecurePasswordTransformation implements TransformationMethod, TextWatcher
{
	private SecureCharSequencePresenter secureCharSequencePresenter;

	private static char DOT = '\u2022';

	private boolean isRemoving;

	private SecureCharSequence secureCharSequence;

	private boolean mask = true;

	public SecurePasswordTransformation(SecureCharSequence secureCharSequence)
	{
		this.secureCharSequence = secureCharSequence;
	}

	public CharSequence getTransformation(CharSequence source, View view)
	{
		this.secureCharSequencePresenter = new SecureCharSequencePresenter(source,view);
		return secureCharSequencePresenter;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after)
	{

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{
		isRemoving = isRemoving(before,count);
	}

	// funckja sprawdza czy aktualna operacja była usuwaniem
	private boolean isRemoving(int beforeCount, int count)
	{
		return count - beforeCount < 0;
	}

	@Override
	public void afterTextChanged(Editable s)
	{
		if(!isRemoving)
		{
			secureCharSequencePresenter.lastVisible(true);
		}
	}

	public void setMask(boolean mask)
	{
		this.mask = mask;
	}

	public boolean isMask()
	{
		return mask;
	}


	public class SecureCharSequencePresenter implements CharSequence
	{

		public static final int CHAR_VISIBLE_TIME = 1500;
		private CharSequence source;
		private boolean lastVisible;
		private View view;


		public SecureCharSequencePresenter(CharSequence source, View view)
		{
			this.source = source;
			this.view = view;
		}

		@Override
		public int length()
		{
			return source.length();
		}

		@Override
		public char charAt(int index)
		{
			if((index == length() -1 && lastVisible) || !isMask())
			{
				return secureCharSequence.charAt(index);
			}
			else
			{
				return DOT;
			}
		}

		@Override
		public CharSequence subSequence(int start, int end)
		{
			return source.subSequence(start,end);
		}

		public void lastVisible(boolean lastCharVisible)
		{
			this.lastVisible = lastCharVisible;
			view.postDelayed(new Runnable()
			{
				@Override
				public void run()
				{
					lastVisible = false;
				}
			}, CHAR_VISIBLE_TIME);
		}
	}

	@Override
	public void onFocusChanged(View view, CharSequence sourceText, boolean focused, int direction, Rect previouslyFocusedRect)
	{

	}

}