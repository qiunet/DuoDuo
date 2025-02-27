package org.qiunet.data.core.mongo.provide;

import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.qiunet.utils.exceptions.CustomException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 *
 * @author qiunet
 * 2023/7/30 07:40
 */
public class DMapCodecProvider implements CodecProvider {


	@Override
	public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
		return null;
	}

	@Override
	public <T> Codec<T> get(Class<T> clazz, List<Type> typeArguments, CodecRegistry registry) {
		if (Map.class.isAssignableFrom(clazz) && typeArguments.size() == 2) {
			Codec<?> valCodec;
			if (ParameterizedType.class.isAssignableFrom(typeArguments.get(1).getClass())) {
				List<Type> typeList = Arrays.asList(((ParameterizedType) typeArguments.get(1)).getActualTypeArguments());
				Class<?> rawType = (Class<?>) ((ParameterizedType) typeArguments.get(1)).getRawType();
				valCodec = registry.get(rawType, typeList);
			}else {
				valCodec = registry.get(((Class<?>) typeArguments.get(1)));
			}
			return new MapCodec(clazz, (Class<?>) typeArguments.get(0), valCodec);
		} else {
			return null;
		}
	}

	private static class MapCodec<K, T> implements Codec<Map<K, T>> {
		private final Class<Map<K, T>> encoderClass;
		private final Class<K> keyClass;
		private final Codec<T> valueCodec;

		public MapCodec(Class<Map<K, T>> encoderClass, Class<K> keyClass, Codec<T> valueCodec) {
			this.encoderClass = encoderClass;
			this.keyClass = keyClass;
			this.valueCodec = valueCodec;
		}

		@Override
		public Map<K, T> decode(BsonReader reader, DecoderContext context) {
			reader.readStartDocument();
			Map<K, T> map = getInstance();
			while (!BsonType.END_OF_DOCUMENT.equals(reader.readBsonType())) {
				K key = returnKeyObj(reader.readName());
				T val = null;
				if (!BsonType.NULL.equals(reader.getCurrentBsonType())) {
					val = valueCodec.decode(reader, context);
				}
				map.put(key, val);
			}
			reader.readEndDocument();
			return map;
		}

		private K returnKeyObj(String val) {
			Class aClass = keyClass;
			if (aClass == Byte.class) return (K) Byte.valueOf(val);
			if (aClass == Short.class) return (K) Short.valueOf(val);
			if (aClass == Integer.class) return (K) Integer.valueOf(val);
			if (aClass == Long.class) return (K) Long.valueOf(val);
			if (aClass == Boolean.class) return (K) Boolean.valueOf(val);
			if (aClass.isEnum()) return (K)Enum.valueOf(aClass, val);
			if (aClass == String.class) return (K) val;
			throw new CustomException("Not Support for type: {}", aClass.getName());
		}

		@Override
		public void encode(BsonWriter writer, Map<K, T> map, final EncoderContext encoderContext) {
			writer.writeStartDocument();
			map.forEach((key, val) -> {
				writer.writeName(key.toString());
				valueCodec.encode(writer, val, encoderContext);
			});
			writer.writeEndDocument();
		}

		@Override
		public Class<Map<K, T>> getEncoderClass() {
			return encoderClass;
		}

		private Map<K, T> getInstance(){
			if (encoderClass.isInterface()) {
				return new HashMap<>();
			}
			try {
				return encoderClass.getDeclaredConstructor().newInstance();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
