package controller;

import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qq.weixin.mp.aes.WXBizMsgCrypt;

@Controller
@RequestMapping(value = "test")
public class TestController {
	@RequestMapping(value = "/token")
	public void sayHello(HttpServletRequest request, HttpServletResponse response) throws Exception {

		if (request.getParameter("echostr") != null) {

			System.out.println(request.getRequestURL());
			Set<Entry<String, String[]>> set = request.getParameterMap().entrySet();
			for (Entry<String, String[]> e : set) {
				System.out.println(e.getKey());
				for (String s : e.getValue()) {
					System.out.print(s + " ");
				}
				System.out.println();
			}
			InputStream in = request.getInputStream();
			byte[] b = new byte[1024];
			int i = -1;
			while ((i = in.read(b)) != -1) {
				System.out.println(new String(b, 0, i));
			}
			response.getOutputStream().write(request.getParameter("echostr").getBytes());
		} else {
			String result = "<xml><ToUserName><![CDATA[o7nHvs5RFprGrLK0eDZ9dI1cGKSw]]></ToUserName><FromUserName><![CDATA[wjPersonalDeveloper]]></FromUserName><CreateTime>1467864626</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[你好]]></Content></xml>";
			response.setContentType("text/xml;charset=UTF-8");
			response.getOutputStream().write(result.getBytes());
		}
	}

	public static void main(String[] args) throws Exception {
		String token = "jZjrd5yN8mRXFiMRQoIh2pKS2s5CJMVO4djpYa8lHJUSYyYVCLjwktduhrhJ-uCqbmvKnSrpCzUUi_i4SwHgbmfqRFQm2u4GSFFyZ5Zc_y3rvnt-Rwsj9IDfF4JLhExzVTGjABAYWJ";
		String timeStamp = "1467858820";
		String nonce = "1996482641";
		String signature = "e7aef5c03d8561fb37cbb3c249ae3648304a3fc1"; // token,timeStamp,nonce三个加密后的验证消息来源
		String msg_signature = "af05062bd595d470a0224b1d03f5f4e7772ed953"; // token,timeStamp,nonce,encrypt四个加密后的验证消息
		String aesKey = "TuXbtcf1kI916mn6wEfh1aIaSg7mbm6kETy9C1oiqOg";
		String appid = "wxd7a938a240ffcea9";
		String encrypt = "<xml><ToUserName><![CDATA[wjPersonalDeveloper]]></ToUserName><Encrypt><![CDATA[QrnkA5ZJ5vazXg9I1DE3IYCck+XBPcAk5czoykoRQhl00ivxsEiBPckNR/Bv/yLL34hofZzZH4Ojv+lVkx0dtDkiEpDR48kdnqkN9XM1KpLF5PItoAXAsNFrQbcyrxwpT1GX/GJmw+sA3upd6I+yi2z/vmblS28wWsnFcwMU8xgxRRBngRkkC2dPxc68y8MW+lIJXBOsOt7CK5pZ1J7yO4zWP1H3HchpjQDVdJDJeA8XuQKtvoveBMiFfcpTDoQk3nNFW+mBa+Urtj4SYkx/7k+cP0BHbZBbG6mAorvmDuqAX9qlV3xopS/B0OCefYfrbDWdxuhi72HNqNh6aFyiETyCJYhd6MqerpuFUV0eRKKZ+PNi4Z6OwTbKE1dc2spTUnNxB/xidWtDd95zqZ+AduWMBUDvorITr/+/1SNmQsou5c/Cc6iY1aFqwjgCd7q9ajuL7/Q14rRUmireDIgGRQ==]]></Encrypt></xml>";
		String[] temp = new String[] { token, timeStamp, nonce };
		Arrays.sort(temp);
		String finalStr = "";
		for (String s : temp) {
			finalStr += s;
		}
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
		byte[] result = digest.digest(finalStr.getBytes());
		StringBuffer sb = new StringBuffer();
		for (byte b : result) {
			String shaHex = Integer.toHexString(b & 0xFF);
			if (shaHex.length() < 2) {
				sb.append(0);
			}
			sb.append(shaHex);
		}
		System.out.println(sb);
		System.out.println(sb.toString().equals(signature));

		// 消息加解密,比较复杂,不模拟了
		WXBizMsgCrypt crypt = new WXBizMsgCrypt(token, aesKey, appid);
		String msg = crypt.decryptMsg(msg_signature, timeStamp, nonce, encrypt);
		System.out.println(msg);

	}
}
