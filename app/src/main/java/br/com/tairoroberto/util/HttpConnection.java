package br.com.tairoroberto.util;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class HttpConnection {
	public static String getSetDataWeb(String url, ArrayList<NameValuePair> valores){
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		String answer = "";

		try{

			httpPost.setEntity(new UrlEncodedFormEntity(valores));
			HttpResponse resposta = httpClient.execute(httpPost);
			answer = EntityUtils.toString(resposta.getEntity());

		}
		catch (UnsupportedEncodingException e) { e.printStackTrace(); }
		catch (ClientProtocolException e) { e.printStackTrace(); }
		catch (IOException e) { e.printStackTrace(); }
		return(answer);
	}
}
