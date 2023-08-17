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
import com.jamiltonquintero.strategyexample.reports.model.ClientRepository;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;

@Service
public class ClientReportStrategy implements IReport {

    private final ClientRepository clientRepository;

    public ClientReportStrategy(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public ReportEnum getReportType() {
        return ReportEnum.CLIENT;
    }

    @Override
    public void portada(Document document) throws DocumentException {

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, BaseColor.BLACK);
        Font authorFont = FontFactory.getFont(FontFactory.HELVETICA, 16, BaseColor.BLACK);

        Paragraph titleParagraph = new Paragraph("REPORTE CLIENTES", titleFont);
        titleParagraph.setAlignment(Element.ALIGN_CENTER);
        titleParagraph.setSpacingBefore(document.getPageSize().getHeight() / 2 - titleFont.getSize());
        document.add(titleParagraph);

        Paragraph authorParagraph = new Paragraph("Generado por: " + "Jamilton", authorFont);
        authorParagraph.setAlignment(Element.ALIGN_CENTER);
        authorParagraph.setSpacingBefore(50);
        document.add(authorParagraph);

        document.newPage();

    }

    @Override
    public PdfPTable tabla() {
        var clients = clientRepository.findAll();
        var fields = clients.get(0).getClass().getDeclaredFields();

        PdfPTable table = new PdfPTable(fields.length);

        addTableHeader(table, fields);
        addRows(table, clients);

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

    private void addRows(PdfPTable table, List<Client> clients) {

        for (Client client : clients) {
            for (Field field : client.getClass().getDeclaredFields()) {
                field.setAccessible(true);

                try {
                    Object value = field.get(client);
                    PdfPCell cell;

                    if (field.getName().equals("age")) {
                        Byte age = (Byte) value;
                        if (age > 25){
                            cell = new PdfPCell(new Phrase(value.toString()));
                            cell.setBackgroundColor(BaseColor.GREEN);
                        }else {
                            cell = new PdfPCell(new Phrase(value.toString()));
                            cell.setBackgroundColor(BaseColor.RED);
                        }

                    } else {
                        cell = new PdfPCell(new Phrase(value.toString()));
                    }

                    table.addCell(cell);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean conclusion(Document document) throws DocumentException {
        Font conclusion = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 60, BaseColor.BLACK);

        Paragraph titleParagraph = new Paragraph("ESTA ES UNA CONCLUSION MUY GRANDE", conclusion);
        titleParagraph.setAlignment(Element.ALIGN_CENTER);
        titleParagraph.setSpacingBefore(document.getPageSize().getHeight() / 2 - conclusion.getSize());
        document.add(titleParagraph);

        return true;
    }
}
