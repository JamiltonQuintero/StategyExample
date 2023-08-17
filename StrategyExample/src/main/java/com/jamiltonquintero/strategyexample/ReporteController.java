package com.jamiltonquintero.strategyexample;

import com.itextpdf.text.DocumentException;
import com.jamiltonquintero.strategyexample.reports.ReportEnum;
import com.jamiltonquintero.strategyexample.reports.ReportFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/reports")
public class ReporteController {
    private final ReportFactory reportFactory;

    public ReporteController(ReportFactory reportFactory) {
        this.reportFactory = reportFactory;
    }

    @GetMapping("/generatePdf")
    public ResponseEntity<byte[]> generarPdf(@RequestParam ReportEnum reportEnum) throws DocumentException, IOException {
        byte[] pdfBytes = reportFactory.genereteReport(reportEnum);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "report.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }


}
