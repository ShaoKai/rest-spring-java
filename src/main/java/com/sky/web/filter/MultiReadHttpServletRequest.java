package com.sky.web.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.collections.iterators.IteratorEnumeration;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiReadHttpServletRequest extends HttpServletRequestWrapper {
	private static final Logger logger = LoggerFactory.getLogger(MultiReadHttpServletRequest.class);
	private Map<String, String[]> values;
	private Map<String, String> value;
	private ByteArrayOutputStream cachedBytes;

	public MultiReadHttpServletRequest(HttpServletRequest request) throws IOException {
		super(request);
		values = new HashMap<String, String[]>();
		value = new HashMap<String, String>();
		cachedBytes = new ByteArrayOutputStream();
		IOUtils.copy(super.getInputStream(), cachedBytes);
		StringBuffer buffer = new StringBuffer(2048);
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(cachedBytes.toByteArray()), "UTF-8"));
			String line = null;
			while ((line = rd.readLine()) != null) {
				buffer.append(line);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (final String s : buffer.toString().split("&")) {
			final String split[] = s.split("=");
			if (split.length > 1) {
				if (split[1].indexOf(",") > 0) {
					values.put(split[0], split[1].split(","));
				} else {
					value.put(split[0], split[1]);
				}
			}
		}
	}

	public String getRequestBody() {
		final StringBuffer buffer = new StringBuffer(2048);
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(cachedBytes.toByteArray()), "UTF-8"));
			String line = null;
			while ((line = rd.readLine()) != null) {
				buffer.append(line);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	@Override
	public Map getParameterMap() {
		Map parameterMap = new HashMap();
		parameterMap.putAll(values);
		parameterMap.putAll(value);
		return parameterMap;
	}

	@Override
	public Enumeration getParameterNames() {
		Map parameterMap = new HashMap();
		parameterMap.putAll(values);
		parameterMap.putAll(value);
		return new IteratorEnumeration(parameterMap.keySet().iterator());
	}

	@Override
	public String getParameter(String name) {
		Object v = value.get(name);
		if (v == null) {
			return null;
		} else {
			return v.toString();
		}
	}

	@Override
	public String[] getParameterValues(String name) {
		if (value.containsKey(name)) {
			return new String[] { value.get(name) };
		}
		return values.get(name);
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {

		return new CachedServletInputStream();
	}

	private void cacheInputStream() throws IOException {
		/*
		 * Cache the inputstream in order to read it multiple times. For convenience, I use apache.commons IOUtils
		 */

	}

	/* An inputstream which reads the cached request body */
	public class CachedServletInputStream extends ServletInputStream {
		private ByteArrayInputStream input;

		public CachedServletInputStream() {
			/* create a new input stream from the cached request body */
			input = new ByteArrayInputStream(cachedBytes.toByteArray());
		}

		@Override
		public int read() throws IOException {
			return input.read();
		}
	}

}
