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

import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * A collection of utility methods to retrieve and parse the values of the Java system properties.
 */
public final class SystemPropertyUtil {
    private static final Logger logger = LoggerType.DUODUO.getLogger();

    /**
     * Returns {@code true} if and only if the system property with the specified {@code key}
     * exists.
     */
    public static boolean contains(String key) {
        return get(key) != null;
    }

    /**
     * Returns the value of the Java system property with the specified
     * {@code key}, while falling back to {@code null} if the property access fails.
     *
     * @return the property value or {@code null}
     */
    public static String get(String key) {
        return get(key, null);
    }

    /**
     * Returns the value of the Java system property with the specified
     * {@code key}, while falling back to the specified default value if
     * the property access fails.
     *
     * @return the property value.
     *         {@code def} if there's no such property or if an access to the
     *         specified property is not allowed.
     */
    public static String get(final String key, String def) {
        if (key == null) {
            throw new NullPointerException("key");
        }
        if (key.isEmpty()) {
            throw new IllegalArgumentException("key must not be empty.");
        }

        String value = null;
        try {
            if (System.getSecurityManager() == null) {
                value = System.getProperty(key);
            } else {
                value = AccessController.doPrivileged(new PrivilegedAction<String>() {
                    @Override
                    public String run() {
                        return System.getProperty(key);
                    }
                });
            }
        } catch (Exception e) {
            logger.error("Unable to retrieve a system property '{"+key+"}'; default values will be used.", e);
        }

        if (value == null) {
            return def;
        }

        return value;
    }

    /**
     * Returns the value of the Java system property with the specified
     * {@code key}, while falling back to the specified default value if
     * the property access fails.
     *
     * @return the property value.
     *         {@code def} if there's no such property or if an access to the
     *         specified property is not allowed.
     */
    public static boolean getBoolean(String key, boolean def) {
        String value = get(key);
        if (value == null) {
            return def;
        }

        value = value.trim().toLowerCase();
        if (value.isEmpty()) {
            return true;
        }

        if ("true".equals(value) || "yes".equals(value) || "1".equals(value)) {
            return true;
        }

        if ("false".equals(value) || "no".equals(value) || "0".equals(value)) {
            return false;
        }

        logger.error(
                "Unable to parse the boolean system property '{"+key+"}':{"+value+"} - using the default value: {"+def+"}"
        );

        return def;
    }

    /**
     * Returns the value of the Java system property with the specified
     * {@code key}, while falling back to the specified default value if
     * the property access fails.
     *
     * @return the property value.
     *         {@code def} if there's no such property or if an access to the
     *         specified property is not allowed.
     */
    public static int getInt(String key, int def) {
        String value = get(key);
        if (value == null) {
            return def;
        }

        value = value.trim();
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            // Ignore
        }

        logger.warn(
                "Unable to parse the integer system property '{"+key+"}':{"+value+"} - using the default value: {"+def+"}"
        );

        return def;
    }

    /**
     * Returns the value of the Java system property with the specified
     * {@code key}, while falling back to the specified default value if
     * the property access fails.
     *
     * @return the property value.
     *         {@code def} if there's no such property or if an access to the
     *         specified property is not allowed.
     */
    public static long getLong(String key, long def) {
        String value = get(key);
        if (value == null) {
            return def;
        }

        value = value.trim();
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            // Ignore
        }

        logger.warn(
                "Unable to parse the long integer system property '{"+key+"}':{"+value+"} - using the default value: {"+def+"}"
        );

        return def;
    }

    private SystemPropertyUtil() {
        // Unused
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

		public static OSType getOSType(String osName) {
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
