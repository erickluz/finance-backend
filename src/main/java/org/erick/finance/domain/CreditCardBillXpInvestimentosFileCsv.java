package org.erick.finance.domain;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.erick.finance.dto.CreditCardFileDTO;
import org.springframework.web.multipart.MultipartFile;

public class CreditCardBillXpInvestimentosFileCsv implements CreditCardBillFile {
	private final String HEAD_FILE = "﻿Data;Estabelecimento;Portador;Valor;Parcela";
	private final MultipartFile file;
	
	public CreditCardBillXpInvestimentosFileCsv(MultipartFile file) {
		this.file = file;
	}
	
	@Override
	public List<CreditCardFileDTO> parse() throws Exception {
		String fileName = file.getOriginalFilename();
		if (!fileName.contains(".csv" )) {
			System.out.println("Invalid File. Invalid file extension. Filename: " + fileName);
			throw new Exception("Invalid file extension");
		}
		List<CreditCardFileDTO> fileDTO = new ArrayList<>();
		try (InputStream inputStream = file.getInputStream()) {
			String[] registers = parseBinToString(inputStream);
//		    if (registers.length <= 0 || registers[0].equals("") || !registers[0].equals(HEAD_FILE)) {
//		    	System.out.println("Invalid File. Invalid Header. Size: " + registers.length + "Header: " + registers[0]);
//		    	throw new Exception("Invalid File");
//		    }
		    for (int i = 1 ; i < registers.length; i++) {
		    	String [] register = registers[i].split(";");
		    	String date = register[0];
		    	String description = register[1];
		    	String value = register[3];
		    	String part = register[4];
		    	fileDTO.add(new CreditCardFileDTO(date, description, value, part));
		    }
		} catch (IOException e) {
			System.out.println(e.getMessage());
			throw new Exception("Invalid file");
		}
		
		return fileDTO;
	}

	private String[] parseBinToString(InputStream inputStream) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int nRead;
		byte[] data = new byte[1024];
		while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
		    buffer.write(data, 0, nRead);
		}

		buffer.flush();
		byte[] byteArray = buffer.toByteArray();
		    
		String stringFile = new String(byteArray, StandardCharsets.UTF_8);
		return stringFile.split("\n");
	}

}
