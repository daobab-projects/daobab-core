import org.junit.jupiter.api.Test;

public class TestDivide {

    @Test
    void testdiv01() {
        long position = 600;
        long number = 4096L;
        int pagesize = 9;
        System.out.println(number >> pagesize);
        System.out.println(position >> pagesize);
        System.out.println(600 - (position >> pagesize << pagesize));
        int n = 1;
        System.out.println(1 << 2);
    }
}
