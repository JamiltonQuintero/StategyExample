package com.jamiltonquintero.strategyexample.reports.strategies;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.jamiltonquintero.strategyexample.reports.ReportEnum;


public interface IReport {

    ReportEnum getReportType();

    void portada(Document document) throws DocumentException;

    PdfPTable tabla();
    boolean conclusion(Document document) throws DocumentException;

}
