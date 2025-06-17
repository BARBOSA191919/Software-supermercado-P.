package com.gestion.inventario.ServiceImpl;

import com.gestion.inventario.entidades.ProductoVendido;
import com.gestion.inventario.entidades.Venta;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

@Service
public class VentaFacturaService {

    private static final Rectangle TICKET_SIZE = new Rectangle(227, PageSize.A4.getHeight());  // ~8cm de ancho
    private static final float MARGIN = 10f;

    public byte[] generarVentapdf(Venta venta) {
        Document document = new Document(TICKET_SIZE, MARGIN, MARGIN, MARGIN, MARGIN);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();

            // Encabezado
            agregarParrafoCentrado(document, "SUPERMERCADO XYZ", 10, Font.BOLD);
            agregarParrafoCentrado(document, "NIT: 123.456.789-0", 8, Font.NORMAL);
            agregarParrafoCentrado(document, "Dir: Calle Falsa 123", 8, Font.NORMAL);
            agregarParrafoCentrado(document, "Tel: +57 300 000 0000", 8, Font.NORMAL);

            agregarLineaSeparadora(document);

            // Información de la factura
            agregarParrafoIzquierda(document, "FACTURA DE VENTA POS", 8, Font.BOLD);
            agregarParrafoIzquierda(document, "No. " + venta.getId(), 8, Font.NORMAL);
            agregarParrafoIzquierda(document, "Fecha: " + venta.getFechaYHora(), 8, Font.NORMAL);

            // Información del cliente si existe
            if (venta.getCliente() != null) {
                agregarLineaSeparadora(document);
                agregarParrafoIzquierda(document, "DATOS DEL CLIENTE:", 8, Font.BOLD);
                agregarParrafoIzquierda(document, "Nombre: " + venta.getCliente().getNombreCliente() + " " +
                        venta.getCliente().getApellido(), 8, Font.NORMAL);
                agregarParrafoIzquierda(document, "Cédula: " + venta.getCliente().getCedula(), 8, Font.NORMAL);



                agregarParrafoIzquierda(document, "Cajero: " + venta.getTurno().getNombre(), 8, Font.NORMAL);
                agregarParrafoIzquierda(document, "Caja: " + venta.getTurno().getCaja(), 8, Font.NORMAL);

            }

            agregarLineaSeparadora(document);

            // Tabla de productos con IVA
            PdfPTable table = new PdfPTable(6); // Cambiar a 6 columnas
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2.2f, 1.4f, 1.8f, 1f, 1f, 2.0f}); // Ajusta el ancho de la columna del producto
            table.setSpacingBefore(5f);
            table.setSpacingAfter(5f);

// Encabezados de la tabla
            agregarCeldaTabla(table, "Producto", 7, Font.BOLD, Element.ALIGN_LEFT);
            agregarCeldaTabla(table, "Cantidad", 6, Font.BOLD, Element.ALIGN_CENTER);
            agregarCeldaTabla(table, "Precio", 7, Font.BOLD, Element.ALIGN_RIGHT);
            agregarCeldaTabla(table, "IVA%", 6, Font.BOLD, Element.ALIGN_CENTER);
            agregarCeldaTabla(table, "DTO", 6, Font.BOLD, Element.ALIGN_RIGHT); // Nueva columna
            agregarCeldaTabla(table, "Total", 7, Font.BOLD, Element.ALIGN_RIGHT);

// Variables para totales
            double subtotal = 0;
            double totalIva = 0;
            double total = 0;
            double totalDescuento = 0; // Nueva variable para el descuento total
            double efectivo = venta.getEfectivo();
            double cambio = venta.getCambio();

// Productos
            if (venta.getProductos() != null && !venta.getProductos().isEmpty()) {
                for (ProductoVendido producto : venta.getProductos()) {
                    double cantidad = producto.getCantidad();
                    double precioUnitario = producto.getPrecio();
                    double subtotalProducto = precioUnitario * cantidad;

                    // Cálculo del IVA
                    BigDecimal porcentajeIva = producto.getIva();
                    double ivaProducto = subtotalProducto * porcentajeIva.doubleValue() / 100;

                    // Cálculo del descuento
                    Float porcentajeDescuento = producto.getDescuento(); // Asumiendo que tienes un metodo para obtener el descuento
                    double descuentoProducto = subtotalProducto * porcentajeDescuento.doubleValue() / 100; // Cálculo del descuento
                    totalDescuento += descuentoProducto; // Acumular el descuento total

                    subtotal += subtotalProducto;
                    totalIva += ivaProducto;
                    total += subtotalProducto + ivaProducto - descuentoProducto; // Restar el descuento del total

                    // Agregar filas de productos con IVA
                    agregarCeldaTabla(table, producto.getNombre(), 7, Font.NORMAL, Element.ALIGN_LEFT);
                    agregarCeldaTabla(table, String.format("%.0f", cantidad), 7, Font.NORMAL, Element.ALIGN_CENTER);
                    agregarCeldaTabla(table, formatearMoneda(precioUnitario), 7, Font.NORMAL, Element.ALIGN_RIGHT);
                    agregarCeldaTabla(table, String.format("%.0f%%", porcentajeIva.doubleValue()), 7, Font.NORMAL, Element.ALIGN_CENTER);
                    agregarCeldaTabla(table, String.format("%.0f%%", porcentajeDescuento.doubleValue()), 7, Font.NORMAL, Element.ALIGN_RIGHT); // Mostrar descuento
                    agregarCeldaTabla(table, formatearMonedaSinDecimales(subtotalProducto + ivaProducto - descuentoProducto), 7, Font.NORMAL, Element.ALIGN_RIGHT);
                }
            }

            document.add(table);

