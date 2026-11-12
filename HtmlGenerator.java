/**
 * Öğrenci bilgilerini tarayıcıda görüntülenecek şık bir HTML sayfasına dönüştüren sınıf.
 *
 * SOLID - Tek Sorumluluk İlkesi (Single Responsibility Principle):
 * Bu sınıfın değişmesi için tek neden HTML/CSS tasarımının değişmesidir.
 * Ağ (Socket) bağlantısı veya sunucu port yönetimiyle ilgilenmez.
 */
public class HtmlGenerator {

    public static final String DEFAULT_PAGE_TITLE = "Öğrenci Biyografi Sayfası";

    /**
     * Varsayılan sayfa başlığı ile HTML içeriği üretir.
     * Metot aşırı yükleme (overloading): işi iki parametreli sürüme devreder.
     *
     * @param student Bilgileri görüntülenecek öğrenci nesnesi
     * @return Biçimlendirilmiş tam HTML metni
     */
    public String generateHtml(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("HTML üretilecek öğrenci nesnesi null olamaz");
        }
        return generateHtml(student, student.getFullName() + " - " + DEFAULT_PAGE_TITLE);
    }

    /**
     * Belirtilen özel sayfa başlığı ile CSS destekli HTML içeriği üretir.
     *
     * @param student   Bilgileri görüntülenecek öğrenci nesnesi
     * @param pageTitle Tarayıcı sekmesinde görünecek başlık
     * @return Biçimlendirilmiş tam HTML metni
     */
    public String generateHtml(Student student, String pageTitle) {
        if (student == null) {
            throw new IllegalArgumentException("HTML üretilecek öğrenci nesnesi null olamaz");
        }
        if (pageTitle == null || pageTitle.trim().isEmpty()) {
            throw new IllegalArgumentException("Sayfa başlığı boş olamaz");
        }

        StringBuilder paragraphsHtml = new StringBuilder();
        String[] paragraphs = student.getBiographyParagraphs();
        for (String paragraph : paragraphs) {
            paragraphsHtml.append("            <p>")
                    .append(escapeHtml(paragraph))
                    .append("</p>\n");
        }

        return "<!DOCTYPE html>\n"
                + "<html lang=\"tr\">\n"
                + "<head>\n"
                + "    <meta charset=\"UTF-8\">\n"
                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "    <title>" + escapeHtml(pageTitle) + "</title>\n"
                + "    <style>\n"
                + "        * {\n"
                + "            box-sizing: border-box;\n"
                + "            margin: 0;\n"
                + "            padding: 0;\n"
                + "        }\n"
                + "        body {\n"
                + "            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\n"
                + "            background: linear-gradient(135deg, #e2e8f0 0%, #f8fafc 100%);\n"
                + "            color: #1e293b;\n"
                + "            min-height: 100vh;\n"
                + "            display: flex;\n"
                + "            justify-content: center;\n"
                + "            align-items: center;\n"
                + "            padding: 24px;\n"
                + "        }\n"
                + "        .profile-card {\n"
                + "            background-color: #ffffff;\n"
                + "            max-width: 720px;\n"
                + "            width: 100%;\n"
                + "            border-radius: 16px;\n"
                + "            box-shadow: 0 12px 32px rgba(15, 23, 42, 0.08);\n"
                + "            border-top: 6px solid #2563eb;\n"
                + "            padding: 40px 48px;\n"
                + "        }\n"
                + "        .header {\n"
                + "            border-bottom: 2px solid #f1f5f9;\n"
                + "            padding-bottom: 24px;\n"
                + "            margin-bottom: 28px;\n"
                + "        }\n"
                + "        h1 {\n"
                + "            font-size: 2.2rem;\n"
                + "            color: #0f172a;\n"
                + "            margin-bottom: 8px;\n"
                + "            letter-spacing: -0.5px;\n"
                + "        }\n"
                + "        h2 {\n"
                + "            font-size: 1.35rem;\n"
                + "            color: #2563eb;\n"
                + "            font-weight: 600;\n"
                + "            background-color: #eff6ff;\n"
                + "            display: inline-block;\n"
                + "            padding: 6px 14px;\n"
                + "            border-radius: 8px;\n"
                + "            letter-spacing: 0.5px;\n"
                + "        }\n"
                + "        .biography p {\n"
                + "            font-size: 1.08rem;\n"
                + "            line-height: 1.8;\n"
                + "            color: #334155;\n"
                + "            margin-bottom: 18px;\n"
                + "            text-align: justify;\n"
                + "        }\n"
                + "        .biography p:last-child {\n"
                + "            margin-bottom: 0;\n"
                + "        }\n"
                + "        .footer {\n"
                + "            margin-top: 32px;\n"
                + "            padding-top: 16px;\n"
                + "            border-top: 1px solid #f1f5f9;\n"
                + "            font-size: 0.88rem;\n"
                + "            color: #64748b;\n"
                + "            text-align: right;\n"
                + "        }\n"
                + "    </style>\n"
                + "</head>\n"
                + "<body>\n"
                + "    <main class=\"profile-card\">\n"
                + "        <header class=\"header\">\n"
                + "            <h1>" + escapeHtml(student.getFullName()) + "</h1>\n"
                + "            <h2>" + escapeHtml(student.getStudentNumber()) + "</h2>\n"
                + "        </header>\n"
                + "        <section class=\"biography\">\n"
                + paragraphsHtml
                + "        </section>\n"
                + "        <footer class=\"footer\">\n"
                + "            Kırklareli Üniversitesi &bull; Yazılım Mühendisliği\n"
                + "        </footer>\n"
                + "    </main>\n"
                + "</body>\n"
                + "</html>\n";
    }

    // Yardımcı metot: metin içindeki özel HTML karakterlerini güvenli hale getirir
    private static String escapeHtml(String text) {
        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
