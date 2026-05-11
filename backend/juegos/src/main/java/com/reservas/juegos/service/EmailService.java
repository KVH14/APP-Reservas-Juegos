package com.reservas.juegos.service;

import com.reservas.juegos.entities.Reserva;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromAddress;

    public void enviarConfirmacionReserva(Reserva reserva) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromAddress);
            helper.setTo(reserva.getEmailCliente());
            helper.setSubject("✅ Confirmación de tu reserva #" + reserva.getId() + " — Playres");
            helper.setText(buildHtml(reserva), true);

            mailSender.send(message);
        } catch (Exception e) {
            // El error de email NO cancela la reserva — solo se registra en el log
            System.err.println("[EmailService] Error al enviar confirmación a "
                    + reserva.getEmailCliente() + ": " + e.getMessage());
        }
    }

    private String buildHtml(Reserva reserva) {
        String nombre       = reserva.getNombreCliente();
        Long   id           = reserva.getId();
        String producto     = reserva.getProducto().getTitulo();
        String plataforma   = reserva.getProducto().getPlataforma() != null
                                ? reserva.getProducto().getPlataforma() : "—";
        String fechaReserva = reserva.getFechaReserva().toString();
        String fechaDev     = reserva.getFechaDevolucion() != null
                                ? reserva.getFechaDevolucion().toString() : "No especificada";
        String estado       = reserva.getEstado();
        String precio       = String.format("$%.0f / día", reserva.getProducto().getPrecio());

        return """
            <!DOCTYPE html>
            <html lang="es">
            <head>
              <meta charset="UTF-8"/>
              <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
            </head>
            <body style="margin:0;padding:0;background:#0a0a0f;font-family:Arial,Helvetica,sans-serif;">
              <table width="100%%" cellpadding="0" cellspacing="0"
                     style="background:#0a0a0f;padding:40px 16px;">
                <tr><td align="center">
                  <table width="100%%" cellpadding="0" cellspacing="0"
                         style="max-width:600px;background:#0f0f18;border-radius:16px;
                                border:1px solid rgba(255,255,255,0.07);overflow:hidden;">

                    <!-- HEADER -->
                    <tr>
                      <td style="background:linear-gradient(135deg,#fa3257 0%%,#c0143a 100%%);
                                 padding:32px 24px;text-align:center;">
                        <p style="margin:0;color:#fff;font-size:26px;font-weight:900;
                                  letter-spacing:5px;">PLAY<span style="color:#ffd166;">R</span>ES</p>
                        <p style="margin:8px 0 0;color:rgba(255,255,255,0.75);font-size:13px;
                                  letter-spacing:2px;text-transform:uppercase;">Confirmación de Reserva</p>
                      </td>
                    </tr>

                    <!-- SALUDO -->
                    <tr>
                      <td style="padding:28px 28px 0;">
                        <p style="margin:0;color:#e6e6ea;font-size:15px;line-height:1.6;">
                          Hola <strong>%s</strong>,<br/>
                          Tu reserva fue registrada exitosamente. Aquí tienes el resumen:
                        </p>
                      </td>
                    </tr>

                    <!-- DETALLES -->
                    <tr>
                      <td style="padding:20px 28px;">
                        <table width="100%%" cellpadding="0" cellspacing="0"
                               style="background:#13131e;border-radius:12px;
                                      border:1px solid rgba(255,255,255,0.06);
                                      overflow:hidden;">
                          <tr>
                            <td style="padding:14px 18px;border-bottom:1px solid rgba(255,255,255,0.06);
                                       color:#6b7290;font-size:13px;width:45%%;">N° de reserva</td>
                            <td style="padding:14px 18px;border-bottom:1px solid rgba(255,255,255,0.06);
                                       color:#e6e6ea;font-size:13px;font-weight:700;">#%d</td>
                          </tr>
                          <tr>
                            <td style="padding:14px 18px;border-bottom:1px solid rgba(255,255,255,0.06);
                                       color:#6b7290;font-size:13px;">Juego</td>
                            <td style="padding:14px 18px;border-bottom:1px solid rgba(255,255,255,0.06);
                                       color:#e6e6ea;font-size:13px;">%s</td>
                          </tr>
                          <tr>
                            <td style="padding:14px 18px;border-bottom:1px solid rgba(255,255,255,0.06);
                                       color:#6b7290;font-size:13px;">Plataforma</td>
                            <td style="padding:14px 18px;border-bottom:1px solid rgba(255,255,255,0.06);
                                       color:#e6e6ea;font-size:13px;">%s</td>
                          </tr>
                          <tr>
                            <td style="padding:14px 18px;border-bottom:1px solid rgba(255,255,255,0.06);
                                       color:#6b7290;font-size:13px;">Precio</td>
                            <td style="padding:14px 18px;border-bottom:1px solid rgba(255,255,255,0.06);
                                       color:#e6e6ea;font-size:13px;">%s</td>
                          </tr>
                          <tr>
                            <td style="padding:14px 18px;border-bottom:1px solid rgba(255,255,255,0.06);
                                       color:#6b7290;font-size:13px;">Fecha de reserva</td>
                            <td style="padding:14px 18px;border-bottom:1px solid rgba(255,255,255,0.06);
                                       color:#e6e6ea;font-size:13px;">%s</td>
                          </tr>
                          <tr>
                            <td style="padding:14px 18px;border-bottom:1px solid rgba(255,255,255,0.06);
                                       color:#6b7290;font-size:13px;">Fecha de devolución</td>
                            <td style="padding:14px 18px;border-bottom:1px solid rgba(255,255,255,0.06);
                                       color:#e6e6ea;font-size:13px;">%s</td>
                          </tr>
                          <tr>
                            <td style="padding:14px 18px;color:#6b7290;font-size:13px;">Estado</td>
                            <td style="padding:14px 18px;color:#4dffb4;font-size:13px;font-weight:700;">%s</td>
                          </tr>
                        </table>
                      </td>
                    </tr>

                    <!-- CONTACTO PROVEEDOR -->
                    <tr>
                      <td style="padding:0 28px 28px;">
                        <table width="100%%" cellpadding="0" cellspacing="0"
                               style="background:#13131e;border-radius:12px;
                                      border:1px solid rgba(255,255,255,0.06);
                                      border-left:3px solid #25d366;overflow:hidden;">
                          <tr>
                            <td style="padding:16px 18px;">
                              <p style="margin:0 0 6px;color:#9198b2;font-size:12px;
                                        text-transform:uppercase;letter-spacing:1px;">
                                Contacto del proveedor
                              </p>
                              <p style="margin:0;color:#e6e6ea;font-size:14px;">
                                📱 WhatsApp:
                                <a href="https://wa.me/573046446995"
                                   style="color:#25d366;text-decoration:none;font-weight:700;">
                                  +57 304 644 6995
                                </a>
                              </p>
                              <p style="margin:6px 0 0;color:#6b7290;font-size:12px;">
                                Escríbenos si tienes dudas sobre tu reserva.
                              </p>
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>

                    <!-- FOOTER -->
                    <tr>
                      <td style="padding:18px 28px;border-top:1px solid rgba(255,255,255,0.06);
                                 text-align:center;">
                        <p style="margin:0;color:#6b7290;font-size:11px;line-height:1.7;">
                          Este correo fue generado automáticamente. Por favor no respondas a este mensaje.<br/>
                          © 2025 Playres · Colombia
                        </p>
                      </td>
                    </tr>

                  </table>
                </td></tr>
              </table>
            </body>
            </html>
            """.formatted(nombre, id, producto, plataforma, precio, fechaReserva, fechaDev, estado);
    }
}
