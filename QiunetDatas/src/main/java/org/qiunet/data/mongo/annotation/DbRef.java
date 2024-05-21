package org.qiunet.data.mongo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 作用在 MongoDbEntity 字段上.
 * 仅能作用在另一个 BasicMongoEntity 对象上, 不作缓存
 * DbRef 的 find是自动注入的. 更新 保存需要业务自己更新. DuoDuo不负责update.
 *
 * @author qiunet
 * 2024/3/2 08:20
 ***/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface DbRef {
}
