package com.cerosoft.tieba;

import android.R.string;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@SuppressLint("SdCardPath")
public class MyTool {

	// public static ArrayList<DownLoad> threadlist = new ArrayList<DownLoad>();

	public static void main(String[] args) throws Exception {

	 
	}

	public static List<TieBaObj> resolveHtml_tiezi_list(Document doc)
			throws IOException, JSONException {
		List<TieBaObj> list = new ArrayList<TieBaObj>();
		String fid = null;
		System.out.println("resolveHtml" + doc.title());
		// System.out.println("resolveHtml"+doc.getAllElements().toString());
		Elements all_li_fid = doc.getElementsByTag("li");
		// for (Element div_fid : all_div_fid) {
		// if (div_fid.attr("id").equals("editor")
		// && div_fid.attr("class").equals("frs_rich_editor")) {
		// String result = div_fid.attr("data-postor");
		// JSONObject json = new JSONObject(result);
		// fid = json.getString("fid");
		// System.out.println("fid : " + fid);
		// }
		// }

		// Elements all = doc.getElementsByClass(" j_thread_list clearfix");
		for (Element a : all_li_fid) {

			if (a.attr("class").equals(" j_thread_list clearfix")) {
				Elements et = a.getElementsByTag("div");
				Elements et1 = a.getElementsByTag("a");
				Elements et2 = a.getElementsByTag("span");
				TieBaObj tieBa = new TieBaObj();
				tieBa.setFid(fid);

				for (Element t : et2) {
					if (t.attr("class").equals("tb_icon_author ")) {
						Elements span_a = t.getElementsByTag("a");
						for (Element s_a : span_a) {
							if (s_a.attr("class").equals(
									"frs-author-name j_user_card ")) {
								tieBa.setAuthor(s_a.ownText());
							}
						}
					}
					if (t.attr("class").equals("tb_icon_author_rely j_replyer")) {
						Elements span_a = t.getElementsByTag("a");
						for (Element s_a : span_a) {
							if (s_a.attr("class").equals(
									"frs-author-name j_user_card ")) {
								tieBa.setLastReply(s_a.ownText());
							}
						}
					}
				}

				for (Element t : et) {
					if (t.attr("class").equals(
							"threadlist_abs threadlist_abs_onlyline ")) {
						System.out.println("内容：" + t.ownText());
						tieBa.setContent(t.ownText());
					}

					// if(t.attr("class").equals("threadlist_rep_num j_rp_num")){
					// System.out.println("回复数："+t.attr("title"));
					// tieBa.setReply(t.attr("title"));
					// }

					// if(t.attr("title").equals("主题作者")){
					// Elements Eauthor = t.getElementsByTag("a");
					// for(Element author_1 : Eauthor){
					// if(author_1.attr("target").equals("_blank")){
					// System.out.println("主题作者："+author_1.ownText());
					// tieBa.setAuthor(author_1.ownText());
					// }
					// }
					//
					// }

				}

				for (Element t : et1) {
					System.out.println("class：" + t.attr("class"));
					if (t.attr("class").toString().equals("j_th_tit ")) {// md_zhege_kongge_hao_exin
						System.out.println("标题：" + t.attr("title"));

						System.out.println("PID：" + t.attr("href"));
						tieBa.setpID(t.attr("href"));
						tieBa.setTitle(t.attr("title"));
					}
					// if(t.attr("class").equals("j_th_tit")){
					// System.out.println("标题："+t.ownText());
					// System.out.println("PID："+t.attr("href"));
					// tieBa.setpID(t.attr("href"));
					// tieBa.setTitle(t.ownText());
					// }

				}
				System.out
						.println("-----------------------------------------------------------");
				list.add(tieBa);
			}
		}
		return list;

	}

