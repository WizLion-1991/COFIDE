
public class test {

    int x = 0;
    int y = 0;

    test() {

    }

    public int funcion1(int x) {
        this.x = x;
        for (int i = 0; i < 10; i++) {
            x += i;
        }
        return x;
    }

    public int funccion2(int y) {
        this.y = y;
        for (int i = 0; i < 10; i++) {
            y += i;
        }
        return y;
    }

}
