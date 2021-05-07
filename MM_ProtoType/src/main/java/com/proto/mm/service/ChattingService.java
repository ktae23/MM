package com.proto.mm.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.SwingWorker;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.proto.mm.model.Movie;
import com.proto.mm.model.Poster;

@Service
public class ChattingService {
	public static boolean flag;

	@Autowired
	MovieService movieService;
	
	@Autowired
	private PosterService posterService;

	String movieGenre = "";
	int movieRating = 0;
	int moviePrice = 0;
	String movieRdate = "";
	int movieRtime = 0;

	public String MM_Chat(Model model, HttpServletRequest request, HttpServletResponse response) {

		String requestMessage = request.getParameter("chat");
		String chatbotMessage = "";

		System.out.println(requestMessage);
		try {
			String apiURL = "https://7a47b2dd7e4f474c9f005449eea5498a.apigw.ntruss.com/custom/v1/4571/67119488042ae085ddf63dddcef829349a6d994504752f146c4c4def7fb7830a";

			URL url = new URL(apiURL);

			if (requestMessage.contains("영화") || requestMessage.contains("추천")) {
				System.out.println("챗봇 질문 다시 시작");
				String message = getReqMessage("다시");

				String secretKey = "cVZMSEliR3J2REZHZVBpZnFLQVpTSFZ0QWNIUnNoTE0=";
				String encodeBase64String = makeSignature(message, secretKey);

				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("Content-Type", "application/json;UTF-8");
				con.setRequestProperty("X-NCP-CHATBOT_SIGNATURE", encodeBase64String);

				// post request
				con.setDoOutput(true);
				DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				wr.write(message.getBytes("UTF-8"));
				wr.flush();
				wr.close();
				int responseCode = con.getResponseCode();
			}

			String message = getReqMessage(requestMessage);
			// System.out.println("##" + message);

			String secretKey = "cVZMSEliR3J2REZHZVBpZnFLQVpTSFZ0QWNIUnNoTE0=";
			String encodeBase64String = makeSignature(message, secretKey);

			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json;UTF-8");
			con.setRequestProperty("X-NCP-CHATBOT_SIGNATURE", encodeBase64String);

			// post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.write(message.getBytes("UTF-8"));
			wr.flush();
			wr.close();
			int responseCode = con.getResponseCode();

			BufferedReader br;

			if (responseCode == 200) { // Normal call
				// System.out.println(con.getResponseMessage());

				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
				String decodedString;
				while ((decodedString = in.readLine()) != null) {
					chatbotMessage = decodedString;
				}
				// chatbotMessage = decodedString;
				in.close();

			} else { // Error occurred
				chatbotMessage = con.getResponseMessage();
			}
			System.out.println(chatbotMessage);

			JSONObject o = new JSONObject(chatbotMessage);
			JSONArray bubbles = o.getJSONArray("bubbles");
			JSONObject bubbles0 = bubbles.getJSONObject(0);
			JSONObject data = bubbles0.getJSONObject("data");
			String description = (String) data.get("description");
			System.out.println("--->" + description);
			
			JSONObject json = new JSONObject();
			json.put("chatMsg", description);

			if (!(requestMessage.contains("영화") || requestMessage.contains("추천") || requestMessage.contains("다시"))) {
				JSONArray slot = bubbles0.getJSONArray("slot");

				System.out.println(slot.toString());
				slot.forEach((item) -> {
					System.out.print("item:" + item + "\n");
					JSONObject jo = (JSONObject) item;
					String name = jo.getString("name");
					if (name.equals("장르")) {
						movieGenre = jo.getString("value");
					} else if (name.equals("평점")) {
						if (jo.getString("value").equals("")) {
							movieRating = 0;
						} else {
							String[] split = jo.getString("value").split("점");
							movieRating = Integer.parseInt(split[0]);
						}
					} else if (name.equals("가격")) {
						if (jo.getString("value").equals("")) {
							moviePrice = 0;
						} else {
							String[] split = jo.getString("value").split("원");
							moviePrice = Integer.parseInt(split[0]);
						}
					} else if (name.equals("개봉일")) {
						String[] split = jo.getString("value").split("년");
						movieRdate = split[0];
					} else if (name.equals("러닝타임")) {
						if (jo.getString("value").equals("")) {
							movieRtime = 0;
						} else {
							String[] split = jo.getString("value").split("분");
							movieRtime = Integer.parseInt(split[0]);
						}
					}

				});// end foreach

				System.out.println(movieGenre + " " + Integer.toString(movieRating) + " " + Integer.toString(moviePrice)
						+ " " + movieRdate + " " + Integer.toString(movieRtime));
				model = movieService.movieFilter(model, movieGenre, movieRating, moviePrice, movieRdate, movieRtime);
				List<Movie> movies = (List<Movie>) model.getAttribute("movies");
								

				if (movies == null || movies.isEmpty()) {
					message = getReqMessage("다시");

					encodeBase64String = makeSignature(message, secretKey);

					con = (HttpURLConnection) url.openConnection();
					con.setRequestMethod("POST");
					con.setRequestProperty("Content-Type", "application/json;UTF-8");
					con.setRequestProperty("X-NCP-CHATBOT_SIGNATURE", encodeBase64String);

					// post request
					con.setDoOutput(true);
					wr = new DataOutputStream(con.getOutputStream());
					wr.write(message.getBytes("UTF-8"));
					wr.flush();
					wr.close();
					responseCode = con.getResponseCode();
					json.put("chatMsg", "죄송합니다.<br>" + "원하시는 조건의 영화를 찾지 못했습니다.<br><br>"
							+ "'영화 추천', '취향에 맞는 영화' 등을 입력해서 취향에 맞는 영화를 다시 찾아보세요.");
				}else {	
					json.put("movies", movies);
					model = posterService.showPosterResult(model);
					List<Poster> posters = (List<Poster>) model.getAttribute("posters");
					json.put("posters", posters);
				}
			}
			
			return json.toString();
		} catch (Exception e) {
			System.out.println(e);
			return "죄송합니다. 다시 입력해주세요";
		}

	}

