package org.qiunet.cfg.manager.exception;

import org.qiunet.utils.exceptions.CustomException;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/***
 *
 * @author qiunet
 * 2021/11/16 11:56
 */
public class UnknownFieldException extends CustomException {
	private static final String SEPARATOR = "\n-------------------------------";
	private final Map<String, Object> stuff = new LinkedHashMap<>();

	public UnknownFieldException(String type, String cfgFile, String field) {
		super("No such field {} in cfg class {}", field, type);
		add("cfgFile", cfgFile);
		add("cfgClass", type);
		add("field", field);
	}

	public UnknownFieldException(String message, Object... params) {
		super(message, params);
	}

	private void addData(String msg, Throwable cause) {
		if (msg != null) {
			add("message", msg);
		}
		if (cause != null) {
			add("cause-exception", cause.getClass().getName());
			add("cause-message", cause.getMessage());
		}
	}

	public String get(String errorKey) {
		return (String) stuff.get(errorKey);
	}

	public void add(String name, String information) {
		String key = name;
		int i = 0;
		while (stuff.containsKey(key)) {
			String value = (String)stuff.get(key);
			if (information.equals(value))
				return;
			key = name + "[" + ++i +"]";
		}
		stuff.put(key, information);
	}

	public void set(String name, String information) {
		String key = name;
		int i = 0;
		stuff.put(key, information); // keep order
		while (stuff.containsKey(key)) {
			if (i != 0) {
				stuff.remove(key);
			}
			key = name + "[" + ++i +"]";
		}
	}

	public Iterator keys() {
		return stuff.keySet().iterator();
	}

	public String getMessage() {
		StringBuffer result = new StringBuffer();
		if (super.getMessage() != null) {
			result.append(super.getMessage());
		}
		if (!result.toString().endsWith(SEPARATOR)) {
			result.append("\n---- Debugging information ----");
		}
		for (Iterator iterator = keys(); iterator.hasNext();) {
			String k = (String) iterator.next();
			String v = get(k);
			result.append('\n').append(k);
			result.append("                    ".substring(Math.min(20, k.length())));
			result.append(": ").append(v);
		}
		result.append(SEPARATOR);
		return result.toString();
	}
}
