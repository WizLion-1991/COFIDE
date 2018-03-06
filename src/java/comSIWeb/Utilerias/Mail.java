/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package comSIWeb.Utilerias;

import com.sun.mail.smtp.SMTPAddressFailedException;
import com.sun.mail.smtp.SMTPAddressSucceededException;
import com.sun.mail.smtp.SMTPSendFailedException;
import com.sun.mail.smtp.SMTPTransport;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import java.io.File;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Flags.Flag;
import org.apache.logging.log4j.LogManager;

/**
 * Envia un correo a travez del paquete JAVAMAIL
 *
 * @author Zeus galindo
 */
public class Mail {

    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    private String host;
    private String puerto;
    private String usuario;
    private String contrasenia;
    private String destino;
    private String destinooculto;
    private String asunto;
    private String mensaje;
    private String ErrMsg;
    private ArrayList<String> arrUrlAdjuntos = null;
    private boolean bolUsaTls;
    private boolean bolUsaStartTls;
    private boolean bolUsaAutenticar;
    private boolean bolDepuracion;
    private boolean bolAcuseRecibo;
    private boolean bolMantenerConexion;
    private boolean bolEstaConectado;
    private SMTPTransport t;
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(Mail.class.getName());

    public boolean isBolEstaConectado() {
        return bolEstaConectado;
    }

    public void setBolEstaConectado(boolean bolEstaConectado) {
        this.bolEstaConectado = bolEstaConectado;
    }

    public boolean isBolMantenerConexion() {
        return bolMantenerConexion;
    }

    public void setBolMantenerConexion(boolean bolMantenerConexion) {
        this.bolMantenerConexion = bolMantenerConexion;
    }

    /**
     * Nos dice si se enviara acuse de recibo
     *
     * @return Es un valor boolean con true indica que si se envia acuse de
     * recibo
     */
    public boolean isBolAcuseRecibo() {
        return bolAcuseRecibo;
    }

    /**
     * Definimos si se envia acuse de recibo
     *
     * @param bolAcuseRecibo Es un valor boolean con true indica que si se envia
     * acuse de recibo
     */
    public void setBolAcuseRecibo(boolean bolAcuseRecibo) {
        this.bolAcuseRecibo = bolAcuseRecibo;
    }

    /**
     * Nos dice si usamos autenticacion
     *
     * @return es una valor boolean
     */
    public boolean isBolUsaAutenticar() {
        return bolUsaAutenticar;
    }

    /**
     * Definimos si usamos autenticacion
     *
     * @param bolUsaAutenticar es una valor boolean
     */
    public void setBolUsaAutenticar(boolean bolUsaAutenticar) {
        this.bolUsaAutenticar = bolUsaAutenticar;
    }

    /**
     * Nos dice si usamos START TLS
     *
     * @return es una valor boolean
     */
    public boolean isBolUsaStartTls() {
        return bolUsaStartTls;
    }

    /**
     * Definimos si usamos START TLS
     *
     * @param bolUsaStartTls es una valor boolean
     */
    public void setBolUsaStartTls(boolean bolUsaStartTls) {
        this.bolUsaStartTls = bolUsaStartTls;
    }

    /**
     * Nos dice si usamos TLS
     *
     * @return es una valor boolean
     */
    public boolean isBolUsaTls() {
        return bolUsaTls;
    }

    /**
     * Definimos si usamos TLS
     *
     * @param bolUsaTls es una valor boolean
     */
    public void setBolUsaTls(boolean bolUsaTls) {
        this.bolUsaTls = bolUsaTls;
    }

    /**
     * Nos regresa el asunto del mail
     *
     * @return regresa el asunto
     */
    public String getAsunto() {
        return asunto;
    }

    /**
     * Definimos el asunto del mail
     *
     * @param asunto Define el asunto
     */
    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    /**
     * regresa la contraseña de nuestro mail
     *
     * @return es un texto con la contraseña
     */
    public String getContrasenia() {
        return contrasenia;
    }