	public static List<TieBaObj> getTieZi() throws IOException {
		List<TieBaObj> list = new ArrayList<TieBaObj>();

		// File input = new File("c:/TB.html");

		Document doc = Jsoup.connect("http://tieba.baidu.com.cn/f?kw=java")
				.get();
		System.out.println(doc.title());
		Elements all = doc.getElementsByClass("j_thread_list clearfix");
		for (Element a : all) {
			Elements et = a.getElementsByTag("div");
			Elements et1 = a.getElementsByTag("a");
			Elements et2 = a.getElementsByTag("span");
			TieBaObj tieBa = new TieBaObj();
			for (Element t : et2) {
				if (t.attr("class").equals("threadlist_rep_num j_rp_num")) {
					Elements span_a = t.getElementsByTag("a");
					for (Element s_a : span_a) {
						if (s_a.attr("class").equals("j_user_card  ")) {
							tieBa.setAuthor(s_a.ownText());
						}
					}
				}
				if (t.attr("class").equals("tb_icon_author_rely j_replyer")) {
					Elements span_a = t.getElementsByTag("a");
					for (Element s_a : span_a) {
						if (s_a.attr("class").equals("j_user_card  ")) {
							tieBa.setLastReply(s_a.ownText());
						}
					}
				}
			}
			for (Element t : et) {
				if (t.attr("class").equals(
						"threadlist_abs threadlist_abs_onlyline")) {
					System.out.println("内容：" + t.ownText());
					tieBa.setContent(t.ownText());
				}

				if (t.attr("class").equals("threadlist_rep_num j_rp_num")) {
					System.out.println("回复数：" + t.attr("title"));
					tieBa.setReply(t.attr("title"));
				}

			}
			for (Element t : et2) {
				if (t.attr("title").equals("最后回复时间")) {
					System.out.println("最后回复时间:" + t.ownText());
					tieBa.setTime(t.ownText());
				}

			}

			for (Element t : et1) {
				if (t.attr("class").equals("j_th_tit")) {
					System.out.println("标题：" + t.ownText());
					System.out.println("PID：" + t.attr("href"));
					tieBa.setpID(t.attr("href"));
					tieBa.setTitle(t.ownText());
				}

			}
			System.out
					.println("-----------------------------------------------------------");
			list.add(tieBa);
		}
		return list;
	}

	public static ArrayList<SonTieZi> NTieView(Document doc) throws Exception {
		// File input = new File(fileName);
		// Document doc = Jsoup.parse(input, "GBK");
		System.out.println(doc.title());
		Elements et = doc.getElementsByTag("div");
		Elements h1 = doc.getElementsByTag("h1");
		ArrayList<SonTieZi> list = new ArrayList<SonTieZi>();
		String title = null;
		for (Element t : h1) {
			if (t.attr("class").equals("core_title_txt")) {
				System.out.println("标题：" + t.ownText());
				title = t.ownText();
			}
		}

		for (Element t : et) {
			SonTieZi son = new SonTieZi();
			if (t.attr("class").equals("l_post noborder")) {
				String data_field = t.attr("data-field");
				// System.out.println(data_field);
				JSONObject json = new JSONObject(data_field);
				JSONObject jAuthor = (JSONObject) json.get("author");
				String authorName = jAuthor.getString("name");
				JSONObject jContent = (JSONObject) json.get("content");
				String jDate = jContent.getString("date");
				Elements div = t.getElementsByTag("div");
				Elements ul = t.getElementsByTag("ul");
				for (Element d : div) {
					if (d.attr("class").equals(
							"d_post_content j_d_post_content ")) {
						System.out.println("楼主内容：" + d.ownText());
						son.setTieZiContent(d.ownText());
					}
				}

				for (Element u : ul) {
					if (u.attr("class").equals("p_author post_icon")) {
						Elements img = u.getElementsByTag("img");
						for (Element i : img) {
							System.out.println("头像的url：" + i.attr("src"));
						}
					}
				}

				System.out.println("时间 ：" + jDate);
				son.setDate(jDate);
				System.out.println("作者： " + authorName);
				son.setAuthor(authorName);
				son.setTitle(title);
				list.add(son);
			}

		}

		for (Element t : et) {
			if (t.attr("class").equals("l_post ")) {
				SonTieZi son = new SonTieZi();
				String data_field = t.attr("data-field");
				// System.out.println(data_field);
				JSONObject json = new JSONObject(data_field);
				JSONObject jAuthor = (JSONObject) json.get("author");
				String authorName = jAuthor.getString("name");
				JSONObject jContent = (JSONObject) json.get("content");
				String jDate = jContent.getString("date");
				Elements eConnent = t.getElementsByTag("div");
				for (Element connent : eConnent) {
					if (connent.attr("class").equals(
							"d_post_content j_d_post_content ")) {
						System.out.println("帖子回复：" + connent.ownText());
						son.setTieZiContent(connent.ownText());
					}

					if (connent.attr("class").equals(
							"j_lzl_container core_reply_wrapper ")) {
						Elements elema = connent.getElementsByTag("a");
						Elements sp = connent.getElementsByTag("span");
						for (Element a : elema) {
							if (a.attr("class").trim().equals("at")) {
								System.out.println("回复人：" + a.attr("username"));
								son.replyer.add(a.attr("username"));
							}
						}

						for (Element s : sp) {
							if (s.attr("class").trim()
									.equals("lzl_content_main")) {
								System.out.println("楼层回复：" + s.ownText());
								son.replyContent.add(s.ownText());
							}
						}

						for (Element s : sp) {
							if (s.attr("class").trim().equals("lzl_time")) {
								System.out.println("楼层回复时间：" + s.ownText());
								son.replyDate.add(s.ownText());
							}
						}
					}
				}
//				System.out.println("时间 ：" + jDate);
				son.setDate(jDate);
//				System.out.println("作者： " + authorName);
				son.setAuthor(authorName);

				list.add(son);
			}
		}
		return list;
	}

