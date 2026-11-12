/**
 * Öğrencinin kimlik ve biyografi bilgilerini temsil eden model (domain) sınıfı.
 *
 * SOLID - Tek Sorumluluk İlkesi (Single Responsibility Principle):
 * Bu sınıf yalnızca öğrenci verilerini geçerli bir durumda tutmaktan sorumludur.
 * Ekrana yazdırma, HTML üretme veya ağ üzerinden veri gönderme işlerini yapmaz.
 */
public class Student {

    // Kimlik ve temel profil alanları: nesne doğduktan sonra değişmemesi için final tanımlanmıştır
    private final String studentNumber;
    private final String fullName;
    private final String[] biographyParagraphs;

    /**
     * Ana (tam) yapıcı: tüm doğrulamalar tek merkezde yapılır.
     * Değişken sayıda parametre (varargs) alarak biyografi paragraflarını kabul eder.
     *
     * @param studentNumber       Öğrenci numarası (boş olamaz)
     * @param fullName            Öğrencinin adı ve soyadı (boş olamaz)
     * @param biographyParagraphs Biyografi metnini oluşturan paragraflar (en az bir paragraf gerekli)
     */
    public Student(String studentNumber, String fullName, String... biographyParagraphs) {
        this.studentNumber = requireText(studentNumber, "Öğrenci numarası");
        this.fullName = requireText(fullName, "Ad ve soyad");

        if (biographyParagraphs == null || biographyParagraphs.length == 0) {
            throw new IllegalArgumentException("Biyografi metni en az bir paragraf içermelidir");
        }

        // Savunmacı kopya (defensive copy): dışarıdan verilen dizi sonradan değiştirilse bile
        // nesnenin iç durumu bozulmaz.
        this.biographyParagraphs = new String[biographyParagraphs.length];
        for (int i = 0; i < biographyParagraphs.length; i++) {
            this.biographyParagraphs[i] = requireText(biographyParagraphs[i], "Biyografi paragrafı");
        }
    }

    /**
     * Kopya yapıcı (Copy Constructor): var olan bir Student nesnesinden aynı değerlere sahip
     * yeni bir Student nesnesi üretir. İşi this(...) ile ana yapıcıya devreder.
     *
     * @param other Kopyalanacak öğrenci nesnesi
     */
    public Student(Student other) {
        this(
                requireStudent(other).studentNumber,
                other.fullName,
                other.biographyParagraphs
        );
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public String getFullName() {
        return fullName;
    }

    /**
     * Kapsüllemeyi korumak için içerideki dizinin referansını doğrudan döndürmek yerine
     * savunmacı bir kopyasını döndürür.
     *
     * @return Biyografi paragraflarının kopyası
     */
    public String[] getBiographyParagraphs() {
        String[] copy = new String[biographyParagraphs.length];
        for (int i = 0; i < biographyParagraphs.length; i++) {
            copy[i] = biographyParagraphs[i];
        }
        return copy;
    }

    // Yardımcı metot: hiçbir nesne alanına ihtiyaç duymadığı için static tanımlanmıştır
    private static String requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " boş olamaz");
        }
        return value.trim();
    }

    // Kopya yapıcıda null kontrolü sağlayan yardımcı static metot
    private static Student requireStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Kopyalanacak öğrenci nesnesi null olamaz");
        }
        return student;
    }

    /**
     * Geliştiriciye yönelik metin temsili (hata ayıklama ve kayıt/log için).
     * Kullanıcıya gösterilecek HTML çıktısı bu sınıfta değil, HtmlGenerator sınıfında üretilir.
     */
    @Override
    public String toString() {
        return "Student{studentNumber=" + studentNumber
                + ", fullName=" + fullName
                + ", paragraphCount=" + biographyParagraphs.length + "}";
    }

    /**
     * Öğrenci kimliğiyle (entity) tanınan bir nesnedir; eşitlik yalnızca studentNumber alanına göre belirlenir.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) { // 1. Aynı nesne mi?
            return true;
        }
        if (o == null || getClass() != o.getClass()) { // 2. null ya da başka sınıf mı?
            return false;
        }
        Student other = (Student) o; // 3. Güvenli tip dönüşümü
        return studentNumber.equals(other.studentNumber); // 4. Kimliği karşılaştır
    }

    /**
     * equals ile sözleşme gereği aynı alanı (studentNumber) kullanır.
     */
    @Override
    public int hashCode() {
        return studentNumber.hashCode();
    }
}