    /**
     * definimos la contraseña del mail
     *
     * @param contrasenia es un texto con la contraseña
     */
    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    /**
     * nos regresa el destino del mail
     *
     * @return es un campo de texto con el correo del destinatario del mail
     */
    public String getDestino() {
        return destino;
    }

    /**
     * definimos el destino del mail
     *
     * @param destino es un campo de texto con el correo del destinatario del
     * mail
     */
    public void setDestino(String destino) {
        this.destino = destino;
    }

    /**
     * nos regresa el destino oculto del mail
     *
     * @return es un campo de texto con el correo del destinatario oculto del
     * mail
     */
    public String getDestinooculto() {
        return destinooculto;
    }

    /**
     * definimos el destino oculto del mail
     *
     * @param destinooculto es un campo de texto con el correo del destinatario
     * oculto del mail
     */
    public void setDestinooculto(String destinooculto) {
        this.destinooculto = destinooculto;
    }

    /**
     * regresa el nombre del host del smtp
     *
     * @return es el nombre del host
     */
    public String getHost() {
        return host;
    }

    /**
     * definimos el nombre del host del smtp
     *
     * @param host es el nombre del host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Nos regresa el texto del mensaje del mail
     *
     * @return es el mensaje del mail
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * Definimos el texto del mensaje del mail
     *
     * @param mensaje es el mensaje del mail
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    /**
     * Definimos el puerto del smtp
     *
     * @return Es el puerto del smtp
     */
    public String getPuerto() {
        return puerto;
    }

    /**
     * Nos regresa el puerto del smtp
     *
     * @param puerto Es el puerto del smtp
     */
    public void setPuerto(String puerto) {
        this.puerto = puerto;
    }

    /**
     * Nos regresa el usuario del smtp
     *
     * @return es el usuario del smtp
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * Definimos el usuario del smtp
     *
     * @param usuario es el usuario del smtp
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * Nos regresa si usamos depuracion
     *
     * @return es un valor boolean
     */
    public boolean isBolDepuracion() {
        return bolDepuracion;
    }

    /**
     * Definimos si usamos depuracion
     *
     * @param bolDepuracion es un valor boolean
     */
    public void setBolDepuracion(boolean bolDepuracion) {
        this.bolDepuracion = bolDepuracion;
    }

    /**
     * definimos un fichero a enviar
     *
     * @param strUrlFichero URL del fichero esta debe ser una direccion absoluta
     * de la maquina ej. C:/misarchivos/archivo.txt
     */
    public void setFichero(String strUrlFichero) {
        arrUrlAdjuntos.add(strUrlFichero);
    }

