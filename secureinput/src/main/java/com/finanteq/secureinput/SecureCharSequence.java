package com.finanteq.secureinput;


import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * implementacja CharSequence która trzyma w pamięci wewnetrzną tablicę znaków ułożoną w losowej kolejności
 *
 * TODO - w przypadku dojscia do maksymalnej granicy pojemnosci obiektu dodac autoamtyczne powiekszanie
 */
public class SecureCharSequence implements CharSequence, Serializable
{
	private char[] characters;

	private ArrayList<Integer> indexes;

	private int length = 0;

	private int internalCapacity;

	public SecureCharSequence(int capacity)
	{
		initializeInternal(capacity);
	}

	public SecureCharSequence(char[] chars, boolean cleanInputArray)
	{
		if (chars == null)
		{
			return;
		}
		initializeInternal(chars.length);
		length = chars.length;
		for (int i = 0; i < chars.length; i++)
		{
			characters[indexes.get(i)] = chars[i];
			if(cleanInputArray)
			{
				chars[i] = 0;
			}
		}
	}

	public SecureCharSequence(char[] chars)
	{
		this(chars,false);
	}

	private void initializeInternal(int capacity)
	{
		internalCapacity = capacity;
		indexes = new ArrayList<Integer>(capacity);
		for (int i = 0; i < capacity; i++)
		{
			indexes.add(i, i);
		}
		Collections.shuffle(indexes);
		characters = new char[capacity];
	}

	@Override
	public int length()
	{
		return length;
	}

	@Override
	public char charAt(int index)
	{
		return characters[indexes.get(index)];
	}

	@Override
	public SecureCharSequence subSequence(int beginIndex, int endIndex)
	{

		if (beginIndex < 0) {
			throw new StringIndexOutOfBoundsException(beginIndex);
		}
		if (endIndex > length) {
			throw new StringIndexOutOfBoundsException(endIndex);
		}
		int subLen = endIndex - beginIndex;
		if (subLen < 0) {
			throw new StringIndexOutOfBoundsException(subLen);
		}
		return ((beginIndex == 0) && (endIndex == length)) ? this
				: createSubSequence(beginIndex,endIndex);
	}

	@NonNull
	private SecureCharSequence createSubSequence(int start, int end)
	{
		int length = end - start;
		char[] subSequenceCharacters = new char[length];
		for (int i = 0; i < length; i++)
		{
			subSequenceCharacters[i] = charAt(i + start);
		}
		return new SecureCharSequence(subSequenceCharacters);
	}

	public void append(char valueToAppend)
	{
		if (internalCapacity == length)
		{
			throw new IllegalStateException(String.format("There is no more available capacity (%s)", length));
		}
		characters[indexes.get(length)] = valueToAppend;
		length++;
	}

	public char pop()
	{
		if (length == 0)
		{
			throw new IllegalStateException("can't pop from empty object");
		}
		length--;
		return characters[indexes.get(length)];
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (!(o instanceof SecureCharSequence))
		{
			return false;
		}


		SecureCharSequence that = (SecureCharSequence) o;
		if(length() != that.length())
		{
			return false;
		}

		for(int i =0; i < length(); i++)
		{
			if(charAt(i) != that.charAt(i))
			{
				return false;
			}
		}
		return true;
	}

	public void clear()
	{
		Random random = new Random(System.currentTimeMillis());
		for(int i = 0; i < length() ; i++)
		{
			characters[indexes.get(i)] = (char) random.nextInt();
		}
		length = 0;
		initializeInternal(internalCapacity);
	}

	@Override
	public int hashCode()
	{
		return 0;
	}
}
