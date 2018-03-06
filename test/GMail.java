//
//import java.util.Properties;
//
//import javax.mail.Message;
//import javax.mail.MessagingException;
//import javax.mail.PasswordAuthentication;
//import javax.mail.Session;
//import javax.mail.internet.AddressException;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//
//public class GMail {
//
//   Properties props = new Properties();
//         props.put("mail.smtp.host", "mail.cofide.org");
////         props.put("mail.smtp.socketFactory.port", "465");
////         props.put("mail.smtp.socketFactory.class",
////            "javax.net.ssl.SSLSocketFactory");
//         props.put("mail.smtp.auth", "true");
//         props.put("mail.smtp.port", "465");
//         props.put("mail.smtp.ssl.enable", "true");
////         MailSSLSocketFactory socketFactory = new MailSSLSocketFactory();
////         socketFactory.setTrustAllHosts(true);
////         props.put("mail.imaps.ssl.socketFactory", socketFactory);
//         System.out.println("Aqui vamos  1...");
//         Session session = Session.getDefaultInstance(props,
//            new javax.mail.Authenticator() {
//            protected PasswordAuthentication getPasswordAuthentication() {
//               return new PasswordAuthentication("pruebas@cofide.org", "pruebas");
//            }
//         });
//         try {
//            System.out.println("Aqui vamos  2...");
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress("pruebas@cofide.org"));
//            message.setRecipients(Message.RecipientType.TO,
//               InternetAddress.parse("aleph_79@hotmail.com"));
//            message.setSubject("Testing Subject");
//            message.setText("Dear Mail Crawler,"
//               + "\n\n No spam to my email, please!");
//            System.out.println("Aqui vamos  3...");
//            Transport.send(message);
//            System.out.println("Done");
//
//         } catch (MessagingException e) {
//            throw new RuntimeException(e);
//         }
//}
