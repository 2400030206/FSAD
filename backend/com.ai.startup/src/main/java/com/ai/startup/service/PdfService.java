package com.ai.startup.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class PdfService {

public String extractText(MultipartFile file)throws Exception{

PDDocument doc=PDDocument.load(file.getInputStream());

PDFTextStripper stripper=new PDFTextStripper();

String text=stripper.getText(doc);

doc.close();

return text;
}

public boolean isPdf(MultipartFile file){

if(file == null || file.isEmpty()) return false;

try(InputStream in=file.getInputStream()){
byte[] header=in.readNBytes(4);
if(header.length < 4) return false;
String magic=new String(header, StandardCharsets.US_ASCII);
return magic.equals("%PDF");
}catch(Exception ex){
return false;
}
}

public List<String> splitChapters(String text){

return Arrays.asList(text.split("CHAPTER"));
}
}