	public static String makeSignature(String message, String secretKey) {

		String encodeBase64String = "";

		try {
			byte[] secrete_key_bytes = secretKey.getBytes("UTF-8");

			SecretKeySpec signingKey = new SecretKeySpec(secrete_key_bytes, "HmacSHA256");
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(signingKey);

			byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
			encodeBase64String = Base64.getEncoder().encodeToString(rawHmac);// 0x00000002
			System.out.println(encodeBase64String);

			return encodeBase64String;

		} catch (Exception e) {
			System.out.println(e);
		}

		return encodeBase64String;

	}

	public static String getReqMessage(String voiceMessage) {

		String requestBody = "";

		try {

			JSONObject obj = new JSONObject();

			long timestamp = new Date().getTime();

			// System.out.println("##"+timestamp);

			obj.put("version", "v2");
			obj.put("userId", "U47b00b58c90f8e47428af8b7bddc1231heo2jes");
//=> userId is a unique code for each chat user, not a fixed value, recommend use UUID. use different id for each user could help you to split chat history for users.

			obj.put("timestamp", timestamp);

			JSONObject bubbles_obj = new JSONObject();

			bubbles_obj.put("type", "text");

			JSONObject data_obj = new JSONObject();
			data_obj.put("description", voiceMessage);

			bubbles_obj.put("type", "text");
			bubbles_obj.put("data", data_obj);

			JSONArray bubbles_array = new JSONArray();
			bubbles_array.put(bubbles_obj);

			obj.put("bubbles", bubbles_array);
			obj.put("event", "send");

			requestBody = obj.toString();

		} catch (Exception e) {
			System.out.println("## Exception : " + e);
		}

		return requestBody;

	}

}