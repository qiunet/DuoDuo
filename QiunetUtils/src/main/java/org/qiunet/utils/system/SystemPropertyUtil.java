/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.qiunet.utils.system;

import org.qiunet.utils.data.IKeyValueData;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

/**
 * A collection of utility methods to retrieve and parse the values of the Java system properties.
 */
public final class SystemPropertyUtil {
    private static final Logger logger = LoggerType.DUODUO.getLogger();
	private static final IKeyValueData<Object, Object> data = System::getProperties;

    private SystemPropertyUtil() {
        // Unused
    }

	/**
	 * 从环境变量获得数据
	 * @param key
	 * @return
	 */
	private static String get(String key) {
		return String.valueOf(data.getValue(key));
	}
	/***
	 * get user.home
	 * @return
	 */
    public static String getUserHome(){
    	return get("user.home");
    }

	/***
	 * get user.dir
	 * @return
	 */
	public static String getUserDir(){
		return get("user.dir");
	}

	/**
	 * get operation system name
	 * @return
	 */
	public static OSType getOsName(){
		return OSType.getOSType(get("os.name"));
	}


	public enum OSType {
		MAC_OS,
		WINDOWS,
		LINUX,;

		public boolean is(OSType type) {
			return this == type;
		}

		private static OSType getOSType(String osName) {
			if (osName.startsWith("Mac OS")) {
				return OSType.MAC_OS;
			} else if (osName.startsWith("Windows")) {
				return OSType.WINDOWS;
			} else {
				return OSType.LINUX;
			}
		}
	}

	public static String getPathSeparator() {
		return get("path.separator");
	}

	public static String getFileSeparator() {
		return get("file.separator");
	}

	public static String getLineSeparator() {
		return get("line.separator");
	}
}