	public ArrayList<SonTieZi> NTieView1(Document doc) throws Exception {
		String PID = null;
		System.out.println(doc.title());
		// System.out.println("NTieView1" + doc.getAllElements().toString());

		Elements div = doc.getElementsByTag("div");
		Elements h1 = doc.getElementsByTag("h3");
		ArrayList<SonTieZi> list = new ArrayList<SonTieZi>();
		String title = null;
		for (Element t : h1) {
			if (t.attr("class").equals(
					"core_title_txt pull-left text-overflow  ")) {
//				System.out.println("标题：" + t.ownText());
				title = t.ownText();
			}
		}
		 
		System.out.println("NTieView3");
		for (Element t : div) {
			System.out.println("for (Element t : et) {");
			SonTieZi son = new SonTieZi();
			if (t.attr("class").equals(
					"l_post l_post_bright j_l_post clearfix  ")) {

				Elements one_div = t.getElementsByTag("div");
				Elements one_a = t.getElementsByTag("a");

				for (Element a : one_a) {

					System.out.println("for (Element a : one_a) {");
					if (a.attr("class").equals("p_author_name j_user_card")) {

						son.setAuthor(a.ownText());
						System.out.println("作者" + a.ownText());
					}
					if (a.attr("class").equals("p_author_face ")) {
						Elements one_img = a.getElementsByTag("img");
						for (Element img : one_img) {
							System.out.println("username"
									+ img.attr("username"));
							if (true) {
								son.setAuthor_image(img.attr("src"));
								System.out.println("son.setAuthor_image = "
										+ img.attr("src"));
								if (img.attr("data-tb-lazyload").length() > 10) {
									son.setAuthor_image(img
											.attr("data-tb-lazyload"));
								}
							}

						}
					}

				}

				for (Element d : one_div) {// tiezi_neirong
					if (d.attr("class").equals(
							"d_post_content j_d_post_content ")) {
						Elements contentImage = t.getElementsByTag("img");
						for (Element image : contentImage) {
							son.addContentImage(image.attr("src"));
						}
						// System.out.println("楼主内容：" + d.ownText());
						son.setTieZiContent(d.ownText());

						son.setTitle(title);
					}
					if (d.attr("class").equals("post-tail-wrap")) {
						Elements one_span = d.getElementsByTag("span");
						int span_count = 0;
						for (Element span1 : one_span) {
							if (span_count == (one_span.size() - 1)) {
								son.setDate(span1.ownText());
							}
							if (span_count == (one_span.size() - 2)) {
								son.setFloor(span1.ownText());
							}
							span_count++;
						}
					}

					if (d.attr("class").equals("j_lzl_r p_reply")) {
						String ziAuthor = null;
						String ziDate = null;
						String ziContent = null;
						Elements louzhonglou_a = d.getElementsByTag("a");
						Elements louzhonglou_span = d.getElementsByTag("span");
						
						 String data_field = d.attr("data-field");
						 // System.out.println(data_field);
						  
						JSONObject json = new JSONObject(data_field);

						String jpid =  json.getString("pid");
						String jtotal_num =  json.getString("total_num");
						System.out.println("jpid = "+jpid+"jtotal_num ="+jtotal_num);
						 
						
						for (Element span1 : louzhonglou_span) {
							if (span1.attr("class").equals("lzl_content_main")) {
								ziContent = span1.ownText();
							}
							if (span1.attr("class").equals("lzl_time")) {
								ziDate = span1.ownText();
							}
						}
						for (Element a1 : louzhonglou_a) {
							if (a1.attr("class").equals("at j_user_card")) {
								ziAuthor = a1.attr("username");
							}

						}
						son.addZiHuiFu(ziAuthor, ziDate, ziContent);
					}
				}

				list.add(son);
			}

		}
		// for (Element t : et) {
		// SonTieZi son = new SonTieZi();
		// son.setAllPage(allPage);
		// // if (t.attr("class").equals("l_post noborder")) {
		// System.out.println("NTieView30");
		// String data_field = t.attr("data-field");
		// // System.out.println(data_field);
		// // JSONObject json = new JSONObject(data_field);
		// System.out.println("NTieView301");
		// // JSONObject jAuthor = (JSONObject) json.get("author");
		// System.out.println("NTieView31");
		// // String authorName = jAuthor.getString("user_name");
		// // JSONObject jContent = (JSONObject) json.get("content");
		// // String jDate = jContent.getString("date");
		// Elements div = t.getElementsByTag("div");
		// Elements ul = t.getElementsByTag("ul");
		// System.out.println("NTieView32");
		// Elements sign_image = t.getElementsByTag("img");
		// // for (Element d : div) {
		// // if (d.attr("class").equals(
		// // "d_post_content j_d_post_content ")) {
		// // Elements contentImage = d.getElementsByTag("img");
		// // for (Element image : contentImage) {
		// // son.addContentImage(image.attr("src"));
		// // }
		// // System.out.println("楼主内容：" + d.ownText());
		// // son.setTieZiContent(d.ownText());
		// // }
		// // }
		// System.out.println("NTieView4");
		// for (Element u : ul) {
		// if (u.attr("class").equals("p_author post_icon")) {
		// Elements img = u.getElementsByTag("img");
		// for (Element i : img) {
		// String tempURL = i.attr("src");
		// if ((!(i.attr("src").substring(0, 1)).equals("h"))) {
		// tempURL = "http://tieba.baidu.com.cn"
		// + i.attr("src");
		// }
		// System.out.println("头像的url：" + tempURL);
		// son.setAuthor_image(tempURL);
		// // DownLoad d1 = new
		// // DownLoad(tempURL,PID,authorName,false);
		// // d1.start();
		// // threadlist.add(d1);
		//
		// }
		// }
		// }
		// System.out.println("NTieView5");
		// for (Element sign : sign_image) {
		// if (sign.attr("class").equals("j_user_sign")) {
		// System.out.println("签名的url：" + sign.attr("src"));
		// if (sign.attr("src") != null
		// && !("".equals(sign.attr("src"))))
		// son.setSign(sign.attr("src"));
		// // DownLoad d2 = new
		// // DownLoad(sign.attr("src"),PID,authorName,true);
		// // d2.start();
		// // threadlist.add(d2);
		//
		// }
		// }
		// // System.out.println("时间 ：" + jDate);
		// // son.setDate(jDate);
		// // System.out.println("作者： " + authorName);
		// // son.setAuthor(authorName);
		// son.setTitle(title);
		// list.add(son);
		// // }
		//
		// }
		// System.out.println("NTieView6");
		// for (Element t : et) {
		// if
		// (t.attr("class").equals("l_post l_post_bright j_l_post clearfix  "))
		// {
		// System.out.println("NTieView6.0");
		// SonTieZi son = new SonTieZi();
		// System.out.println("NTieView6.1");
		// son.setAllPage(allPage);
		// String data_field = t.attr("data-field");
		// // System.out.println(data_field);
		// JSONObject json = new JSONObject(data_field);
		// System.out.println("NTieView6.2");
		// JSONObject jAuthor = (JSONObject) json.get("author");
		// System.out.println("NTieView6.3");
		// String authorName = jAuthor.getString("user_name");
		// System.out.println("NTieView6.40");
		// // JSONObject jContent = (JSONObject) json.get("content");
		// // String jDate = jContent.getString("date");
		// System.out.println("NTieView6.41");
		// Elements eConnent = t.getElementsByTag("div");
		// System.out.println("NTieView6.5");
		// // Elements eLi= t.getElementsByTag("li");
		// Elements ul = t.getElementsByTag("ul");
		// Elements sign_image = t.getElementsByTag("img");
		// System.out.println("NTieView6.4");
		// for (Element u : ul) {
		// if (u.attr("class").equals("p_author post_icon")) {
		// Elements img = u.getElementsByTag("img");
		// for (Element i : img) {
		// if (i.attr("data-passive") != null
		// && !("".equals(i.attr("data-passive")))) {
		// System.out.println("头像的url："
		// + i.attr("data-passive"));
		// son.setAuthor_image(i.attr("data-passive"));
		// // DownLoad d3 = new
		// // DownLoad(i.attr("data-passive"),PID,authorName,false);
		// // d3.start();
		// // threadlist.add(d3);
		// } else {
		// System.out.println("头像的url：" + i.attr("src"));
		// String tempURL = i.attr("src");
		// System.out.println("NTieView6.1");
		// if ((!(i.attr("src").substring(0, 1))
		// .equals("h"))) {
		// System.out.println("NTieView6.2");
		// tempURL = "http://tieba.baidu.com.cn"
		// + i.attr("src");
		// }
		//
		// System.out.println("头像的url：" + tempURL);
		// son.setAuthor_image(tempURL);
		// // DownLoad d4 = new
		// // DownLoad(tempURL,PID,authorName,false);
		// // d4.start();
		// // threadlist.add(d4);
		// }
		//
		// }
		// }
		// }
		// System.out.println("NTieView7");
		// for (Element connent : eConnent) {
		// if (connent.attr("class").equals(
		// "d_post_content j_d_post_content")) {
		// Elements contentImage = connent.getElementsByTag("img");
		// for (Element image : contentImage) {
		// son.addContentImage(image.attr("src"));
		// }
		//
		// System.out.println("帖子回复：" + connent.ownText());
		// son.setTieZiContent(connent.ownText());
		// }
		// }
		// System.out.println("NTieView8");
		// for (Element u : ul) {
		// if (u.attr("class").equals("j_lzl_m_w")) {
		// Elements Eli = u.getElementsByTag("li");
		// for (Element li : Eli) {
		// if (li.attr("class")
		// .equals("lzl_single_post j_lzl_s_p first_no_border")
		// || li.attr("class").equals(
		// "lzl_single_post j_lzl_s_p ")) {
		// Elements liA = li.getElementsByTag("a");
		// Elements sp = li.getElementsByTag("span");
		// for (Element lia : liA) {
		// if (lia.attr("class").trim().equals("at")
		// && lia.attr("alog-group").trim()
		// .equals("p_author")) {
		// System.out.println("------回复人："
		// + lia.ownText());
		// son.replyer.add(lia.ownText());
		// }
		// if (lia.attr("class").trim()
		// .equals("lzl_p_p")) {
		// Elements img = lia
		// .getElementsByTag("img");
		// for (Element url : img) {
		// String tempURL = url.attr("src");
		// if ((!(url.attr("src").substring(0,
		// 1)).equals("h"))) {
		// tempURL = "http://tieba.baidu.com.cn"
		// + url.attr("src");
		// }
		//
		// System.out
		// .println("------LZL的url：："
		// + tempURL);
		// son.replyAuthorImage.add(tempURL);
		// // InputStream in =
		// // TieBaUtilTool.doGet(tempURL,
		// // null);
		// // File file = new
		// //
		// File("d:\\tieba\\"+PID+"\\"+lia.ownText()+"_LZL_author_image.png");
		// // createFile(file);
		// // inputFile(file,in);
		// }
		// }
		//
		// }
		// System.out.println("NTieView9");
		//
		// for (Element s : sp) {
		// if (s.attr("class").trim()
		// .equals("lzl_content_main")) {
		// System.out.println("------楼层回复："
		// + s.ownText());
		// son.replyContent.add(s.ownText());
		// }
		// }
		//
		// for (Element s : sp) {
		// if (s.attr("class").trim()
		// .equals("lzl_time")) {
		// System.out.println("------楼层回复时间："
		// + s.ownText());
		// son.replyDate.add(s.ownText());
		// }
		// }
		//
		// }
		// }
		// }
		//
		// }
		// System.out.println("NTieView10");
		//
		// for (Element sign : sign_image) {
		// if (sign.attr("class").equals("j_user_sign")) {
		// if (sign.attr("data-passive") != null) {
		// System.out.println("签名的url："
		// + sign.attr("data-passive"));
		// if (sign.attr("data-passive") != null
		// && !("".equals(sign.attr("data-passive"))))
		// son.setSign(sign.attr("data-passive"));
		// // DownLoad d5 = new
		// // DownLoad(sign.attr("data-passive"),PID,authorName,true);
		// // d5.start();
		// // threadlist.add(d5);
		//
		// } else {
		// System.out.println("签名的url：" + sign.attr("src"));
		// if (sign.attr("src") != null
		// && !("".equals(sign.attr("src"))))
		// son.setSign(sign.attr("src"));
		// // DownLoad d6 = new
		// // DownLoad(sign.attr("src"),PID,authorName,true);
		// // d6.start();
		// // threadlist.add(d6);
		//
		// }
		// }
		// }
		//
		// // System.out.println("时间 ：" + jDate);
		// // son.setDate(jDate);
		// System.out.println("作者： " + authorName);
		// son.setAuthor(authorName);
		//
		// list.add(son);
		// }
		// }
		return list;

	}

