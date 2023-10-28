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
import com.jamiltonquintero.strategyexample.reports.model.User;
import com.jamiltonquintero.strategyexample.reports.model.UserRepository;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;

@Service
public class UserReportStrategy implements IReport{
    private final UserRepository userRepository;

    public UserReportStrategy(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ReportEnum getReportType() {
        return null;
    }

    @Override
    public void portada(Document document) throws DocumentException {
        Font titleFont = FontFactory.getFont(FontFactory.COURIER_BOLD, 30, BaseColor.GREEN);
        Font authorFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.CYAN);

        Paragraph titleParagraph = new Paragraph("REPORTE USUARIOS", titleFont);
        titleParagraph.setAlignment(Element.ALIGN_CENTER);
        titleParagraph.setSpacingBefore(document.getPageSize().getHeight() / 2 - titleFont.getSize());
        document.add(titleParagraph);

        document.newPage();
    }

    @Override
    public PdfPTable tabla() {
        var users = userRepository.findAll();
        var fields = users.get(0).getClass().getDeclaredFields();

        PdfPTable table = new PdfPTable(fields.length);

        addTableHeader(table, fields);
        addRows(table, users);

        return table;
    }

    private void addTableHeader(PdfPTable table, Field[] fields) {

        for (Field field : fields) {
            field.setAccessible(true);
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setBorderWidth(2);
            header.setPhrase(new Phrase(field.getName()));
            table.addCell(header);
        }
    }

    private void addRows(PdfPTable table, List<User> users) {

        for (User user : users) {
            for (Field field : user.getClass().getDeclaredFields()) {
                field.setAccessible(true);

                try {
                    Object value = field.get(user);
                    table.addCell(new PdfPCell(new Phrase(value.toString())));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean conclusion(Document document) throws DocumentException {
        Font conclusion = FontFactory.getFont(FontFactory.COURIER_OBLIQUE, 10, BaseColor.ORANGE);

        Paragraph titleParagraph = new Paragraph("Lorem Ipsum es simplemente el texto de relleno de las imprentas y archivos de texto. Lorem Ipsum ha sido el texto de relleno estándar de las industrias desde el año 1500, cuando un impresor (N. del T. persona que se dedica a la imprenta) desconocido usó una galería de textos y los mezcló de tal manera que logró hacer un libro de textos especimen. No sólo sobrevivió 500 años, sino que tambien ingresó como texto de relleno en documentos electrónicos, quedando esencialmente igual al original. Fue popularizado en los 60s con la creación de las hojas \"Letraset\", las cuales contenian pasajes de Lorem Ipsum, y más recientemente con software de autoedición, como por ejemplo Aldus PageMaker, el cual incluye versiones de Lorem Ipsum.", conclusion);
        titleParagraph.setAlignment(Element.ALIGN_CENTER);
        titleParagraph.setSpacingBefore(document.getPageSize().getHeight() / 2 - conclusion.getSize());
        document.add(titleParagraph);

        return false;
    }
}
