package com.jamiltonquintero.strategyexample.reports;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.jamiltonquintero.strategyexample.reports.strategies.IReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class ReportFactory {

    private Map<ReportEnum, IReport> reportEnumIReportMap;

    @Autowired
    public ReportFactory(Set<IReport> types) {
        this.reportEnumIReportMap = new HashMap<>();
        types.forEach(type -> this.reportEnumIReportMap.put(type.getReportType(), type));
    }

    public byte[] genereteReport(ReportEnum reportEnum) throws DocumentException, IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Document document = new Document();

        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();

        var report = this.reportEnumIReportMap.get(reportEnum);

        report.portada(document);
        document.add(report.tabla());
        document.newPage();

        var isComplex = report.conclusion(document);

        if (isComplex){
            document.newPage();
            Font titleComplex = FontFactory.getFont(FontFactory.COURIER, 100, BaseColor.ORANGE);

            Paragraph titleParagraph = new Paragraph("ESTA MUY COMPLEJA ESTA LOGICA", titleComplex);
            titleParagraph.setAlignment(Element.ALIGN_CENTER);
            titleParagraph.setSpacingBefore(document.getPageSize().getHeight() / 2 - titleComplex.getSize());
            document.add(titleParagraph);

        }

        document.close();

        return outputStream.toByteArray();
    }

}
