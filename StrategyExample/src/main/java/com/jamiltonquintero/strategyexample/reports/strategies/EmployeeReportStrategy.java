package com.jamiltonquintero.strategyexample.reports.strategies;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.jamiltonquintero.strategyexample.reports.ReportEnum;
import com.jamiltonquintero.strategyexample.reports.model.Client;
import com.jamiltonquintero.strategyexample.reports.model.Employee;
import com.jamiltonquintero.strategyexample.reports.model.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;

@Service
public class EmployeeReportStrategy implements IReport {

    private final EmployeeRepository employeeRepository;

    public EmployeeReportStrategy(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public ReportEnum getReportType() {
        return ReportEnum.EMPLOYEE;
    }

    @Override
    public void portada(Document document) throws DocumentException {
        Font titleFont = FontFactory.getFont(FontFactory.COURIER_BOLD, 30, BaseColor.GREEN);
        Font authorFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.CYAN);

        Paragraph titleParagraph = new Paragraph("REPORTE EMPLEADOS", titleFont);
        titleParagraph.setAlignment(Element.ALIGN_CENTER);
        titleParagraph.setSpacingBefore(document.getPageSize().getHeight() / 2 - titleFont.getSize());
        document.add(titleParagraph);

        Paragraph authorParagraph = new Paragraph("Generado por: " + "Jamilton", authorFont);
        authorParagraph.setAlignment(Element.ALIGN_CENTER);
        authorParagraph.setSpacingBefore(50);
        document.add(authorParagraph);

        Paragraph resume = new Paragraph("Esto es un texto diferente", authorFont);
        authorParagraph.setAlignment(Element.ALIGN_CENTER);
        authorParagraph.setSpacingBefore(50);
        document.add(resume);

        document.newPage();
    }

    @Override
    public PdfPTable tabla() {
        var employees = employeeRepository.findAll();
        var fields = employees.get(0).getClass().getDeclaredFields();

        PdfPTable table = new PdfPTable(fields.length + 1);

        addTableHeader(table, fields);
        addRows(table, employees);

        return table;
    }

    private void addTableHeader(PdfPTable table, Field[] fields) {

        PdfPCell header = new PdfPCell();
        header.setBackgroundColor(BaseColor.MAGENTA);
        header.setBorderWidth(2);
        for (Field field : fields) {
            field.setAccessible(true);
            header.setPhrase(new Phrase(field.getName()));
            table.addCell(header);
        }
        header.setPhrase(new Phrase("Aplica para regalo"));
        table.addCell(header);
    }

    private void addRows(PdfPTable table, List<Employee> employees) {

        for (Employee employee : employees) {
            boolean isApplicableForGift = false;
            for (Field field : employee.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    Object value = field.get(employee);
                    PdfPCell cell;
                    new PdfPCell(new Phrase(value.toString()));
                    if (field.getName().equals("timeWithCompany")) {
                        Byte timeWithCompany = (Byte) value;
                        if (timeWithCompany > 10){
                            isApplicableForGift = true;
                        }
                    }

                    table.addCell(new PdfPCell(new Phrase(value.toString())));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
                String message;
                if (isApplicableForGift){
                    message = "SI";
                }else{
                    message = "NO";
                }
            table.addCell(new PdfPCell(new Phrase(message)));

        }
    }

    @Override
    public boolean conclusion(Document document) throws DocumentException {
        Font conclusion = FontFactory.getFont(FontFactory.COURIER_OBLIQUE, 16, BaseColor.BLACK);

        Paragraph titleParagraph = new Paragraph("Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas \"Letraset\", las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.", conclusion);
        titleParagraph.setAlignment(Element.ALIGN_CENTER);
        titleParagraph.setSpacingBefore(document.getPageSize().getHeight() / 2 - conclusion.getSize());
        document.add(titleParagraph);

        return false;
    }
}
