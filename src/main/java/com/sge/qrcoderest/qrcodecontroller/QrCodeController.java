package com.sge.qrcoderest.qrcodecontroller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.nayuki.qrcodegen.QrCode;

@RestController
public class QrCodeController {
	
	@RequestMapping("/")
	@ResponseBody
	public String qr() {
		System.out.println("QRController::home()");
		return HOME_PAGE;
	}

	final static String HOME_PAGE = "<html>\r\n"
			+ "<head>\r\n"
			+ "<meta charset=\"ISO-8859-1\">\r\n"
			+ "<title>QR Code Generator</title>\r\n"
			+ "</head>\r\n"
			+ "<style>\r\n"
			+ "	body {color: White; background-color: DodgerBlue;}\r\n"
			+ "</style>\r\n"
			+ "<body>      \r\n"
			+ "	<center>\r\n"
			+ "	<h1>QR Code Generator</h1>\r\n"
			+ "	<p>Insert the text for the QR-Code image generation:<p>\r\n"
			+ "	<form action=\"displayqr\">\r\n"
			+ "	    <textarea name=\"qrText\" cols=\"40\" rows=\"5\"></textarea><br>\r\n"
			+ "		<input type=\"submit\"><br>\r\n"
			+ "	</form>\r\n"
			+ "	</center>\r\n"
			+ "</body>\r\n"
			+ "</html>";

	
	@RequestMapping("/displayqr")
	@ResponseBody
	public String displayqr(@RequestParam String qrText) {
		System.out.println("QRController::displayQRCode()");
		System.out.println("qr text: " + qrText);
		
		return "<html>\r\n"
				+ "<head>\r\n"
				+ "<meta charset=\"ISO-8859-1\">\r\n"
				+ "<title>QR Code Generator</title>\r\n"
				+ "</head>\r\n"
				+ "<style>\r\n"
				+ "	body {color: White;\r\n"
				+ "	      background-color: DodgerBlue;}\r\n"
				+ "</style>\r\n"
				+ "<body>      \r\n"
				+ "	<center>\r\n"
				+ "	<h1>QR Code Generator</h1>\r\n"
				+ "	<p>Generated image:</p>\r\n"
				+ "	\r\n"
				+ "	<img src=\"/imageqrcode/${qrtext}\" alt=\"place holder for image\"\">\r\n"
				+ "	\r\n"
				+ "	<p>" + qrText + "</p>\r\n"
				+ "	</center>\r\n"
				+ "	<a href=\"/downloadsvg/qrcode\" download>download (svg scalable graphic)</a><br>\r\n"
				+ "	<script>\r\n"
				+ "	   function submitForm(x){\r\n"
				+ "	      if(x.id=='backButton'){\r\n"
				+ "	         document.getElementById('hid1').value='backButton';\r\n"
				+ "	      }\r\n"
				+ "	      document.forms[0].submit();\r\n"
				+ "	   }\r\n"
				+ "	</script>	\r\n"
				+ "	<form action=/>\r\n"
				+ "	    <th>\r\n"
				+ "	    	<br>\r\n"
				+ "	    	<input type=\"button\" id=backButton value=\"back\" name=\"backButton\" onClick='submitForm(this)'/></th> \r\n"
				+ "		<input type='hidden' id='hid1'  name='hid1'>\r\n"
				+ "	</form>\r\n"
				+ "	\r\n"
				+ "</body>\r\n"
				+ "</html>";
	}

	
	@RequestMapping(value = "imageqrcode/{qrtext}")
	@ResponseBody
	public ResponseEntity<byte[]> imageqrcode(@PathVariable String qrtext, HttpServletRequest request) {
		System.out.println("QRController::imageqrcode() " + qrtext);

		BufferedImage bufferedImage = ImageOperations.toImage(QrCode.encodeText(qrtext, QrCode.Ecc.QUARTILE), 20, 4);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(bufferedImage, "png", baos);
		} catch (IOException e) {
			System.out.println("QRController::imageqrcode() IOException");
			e.printStackTrace();
		}
		byte[] imageBytes = baos.toByteArray();
		return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageBytes);
	}	

	
	@RequestMapping(value = "downloadsvg/{qrtext}")
	public ResponseEntity<String> downloadsvg(@PathVariable String qrtext)  {
		System.out.println("QRController::downloadsvg()");

		QrCode qr = QrCode.encodeText(qrtext, QrCode.Ecc.QUARTILE);
		String svg = ImageOperations.toSvgString(qr, 4, "#FFFFFF", "#000000");  
		
		return ResponseEntity.ok().contentType(MediaType.TEXT_XML).body(svg);
	}
	
		
}