    /**
     * Nos regresa el mensaje de error
     *
     * @return Regresa un texto con el mensaje de error
     */
    public String getErrMsg() {
        return new String(ErrMsg);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructores">
    /**
     * Instancia el objeto para correo masivo
     */
    public Mail() {
        arrUrlAdjuntos = new ArrayList<String>();
        this.host = "";
        this.puerto = "";
        this.usuario = "";
        this.contrasenia = "";
        this.destino = "";
        this.destinooculto = "";
        this.asunto = "";
        this.mensaje = "";
        this.ErrMsg = "";
        this.bolUsaTls = false;
        this.bolUsaStartTls = false;
        this.bolUsaAutenticar = true;
        this.bolDepuracion = false;
        this.bolAcuseRecibo = false;
        this.bolMantenerConexion = false;
        this.bolEstaConectado = false;
    }

    /**
     * Constructor con los valores iniciales para enviar el mail
     *
     * @param host : nombre o IP del servidor SMTP
     * @param puerto : puerto por el cual se transmitira el mensaje
     * @param usuario : correo del quien envia el correo
     * @param contrasenia : contrasema de quien envia el correo
     */
    public Mail(String host, String puerto, String usuario, String contrasenia) {
        this.host = host;
        this.puerto = puerto;
        this.usuario = usuario;
        this.contrasenia = contrasenia;
        arrUrlAdjuntos = new ArrayList<String>();
        this.destino = "";
        this.destinooculto = "";
        this.asunto = "";
        this.mensaje = "";
        this.ErrMsg = "";
        this.bolUsaTls = false;
        this.bolUsaAutenticar = true;
        this.bolDepuracion = false;
        this.bolAcuseRecibo = false;
        this.bolUsaStartTls = false;
        this.bolMantenerConexion = false;
        this.bolEstaConectado = false;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Metodos">
    /**
     * Envia un correo electronico
     *
     * @return verdadero cuando el correo se envio de manera correcta
     */
    public boolean sendMail() {
        boolean bolResult = false;
        if (this.bolDepuracion) {
            System.out.println("host:" + this.host);
            System.out.println("puerto:" + this.puerto);
            System.out.println("usuario:" + this.usuario);
            System.out.println("destino:" + this.destino);
            System.out.println("asunto:" + this.asunto);
            System.out.println("mensaje:" + this.mensaje);
        }
        // Inicializar las dos variables de clase
        // Obtener las propiedades del sistema y establecer el servidor
        // SMTP que se va a usar
        String smtpHost = this.host;
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        if (this.bolUsaStartTls) {
            props.setProperty("mail.smtp.starttls.enable", "true");
        }
        if (this.bolUsaTls) {
            props.setProperty("mail.smtps.port", this.puerto);
        } else {
            props.setProperty("mail.smtp.port", this.puerto);
        }
        //System.out.println("Obtener las propiedades del sistema y establecer el servidor");
        Authenticator auth = new MiAutenticador();
        //System.out.println("Obtener una sesin con las propiedades anteriormente definidas");
        Session sesion = Session.getInstance(props, auth);
        if (this.bolDepuracion) {
            sesion.setDebug(true);
        }
        if (bolUsaAutenticar) {
            if (bolUsaTls) {
                props.put("mail.smtps.auth", "true");
            } else {
                props.put("mail.smtp.auth", "true");
            }
        }
        // Capturar las excepciones
        try {
            Message msg = new MimeMessage(sesion);
            // -- Set some other header information --
            //msg.setHeader("X-Mailer", "LOTONtechEmail");
            msg.setSentDate(new Date());
            // Asunto
            msg.setSubject(this.asunto);

            // Emisor del mensaje
            boolean bolValidoMail = false;
            StringTokenizer tokenAlm = new StringTokenizer(this.destino, ",");
            while (tokenAlm.hasMoreTokens()) {
                String strEviarA = tokenAlm.nextToken();
                if (isEmail(strEviarA)) {
                    bolValidoMail = true;
                    msg.addRecipient(Message.RecipientType.TO, new InternetAddress(strEviarA));
                }
            }

            // Emisor del mensaje oculto
            tokenAlm = new StringTokenizer(this.destinooculto, ",");
            while (tokenAlm.hasMoreTokens()) {
                String strEviarA = tokenAlm.nextToken();
                //System.out.println(strEviarA);
                if (isEmail(strEviarA)) {
                    msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(strEviarA));
                }
            }
            msg.setFrom(new InternetAddress(this.usuario));
            // create and fill the first message part
            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setContent(this.mensaje, "text/html");

            //Validamos si se envia acuse de recibo
            if (this.bolAcuseRecibo) {
                // Acuse de recibo
                msg.addHeader("Disposition-Notification-To", new InternetAddress(this.usuario).getAddress());
            }
            // create the Multipart and add its parts to it
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(mbp1);

            //Adjuntos los archivos en caso de haber
            if (arrUrlAdjuntos.size() > 0) {
                Iterator<String> itUrlDirecciones = this.arrUrlAdjuntos.iterator();
                while (itUrlDirecciones.hasNext()) {
                    String strUrlDireccion = itUrlDirecciones.next();
                    // create the second message part
                    MimeBodyPart mbp2 = new MimeBodyPart();
                    File path = new File(strUrlDireccion);
                    DataSource source = new FileDataSource(path);
                    mbp2.setDataHandler(new DataHandler(source));
                    mbp2.setFileName(path.getName());
                    mp.addBodyPart(mbp2);
                }
            }
            // add the Multipart to the message
            msg.setContent(mp);
            //Validamos si es valido el mail
            if (bolValidoMail) {
                //Validamos si esta conectado
                if (t != null) {
                    if (!t.isConnected()) {
                        this.bolEstaConectado = false;
                    }
                }
                //Validamos si esta manteniendo la conexion
                if (this.bolMantenerConexion && this.bolEstaConectado) {
                    t.sendMessage(msg, msg.getAllRecipients());
                    bolResult = true;
                } else {
                    // Generamos el objeto de envio
                    t = (SMTPTransport) sesion.getTransport(bolUsaTls ? "smtps" : "smtp"); //si ssl=true, smtps si no smtp
                    try {
                        if (this.bolUsaStartTls) {
                            t.setStartTLS(true);
                        }
                        if (bolUsaAutenticar) {
                            System.out.println("nos conectamos autenticandonos....");
                            t.connect(this.host, this.usuario, this.contrasenia);
                            this.bolEstaConectado = true;
                        } else {
                            t.connect();
                            this.bolEstaConectado = true;
                        }
                        t.sendMessage(msg, msg.getAllRecipients());
                        bolResult = true;
                    } catch (Exception e) {
                        log.error(" cant send mail... init");
                        log.error(" cant send mail..." + e.getLocalizedMessage());
                        log.error(" cant send mail..." + msg);
                        log.error(" cant send mail..." + msg.getAllRecipients());
                        log.error(" cant send mail..." + this.destino);
                        log.error(" cant send mail... end");
                        this.ErrMsg = e.getLocalizedMessage() + " " + e.getMessage();
                    } finally {
                        //Si vamos a mantener la conexion no cerramos la sesion
                        if (!this.bolMantenerConexion) {
                            t.close();
                        }
                    }
                }
            } else {
                this.ErrMsg = "Mail no valido";
                return false;
            }

        } catch (MessagingException e) {
            if (e instanceof SendFailedException) {
                MessagingException sfe = (MessagingException) e;
                if (sfe instanceof SMTPSendFailedException) {
                    SMTPSendFailedException ssfe
                            = (SMTPSendFailedException) sfe;
                    System.out.println("SMTP SEND FAILED:");
                    this.ErrMsg = "SMTP SEND FAILED:";
                }
                Exception ne;
                while ((ne = sfe.getNextException()) != null
                        && ne instanceof MessagingException) {
                    sfe = (MessagingException) ne;
                    if (sfe instanceof SMTPAddressFailedException) {
                        SMTPAddressFailedException ssfe
                                = (SMTPAddressFailedException) sfe;
                        System.out.println("ADDRESS FAILED:");
                        System.out.println(ssfe.toString());
                        System.out.println("  Address: " + ssfe.getAddress());
                        System.out.println("  Command: " + ssfe.getCommand());
                        System.out.println("  RetCode: " + ssfe.getReturnCode());
                        System.out.println("  Response: " + ssfe.getMessage());
                        this.ErrMsg = "ADDRESS FAILED:" + ssfe.getAddress() + "  RetCode: " + ssfe.getReturnCode();
                    } else if (sfe instanceof SMTPAddressSucceededException) {
                        System.out.println("ADDRESS SUCCEEDED:");
                        SMTPAddressSucceededException ssfe
                                = (SMTPAddressSucceededException) sfe;
                    }
                }
            } else {
                System.out.println("Got Exception : " + e);
                this.ErrMsg = "Got Exception : " + e.getMessage();
            }
        }
        return bolResult;
    }

    /**
     * Envia mails por metodos nativos de java
     *
     * @param server Es el servidor
     * @param userName Es el usuario
     * @param password Es el password
     * @param fromAddress Es la direccion remitente
     * @param toAddress Es la direccion destino
     * @param cc Es la copia del mail
     * @param bcc Es la copia oculta del mail
     * @param htmlFormat Indicamos si el formato es HTML
     * @param subject Es el asunto del mail
     * @param body Es el cuerpo del mail
     * @param strPuerto Es el puerto del smtp
     * @param strPuertoHttps Es el puerto https del smtp
     */
    public void sendMail(String server, String userName, String password, String fromAddress,
            String toAddress, String cc, String bcc, boolean htmlFormat, String subject, String body, String strPuerto, String strPuertoHttps) {

        // in the case of an exception, print a message to the output log
        Properties props = new Properties();

        if (server != null) {
            props.put("mail.smtp.host", server);
            props.put("mail.smtps.host", server);
        }
        if (bolUsaAutenticar) {
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtps.auth", "true");
        }
        props.put("mail.smtp.port", strPuerto);
        props.put("mail.smtps.port", strPuertoHttps);

        // Get a Session object
        javax.mail.Session session = javax.mail.Session.getInstance(props, null);
        if (this.bolDepuracion == true) {
            session.setDebug(true);
        }

        // construct the message
        javax.mail.Message msg = new MimeMessage(session);

        try {
            //  Set message details
            msg.setFrom(new InternetAddress(fromAddress));
            if (isEmail(toAddress)) {
                msg.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(toAddress));
            }
            if (!cc.equals("")) {
                if (isEmail(cc)) {
                    msg.addRecipient(javax.mail.Message.RecipientType.CC, new InternetAddress(cc));
                }

            }
            if (!bcc.equals("")) {
                if (isEmail(bcc)) {
                    msg.addRecipient(javax.mail.Message.RecipientType.BCC, new InternetAddress(bcc));
                }

            }
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            msg.setContent(body, "text/html");
            //msg.setText(message);

            // send the thing off
            // -- Set some other header information --
            //Validamos si se envia acuse de recibo
            if (this.bolAcuseRecibo) {
                // Acuse de recibo
                msg.addHeader("Disposition-Notification-To", new InternetAddress(this.usuario).getAddress());
            }

            msg.setSentDate(new Date());
            SMTPTransport t = (SMTPTransport) session.getTransport(bolUsaTls ? "smtps" : "smtp"); //si ssl=true, smtps si no smtp
            try {
                if (this.bolUsaStartTls) {
                    t.setStartTLS(true);
                }
                if (bolUsaAutenticar) {
                    t.connect(server, userName, password);
                } else {
                    t.connect();
                }
                t.sendMessage(msg, msg.getAllRecipients());
            } finally {
                t.close();
            }
            System.out.println("Mail was sent successfully.");

        } catch (Exception e) {
            if (e instanceof SendFailedException) {
                MessagingException sfe = (MessagingException) e;
                if (sfe instanceof SMTPSendFailedException) {
                    SMTPSendFailedException ssfe
                            = (SMTPSendFailedException) sfe;
                    System.out.println("SMTP SEND FAILED:");
                }
                Exception ne;
                while ((ne = sfe.getNextException()) != null
                        && ne instanceof MessagingException) {
                    sfe = (MessagingException) ne;
                    if (sfe instanceof SMTPAddressFailedException) {
                        SMTPAddressFailedException ssfe
                                = (SMTPAddressFailedException) sfe;
                        System.out.println("ADDRESS FAILED:");
                        System.out.println(ssfe.toString());
                        System.out.println("  Address: " + ssfe.getAddress());
                        System.out.println("  Command: " + ssfe.getCommand());
                        System.out.println("  RetCode: " + ssfe.getReturnCode());
                        System.out.println("  Response: " + ssfe.getMessage());
                    } else if (sfe instanceof SMTPAddressSucceededException) {
                        System.out.println("ADDRESS SUCCEEDED:");
                        SMTPAddressSucceededException ssfe
                                = (SMTPAddressSucceededException) sfe;
                    }
                }
            } else {
                System.out.println("Got Exception : " + e);
            }
        }

    }

