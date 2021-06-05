package com.hyj.spark.util;

import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;

import java.io.File;
import java.io.FileInputStream;

public class PDFUtil {
    public static void main(String[] args) {
        try {

            String filePath = "xx.pdf";
            parsePdf(filePath);
            File file = new File(filePath);

            Tika tika = new Tika();

            String filecontent = tika.parseToString(file);

            System.out.println("Extracted Content: " + filecontent);


        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public static String parsePdf(String filePath) {

        try {

            BodyContentHandler handler = new BodyContentHandler();

            Metadata metadata = new Metadata();

            FileInputStream inputstream = new FileInputStream(filePath);

            ParseContext pcontext = new ParseContext();


            //parsing the document using PDF parser

            PDFParser pdfparser = new PDFParser();

            pdfparser.parse(inputstream, handler, metadata, pcontext);

            //getting the content of the document

            System.out.println("Contents of the PDF :" + handler.toString());

            // 元数据提取

            System.out.println("Metadata of the PDF:");

            String[] metadataNames = metadata.names();

            for (String name : metadataNames) {

                System.out.println(name + " : " + metadata.get(name));

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return "";

    }
}
