import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;

public class RandomNumberDatabase {
    private static final String URL = "jdbc:mysql://localhost:3306/testdb?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "1456";

    public static void main(String[] args) {
        try {
            System.out.println("Veritabanına bağlanılıyor...");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Bağlantı başarılı!");

            Statement stmt = conn.createStatement();

            System.out.println("Tablo oluşturuluyor...");
            stmt.execute("CREATE TABLE IF NOT EXISTS numbers (number INT)");
            System.out.println("Tablo oluşturma başarılı!");

            System.out.println("Rastgele sayılar oluşturuluyor...");
            Random random = new Random();
            int[] randomNumbers = new int[100000];
            for (int i = 0; i < 100000; i++) {
                randomNumbers[i] = random.nextInt(200000) + 1;
            }
            System.out.println("Rastgele sayılar oluşturma başarılı!");

            System.out.println("Sayılar veritabanına ekleniyor...");
            long startTimeInsert = System.currentTimeMillis();
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO numbers (number) VALUES (?)");
            for (int number : randomNumbers) {
                pstmt.setInt(1, number);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            long endTimeInsert = System.currentTimeMillis();

            long insertDuration = endTimeInsert - startTimeInsert;
            System.out.println("Veritabanına ekleme süresi: " + insertDuration + " ms");


            System.out.println("Sayılar veritabanından çekiliyor...");
            long startTimeSelect = System.currentTimeMillis();
            ResultSet rs = stmt.executeQuery("SELECT * FROM numbers");
            while (rs.next()) {
                int number = rs.getInt("number");
            }
            long endTimeSelect = System.currentTimeMillis();

            long selectDuration = endTimeSelect - startTimeSelect;
            System.out.println("Veritabanından çekme süresi: " + selectDuration + " ms");

            conn.close();
            System.out.println("Bağlantı kapatıldı.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