    /**
     * Valida el mail
     *
     * @param correo Es el mail por validar
     * @return Regresa true en caso de ser un mail valido
     */
    public boolean isEmail(String correo) {
        correo = correo.toLowerCase();
        Pattern pat = null;
        Matcher mat = null;
        pat = Pattern.compile("^(?!(?:(?:\\x22?\\x5C[\\x00-\\x7E]\\x22?)|(?:\\x22?[^\\x5C\\x22]\\x22?)){255,})(?!(?:(?:\\x22?\\x5C[\\x00-\\x7E]\\x22?)|(?:\\x22?[^\\x5C\\x22]\\x22?)){65,}@)(?:(?:[\\x21\\x23-\\x27\\x2A\\x2B\\x2D\\x2F-\\x39\\x3D\\x3F\\x5E-\\x7E]+)|(?:\\x22(?:[\\x01-\\x08\\x0B\\x0C\\x0E-\\x1F\\x21\\x23-\\x5B\\x5D-\\x7F]|(?:\\x5C[\\x00-\\x7F]))*\\x22))(?:\\.(?:(?:[\\x21\\x23-\\x27\\x2A\\x2B\\x2D\\x2F-\\x39\\x3D\\x3F\\x5E-\\x7E]+)|(?:\\x22(?:[\\x01-\\x08\\x0B\\x0C\\x0E-\\x1F\\x21\\x23-\\x5B\\x5D-\\x7F]|(?:\\x5C[\\x00-\\x7F]))*\\x22)))*@(?:(?:(?!.*[^.]{64,})(?:(?:(?:xn--)?[a-z0-9]+(?:-[a-z0-9]+)*\\.){1,126}){1,}(?:(?:[a-z][a-z0-9]*)|(?:(?:xn--)[a-z0-9]+))(?:-[a-z0-9]+)*)|(?:\\[(?:(?:IPv6:(?:(?:[a-f0-9]{1,4}(?::[a-f0-9]{1,4}){7})|(?:(?!(?:.*[a-f0-9][:\\]]){7,})(?:[a-f0-9]{1,4}(?::[a-f0-9]{1,4}){0,5})?::(?:[a-f0-9]{1,4}(?::[a-f0-9]{1,4}){0,5})?)))|(?:(?:IPv6:(?:(?:[a-f0-9]{1,4}(?::[a-f0-9]{1,4}){5}:)|(?:(?!(?:.*[a-f0-9]:){5,})(?:[a-f0-9]{1,4}(?::[a-f0-9]{1,4}){0,3})?::(?:[a-f0-9]{1,4}(?::[a-f0-9]{1,4}){0,3}:)?)))?(?:(?:25[0-5])|(?:2[0-4][0-9])|(?:1[0-9]{2})|(?:[1-9]?[0-9]))(?:\\.(?:(?:25[0-5])|(?:2[0-4][0-9])|(?:1[0-9]{2})|(?:[1-9]?[0-9]))){3}))\\]))$");
        //pat = Pattern.compile("^([0-9a-zA-Z]([_.w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-w]*[0-9a-zA-Z].)+([a-zA-Z]{2,9}.)+[a-zA-Z]{2,3})$");
        mat = pat.matcher(correo);
        if (mat.find()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Este metodo recibe mails por medio de pop
     *
     * @param server Es el servidor
     * @param userName Es el usuario
     * @param password Es el password
     * @return Nos regresa Los mensajes de la bandeja de entrada
     * @throws java.lang.Exception Es la excepcion generada
     */
    public ArrayList RecibirMail(String server, String userName, String password) throws Exception {

        ArrayList<MailMessage> lsMails = new ArrayList<MailMessage>();
        // Get system properties
        Properties props = new Properties();
        props.put("mail.pop3.host", server);
        //System.out.println("Recibiendo mail");
        //Session session = Session.getInstance(props, auth);
        Session session = Session.getInstance(props, null);
        if (this.bolDepuracion == true) {
            session.setDebug(true);
        }
        // Get the store
        Store store = session.getStore("pop3");
        //store.connect();
        store.connect(userName, password);

        // Get folder
        Folder folder = store.getFolder("INBOX");
        folder.open(Folder.READ_ONLY);

        // Get directory
        Message message[] = folder.getMessages();
        for (int i = 0, n = message.length; i < n; i++) {
            //Instanciamos un objeto que representa el mail
            MailMessage mail = new MailMessage();
            mail.strSubject = message[i].getSubject();
            mail.strFrom = message[i].getFrom()[0].toString();
            mail.strContent = message[i].getContent().toString();
            lsMails.add(mail);
            /*
         System.out.println(i + ": " + message[i].getFrom()[0] + "\\t" + message[i].getSubject());
         String content =
         message[i].getContent().toString();
         if (content.length() > 200) {
         content = content.substring(0, 200);
         }
         System.out.print(content);
             */
        }
        // Close connection
        folder.close(false);
        store.close();
        System.out.println("Cerrando Recibiendo mail");
        return lsMails;
    }

    /**
     * Obtiene los parametros de un template de mail
     *
     * @param strAbrev Es la abreviacion del mail
     * @param oConn Es la conexion del mail
     * @return regresa true si encontro el template
     */
    public boolean getTemplate(String strAbrev, Conexion oConn) {
        boolean bolEncontro = false;
        String strSql = "SELECT * FROM mailtemplates where MT_ABRV ='" + strAbrev + "'";
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                this.setUsuario(rs.getString("MT_USER"));
                this.setContrasenia(rs.getString("MT_PASS"));
                this.setHost(rs.getString("MT_HOST"));
                this.setPuerto(rs.getString("MT_PORT"));
                this.setAsunto(rs.getString("MT_ASUNTO"));
                this.setMensaje(rs.getString("MT_CONTENIDO"));
                if (rs.getInt("MT_TLS") == 1) {
                    this.setBolUsaTls(true);
                }
                if (rs.getInt("MT_STTLS") == 1) {
                    this.setBolUsaStartTls(false);
                }
                bolEncontro = true;
            }
            if (rs.getStatement() != null) {
                //rs.getStatement().close();
            }
            rs.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
        return bolEncontro;
    }

    /**
     * Reemplaza el contenido del mensaje con los valores del resultset
     *
     * @param rs Es el resultset con los valores a recuperar
     * @return Nos regresa true si nada fallo
     */
    public boolean setReplaceContent(ResultSet rs) {
        boolean bolSucess = false;
        try {
            rs.beforeFirst();
            ResultSetMetaData rsmd = rs.getMetaData();
            int numberOfColumns = rsmd.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= numberOfColumns; i++) {
                    String strValor = rs.getString(rsmd.getColumnName(i));
                    if (strValor != null) {
                        this.mensaje = this.mensaje.replace("%" + rsmd.getColumnName(i) + "%", strValor);
                        this.asunto = this.asunto.replace("%" + rsmd.getColumnName(i) + "%", strValor);
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Mail.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bolSucess;
    }

    /**
     * Reemplaza el contenido del mensaje con los valores del TableMaster
     *
     * @param tb Es el objeto Tablemaster
     * @return Nos regresa true si nada fallo
     */
    public boolean setReplaceContent(TableMaster tb) {
        boolean bolSucess = false;
        Iterator<String> it = tb.getNomFields().iterator();
        while (it.hasNext()) {
            String strNom = it.next();
            String strValor = tb.getFieldString(strNom);
            if (this.mensaje.contains("%" + strNom + "%")) {
                this.mensaje = this.mensaje.replace("%" + strNom + "%", strValor);
            }
            if (this.asunto.contains("%" + strNom + "%")) {
                this.asunto = this.asunto.replace("%" + strNom + "%", strValor);
            }
        }
        return bolSucess;
    }

    public void cerrarConexion() {
        try {
            t.close();
            this.bolEstaConectado = false;
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
    // </editor-fold>

    /**
     * clase para generar un objeto de tipo PasswordAuthentication
     */
    private class MiAutenticador extends Authenticator {

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(usuario, contrasenia);
        }
    }
}

/**
 * Representa un mial
 *
 * @author zeus
 */
class MailMessage {

    /**
     * Es el remitente del correo
     */
    public String strFrom;
    /**
     * Es el asunto del correo
     */
    public String strSubject;
    /**
     * Es el contenido del correo
     */
    public String strContent;
}