// Tabla de totales
            PdfPTable tablaTotales = new PdfPTable(2);
            tablaTotales.setWidthPercentage(100);
            tablaTotales.setWidths(new float[]{1.2f, 0.8f});
            tablaTotales.setSpacingBefore(5f);
            tablaTotales.setSpacingAfter(5f);

            agregarFilaTotales(tablaTotales, "SUBTOTAL:", formatearMonedaSinDecimales(subtotal));
            agregarFilaTotales(tablaTotales, "TOTAL IVA:", formatearMonedaSinDecimales(totalIva));
            agregarFilaTotales(tablaTotales, "DESCUENTO TOTAL:", formatearMonedaSinDecimales(totalDescuento)); // Agregar descuento total
            agregarFilaTotales(tablaTotales, "TOTAL:", formatearMonedaSinDecimales(total));
            agregarFilaTotales(tablaTotales, "EFECTIVO:", formatearMonedaSinDecimales(efectivo));
            agregarFilaTotales(tablaTotales, "CAMBIO:", formatearMonedaSinDecimales(cambio));

            document.add(tablaTotales);
            agregarLineaSeparadora(document);
            // Códigos QR y barras
            Image codigoBarras = generarCodigoBarras(venta.getId().toString(), writer);
            codigoBarras.setAlignment(Element.ALIGN_CENTER);
            codigoBarras.scaleToFit(180, 30);
            document.add(codigoBarras);

            Image codigoQR = generarCodigoQR("https://aratiendas.com/" + venta.getId(), 100, 100);
            codigoQR.setAlignment(Element.ALIGN_CENTER);
            codigoQR.scaleToFit(100, 100);
            document.add(codigoQR);

            // Pie de página
            agregarLineaSeparadora(document);
            agregarParrafoCentrado(document, "¡GRACIAS POR SU COMPRA!", 8, Font.BOLD);
            agregarParrafoCentrado(document, "Conserve su factura para cualquier reclamación", 7, Font.NORMAL);
            agregarParrafoCentrado(document, "Resolución DIAN No. 18764000001234", 7, Font.NORMAL);

            document.close();
        } catch (DocumentException | WriterException e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }

    private String formatearMonedaSinDecimales(double total) {
        return String.format("$%,.0f", total);
    }

    private String formatearMoneda(double valor) {
        return String.format("$%,.0f", valor);
    }

    private String truncarTexto(String texto, int maxLength) {
        if (texto == null || texto.length() <= maxLength) {
            return texto;
        }
        return texto.substring(0, maxLength - 3) + "...";
    }

    private void agregarLineaSeparadora(Document document) throws DocumentException {
        Paragraph linea = new Paragraph("----------------------------------------",
                FontFactory.getFont(FontFactory.COURIER, 8, Font.NORMAL));
        linea.setAlignment(Element.ALIGN_CENTER);
        document.add(linea);
    }

    private void agregarParrafoCentrado(Document document, String texto, int tamaño, int estilo)
            throws DocumentException {
        Paragraph parrafo = new Paragraph(texto,
                FontFactory.getFont(FontFactory.HELVETICA, tamaño, estilo));
        parrafo.setAlignment(Element.ALIGN_CENTER);
        document.add(parrafo);
    }

    private void agregarParrafoIzquierda(Document document, String texto, int tamaño, int estilo)
            throws DocumentException {
        Paragraph parrafo = new Paragraph(texto,
                FontFactory.getFont(FontFactory.HELVETICA, tamaño, estilo));
        parrafo.setAlignment(Element.ALIGN_LEFT);
        document.add(parrafo);
    }

    private void agregarCeldaTabla(PdfPTable table, String texto, int tamaño, int estilo, int alineacion) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA, tamaño, estilo);
        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setHorizontalAlignment(alineacion);
        cell.setPadding(2f);
        cell.setMinimumHeight(20f); // Asegúrate de que la celda tenga al menos esta altura
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // Alinear verticalmente el texto
        cell.setUseAscender(true); // Permitir que el texto use el ascensor
        cell.setUseDescender(true); // Permitir que el texto use el descendente
        cell.setPhrase(new Phrase(texto, font)); // Asegúrate de que el texto se ajuste
        table.addCell(cell);
    }

    private void agregarFilaTotales(PdfPTable table, String etiqueta, String valor) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD);

        PdfPCell cellEtiqueta = new PdfPCell(new Phrase(etiqueta, font));
        cellEtiqueta.setBorder(PdfPCell.NO_BORDER);
        cellEtiqueta.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cellEtiqueta);

        PdfPCell cellValor = new PdfPCell(new Phrase(valor, font));
        cellValor.setBorder(PdfPCell.NO_BORDER);
        cellValor.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cellValor);
    }

    private Image generarCodigoBarras(String texto, PdfWriter writer) {
        Barcode128 barcode = new Barcode128();
        barcode.setCode(texto);
        return barcode.createImageWithBarcode(writer.getDirectContent(), null, null);
    }

    private Image generarCodigoQR(String texto, int ancho, int alto) throws WriterException, BadElementException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(texto, BarcodeFormat.QR_CODE, ancho, alto);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        byte[] pngBytes = new byte[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pngBytes[y * width + x] = (byte) (bitMatrix.get(x, y) ? 0 : 255);
            }
        }
        return Image.getInstance(width, height, 1, 8, pngBytes);
    }
}