package org.erick.finance.resource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@CrossOrigin()
@RequestMapping("/spendingCheck")
@RestController
public class SpendingCheckResource {
	
	
	
	@PostMapping("/upload")
	public void handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
		store(file);
	}

	private void store(MultipartFile file) {

		System.out.println(file.getOriginalFilename());
		System.out.println(file.getSize());
		
		try (InputStream inputStream = file.getInputStream()) {
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		    int nRead;
		    byte[] data = new byte[1024];
		    while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
		        buffer.write(data, 0, nRead);
		    }
	
		    buffer.flush();
		    byte[] byteArray = buffer.toByteArray();
		        
		    String text = new String(byteArray, StandardCharsets.UTF_8);
		    System.out.println(text);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
