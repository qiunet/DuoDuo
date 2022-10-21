package org.qiunet.utils.http;


import okhttp3.Call;
import okhttp3.Callback;
import org.qiunet.utils.logger.LoggerType;

import java.io.IOException;

/***
 * Http 回调
 * @author qiunet
 * 2020-04-22 19:48
 ***/
public interface IHttpCallBack extends Callback {

	@Override
	default void onFailure(Call call, IOException e){
		LoggerType.DUODUO_HTTP.error(call.request() + "Exception: ", e);
	}
}