	public static boolean createFile(File file) throws IOException {
		if (!file.exists()) {
			makeDir(file.getParentFile());
		}
		return file.createNewFile();
	}

	public static void makeDir(File dir) {
		if (!dir.getParentFile().exists()) {
			makeDir(dir.getParentFile());
		}
		dir.mkdir();
	}

	public static void inputFile(File file, InputStream in) throws Exception {
		FileOutputStream fos = new FileOutputStream(file);
		int data = in.read();
		while (data != -1) {
			fos.write(data);
			data = in.read();
		}
		fos.close();
		in.close();
	}

	/*
	 * 
	 * public class DownLoad extends Thread{ String url; String pid; String
	 * name; boolean isSign; public DownLoad(String url,String pid,String
	 * name,boolean isSign){ this.url = url; this.pid = pid; this.name = name;
	 * this.isSign = isSign; }
	 * 
	 * 
	 * public void run() { InputStream in; try { in = TieBaUtilTool.doGet(url,
	 * null); try { sleep((int) Math.random() * 10); } catch
	 * (InterruptedException e) { e.printStackTrace(); } File file; if(isSign)
	 * file = new File("/sdcard/tieba/"+pid+"/"+name+"_sign_image.png"); else
	 * file = new File("/sdcard/tieba/"+pid+"/"+name+"_author_image.png");
	 * createFile(file); inputFile(file,in); } catch (Exception e) {
	 * e.printStackTrace(); }
	 * 
	 * }
	 * 
	 * 
	 * }
	 */
	public static String getNum(String src) {
		if (src == null || "".equals(src)) {
			return "";
		}
		String regex = "\\d+";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(src);
		String s = null;
		while (m.find()) {
			s = m.group();
			System.out.println(s);
		}
		return s;
	}

	public static Bitmap downloadBitmap(String url) {
		final AndroidHttpClient client = AndroidHttpClient
				.newInstance("Android");
		final HttpGet getRequest = new HttpGet(url);

		try {
			HttpResponse response = client.execute(getRequest);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.w("ImageDownloader", "Error " + statusCode
						+ " while retrieving bitmap from " + url);
				return null;
			}

			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();
					final Bitmap bitmap = BitmapFactory
							.decodeStream(inputStream);
					return bitmap;
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (Exception e) {
			// Could provide a more explicit error message for IOException or
			// IllegalStateException
			getRequest.abort();
			// Log.w("ImageDownloader", "Error while retrieving bitmap from " +
			// url, e.toString());
		} finally {
			if (client != null) {
				client.close();
			}
		}
		return null;
	}

	public static int getScreenWidth(Activity ay) {
		WindowManager manage = ay.getWindowManager();
		Display display = manage.getDefaultDisplay();
		int screenWidth = display.getWidth();
		return screenWidth;
	}

	public static int getScreenHeight(Activity ay) {
		WindowManager manage = ay.getWindowManager();
		Display display = manage.getDefaultDisplay();
		int hi = display.getHeight();
		return hi;

	}
}
