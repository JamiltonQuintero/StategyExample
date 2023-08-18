package com.jamiltonquintero.strategyexample.reports.strategies;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.jamiltonquintero.strategyexample.reports.ReportEnum;

public class XStrategy implements IReport {
    @Override
    public ReportEnum getReportType() {
        return ReportEnum.X;
    }

    @Override
    public void portada(Document document) throws DocumentException {

    }

    @Override
    public PdfPTable tabla() {
        return null;
    }

    @Override
    public boolean conclusion(Document document) throws DocumentException {
        return false;
    }
}
